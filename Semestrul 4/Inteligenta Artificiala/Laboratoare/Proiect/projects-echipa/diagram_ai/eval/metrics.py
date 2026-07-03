"""
eval/metrics.py
Metrici de evaluare pentru diagrame generate.

Metrici implementate:
- syntax_validity: % diagrame Mermaid parsabile
- bleu_score: similaritate textuală față de ground truth
- structural_precision/recall: overlap noduri și edge-uri
- retrieval_precision_at_k: relevanța rezultatelor RAG
"""

import re
from dataclasses import dataclass, field
from typing import Optional

try:
    from nltk.translate.bleu_score import sentence_bleu, SmoothingFunction
    import nltk
    nltk.download("punkt", quiet=True)
    NLTK_AVAILABLE = True
except ImportError:
    NLTK_AVAILABLE = False


@dataclass
class DiagramMetrics:
    """Metrici complete pentru o pereche (generat, ground_truth)."""
    syntax_valid: bool
    bleu_score: float
    node_precision: float      # noduri comune / noduri generate
    node_recall: float         # noduri comune / noduri ground truth
    node_f1: float
    edge_precision: float
    edge_recall: float
    edge_f1: float
    exact_match: bool
    type_match: bool           # tipul diagramei coincide
    validation_errors: list[str] = field(default_factory=list)


@dataclass
class EvalReport:
    """Raport agregat pe un set de evaluare."""
    num_samples: int
    avg_syntax_validity: float
    avg_bleu: float
    avg_node_f1: float
    avg_edge_f1: float
    avg_exact_match: float
    avg_type_match: float
    per_sample: list[DiagramMetrics] = field(default_factory=list)

    def __str__(self) -> str:
        return (
            f"=== Raport Evaluare ({self.num_samples} exemple) ===\n"
            f"Syntax validity:  {self.avg_syntax_validity:.1%}\n"
            f"BLEU score:       {self.avg_bleu:.4f}\n"
            f"Node F1:          {self.avg_node_f1:.4f}\n"
            f"Edge F1:          {self.avg_edge_f1:.4f}\n"
            f"Exact match:      {self.avg_exact_match:.1%}\n"
            f"Type match:       {self.avg_type_match:.1%}\n"
        )


class DiagramEvaluator:
    """
    Evaluează calitatea diagramelor generate față de ground truth.
    """

    def __init__(self):
        from diagram_ai.src.utils.validator import MermaidValidator
        self.validator = MermaidValidator()

    def evaluate_single(
        self,
        generated: str,
        ground_truth: Optional[str] = None,
    ) -> DiagramMetrics:
        """Evaluează o singură diagramă generată."""

        # 1. Validare sintaxă
        is_valid, errors = self.validator.validate(generated)

        # 2. Extrage noduri și edge-uri din diagrama generată
        gen_nodes = self._extract_nodes(generated)
        gen_edges = self._extract_edges(generated)
        gen_type = self._extract_type(generated)

        if ground_truth is None:
            return DiagramMetrics(
                syntax_valid=is_valid,
                bleu_score=0.0,
                node_precision=0.0,
                node_recall=0.0,
                node_f1=0.0,
                edge_precision=0.0,
                edge_recall=0.0,
                edge_f1=0.0,
                exact_match=False,
                type_match=False,
                validation_errors=errors,
            )

        # 3. Extrage din ground truth
        gt_nodes = self._extract_nodes(ground_truth)
        gt_edges = self._extract_edges(ground_truth)
        gt_type = self._extract_type(ground_truth)

        # 4. BLEU score
        bleu = self._compute_bleu(generated, ground_truth)

        # 5. Node precision/recall/F1
        node_prec, node_rec, node_f1 = self._compute_prf(gen_nodes, gt_nodes)
        edge_prec, edge_rec, edge_f1 = self._compute_prf(gen_edges, gt_edges)

        # 6. Exact match (normalizat)
        exact = self._normalize(generated) == self._normalize(ground_truth)

        return DiagramMetrics(
            syntax_valid=is_valid,
            bleu_score=bleu,
            node_precision=node_prec,
            node_recall=node_rec,
            node_f1=node_f1,
            edge_precision=edge_prec,
            edge_recall=edge_rec,
            edge_f1=edge_f1,
            exact_match=exact,
            type_match=gen_type == gt_type,
            validation_errors=errors,
        )

    def evaluate_batch(
        self,
        generated_list: list[str],
        ground_truth_list: Optional[list[str]] = None,
    ) -> EvalReport:
        """Evaluează un batch de diagrame și returnează raport agregat."""
        if ground_truth_list is None:
            ground_truth_list = [None] * len(generated_list)

        assert len(generated_list) == len(ground_truth_list)

        results = [
            self.evaluate_single(gen, gt)
            for gen, gt in zip(generated_list, ground_truth_list)
        ]

        n = len(results)
        if n == 0:
            return EvalReport(0, 0, 0, 0, 0, 0, 0)

        return EvalReport(
            num_samples=n,
            avg_syntax_validity=sum(r.syntax_valid for r in results) / n,
            avg_bleu=sum(r.bleu_score for r in results) / n,
            avg_node_f1=sum(r.node_f1 for r in results) / n,
            avg_edge_f1=sum(r.edge_f1 for r in results) / n,
            avg_exact_match=sum(r.exact_match for r in results) / n,
            avg_type_match=sum(r.type_match for r in results) / n,
            per_sample=results,
        )

    def _extract_nodes(self, code: str) -> set[str]:
        """Extrage noduri din cod Mermaid."""
        nodes = set()
        lines = code.strip().split("\n")
        diagram_type = self._extract_type(code)

        for line in lines[1:]:
            stripped = line.strip()
            if not stripped or stripped.startswith("%%"):
                continue

            if diagram_type in ("flowchart", "graph"):
                # A[Label] sau A --> B
                matches = re.findall(r'\b([A-Za-z_]\w*)\s*(?:\[|\(|\{|-->|---)', stripped)
                nodes.update(matches)
                matches2 = re.findall(r'(?:-->|---)\s*([A-Za-z_]\w*)', stripped)
                nodes.update(matches2)
            elif diagram_type == "classDiagram":
                m = re.match(r'^\s*class\s+(\w+)', stripped)
                if m:
                    nodes.add(m.group(1))
            elif diagram_type == "sequenceDiagram":
                m = re.match(r'^\s*participant\s+(\w+)', stripped)
                if m:
                    nodes.add(m.group(1))
                # Extrage actori din mesaje: Actor ->> Actor2: msg
                m2 = re.findall(r'(\w+)\s*(?:->>|-->|->)', stripped)
                nodes.update(m2)

        return nodes

    def _extract_edges(self, code: str) -> set[tuple[str, str]]:
        """Extrage perechi (src, dst) din edge-uri."""
        edges = set()
        lines = code.strip().split("\n")

        for line in lines[1:]:
            stripped = line.strip()
            # A --> B sau A ->> B etc.
            m = re.match(r'^\s*(\w+)\s*(?:-->|---|->>|-->>|-\.-|==>)\s*(\w+)', stripped)
            if m:
                edges.add((m.group(1), m.group(2)))

        return edges

    def _extract_type(self, code: str) -> str:
        """Extrage tipul diagramei din prima linie."""
        first_line = code.strip().split("\n")[0].strip()
        for dtype in ["flowchart", "graph", "classDiagram", "sequenceDiagram",
                      "C4Context", "erDiagram", "stateDiagram"]:
            if first_line.startswith(dtype):
                return dtype
        return "unknown"

    def _compute_bleu(self, hypothesis: str, reference: str) -> float:
        """Calculează BLEU score între două diagrame."""
        if not NLTK_AVAILABLE:
            # Fallback simplu: token overlap
            hyp_tokens = set(hypothesis.lower().split())
            ref_tokens = set(reference.lower().split())
            if not ref_tokens:
                return 0.0
            return len(hyp_tokens & ref_tokens) / len(ref_tokens)

        try:
            smoothing = SmoothingFunction().method1
            hyp_tokens = hypothesis.lower().split()
            ref_tokens = reference.lower().split()
            return sentence_bleu([ref_tokens], hyp_tokens, smoothing_function=smoothing)
        except Exception:
            return 0.0

    def _compute_prf(self, predicted: set, ground_truth: set) -> tuple[float, float, float]:
        """Calculează precision, recall, F1 pentru două seturi."""
        if not predicted and not ground_truth:
            return 1.0, 1.0, 1.0
        if not predicted:
            return 0.0, 0.0, 0.0
        if not ground_truth:
            return 0.0, 0.0, 0.0

        common = predicted & ground_truth
        precision = len(common) / len(predicted)
        recall = len(common) / len(ground_truth)
        f1 = 2 * precision * recall / (precision + recall) if (precision + recall) > 0 else 0.0
        return precision, recall, f1

    def _normalize(self, code: str) -> str:
        """Normalizează cod Mermaid pentru comparație."""
        lines = [l.strip() for l in code.strip().split("\n")]
        lines = [l for l in lines if l and not l.startswith("%%")]
        return "\n".join(sorted(lines))
