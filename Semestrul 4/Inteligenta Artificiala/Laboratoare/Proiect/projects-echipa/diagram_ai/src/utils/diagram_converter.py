"""
utils/diagram_converter.py
Conversie diagrame Mermaid → alte formate DSL:
  - PlantUML
  - Graphviz DOT
  - Structurizr DSL (subset)

Suportă:
  - flowchart / graph  → toate formatele
  - classDiagram       → PlantUML / DOT
  - sequenceDiagram    → PlantUML
"""

import re
from dataclasses import dataclass
from enum import Enum
from typing import Optional


class OutputFormat(str, Enum):
    MERMAID = "mermaid"
    PLANTUML = "plantuml"
    GRAPHVIZ = "graphviz"
    STRUCTURIZR = "structurizr"


@dataclass
class ConversionResult:
    output_code: str
    output_format: OutputFormat
    source_diagram_type: str
    warnings: list[str]
    success: bool


# ─── Parsare internă Mermaid ────────────────────────────────────────────────

def _parse_mermaid_edges(lines: list[str]) -> list[tuple[str, str, str]]:
    """Returnează lista de (src, dst, label) pentru edge-uri simple."""
    edges = []
    pattern = re.compile(
        r'(\w[\w\s]*)(?:\[.*?\]|\(.*?\)|\{.*?\})?\s*'
        r'(?:-->|---|->>|-->>'
        r'|-.->|==>)\|?([^|]*)\|?\s*'
        r'(\w[\w\s]*)(?:\[.*?\]|\(.*?\)|\{.*?\})?'
    )
    simple = re.compile(
        r'^(\w+)\s*(?:-->|---|->|->>|-->>'
        r'|-.->|==>)\s*(\w+)(?::\s*(.+))?'
    )
    for line in lines:
        stripped = line.strip()
        if stripped.startswith('%%') or not stripped:
            continue
        m = simple.match(stripped)
        if m:
            edges.append((m.group(1), m.group(2), m.group(3) or ""))
    return edges


def _parse_node_labels(lines: list[str]) -> dict[str, str]:
    """Extrage ID→label din definiții de noduri: A[Label] sau A(Label)."""
    labels: dict[str, str] = {}
    node_def = re.compile(r'^(\w+)\s*[\[\(\{]([^\]\)\}]+)[\]\)\}]')
    for line in lines:
        m = node_def.match(line.strip())
        if m:
            labels[m.group(1)] = m.group(2).strip('"')
    return labels


def _detect_type(first_line: str) -> str:
    for dtype in ["flowchart", "graph", "classDiagram", "sequenceDiagram",
                  "C4Context", "erDiagram", "stateDiagram"]:
        if first_line.strip().startswith(dtype):
            return dtype
    return "unknown"


# ─── Conversia la PlantUML ───────────────────────────────────────────────────

def _flowchart_to_plantuml(lines: list[str], labels: dict[str, str]) -> str:
    edges = _parse_mermaid_edges(lines[1:])
    nodes: set[str] = set()
    for src, dst, _ in edges:
        nodes.add(src)
        nodes.add(dst)

    out = ["@startuml", ""]
    for nid in sorted(nodes):
        label = labels.get(nid, nid)
        out.append(f':{label};')
    out.append("")
    for src, dst, label in edges:
        src_lbl = labels.get(src, src)
        dst_lbl = labels.get(dst, dst)
        if label:
            out.append(f':{src_lbl}; --> :{dst_lbl}; : {label}')
        else:
            out.append(f':{src_lbl}; --> :{dst_lbl};')
    out.append("")
    out.append("@enduml")
    return "\n".join(out)


def _class_diagram_to_plantuml(lines: list[str]) -> str:
    out = ["@startuml", ""]
    class_pat = re.compile(r'^\s*class\s+(\w+)\s*\{?')
    inherit_pat = re.compile(r'^\s*(\w+)\s*<\|--\s*(\w+)')
    rel_pat = re.compile(r'^\s*(\w+)\s*-->\s*(\w+)(?:\s*:\s*(.+))?')
    member_pat = re.compile(r'^\s*[+\-#~](.+)')
    in_class = False
    for line in lines[1:]:
        stripped = line.strip()
        if not stripped or stripped.startswith('%%'):
            continue
        m_cls = class_pat.match(stripped)
        if m_cls:
            out.append(f'class {m_cls.group(1)} {{')
            in_class = '{' in stripped
            continue
        if in_class:
            if stripped == '}':
                out.append('}')
                in_class = False
            else:
                out.append(f'    {stripped}')
            continue
        m_inh = inherit_pat.match(stripped)
        if m_inh:
            out.append(f'{m_inh.group(1)} <|-- {m_inh.group(2)}')
            continue
        m_rel = rel_pat.match(stripped)
        if m_rel:
            label = f' : {m_rel.group(3)}' if m_rel.group(3) else ''
            out.append(f'{m_rel.group(1)} --> {m_rel.group(2)}{label}')
    out.append("")
    out.append("@enduml")
    return "\n".join(out)


def _sequence_to_plantuml(lines: list[str]) -> str:
    out = ["@startuml", ""]
    part_pat = re.compile(r'^\s*participant\s+(\w+)(?:\s+as\s+(.+))?')
    msg_pat = re.compile(r'^\s*(\w+)\s*(->>|-->|->|-->>)\s*(\w+)\s*:\s*(.+)')
    for line in lines[1:]:
        stripped = line.strip()
        if not stripped or stripped.startswith('%%'):
            continue
        m_p = part_pat.match(stripped)
        if m_p:
            alias = m_p.group(2) or m_p.group(1)
            out.append(f'participant "{alias}" as {m_p.group(1)}')
            continue
        m_msg = msg_pat.match(stripped)
        if m_msg:
            arrow_map = {"->>": "->", "-->": "-->", "->": "->", "-->>": "-->"}
            arrow = arrow_map.get(m_msg.group(2), "->")
            out.append(f'{m_msg.group(1)} {arrow} {m_msg.group(3)} : {m_msg.group(4)}')
            continue
        low = stripped.lower()
        if low in ('end', 'else'):
            out.append('end')
        elif low.startswith('loop') or low.startswith('alt') or low.startswith('opt'):
            out.append(stripped)
    out.append("")
    out.append("@enduml")
    return "\n".join(out)


def to_plantuml(mermaid_code: str) -> ConversionResult:
    """Converteste cod Mermaid la PlantUML."""
    lines = mermaid_code.strip().split('\n')
    diagram_type = _detect_type(lines[0])
    labels = _parse_node_labels(lines[1:])
    warnings: list[str] = []

    try:
        if diagram_type in ('flowchart', 'graph'):
            code = _flowchart_to_plantuml(lines, labels)
        elif diagram_type == 'classDiagram':
            code = _class_diagram_to_plantuml(lines)
        elif diagram_type == 'sequenceDiagram':
            code = _sequence_to_plantuml(lines)
        else:
            warnings.append(f"Tip nesprijinit pentru PlantUML: {diagram_type}. Conversie parțială.")
            edges = _parse_mermaid_edges(lines[1:])
            code = "@startuml\n"
            for src, dst, label in edges:
                code += f"{src} --> {dst}" + (f" : {label}" if label else "") + "\n"
            code += "@enduml"

        return ConversionResult(
            output_code=code,
            output_format=OutputFormat.PLANTUML,
            source_diagram_type=diagram_type,
            warnings=warnings,
            success=True,
        )
    except Exception as e:
        return ConversionResult(
            output_code="",
            output_format=OutputFormat.PLANTUML,
            source_diagram_type=diagram_type,
            warnings=[f"Eroare conversie PlantUML: {e}"],
            success=False,
        )


# ─── Conversia la Graphviz DOT ───────────────────────────────────────────────

def to_graphviz(mermaid_code: str) -> ConversionResult:
    """Converteste cod Mermaid la Graphviz DOT."""
    lines = mermaid_code.strip().split('\n')
    diagram_type = _detect_type(lines[0])
    labels = _parse_node_labels(lines[1:])
    edges = _parse_mermaid_edges(lines[1:])
    warnings: list[str] = []

    # Direcție
    direction = "LR" if "LR" in lines[0].upper() else "TD"
    dot_rankdir = "LR" if direction == "LR" else "TB"

    # Tip graf
    if diagram_type == 'sequenceDiagram':
        warnings.append("sequenceDiagram → Graphviz: conversia e aproximativă.")

    out = [
        f'digraph diagram {{',
        f'    rankdir={dot_rankdir};',
        f'    node [shape=box, style=filled, fillcolor="#e8f4fd", fontname="Helvetica"];',
        f'    edge [fontname="Helvetica", fontsize=10];',
        '',
    ]

    # Noduri cu label
    all_nodes: set[str] = set()
    for src, dst, _ in edges:
        all_nodes.add(src)
        all_nodes.add(dst)
    # și noduri standalone din labels
    for nid in labels:
        all_nodes.add(nid)

    for nid in sorted(all_nodes):
        label = labels.get(nid, nid)
        out.append(f'    {nid} [label="{label}"];')

    out.append('')

    # Edge-uri
    for src, dst, label in edges:
        if label:
            out.append(f'    {src} -> {dst} [label="{label}"];')
        else:
            out.append(f'    {src} -> {dst};')

    # Clase din classDiagram
    if diagram_type == 'classDiagram':
        inherit_pat = re.compile(r'^\s*(\w+)\s*<\|--\s*(\w+)')
        for line in lines[1:]:
            m = inherit_pat.match(line)
            if m:
                out.append(f'    {m.group(2)} -> {m.group(1)} [arrowhead=empty, label="inherits"];')

    out.append('}')

    return ConversionResult(
        output_code="\n".join(out),
        output_format=OutputFormat.GRAPHVIZ,
        source_diagram_type=diagram_type,
        warnings=warnings,
        success=True,
    )


# ─── Conversia la Structurizr DSL ────────────────────────────────────────────

def to_structurizr(mermaid_code: str, system_name: str = "Software System") -> ConversionResult:
    """
    Converteste cod Mermaid la Structurizr DSL (C4 subset — workspace + system context).
    Cel mai relevant pentru flowchart-uri și C4Context.
    """
    lines = mermaid_code.strip().split('\n')
    diagram_type = _detect_type(lines[0])
    labels = _parse_node_labels(lines[1:])
    edges = _parse_mermaid_edges(lines[1:])
    warnings: list[str] = []

    if diagram_type not in ('flowchart', 'graph', 'C4Context'):
        warnings.append(
            f"Structurizr DSL este cel mai potrivit pentru flowchart/C4Context. "
            f"Tipul '{diagram_type}' va fi tratat ca sistem generic."
        )

    all_nodes: set[str] = set()
    for src, dst, _ in edges:
        all_nodes.add(src)
        all_nodes.add(dst)

    out = [
        "workspace {",
        f'    name "{system_name}"',
        "",
        "    model {",
    ]

    # Generează elemente
    for nid in sorted(all_nodes):
        label = labels.get(nid, nid)
        safe_label = label.replace('"', "'")
        out.append(f'        {nid} = softwareSystem "{safe_label}"')

    out.append("")

    # Relații
    for src, dst, label in edges:
        if label:
            out.append(f'        {src} -> {dst} "{label}"')
        else:
            out.append(f'        {src} -> {dst}')

    out += [
        "    }",
        "",
        "    views {",
        f'        systemContext {next(iter(sorted(all_nodes)), "system")} "SystemContext" {{',
        "            include *",
        "            autoLayout",
        "        }",
        "",
        '        theme default',
        "    }",
        "}",
    ]

    return ConversionResult(
        output_code="\n".join(out),
        output_format=OutputFormat.STRUCTURIZR,
        source_diagram_type=diagram_type,
        warnings=warnings,
        success=True,
    )


# ─── API unificat ─────────────────────────────────────────────────────────────

def convert(
    mermaid_code: str,
    output_format: OutputFormat,
    **kwargs,
) -> ConversionResult:
    """
    Punct de intrare unificat pentru toate conversiile.

    Args:
        mermaid_code:   Codul Mermaid sursă.
        output_format:  Formatul de output dorit.
        **kwargs:       Argumente specifice formatului (ex: system_name pentru Structurizr).
    """
    if output_format == OutputFormat.MERMAID:
        lines = mermaid_code.strip().split('\n')
        return ConversionResult(
            output_code=mermaid_code,
            output_format=OutputFormat.MERMAID,
            source_diagram_type=_detect_type(lines[0]),
            warnings=[],
            success=True,
        )
    elif output_format == OutputFormat.PLANTUML:
        return to_plantuml(mermaid_code)
    elif output_format == OutputFormat.GRAPHVIZ:
        return to_graphviz(mermaid_code)
    elif output_format == OutputFormat.STRUCTURIZR:
        return to_structurizr(mermaid_code, **kwargs)
    else:
        return ConversionResult(
            output_code="",
            output_format=output_format,
            source_diagram_type="unknown",
            warnings=[f"Format necunoscut: {output_format}"],
            success=False,
        )


def get_file_extension(fmt: OutputFormat) -> str:
    """Returnează extensia de fișier pentru un format dat."""
    return {
        OutputFormat.MERMAID: ".mmd",
        OutputFormat.PLANTUML: ".puml",
        OutputFormat.GRAPHVIZ: ".dot",
        OutputFormat.STRUCTURIZR: ".dsl",
    }.get(fmt, ".txt")
