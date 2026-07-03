"""
ast_analyzer/analyzer.py
Parsează cod Python folosind modulul ast standard și extrage:
- clase și metodele lor
- funcții și semnăturile lor
- importuri / dependențe
- relații de moștenire
- apeluri de funcții între module
"""

import ast
import os
from dataclasses import dataclass, field
from typing import Optional
import networkx as nx


@dataclass
class ClassInfo:
    name: str
    bases: list[str]
    methods: list[str]
    attributes: list[str]
    decorators: list[str]
    lineno: int


@dataclass
class FunctionInfo:
    name: str
    args: list[str]
    returns: Optional[str]
    calls: list[str]
    decorators: list[str]
    is_async: bool
    lineno: int


@dataclass
class ModuleInfo:
    name: str
    path: str
    imports: list[str]
    from_imports: dict[str, list[str]]
    classes: list[ClassInfo]
    functions: list[FunctionInfo]
    global_calls: list[str]


@dataclass
class CodeGraph:
    """Graf de dependențe extras din codul sursă."""
    modules: dict[str, ModuleInfo] = field(default_factory=dict)
    dependency_graph: nx.DiGraph = field(default_factory=nx.DiGraph)
    call_graph: nx.DiGraph = field(default_factory=nx.DiGraph)

    def summary(self) -> dict:
        return {
            "num_modules": len(self.modules),
            "num_classes": sum(len(m.classes) for m in self.modules.values()),
            "num_functions": sum(len(m.functions) for m in self.modules.values()),
            "num_dependencies": self.dependency_graph.number_of_edges(),
            "num_calls": self.call_graph.number_of_edges(),
        }


class PythonASTAnalyzer:
    """
    Analizor static pentru cod Python.
    Extrage structura codului și o convertește într-un graf de dependențe
    care poate fi transformat ulterior în diagrame Mermaid.
    """

    def analyze_file(self, filepath: str) -> ModuleInfo:
        """Analizează un singur fișier Python."""
        with open(filepath, "r", encoding="utf-8") as f:
            source = f.read()

        module_name = os.path.splitext(os.path.basename(filepath))[0]
        return self._parse_source(source, module_name, filepath)

    def analyze_directory(self, dirpath: str) -> CodeGraph:
        """Analizează recursiv un director și construiește graful complet."""
        graph = CodeGraph()

        for root, _, files in os.walk(dirpath):
            for fname in files:
                if fname.endswith(".py") and not fname.startswith("_"):
                    fpath = os.path.join(root, fname)
                    try:
                        module_info = self.analyze_file(fpath)
                        graph.modules[module_info.name] = module_info
                    except SyntaxError:
                        pass

        self._build_graphs(graph)
        return graph

    def analyze_source(self, source: str, name: str = "module") -> ModuleInfo:
        """Analizează cod Python dat ca string."""
        return self._parse_source(source, name, "<string>")

    def _parse_source(self, source: str, name: str, path: str) -> ModuleInfo:
        tree = ast.parse(source)
        visitor = _ASTVisitor()
        visitor.visit(tree)

        return ModuleInfo(
            name=name,
            path=path,
            imports=visitor.imports,
            from_imports=visitor.from_imports,
            classes=visitor.classes,
            functions=visitor.functions,
            global_calls=visitor.global_calls,
        )

    def _build_graphs(self, graph: CodeGraph) -> None:
        """Construiește grafuri de dependențe între module."""
        module_names = set(graph.modules.keys())

        for mod_name, mod_info in graph.modules.items():
            graph.dependency_graph.add_node(mod_name)
            graph.call_graph.add_node(mod_name)

            # Dependențe din importuri
            for imp in mod_info.imports:
                base = imp.split(".")[0]
                if base in module_names:
                    graph.dependency_graph.add_edge(mod_name, base)

            for from_mod in mod_info.from_imports:
                base = from_mod.split(".")[0]
                if base in module_names:
                    graph.dependency_graph.add_edge(mod_name, base)

    def to_mermaid_flowchart(self, module_info: ModuleInfo) -> str:
        """Convertește un ModuleInfo într-o diagramă Mermaid flowchart."""
        lines = ["flowchart TD"]

        # Nod principal modul
        lines.append(f'    {module_info.name}["{module_info.name}"]')

        # Clase
        for cls in module_info.classes:
            cls_id = f"{module_info.name}_{cls.name}"
            lines.append(f'    {cls_id}["{cls.name}"]')
            lines.append(f"    {module_info.name} --> {cls_id}")

            for method in cls.methods[:5]:  # max 5 metode afișate
                method_id = f"{cls_id}_{method}"
                lines.append(f'    {method_id}("{method}()")')
                lines.append(f"    {cls_id} --> {method_id}")

        # Importuri externe relevante
        for imp in module_info.imports[:8]:
            imp_id = imp.replace(".", "_")
            lines.append(f'    ext_{imp_id}["{imp}"]:::external')
            lines.append(f"    {module_info.name} -.->|imports| ext_{imp_id}")

        lines.append("    classDef external fill:#f5f5f5,stroke:#aaa")
        return "\n".join(lines)

    def to_mermaid_class_diagram(self, module_info: ModuleInfo) -> str:
        """Convertește clasele unui modul într-o classDiagram Mermaid."""
        lines = ["classDiagram"]

        for cls in module_info.classes:
            lines.append(f"    class {cls.name} {{")
            for attr in cls.attributes[:6]:
                lines.append(f"        +{attr}")
            for method in cls.methods[:6]:
                lines.append(f"        +{method}()")
            lines.append("    }")

            # Relații de moștenire
            for base in cls.bases:
                if base != "object":
                    lines.append(f"    {base} <|-- {cls.name}")

        return "\n".join(lines)

    def to_mermaid_graph(self, code_graph: CodeGraph) -> str:
        """Convertește graful de dependențe între module într-o diagramă Mermaid."""
        lines = ["graph LR"]

        for mod_name in code_graph.modules:
            safe_name = mod_name.replace("-", "_")
            lines.append(f'    {safe_name}["{mod_name}"]')

        for src, dst in code_graph.dependency_graph.edges():
            src_safe = src.replace("-", "_")
            dst_safe = dst.replace("-", "_")
            lines.append(f"    {src_safe} --> {dst_safe}")

        return "\n".join(lines)


class _ASTVisitor(ast.NodeVisitor):
    """Visitor intern care extrage informații din AST."""

    def __init__(self):
        self.imports: list[str] = []
        self.from_imports: dict[str, list[str]] = {}
        self.classes: list[ClassInfo] = []
        self.functions: list[FunctionInfo] = []
        self.global_calls: list[str] = []
        self._current_class: Optional[str] = None

    def visit_Import(self, node: ast.Import):
        for alias in node.names:
            self.imports.append(alias.name)
        self.generic_visit(node)

    def visit_ImportFrom(self, node: ast.ImportFrom):
        if node.module:
            names = [alias.name for alias in node.names]
            self.from_imports[node.module] = names
        self.generic_visit(node)

    def visit_ClassDef(self, node: ast.ClassDef):
        bases = []
        for base in node.bases:
            if isinstance(base, ast.Name):
                bases.append(base.id)
            elif isinstance(base, ast.Attribute):
                bases.append(f"{base.value.id}.{base.attr}" if isinstance(base.value, ast.Name) else base.attr)

        methods = []
        attributes = []
        for item in node.body:
            if isinstance(item, (ast.FunctionDef, ast.AsyncFunctionDef)):
                methods.append(item.name)
            elif isinstance(item, ast.Assign):
                for target in item.targets:
                    if isinstance(target, ast.Name):
                        attributes.append(target.id)

        decorators = [
            d.id if isinstance(d, ast.Name) else
            (d.attr if isinstance(d, ast.Attribute) else "decorator")
            for d in node.decorator_list
        ]

        self.classes.append(ClassInfo(
            name=node.name,
            bases=bases,
            methods=methods,
            attributes=attributes,
            decorators=decorators,
            lineno=node.lineno,
        ))

        old = self._current_class
        self._current_class = node.name
        self.generic_visit(node)
        self._current_class = old

    def visit_FunctionDef(self, node: ast.FunctionDef):
        self._process_function(node, is_async=False)

    def visit_AsyncFunctionDef(self, node: ast.AsyncFunctionDef):
        self._process_function(node, is_async=True)

    def _process_function(self, node, is_async: bool):
        if self._current_class:
            return  # metodele sunt deja capturate în ClassInfo

        args = [arg.arg for arg in node.args.args if arg.arg != "self"]

        returns = None
        if node.returns:
            if isinstance(node.returns, ast.Name):
                returns = node.returns.id
            elif isinstance(node.returns, ast.Constant):
                returns = str(node.returns.value)

        calls = []
        for child in ast.walk(node):
            if isinstance(child, ast.Call):
                if isinstance(child.func, ast.Name):
                    calls.append(child.func.id)
                elif isinstance(child.func, ast.Attribute):
                    calls.append(child.func.attr)

        decorators = [
            d.id if isinstance(d, ast.Name) else
            (d.attr if isinstance(d, ast.Attribute) else "decorator")
            for d in node.decorator_list
        ]

        self.functions.append(FunctionInfo(
            name=node.name,
            args=args,
            returns=returns,
            calls=list(set(calls)),
            decorators=decorators,
            is_async=is_async,
            lineno=node.lineno,
        ))
        self.generic_visit(node)

    def visit_Expr(self, node: ast.Expr):
        if isinstance(node.value, ast.Call):
            if isinstance(node.value.func, ast.Name):
                self.global_calls.append(node.value.func.id)
        self.generic_visit(node)
