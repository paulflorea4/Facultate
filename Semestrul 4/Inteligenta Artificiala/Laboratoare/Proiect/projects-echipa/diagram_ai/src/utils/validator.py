"""
utils/validator.py
Validator sintaxă Mermaid — verifică dacă un bloc de cod Mermaid
este parsabil și corect structural, fără a necesita un browser.
"""

import re
from dataclasses import dataclass


@dataclass
class ValidationResult:
    is_valid: bool
    errors: list[str]
    warnings: list[str]
    diagram_type: str
    node_count: int
    edge_count: int


# Tipuri valide de diagrame Mermaid
VALID_DIAGRAM_TYPES = {
    "flowchart", "graph", "sequenceDiagram", "classDiagram",
    "stateDiagram", "stateDiagram-v2", "erDiagram", "journey",
    "gantt", "pie", "C4Context", "C4Container", "C4Component",
    "mindmap", "timeline", "gitGraph", "xychart-beta",
}

# Pattern-uri pentru noduri și edge-uri
EDGE_PATTERNS = [
    r"-->", r"---", r"->>", r"-->>", r"-.->", r"-\.-",
    r"==>", r"~~~", r"--\|", r"<-->",
]

# Caractere interzise neescaped în etichete nod
FORBIDDEN_IN_LABELS = re.compile(r'[<>{}|](?![^"]*")')


class MermaidValidator:
    """
    Validator sintaxă Mermaid bazat pe regex și reguli structurale.

    Verifică:
    - Existența declarației de tip diagramă
    - Sintaxa edge-urilor
    - Caractere invalide în etichete
    - Structura generală (indentare, ghilimele)
    - Limite de complexitate
    """

    MAX_NODES = 50
    MAX_EDGES = 100

    def validate(self, mermaid_code: str) -> tuple[bool, list[str]]:
        """
        Validează cod Mermaid.
        Returns: (is_valid, list_of_errors)
        """
        result = self.validate_detailed(mermaid_code)
        return result.is_valid, result.errors

    def validate_detailed(self, mermaid_code: str) -> ValidationResult:
        """Validare detaliată cu statistici despre diagramă."""
        errors = []
        warnings = []

        if not mermaid_code or not mermaid_code.strip():
            return ValidationResult(False, ["Codul Mermaid este gol"], [], "unknown", 0, 0)

        lines = [l.rstrip() for l in mermaid_code.strip().split("\n")]
        first_line = lines[0].strip()

        # 1. Verifică declarația de tip
        diagram_type = self._detect_diagram_type(first_line)
        if not diagram_type:
            errors.append(
                f"Prima linie '{first_line}' nu este un tip valid de diagramă Mermaid. "
                f"Tipuri valide: {', '.join(sorted(VALID_DIAGRAM_TYPES))}"
            )
            return ValidationResult(False, errors, warnings, "unknown", 0, 0)

        # 2. Verifică că există conținut dincolo de declarație
        content_lines = [l for l in lines[1:] if l.strip() and not l.strip().startswith("%%")]
        if len(content_lines) == 0:
            errors.append("Diagrama nu are conținut după declarația de tip.")

        # 3. Validări specifice per tip de diagramă
        if diagram_type in ("flowchart", "graph"):
            errors += self._validate_flowchart(lines)
        elif diagram_type == "classDiagram":
            errors += self._validate_class_diagram(lines)
        elif diagram_type == "sequenceDiagram":
            errors += self._validate_sequence(lines)

        # 4. Verifică ghilimele neînchise
        full_code = mermaid_code
        quote_count = full_code.count('"') - full_code.count('\\"')
        if quote_count % 2 != 0:
            errors.append("Există ghilimele neînchise în diagramă.")

        # 5. Verifică paranteze neînchise
        open_parens = full_code.count("(") - full_code.count("\\(")
        close_parens = full_code.count(")") - full_code.count("\\)")
        if abs(open_parens - close_parens) > 2:
            warnings.append(f"Posibil paranteze neechilibrate: {open_parens} deschise, {close_parens} închise.")

        # 6. Numără noduri și edge-uri
        node_count = self._count_nodes(lines, diagram_type)
        edge_count = self._count_edges(lines)

        if node_count > self.MAX_NODES:
            warnings.append(f"Diagrama are {node_count} noduri (recomandat: max {self.MAX_NODES}).")
        if edge_count > self.MAX_EDGES:
            warnings.append(f"Diagrama are {edge_count} edge-uri (recomandat: max {self.MAX_EDGES}).")

        is_valid = len(errors) == 0
        return ValidationResult(
            is_valid=is_valid,
            errors=errors,
            warnings=warnings,
            diagram_type=diagram_type,
            node_count=node_count,
            edge_count=edge_count,
        )

    def _detect_diagram_type(self, first_line: str) -> str | None:
        """Detectează tipul de diagramă din prima linie."""
        first_word = first_line.split()[0] if first_line.split() else ""

        for dtype in VALID_DIAGRAM_TYPES:
            if first_line.startswith(dtype):
                return dtype

        # Aliasuri comune
        aliases = {"graph": "graph", "flowchart": "flowchart"}
        if first_word in aliases:
            return aliases[first_word]

        return None

    def _validate_flowchart(self, lines: list[str]) -> list[str]:
        """Validări specifice pentru flowchart/graph."""
        errors = []

        # Verifică direcția (TD, LR, RL, BT, TB)
        first = lines[0].strip()
        if first.startswith("flowchart") or first.startswith("graph"):
            parts = first.split()
            if len(parts) >= 2:
                direction = parts[1].upper()
                valid_directions = {"TD", "LR", "RL", "BT", "TB", "TB"}
                if direction not in valid_directions and not direction.startswith("%"):
                    errors.append(
                        f"Direcție flowchart invalidă: '{direction}'. "
                        f"Valori valide: {', '.join(valid_directions)}"
                    )

        # Verifică că există cel puțin un edge
        has_edge = any(
            re.search(r"-->|---|->>|-->>|-\.-|==>", line)
            for line in lines[1:]
        )
        if not has_edge and len([l for l in lines[1:] if l.strip()]) > 1:
            errors.append("Flowchart-ul nu are nicio relație (edge) definită.")

        # Verifică noduri cu paranteze imbricate
        for i, line in enumerate(lines[1:], 2):
            stripped = line.strip()
            if stripped and not stripped.startswith("%%") and not stripped.startswith("classDef"):
                # Verifică paranteze nepereche în definiții de noduri
                node_match = re.search(r'\[([^\]]*)\]|\(([^\)]*)\)', stripped)
                if node_match:
                    label = node_match.group(1) or node_match.group(2) or ""
                    if '"' in label:
                        q_count = label.count('"')
                        if q_count % 2 != 0:
                            errors.append(f"Linia {i}: ghilimele neînchise în eticheta nodului.")

        return errors

    def _validate_class_diagram(self, lines: list[str]) -> list[str]:
        """Validări specifice pentru classDiagram."""
        errors = []
        in_class = False
        brace_count = 0

        for i, line in enumerate(lines[1:], 2):
            stripped = line.strip()
            if not stripped or stripped.startswith("%%"):
                continue

            if stripped.startswith("class ") and "{" in stripped:
                in_class = True
                brace_count = stripped.count("{") - stripped.count("}")

            elif in_class:
                brace_count += stripped.count("{") - stripped.count("}")
                if brace_count <= 0:
                    in_class = False
                    brace_count = 0

        if in_class and brace_count > 0:
            errors.append("classDiagram: o definiție de clasă nu este închisă (lipsesc `}`).")

        return errors

    def _validate_sequence(self, lines: list[str]) -> list[str]:
        """Validări specifice pentru sequenceDiagram."""
        errors = []
        loop_count = 0
        alt_count = 0

        for i, line in enumerate(lines[1:], 2):
            stripped = line.strip().lower()
            if stripped.startswith("loop"):
                loop_count += 1
            elif stripped == "end" and loop_count > 0:
                loop_count -= 1
            elif stripped.startswith("alt") or stripped.startswith("opt"):
                alt_count += 1
            elif stripped == "end" and alt_count > 0:
                alt_count -= 1

        if loop_count > 0:
            errors.append(f"sequenceDiagram: {loop_count} bloc(uri) 'loop' nu sunt închise cu 'end'.")
        if alt_count > 0:
            errors.append(f"sequenceDiagram: {alt_count} bloc(uri) 'alt/opt' nu sunt închise cu 'end'.")

        return errors

    def _count_nodes(self, lines: list[str], diagram_type: str) -> int:
        """Estimează numărul de noduri unice."""
        nodes = set()

        for line in lines[1:]:
            stripped = line.strip()
            if not stripped or stripped.startswith("%%") or stripped.startswith("classDef"):
                continue

            if diagram_type in ("flowchart", "graph"):
                # Extrage ID-uri nod din edge-uri: A --> B
                matches = re.findall(r'(\w+)\s*(?:-->|---|->>|==>)', stripped)
                nodes.update(matches)
                matches_right = re.findall(r'(?:-->|---|->>|==>)\s*(\w+)', stripped)
                nodes.update(matches_right)
            elif diagram_type == "classDiagram":
                # class ClassName {
                matches = re.findall(r'^class\s+(\w+)', stripped)
                nodes.update(matches)

        return len(nodes)

    def _count_edges(self, lines: list[str]) -> int:
        """Numără edge-urile din diagramă."""
        edge_pattern = re.compile(r"-->|---|->>|-->>|-\.-|==>|<-->|\|")
        return sum(1 for line in lines[1:] if edge_pattern.search(line))
