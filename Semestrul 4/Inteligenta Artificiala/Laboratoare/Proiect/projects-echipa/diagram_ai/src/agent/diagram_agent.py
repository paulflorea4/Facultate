"""
agent/diagram_agent.py
Agentul principal care orchestrează:
1. Clasificare intent (ce tip de diagramă se dorește)
2. Retrieval RAG (exemple relevante)
3. Generare diagramă cu Azure OpenAI API (GPT-4o / GPT-3.5-turbo)
4. Validare sintaxă Mermaid
5. Auto-corectare dacă diagrama e invalidă (max 3 retries)
"""

import os
import re
from dataclasses import dataclass
from enum import Enum
from typing import Optional

from openai import AzureOpenAI
from dotenv import load_dotenv

from ..ast_analyzer.analyzer import PythonASTAnalyzer, ModuleInfo
from ..rag_engine.engine import RAGEngine, RetrievalResult
from ..utils.validator import MermaidValidator

load_dotenv()


class DiagramType(str, Enum):
    FLOWCHART = "flowchart"
    CLASS_DIAGRAM = "classDiagram"
    SEQUENCE = "sequenceDiagram"
    C4_CONTEXT = "C4Context"
    ER_DIAGRAM = "erDiagram"


@dataclass
class AgentInput:
    """Input pentru agent — text și/sau cod sursă."""
    text_description: Optional[str] = None
    source_code: Optional[str] = None
    source_file: Optional[str] = None
    requested_diagram_type: Optional[DiagramType] = None
    context: Optional[str] = None


@dataclass
class AgentOutput:
    """Output produs de agent."""
    mermaid_code: str
    diagram_type: DiagramType
    is_valid: bool
    validation_errors: list[str]
    retrieved_examples_count: int
    retries_used: int
    explanation: str
    raw_llm_response: str


# === Prompturi ===

SYSTEM_PROMPT = """Ești un expert în arhitectură software specializat în generarea de diagrame Mermaid.

Rolul tău:
- Primești descrieri de arhitecturi software sau cod sursă Python
- Generezi diagrame Mermaid corecte sintactic și relevante semantic
- Folosești exemplele furnizate ca ghid de stil și structură

Reguli stricte:
1. Răspunde DOAR cu blocul de cod Mermaid, fără explicații suplimentare
2. Începe ÎNTOTDEAUNA cu tipul diagramei (flowchart TD, classDiagram, sequenceDiagram etc.)
3. Folosește etichete clare și concise în engleză
4. Evită caractere speciale în noduri (folosește ghilimele dacă e necesar)
5. Asigură-te că diagrama este completă și self-contained
6. Maximum 20 noduri pentru lizibilitate

Format output OBLIGATORIU:
```mermaid
<diagrama ta aici>
```
"""

INTENT_PROMPT = """Analizează următoarea cerere și determină ce tip de diagramă Mermaid este cel mai potrivit.

Cerere: {query}

Tipuri disponibile:
- flowchart: pentru procese, fluxuri de date, algoritmi
- classDiagram: pentru structuri OOP, clase și relații
- sequenceDiagram: pentru interacțiuni între componente în timp
- C4Context: pentru arhitecturi la nivel înalt (sisteme, utilizatori)
- erDiagram: pentru scheme de baze de date

Răspunde cu UN SINGUR cuvânt din lista de mai sus."""

GENERATION_PROMPT = """Generează o diagramă Mermaid de tip {diagram_type} pentru:

{user_request}

{rag_context}

{ast_context}

Cerințe specifice:
- Tip diagramă: {diagram_type}
- Nivel de detaliu: mediu (nu prea simplu, nu prea complex)
- Noduri: maxim 20

Răspunde DOAR cu blocul ```mermaid ... ```"""

CORRECTION_PROMPT = """Diagrama generată are erori de sintaxă Mermaid:

Erori: {errors}

Diagrama cu erori:
```mermaid
{broken_diagram}
```

Corectează diagrama păstrând aceeași structură și conținut.
Răspunde DOAR cu blocul ```mermaid ... ``` corectat."""


class DiagramAgent:
    """
    Agent AI pentru generarea de diagrame Mermaid.
    """

    def __init__(
        self,
        rag_engine: Optional[RAGEngine] = None,
        model_name: Optional[str] = None,
        max_retries: int = 3,
        temperature: float = 0.2,
    ):
        api_key = os.getenv("AZURE_OPENAI_API_KEY")
        endpoint = os.getenv("AZURE_OPENAI_ENDPOINT")
        api_version = os.getenv("AZURE_OPENAI_API_VERSION", "2025-01-01-preview")

        if not api_key:
            raise ValueError("AZURE_OPENAI_API_KEY nu este setat în .env")
        if not endpoint:
            raise ValueError("AZURE_OPENAI_ENDPOINT nu este setat în .env")

        self.client = AzureOpenAI(
            api_key=api_key,
            azure_endpoint=endpoint,
            api_version=api_version,
        )
        self.model_name = model_name or os.getenv("AZURE_OPENAI_DEPLOYMENT", "gpt-4o")
        self.temperature = temperature

        self.rag_engine = rag_engine
        self.ast_analyzer = PythonASTAnalyzer()
        self.validator = MermaidValidator()
        self.max_retries = max_retries

        print(f"[Agent] Inițializat cu model Azure OpenAI: {self.model_name}")

    def _generate(self, prompt: str, system: Optional[str] = None) -> str:
        """Apel la Azure OpenAI Chat Completions API."""
        response = self.client.chat.completions.create(
            model=self.model_name,
            messages=[
                {"role": "system", "content": system or SYSTEM_PROMPT},
                {"role": "user", "content": prompt},
            ],
            max_tokens=2048,
            temperature=self.temperature,
        )
        return response.choices[0].message.content

    def run(self, agent_input: AgentInput) -> AgentOutput:
        """Rulează pipeline-ul complet."""
        query = self._build_query(agent_input)

        diagram_type = agent_input.requested_diagram_type
        if not diagram_type:
            diagram_type = self._classify_intent(query)
        print(f"[Agent] Tip diagramă detectat: {diagram_type}")

        ast_context = ""
        if agent_input.source_code or agent_input.source_file:
            ast_context = self._run_ast_analysis(agent_input)

        rag_context = ""
        retrieved_count = 0
        if self.rag_engine:
            retrieval = self.rag_engine.retrieve(
                query=query,
                diagram_type=diagram_type.value if hasattr(diagram_type, "value") else diagram_type,
                top_k=int(os.getenv("RAG_TOP_K", "5")),
            )
            rag_context = self.rag_engine.format_few_shot_context(retrieval)
            retrieved_count = len(retrieval.examples)
            print(f"[Agent] RAG: {retrieved_count} exemple recuperate")

        mermaid_code, retries, raw_response = self._generate_with_retries(
            query=query,
            diagram_type=diagram_type,
            rag_context=rag_context,
            ast_context=ast_context,
        )

        is_valid, errors = self.validator.validate(mermaid_code)
        explanation = self._generate_explanation(mermaid_code, diagram_type, query)

        return AgentOutput(
            mermaid_code=mermaid_code,
            diagram_type=diagram_type,
            is_valid=is_valid,
            validation_errors=errors,
            retrieved_examples_count=retrieved_count,
            retries_used=retries,
            explanation=explanation,
            raw_llm_response=raw_response,
        )

    def _build_query(self, agent_input: AgentInput) -> str:
        parts = []
        if agent_input.text_description:
            parts.append(agent_input.text_description)
        if agent_input.context:
            parts.append(f"Context: {agent_input.context}")
        if agent_input.source_code:
            parts.append(f"Cod sursă:\n{agent_input.source_code[:2000]}")
        return "\n\n".join(parts) if parts else "Generează o diagramă de arhitectură generică."

    def _classify_intent(self, query: str) -> DiagramType:
        prompt = INTENT_PROMPT.format(query=query[:1000])
        try:
            raw = self._generate(prompt, system="Răspunde cu un singur cuvânt.").strip().lower()
            mapping = {
                "flowchart": DiagramType.FLOWCHART,
                "classdiagram": DiagramType.CLASS_DIAGRAM,
                "sequencediagram": DiagramType.SEQUENCE,
                "c4context": DiagramType.C4_CONTEXT,
                "erdiagram": DiagramType.ER_DIAGRAM,
            }
            for key, dtype in mapping.items():
                if key in raw.replace(" ", "").replace("_", ""):
                    return dtype
        except Exception as e:
            print(f"[Agent] Eroare clasificare intent: {e}")
        return DiagramType.FLOWCHART

    def _run_ast_analysis(self, agent_input: AgentInput) -> str:
        try:
            if agent_input.source_file:
                module_info = self.ast_analyzer.analyze_file(agent_input.source_file)
            else:
                module_info = self.ast_analyzer.analyze_source(
                    agent_input.source_code, name="analyzed_module"
                )
            lines = ["=== ANALIZĂ STATICĂ AST ==="]
            lines.append(f"Modul: {module_info.name}")
            lines.append(f"Importuri: {', '.join(module_info.imports[:10])}")
            if module_info.classes:
                lines.append("\nClase detectate:")
                for cls in module_info.classes:
                    bases_str = f" (extinde: {', '.join(cls.bases)})" if cls.bases else ""
                    lines.append(f"  - {cls.name}{bases_str}")
                    lines.append(f"    Metode: {', '.join(cls.methods[:8])}")
            if module_info.functions:
                lines.append("\nFuncții detectate:")
                for fn in module_info.functions[:10]:
                    async_str = "async " if fn.is_async else ""
                    lines.append(f"  - {async_str}{fn.name}({', '.join(fn.args[:5])})")
            return "\n".join(lines)
        except Exception as e:
            print(f"[Agent] Eroare AST: {e}")
            return ""

    def _generate_with_retries(self, query, diagram_type, rag_context, ast_context):
        dtype_str = diagram_type.value if hasattr(diagram_type, "value") else str(diagram_type)
        prompt = GENERATION_PROMPT.format(
            diagram_type=dtype_str,
            user_request=query,
            rag_context=rag_context or "Nu există exemple în baza de cunoștințe.",
            ast_context=ast_context or "",
        )

        last_code = ""
        last_response = ""

        for attempt in range(self.max_retries):
            try:
                if attempt == 0:
                    raw_text = self._generate(prompt)
                else:
                    _, errors = self.validator.validate(last_code)
                    correction_prompt = CORRECTION_PROMPT.format(
                        errors="\n".join(errors),
                        broken_diagram=last_code,
                    )
                    raw_text = self._generate(correction_prompt)

                last_response = raw_text
                last_code = self._extract_mermaid(raw_text)
                is_valid, errors = self.validator.validate(last_code)
                print(f"[Agent] Attempt {attempt + 1}: valid={is_valid}, erori={len(errors)}")

                if is_valid:
                    return last_code, attempt, raw_text

            except Exception as e:
                print(f"[Agent] Eroare attempt {attempt + 1}: {e}")

        return last_code, self.max_retries - 1, last_response

    def _extract_mermaid(self, text: str) -> str:
        match = re.search(r"```mermaid\s*(.*?)\s*```", text, re.DOTALL)
        if match:
            return match.group(1).strip()
        for starter in ["flowchart", "graph ", "classDiagram", "sequenceDiagram", "C4Context", "erDiagram"]:
            if text.strip().startswith(starter):
                return text.strip()
        return text.strip()

    def _generate_explanation(self, mermaid_code, diagram_type, query) -> str:
        lines = mermaid_code.split("\n")
        node_count = sum(1 for l in lines if "-->" in l or "---" in l)
        return (
            f"Diagramă {diagram_type} generată cu {len(lines)} linii și "
            f"aproximativ {node_count} relații, bazată pe: {query[:100]}..."
        )
