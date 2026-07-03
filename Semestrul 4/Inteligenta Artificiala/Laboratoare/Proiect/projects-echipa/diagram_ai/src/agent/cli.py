"""
agent/cli.py
Interfață CLI pentru agentul de generare diagrame.

Utilizare:
  python -m src.agent.cli generate --text "Descrie un sistem de autentificare"
  python -m src.agent.cli generate --file myapp.py --type classDiagram
  python -m src.agent.cli generate --text "..." --output diagram.mmd
  python -m src.agent.cli generate --text "..." --format plantuml
  python -m src.agent.cli convert diagram.mmd --format graphviz
"""

import sys
import os
import click
from pathlib import Path
from rich.console import Console
from rich.panel import Panel
from rich.syntax import Syntax
from rich.table import Table

sys.path.insert(0, str(Path(__file__).parent.parent.parent))

from src.agent.diagram_agent import DiagramAgent, AgentInput, DiagramType
from src.rag_engine.engine import RAGEngine
from src.utils.diagram_converter import convert as convert_diagram, OutputFormat, get_file_extension

# Fix encoding Windows (cp1252 nu suporta emoji) — forteaza UTF-8
if sys.platform == "win32":
    import io
    sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding="utf-8", errors="replace")
    sys.stderr = io.TextIOWrapper(sys.stderr.buffer, encoding="utf-8", errors="replace")

console = Console(force_terminal=True, highlight=False)


def get_agent(use_rag: bool = True) -> DiagramAgent:
    """Inițializează agentul cu sau fără RAG."""
    rag = None
    if use_rag:
        db_path = os.getenv("CHROMA_DB_PATH", "./data/chromadb")
        try:
            rag = RAGEngine(db_path=db_path)
        except Exception as e:
            console.print(f"[yellow]⚠️  RAG indisponibil: {e}[/yellow]")

    return DiagramAgent(rag_engine=rag)


@click.group()
def cli():
    """🏗️  Diagram-as-Code AI Assistant — Generează diagrame Mermaid din text sau cod."""
    pass


@cli.command()
@click.option("--text", "-t", help="Descriere text a arhitecturii")
@click.option("--file", "-f", "source_file", help="Fișier Python de analizat")
@click.option("--type", "-d", "diagram_type",
              type=click.Choice(["flowchart", "classDiagram", "sequenceDiagram", "C4Context", "erDiagram"]),
              help="Tipul de diagramă dorit")
@click.option("--output", "-o", help="Fișier de output (.mmd / .puml / .dot / .dsl)")
@click.option("--format", "-F", "output_format",
              type=click.Choice(["mermaid", "plantuml", "graphviz", "structurizr"]),
              default="mermaid", show_default=True,
              help="Formatul de output al diagramei")
@click.option("--no-rag", is_flag=True, default=False, help="Dezactivează RAG engine")
def generate(text, source_file, diagram_type, output, output_format, no_rag):
    """Generează o diagramă Mermaid din text și/sau cod sursă."""

    if not text and not source_file:
        console.print("[red]❌ Furnizați cel puțin --text sau --file[/red]")
        raise click.Abort()

    console.print(Panel.fit(
        "[bold blue]🏗️ Diagram-as-Code AI Assistant[/bold blue]\n"
        "Generare diagramă în curs...",
        border_style="blue"
    ))

    # Citire cod sursă dacă e furnizat fișier
    source_code = None
    if source_file:
        try:
            with open(source_file, "r", encoding="utf-8") as f:
                source_code = f.read()
            console.print(f"[green]✓ Fișier citit:[/green] {source_file} ({len(source_code)} caractere)")
        except FileNotFoundError:
            console.print(f"[red]❌ Fișierul nu există: {source_file}[/red]")
            raise click.Abort()

    # Inițializare agent
    agent = get_agent(use_rag=not no_rag)

    # Construire input
    dtype = DiagramType(diagram_type) if diagram_type else None
    agent_input = AgentInput(
        text_description=text,
        source_code=source_code,
        source_file=source_file if source_file else None,
        requested_diagram_type=dtype,
    )

    # Rulare agent
    with console.status("[bold green]Agent în execuție..."):
        result = agent.run(agent_input)

    # Afișare rezultat
    console.print()

    # Tabel statistici
    table = Table(title="📊 Rezultat generare", show_header=True)
    table.add_column("Proprietate", style="cyan")
    table.add_column("Valoare", style="white")
    table.add_row("Tip diagramă", str(result.diagram_type))
    table.add_row("Valid sintactic", "✅ Da" if result.is_valid else "❌ Nu")
    table.add_row("Exemple RAG folosite", str(result.retrieved_examples_count))
    table.add_row("Retry-uri folosite", str(result.retries_used))
    console.print(table)

    if result.validation_errors:
        console.print(f"\n[yellow]⚠️  Avertismente:[/yellow]")
        for err in result.validation_errors:
            console.print(f"  • {err}")

    # Conversie format
    fmt = OutputFormat(output_format)
    if fmt != OutputFormat.MERMAID:
        conv = convert_diagram(result.mermaid_code, fmt)
        if not conv.success:
            console.print(f"[yellow]⚠️  Conversie {output_format} eșuată: {conv.warnings}[/yellow]")
            final_code = result.mermaid_code
            final_fmt_label = "mermaid"
        else:
            final_code = conv.output_code
            final_fmt_label = output_format
            if conv.warnings:
                for w in conv.warnings:
                    console.print(f"[yellow]ℹ️  {w}[/yellow]")
    else:
        final_code = result.mermaid_code
        final_fmt_label = "mermaid"

    # Afișare diagramă
    lang_map = {"mermaid": "markdown", "plantuml": "text", "graphviz": "dot", "structurizr": "text"}
    lang = lang_map.get(final_fmt_label, "text")
    console.print(f"\n[bold]📄 Diagramă generată [{final_fmt_label.upper()}]:[/bold]")
    syntax = Syntax(final_code, lang, theme="monokai", line_numbers=True)
    console.print(Panel(syntax, border_style="green"))

    console.print(f"\n[dim]{result.explanation}[/dim]")

    # Salvare output
    if output:
        # Auto-extensie dacă nu e specificată
        out_path = output
        if not Path(output).suffix and fmt != OutputFormat.MERMAID:
            out_path = output + get_file_extension(fmt)
            
        # Creare automată a directoarelor lipsă
        Path(out_path).parent.mkdir(parents=True, exist_ok=True)
        
        with open(out_path, "w", encoding="utf-8") as f:
            f.write(final_code)
        console.print(f"\n[green]✅ Salvat în:[/green] {out_path}")
    else:
        if fmt == OutputFormat.MERMAID:
            console.print("\n[dim]💡 Tip: Copiați diagrama la https://mermaid.live pentru preview[/dim]")
        elif fmt == OutputFormat.PLANTUML:
            console.print("\n[dim]💡 Tip: Copiați la https://www.plantuml.com/plantuml/uml/ pentru preview[/dim]")
        elif fmt == OutputFormat.GRAPHVIZ:
            console.print("\n[dim]💡 Tip: Vizualizați cu 'dot -Tsvg diagram.dot > diagram.svg'[/dim]")


@cli.command()
@click.argument("input_file")
@click.option("--format", "-F", "output_format",
              type=click.Choice(["plantuml", "graphviz", "structurizr"]),
              required=True, help="Formatul de output")
@click.option("--output", "-o", help="Fișier de output (opțional)")
def convert(input_file, output_format, output):
    """Convertește un fișier .mmd Mermaid la alt format (PlantUML, Graphviz, Structurizr)."""
    try:
        with open(input_file, "r", encoding="utf-8") as f:
            mermaid_code = f.read()
    except FileNotFoundError:
        console.print(f"[red]❌ Fișierul nu există: {input_file}[/red]")
        raise click.Abort()

    fmt = OutputFormat(output_format)
    result = convert_diagram(mermaid_code, fmt)

    if not result.success:
        console.print(f"[red]❌ Conversie eșuată: {result.warnings}[/red]")
        raise click.Abort()

    for w in result.warnings:
        console.print(f"[yellow]ℹ️  {w}[/yellow]")

    lang_map = {"plantuml": "text", "graphviz": "dot", "structurizr": "text"}
    lang = lang_map.get(output_format, "text")
    console.print(f"\n[bold]📄 Diagramă convertită [{output_format.upper()}]:[/bold]")
    syntax = Syntax(result.output_code, lang, theme="monokai", line_numbers=True)
    console.print(Panel(syntax, border_style="cyan"))

    if output:
        out_path = output
        if not Path(output).suffix:
            out_path = output + get_file_extension(fmt)
            
        Path(out_path).parent.mkdir(parents=True, exist_ok=True)
        
        with open(out_path, "w", encoding="utf-8") as f:
            f.write(result.output_code)
        console.print(f"\n[green]✅ Salvat în:[/green] {out_path}")


@cli.command()
@click.argument("jsonl_file")
def index(jsonl_file):
    """Indexează diagrame dintr-un fișier JSONL în baza de cunoștințe RAG."""
    db_path = os.getenv("CHROMA_DB_PATH", "./data/chromadb")

    console.print(f"[blue]📚 Indexare din:[/blue] {jsonl_file}")

    try:
        rag = RAGEngine(db_path=db_path)
        count = rag.load_from_jsonl(jsonl_file)
        console.print(f"[green]✅ Indexate {count} diagrame[/green]")

        stats = rag.get_stats()
        console.print(f"Total în baza de cunoștințe: {stats['total']}")
        if "by_type" in stats:
            for dtype, cnt in stats["by_type"].items():
                console.print(f"  {dtype}: {cnt}")
    except Exception as e:
        console.print(f"[red]❌ Eroare: {e}[/red]")


@cli.command()
def stats():
    """Afișează statistici despre baza de cunoștințe RAG."""
    db_path = os.getenv("CHROMA_DB_PATH", "./data/chromadb")
    try:
        rag = RAGEngine(db_path=db_path)
        info = rag.get_stats()

        table = Table(title="📚 Statistici bază de cunoștințe RAG")
        table.add_column("Metric", style="cyan")
        table.add_column("Valoare", style="white")
        table.add_row("Total diagrame indexate", str(info.get("total", 0)))

        if "by_type" in info:
            for dtype, cnt in info["by_type"].items():
                table.add_row(f"  Tip: {dtype}", str(cnt))
        if "by_source" in info:
            for src, cnt in info["by_source"].items():
                table.add_row(f"  Sursă: {src}", str(cnt))

        console.print(table)
    except Exception as e:
        console.print(f"[red]❌ Eroare: {e}[/red]")


if __name__ == "__main__":
    cli()
