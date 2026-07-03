"""
tests/test_ast_analyzer.py
Teste pentru modulul de analiză AST Python.
"""

import sys
from pathlib import Path
sys.path.insert(0, str(Path(__file__).parent.parent))

import pytest
from diagram_ai.src.ast_analyzer.analyzer import PythonASTAnalyzer


@pytest.fixture
def analyzer():
    return PythonASTAnalyzer()


SIMPLE_CODE = '''
import os
from typing import Optional

class Animal:
    def __init__(self, name: str):
        self.name = name
    def speak(self) -> str:
        return ""
    def move(self):
        pass

class Dog(Animal):
    def speak(self) -> str:
        return "Woof!"
    def fetch(self, item: str) -> str:
        return f"Fetched {item}"

def create_animal(name: str, animal_type: str = "dog") -> Animal:
    if animal_type == "dog":
        return Dog(name)
    return Animal(name)
'''


def test_analyze_classes(analyzer):
    module = analyzer.analyze_source(SIMPLE_CODE, "test_module")
    assert len(module.classes) == 2
    class_names = {c.name for c in module.classes}
    assert "Animal" in class_names
    assert "Dog" in class_names


def test_analyze_inheritance(analyzer):
    module = analyzer.analyze_source(SIMPLE_CODE, "test_module")
    dog = next(c for c in module.classes if c.name == "Dog")
    assert "Animal" in dog.bases


def test_analyze_methods(analyzer):
    module = analyzer.analyze_source(SIMPLE_CODE, "test_module")
    animal = next(c for c in module.classes if c.name == "Animal")
    assert "speak" in animal.methods
    assert "move" in animal.methods


def test_analyze_functions(analyzer):
    module = analyzer.analyze_source(SIMPLE_CODE, "test_module")
    assert len(module.functions) >= 1
    fn_names = {f.name for f in module.functions}
    assert "create_animal" in fn_names


def test_analyze_imports(analyzer):
    module = analyzer.analyze_source(SIMPLE_CODE, "test_module")
    assert "os" in module.imports
    assert "typing" in module.from_imports


def test_mermaid_class_diagram(analyzer):
    module = analyzer.analyze_source(SIMPLE_CODE, "test_module")
    diagram = analyzer.to_mermaid_class_diagram(module)
    assert diagram.startswith("classDiagram")
    assert "Animal" in diagram
    assert "Dog" in diagram
    assert "<|--" in diagram  # inheritance


def test_mermaid_flowchart(analyzer):
    module = analyzer.analyze_source(SIMPLE_CODE, "test_module")
    diagram = analyzer.to_mermaid_flowchart(module)
    assert diagram.startswith("flowchart")
    assert "test_module" in diagram


def test_empty_code(analyzer):
    module = analyzer.analyze_source("", "empty")
    assert module.name == "empty"
    assert len(module.classes) == 0
    assert len(module.functions) == 0


def test_syntax_error(analyzer):
    with pytest.raises(SyntaxError):
        analyzer.analyze_source("def broken(: pass", "broken")
