"""
agent/api_server.py
REST API minimal pentru agentul de generare diagrame.

Endpoints:
  POST /generate      — Generează o diagramă
  POST /analyze       — Analizează cod Python și returnează structura
  GET  /health        — Health check
  GET  /stats         — Statistici RAG
"""

import os
import sys
from pathlib import Path
from typing import Optional

from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
import uvicorn

sys.path.insert(0, str(Path(__file__).parent.parent.parent))

from src.agent.diagram_agent import DiagramAgent, AgentInput, DiagramType
from src.rag_engine.engine import RAGEngine
from src.ast_analyzer.analyzer import PythonASTAnalyzer

app = FastAPI(
    title="Diagram-as-Code AI Assistant",
    description="Generează automat diagrame Mermaid din text sau cod Python",
    version="1.0.0",
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_methods=["*"],
    allow_headers=["*"],
)

# Inițializare singleton-uri (lazy)
_agent: Optional[DiagramAgent] = None
_rag: Optional[RAGEngine] = None


def get_rag() -> RAGEngine:
    global _rag
    if _rag is None:
        db_path = os.getenv("CHROMA_DB_PATH", "./data/chromadb")
        _rag = RAGEngine(db_path=db_path)
    return _rag


def get_agent() -> DiagramAgent:
    global _agent
    if _agent is None:
        _agent = DiagramAgent(rag_engine=get_rag())
    return _agent


# === Modele Pydantic ===

class GenerateRequest(BaseModel):
    text_description: Optional[str] = None
    source_code: Optional[str] = None
    diagram_type: Optional[str] = None
    context: Optional[str] = None

    class Config:
        json_schema_extra = {
            "example": {
                "text_description": "Un sistem de autentificare cu JWT și refresh tokens",
                "diagram_type": "sequenceDiagram",
            }
        }


class GenerateResponse(BaseModel):
    mermaid_code: str
    diagram_type: str
    is_valid: bool
    validation_errors: list[str]
    retrieved_examples_count: int
    retries_used: int
    explanation: str


class AnalyzeRequest(BaseModel):
    source_code: str
    output_format: str = "flowchart"  # flowchart | classDiagram | json


class AnalyzeResponse(BaseModel):
    module_name: str
    classes_count: int
    functions_count: int
    imports_count: int
    mermaid_diagram: str
    structure_json: dict


# === Endpoints ===

@app.get("/health")
async def health():
    return {"status": "ok", "version": "1.0.0"}


@app.get("/stats")
async def stats():
    try:
        rag = get_rag()
        return rag.get_stats()
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


@app.post("/generate", response_model=GenerateResponse)
async def generate_diagram(request: GenerateRequest):
    """
    Generează o diagramă Mermaid din descriere text și/sau cod Python.
    """
    if not request.text_description and not request.source_code:
        raise HTTPException(
            status_code=400,
            detail="Furnizați cel puțin 'text_description' sau 'source_code'"
        )

    dtype = None
    if request.diagram_type:
        try:
            dtype = DiagramType(request.diagram_type)
        except ValueError:
            raise HTTPException(
                status_code=400,
                detail=f"Tip diagramă invalid: {request.diagram_type}. "
                       f"Valori valide: {[d.value for d in DiagramType]}"
            )

    agent_input = AgentInput(
        text_description=request.text_description,
        source_code=request.source_code,
        requested_diagram_type=dtype,
        context=request.context,
    )

    try:
        agent = get_agent()
        result = agent.run(agent_input)

        return GenerateResponse(
            mermaid_code=result.mermaid_code,
            diagram_type=result.diagram_type.value if hasattr(result.diagram_type, "value") else str(result.diagram_type),
            is_valid=result.is_valid,
            validation_errors=result.validation_errors,
            retrieved_examples_count=result.retrieved_examples_count,
            retries_used=result.retries_used,
            explanation=result.explanation,
        )
    except ValueError as e:
        raise HTTPException(status_code=500, detail=str(e))
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Eroare internă: {str(e)}")


@app.post("/analyze", response_model=AnalyzeResponse)
async def analyze_code(request: AnalyzeRequest):
    """
    Analizează cod Python prin AST și returnează structura + opțional diagramă Mermaid.
    """
    analyzer = PythonASTAnalyzer()

    try:
        module_info = analyzer.analyze_source(request.source_code, name="analyzed")

        mermaid_diagram = ""
        if request.output_format == "classDiagram":
            mermaid_diagram = analyzer.to_mermaid_class_diagram(module_info)
        else:
            mermaid_diagram = analyzer.to_mermaid_flowchart(module_info)

        structure_json = {
            "name": module_info.name,
            "imports": module_info.imports,
            "from_imports": module_info.from_imports,
            "classes": [
                {
                    "name": c.name,
                    "bases": c.bases,
                    "methods": c.methods,
                    "attributes": c.attributes,
                }
                for c in module_info.classes
            ],
            "functions": [
                {
                    "name": f.name,
                    "args": f.args,
                    "returns": f.returns,
                    "is_async": f.is_async,
                }
                for f in module_info.functions
            ],
        }

        return AnalyzeResponse(
            module_name=module_info.name,
            classes_count=len(module_info.classes),
            functions_count=len(module_info.functions),
            imports_count=len(module_info.imports) + len(module_info.from_imports),
            mermaid_diagram=mermaid_diagram,
            structure_json=structure_json,
        )

    except SyntaxError as e:
        raise HTTPException(status_code=400, detail=f"Eroare sintaxă Python: {str(e)}")
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


if __name__ == "__main__":
    port = int(os.getenv("PORT", "8000"))
    uvicorn.run("src.agent.api_server:app", host="0.0.0.0", port=port, reload=True)
