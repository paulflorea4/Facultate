"""
eval/run_eval.py
Script standalone pentru evaluarea agentului pe un set de test.

Utilizare:
  python eval/run_eval.py                     # evalueaza setul de test predefinit
  python eval/run_eval.py --jsonl test.jsonl  # evalueaza un fisier JSONL custom
  python eval/run_eval.py --output results.json  # salveaza rezultatele
"""

import sys
import os
import json
import time
import argparse
from pathlib import Path
from dataclasses import asdict

sys.path.insert(0, str(Path(__file__).parent.parent))

from dotenv import load_dotenv
load_dotenv()

from diagram_ai.src.rag_engine.engine import RAGEngine
from diagram_ai.src.agent.diagram_agent import DiagramAgent, AgentInput, DiagramType
from diagram_ai.src.utils.validator import MermaidValidator
from eval.metrics import DiagramEvaluator, EvalReport


# ─── Set de test predefinit ──────────────────────────────────────────────────

DEFAULT_TEST_SET = [
    {
        "description": "Simple login flow with JWT",
        "input": {
            "text_description": "Simple login flow: user enters credentials, server validates, returns JWT or error",
            "diagram_type": "flowchart",
        },
        "ground_truth": """flowchart TD
    A[User] --> B[Enter credentials]
    B --> C[POST /login]
    C --> D{Valid?}
    D -->|Yes| E[Return JWT]
    D -->|No| F[Return 401 error]""",
    },
    {
        "description": "Repository pattern class diagram",
        "input": {
            "text_description": "Repository pattern: base Repository class with UserRepository and ProductRepository",
            "diagram_type": "classDiagram",
        },
        "ground_truth": """classDiagram
    class Repository {
        +find_by_id(id)
        +save(entity)
        +delete(id)
    }
    class UserRepository {
        +find_by_email(email)
    }
    class ProductRepository {
        +find_by_category(cat)
    }
    Repository <|-- UserRepository
    Repository <|-- ProductRepository""",
    },
    {
        "description": "Client-server sequence diagram",
        "input": {
            "text_description": "Client sends request to server, server queries database, returns response",
            "diagram_type": "sequenceDiagram",
        },
        "ground_truth": """sequenceDiagram
    participant C as Client
    participant S as Server
    participant D as Database
    C->>S: HTTP Request
    S->>D: Query
    D-->>S: Result
    S-->>C: HTTP Response""",
    },
    {
        "description": "Microservices with message queue",
        "input": {
            "text_description": "API Gateway receives orders, places them in RabbitMQ queue, consumed by Order Service and Notification Service",
            "diagram_type": "flowchart",
        },
        "ground_truth": """flowchart TD
    A[API Gateway] --> B[RabbitMQ]
    B --> C[Order Service]
    B --> D[Notification Service]
    C --> E[Database]""",
    },
    {
        "description": "Observer pattern",
        "input": {
            "text_description": "Observer design pattern: Observable with list of observers, each observer has on_event method",
            "diagram_type": "classDiagram",
        },
        "ground_truth": """classDiagram
    class Observable {
        +observers: list
        +subscribe(observer)
        +unsubscribe(observer)
        +notify(event)
    }
    class Observer {
        +on_event(event)
    }
    Observable --> Observer : notifies""",
    },
]


# ─── Runner ──────────────────────────────────────────────────────────────────

def load_test_set_from_jsonl(filepath: str) -> list[dict]:
    """Incarca set de test dintr-un fisier JSONL."""
    tests = []
    with open(filepath, "r", encoding="utf-8") as f:
        for i, line in enumerate(f):
            line = line.strip()
            if not line:
                continue
            try:
                data = json.loads(line)
                tests.append({
                    "description": data.get("description", f"test_{i}"),
                    "input": {
                        "text_description": data["description"],
                        "diagram_type": data.get("diagram_type", "flowchart"),
                    },
                    "ground_truth": data.get("mermaid_code"),
                })
            except (KeyError, json.JSONDecodeError) as e:
                print(f"  ⚠️  Linia {i}: {e}")
    return tests


def run_evaluation(
    test_set: list[dict],
    agent: DiagramAgent,
    evaluator: DiagramEvaluator,
    verbose: bool = True,
) -> tuple[EvalReport, list[dict]]:
    """Ruleaza evaluarea si returneaza raportul + datele brute."""
    generated_diagrams = []
    ground_truths = []
    raw_results = []

    for i, test_case in enumerate(test_set, 1):
        desc = test_case["description"]
        inp = test_case["input"]
        gt = test_case.get("ground_truth")

        if verbose:
            print(f"\n[{i}/{len(test_set)}] {desc}")

        try:
            dtype = None
            if inp.get("diagram_type"):
                try:
                    dtype = DiagramType(inp["diagram_type"])
                except ValueError:
                    pass

            agent_input = AgentInput(
                text_description=inp.get("text_description"),
                source_code=inp.get("source_code"),
                requested_diagram_type=dtype,
            )

            t0 = time.time()
            output = agent.run(agent_input)
            elapsed = time.time() - t0

            metrics = evaluator.evaluate_single(
                generated=output.mermaid_code,
                ground_truth=gt,
            )

            generated_diagrams.append(output.mermaid_code)
            ground_truths.append(gt)

            row = {
                "test_id": i,
                "description": desc,
                "diagram_type": str(output.diagram_type),
                "syntax_valid": metrics.syntax_valid,
                "bleu": round(metrics.bleu_score, 4),
                "node_f1": round(metrics.node_f1, 4),
                "edge_f1": round(metrics.edge_f1, 4),
                "type_match": metrics.type_match,
                "retries": output.retries_used,
                "rag_examples": output.retrieved_examples_count,
                "elapsed_s": round(elapsed, 2),
                "generated": output.mermaid_code,
                "ground_truth": gt or "",
                "errors": output.validation_errors,
            }
            raw_results.append(row)

            if verbose:
                status = "✅ VALID" if metrics.syntax_valid else "❌ INVALID"
                print(f"  {status} | BLEU={metrics.bleu_score:.3f} | Node F1={metrics.node_f1:.3f} | {elapsed:.1f}s")

        except Exception as e:
            if verbose:
                print(f"  ❌ Eroare: {e}")
            raw_results.append({
                "test_id": i,
                "description": desc,
                "error": str(e),
                "syntax_valid": False,
                "bleu": 0,
                "node_f1": 0,
                "edge_f1": 0,
            })
            generated_diagrams.append("")
            ground_truths.append(gt)

    report = evaluator.evaluate_batch(
        generated_list=generated_diagrams,
        ground_truth_list=ground_truths,
    )

    return report, raw_results


def print_report(report: EvalReport, raw_results: list[dict]) -> None:
    """Afiseaza raportul formatat in terminal."""
    print("\n" + "=" * 60)
    print("         RAPORT EVALUARE AGENT DIAGRAM-AS-CODE")
    print("=" * 60)
    print(f"\nNumar exemple:     {report.num_samples}")
    print(f"Syntax validity:   {report.avg_syntax_validity:.1%}")
    print(f"BLEU score:        {report.avg_bleu:.4f}")
    print(f"Node F1:           {report.avg_node_f1:.4f}")
    print(f"Edge F1:           {report.avg_edge_f1:.4f}")
    print(f"Type match:        {report.avg_type_match:.1%}")
    print(f"Exact match:       {report.avg_exact_match:.1%}")

    print("\n--- Detalii per exemplu ---")
    print(f"{'#':<4} {'Descriere':<35} {'Valid':<6} {'BLEU':<6} {'NodeF1':<8} {'s':<5}")
    print("-" * 70)
    for r in raw_results:
        valid = "✅" if r.get("syntax_valid") else "❌"
        bleu = r.get("bleu", 0)
        node_f1 = r.get("node_f1", 0)
        elapsed = r.get("elapsed_s", 0)
        desc = r.get("description", "")[:34]
        print(f"{r['test_id']:<4} {desc:<35} {valid:<6} {bleu:<6.3f} {node_f1:<8.3f} {elapsed:<5.1f}")

    print("\n--- Targets ---")
    syntax_ok = "✅" if report.avg_syntax_validity >= 0.9 else "⚠️ "
    bleu_ok = "✅" if report.avg_bleu >= 0.6 else "⚠️ "
    node_ok = "✅" if report.avg_node_f1 >= 0.7 else "⚠️ "
    print(f"  {syntax_ok} Syntax validity: {report.avg_syntax_validity:.1%}  (target: 90%)")
    print(f"  {bleu_ok} BLEU score:      {report.avg_bleu:.4f}  (target: 0.60)")
    print(f"  {node_ok} Node F1:         {report.avg_node_f1:.4f}  (target: 0.70)")
    print("=" * 60)


def main():
    parser = argparse.ArgumentParser(
        description="Evaluare agent Diagram-as-Code"
    )
    parser.add_argument(
        "--jsonl", default=None,
        help="Fisier JSONL custom cu cazuri de test"
    )
    parser.add_argument(
        "--output", "-o", default=None,
        help="Fisier JSON pentru salvarea rezultatelor"
    )
    parser.add_argument(
        "--no-rag", action="store_true",
        help="Dezactiveaza RAG engine"
    )
    parser.add_argument(
        "--model", default=None,
        help="Deployment Azure OpenAI (ex: gpt-4o, gpt-4o-mini)"
    )
    parser.add_argument(
        "--limit", type=int, default=None,
        help="Limita numarul de teste (pentru debugging rapid)"
    )
    args = parser.parse_args()

    # Setup
    print("🔧 Initializare componente...")

    rag = None
    if not args.no_rag:
        db_path = os.getenv("CHROMA_DB_PATH", "./data/chromadb")
        try:
            rag = RAGEngine(db_path=db_path)
            print(f"  ✅ RAG: {rag.collection.count()} diagrame indexate")
        except Exception as e:
            print(f"  ⚠️  RAG indisponibil: {e}")

    try:
        agent = DiagramAgent(rag_engine=rag, model_name=args.model)
    except ValueError as e:
        print(f"❌ {e}")
        sys.exit(1)

    evaluator = DiagramEvaluator()

    # Incarca test set
    if args.jsonl:
        print(f"\n📂 Incarcare test set: {args.jsonl}")
        test_set = load_test_set_from_jsonl(args.jsonl)
    else:
        print(f"\n📂 Folosire test set predefinit ({len(DEFAULT_TEST_SET)} exemple)")
        test_set = DEFAULT_TEST_SET

    if args.limit:
        test_set = test_set[:args.limit]
        print(f"  ℹ️  Limitat la {args.limit} exemple")

    # Evaluare
    print(f"\n🚀 Start evaluare pe {len(test_set)} exemple...")
    report, raw_results = run_evaluation(test_set, agent, evaluator, verbose=True)

    # Afisare raport
    print_report(report, raw_results)

    # Salvare
    if args.output:
        output_data = {
            "report": {
                "num_samples": report.num_samples,
                "avg_syntax_validity": report.avg_syntax_validity,
                "avg_bleu": report.avg_bleu,
                "avg_node_f1": report.avg_node_f1,
                "avg_edge_f1": report.avg_edge_f1,
                "avg_exact_match": report.avg_exact_match,
                "avg_type_match": report.avg_type_match,
            },
            "per_sample": raw_results,
        }
        os.makedirs(os.path.dirname(os.path.abspath(args.output)), exist_ok=True)
        with open(args.output, "w", encoding="utf-8") as f:
            json.dump(output_data, f, indent=2, ensure_ascii=False)
        print(f"\n💾 Rezultate salvate în: {args.output}")


if __name__ == "__main__":
    main()
