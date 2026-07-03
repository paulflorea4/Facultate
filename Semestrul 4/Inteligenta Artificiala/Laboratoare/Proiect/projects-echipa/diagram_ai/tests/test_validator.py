"""
tests/test_validator.py
Teste pentru modulul de validare sintaxă Mermaid.
"""

import sys
from pathlib import Path
sys.path.insert(0, str(Path(__file__).parent.parent))

import pytest
from diagram_ai.src.utils.validator import MermaidValidator


@pytest.fixture
def validator():
    return MermaidValidator()


VALID_FLOWCHART = """flowchart TD
    A[Start] --> B[Process]
    B --> C{Decision}
    C -->|Yes| D[End]
    C -->|No| A"""

VALID_CLASS_DIAGRAM = """classDiagram
    class User {
        +id: int
        +email: str
        +login()
    }
    class Admin {
        +permissions: list
    }
    User <|-- Admin"""

VALID_SEQUENCE = """sequenceDiagram
    participant A as Client
    participant B as Server
    A->>B: Request
    B-->>A: Response"""


# === Teste diagrame valide ===

def test_valid_flowchart(validator):
    is_valid, errors = validator.validate(VALID_FLOWCHART)
    assert is_valid is True
    assert len(errors) == 0


def test_valid_class_diagram(validator):
    is_valid, errors = validator.validate(VALID_CLASS_DIAGRAM)
    assert is_valid is True


def test_valid_sequence(validator):
    is_valid, errors = validator.validate(VALID_SEQUENCE)
    assert is_valid is True


# === Teste diagrame invalide ===

def test_empty_string(validator):
    is_valid, errors = validator.validate("")
    assert is_valid is False
    assert len(errors) > 0


def test_invalid_diagram_type(validator):
    is_valid, errors = validator.validate("notadiagram\n    A --> B")
    assert is_valid is False


def test_missing_content(validator):
    is_valid, errors = validator.validate("flowchart TD")
    assert is_valid is False


def test_unclosed_quotes(validator):
    bad = 'flowchart TD\n    A["Unclosed --> B'
    is_valid, errors = validator.validate(bad)
    # Ghilimele neînchise trebuie detectate
    assert is_valid is False or len(errors) > 0


def test_sequence_unclosed_loop(validator):
    bad = """sequenceDiagram
    participant A
    A->>A: Request
    loop Retry
        A->>A: Retry"""  # lipsește end
    is_valid, errors = validator.validate(bad)
    assert is_valid is False
    assert any("loop" in e.lower() for e in errors)


# === Teste detaliate ===

def test_detailed_node_count(validator):
    result = validator.validate_detailed(VALID_FLOWCHART)
    assert result.node_count > 0


def test_detailed_edge_count(validator):
    result = validator.validate_detailed(VALID_FLOWCHART)
    assert result.edge_count > 0


def test_detect_diagram_type(validator):
    result = validator.validate_detailed(VALID_FLOWCHART)
    assert result.diagram_type == "flowchart"

    result2 = validator.validate_detailed(VALID_CLASS_DIAGRAM)
    assert result2.diagram_type == "classDiagram"


def test_graph_lr_valid(validator):
    diagram = "graph LR\n    A --> B --> C"
    is_valid, errors = validator.validate(diagram)
    assert is_valid is True
