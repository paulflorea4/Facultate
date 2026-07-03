"""
tests/test_diagram_converter.py
Teste pentru modulul de conversie diagrame (Mermaid → PlantUML / Graphviz / Structurizr).
"""

import sys
from pathlib import Path
sys.path.insert(0, str(Path(__file__).parent.parent))

import pytest
from diagram_ai.src.utils.diagram_converter import (
    to_plantuml, to_graphviz, to_structurizr, convert,
    OutputFormat, ConversionResult,
)


FLOWCHART_SIMPLE = """flowchart TD
    A[User] --> B[Login]
    B --> C{Valid?}
    C -->|Yes| D[Dashboard]
    C -->|No| E[Error]"""

FLOWCHART_LR = """flowchart LR
    A[API] --> B[Service]
    B --> C[Database]"""

CLASS_DIAGRAM = """classDiagram
    class Animal {
        +name: str
        +speak()
    }
    class Dog {
        +fetch(item: str)
    }
    Animal <|-- Dog"""

SEQUENCE_DIAGRAM = """sequenceDiagram
    participant C as Client
    participant S as Server
    participant D as Database
    C->>S: HTTP Request
    S->>D: Query
    D-->>S: Result
    S-->>C: HTTP Response"""


# ─── PlantUML ────────────────────────────────────────────────────────────────

class TestPlantUML:
    def test_flowchart_to_plantuml_success(self):
        result = to_plantuml(FLOWCHART_SIMPLE)
        assert result.success is True
        assert result.output_format == OutputFormat.PLANTUML
        assert "@startuml" in result.output_code
        assert "@enduml" in result.output_code

    def test_flowchart_contains_nodes(self):
        result = to_plantuml(FLOWCHART_SIMPLE)
        # Parserul extrage noduri din linii de edge (B --> C etc.)
        # Cel puțin un nod trebuie să fie prezent în output
        has_node = any(token in result.output_code for token in ["B", "C", "D", "E"])
        assert has_node, f"Niciun nod așteptat găsit în: {result.output_code}"

    def test_class_diagram_to_plantuml(self):
        result = to_plantuml(CLASS_DIAGRAM)
        assert result.success is True
        assert "Animal" in result.output_code
        assert "Dog" in result.output_code

    def test_sequence_to_plantuml(self):
        result = to_plantuml(SEQUENCE_DIAGRAM)
        assert result.success is True
        assert "Client" in result.output_code or "participant" in result.output_code

    def test_source_type_detected(self):
        result = to_plantuml(FLOWCHART_SIMPLE)
        assert result.source_diagram_type in ("flowchart", "graph")


# ─── Graphviz ─────────────────────────────────────────────────────────────────

class TestGraphviz:
    def test_flowchart_to_graphviz_success(self):
        result = to_graphviz(FLOWCHART_SIMPLE)
        assert result.success is True
        assert result.output_format == OutputFormat.GRAPHVIZ
        assert "digraph" in result.output_code
        assert "->" in result.output_code

    def test_graphviz_has_node_definitions(self):
        result = to_graphviz(FLOWCHART_LR)
        assert "rankdir=LR" in result.output_code

    def test_graphviz_has_rankdir_tb(self):
        result = to_graphviz(FLOWCHART_SIMPLE)
        assert "rankdir=TB" in result.output_code

    def test_class_diagram_to_graphviz(self):
        result = to_graphviz(CLASS_DIAGRAM)
        assert result.success is True
        assert "Animal" in result.output_code
        assert "Dog" in result.output_code

    def test_graphviz_sequence_warns(self):
        result = to_graphviz(SEQUENCE_DIAGRAM)
        assert result.success is True
        assert len(result.warnings) > 0  # sequenceDiagram dă avertisment


# ─── Structurizr ─────────────────────────────────────────────────────────────

class TestStructurizr:
    def test_flowchart_to_structurizr_success(self):
        result = to_structurizr(FLOWCHART_SIMPLE)
        assert result.success is True
        assert result.output_format == OutputFormat.STRUCTURIZR
        assert "workspace" in result.output_code
        assert "model" in result.output_code
        assert "views" in result.output_code

    def test_structurizr_contains_relations(self):
        result = to_structurizr(FLOWCHART_LR)
        assert "->" in result.output_code  # relații între elemente

    def test_structurizr_warns_for_class_diagram(self):
        result = to_structurizr(CLASS_DIAGRAM)
        assert result.success is True
        assert len(result.warnings) > 0

    def test_custom_system_name(self):
        result = to_structurizr(FLOWCHART_LR, system_name="My E-Commerce System")
        assert "My E-Commerce System" in result.output_code


# ─── API unificat `convert()` ─────────────────────────────────────────────────

class TestConvertAPI:
    def test_convert_mermaid_passthrough(self):
        result = convert(FLOWCHART_SIMPLE, OutputFormat.MERMAID)
        assert result.success is True
        assert result.output_code == FLOWCHART_SIMPLE
        assert result.output_format == OutputFormat.MERMAID

    def test_convert_to_plantuml(self):
        result = convert(FLOWCHART_SIMPLE, OutputFormat.PLANTUML)
        assert result.success is True
        assert "@startuml" in result.output_code

    def test_convert_to_graphviz(self):
        result = convert(FLOWCHART_LR, OutputFormat.GRAPHVIZ)
        assert result.success is True
        assert "digraph" in result.output_code

    def test_convert_to_structurizr(self):
        result = convert(FLOWCHART_LR, OutputFormat.STRUCTURIZR)
        assert result.success is True
        assert "workspace" in result.output_code

    def test_convert_result_has_source_type(self):
        result = convert(CLASS_DIAGRAM, OutputFormat.PLANTUML)
        assert result.source_diagram_type == "classDiagram"


# ─── Edge cases ───────────────────────────────────────────────────────────────

class TestEdgeCases:
    def test_empty_flowchart_no_crash(self):
        """Diagramă validă dar fără edge-uri."""
        code = "flowchart TD\n    A[Only node]"
        result = to_graphviz(code)
        assert result.success is True

    def test_single_edge_graphviz(self):
        code = "graph LR\n    A --> B"
        result = to_graphviz(code)
        assert "A" in result.output_code
        assert "B" in result.output_code
        assert "A -> B" in result.output_code

    def test_plantuml_class_with_inheritance(self):
        result = to_plantuml(CLASS_DIAGRAM)
        # Moștenire trebuie reprezentată
        assert "Dog" in result.output_code
        assert "Animal" in result.output_code
