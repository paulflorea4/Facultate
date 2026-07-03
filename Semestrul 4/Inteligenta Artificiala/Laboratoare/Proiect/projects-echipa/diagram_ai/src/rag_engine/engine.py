"""
rag_engine/engine.py
Motor RAG pentru diagrame Mermaid.
Folosește ChromaDB cu embedding function built-in (fără sentence_transformers).
"""

import json
import os
from dataclasses import dataclass
from typing import Optional

import chromadb
from chromadb.utils.embedding_functions import DefaultEmbeddingFunction


@dataclass
class DiagramExample:
    doc_id: str
    description: str
    diagram_type: str
    mermaid_code: str
    source: str
    metadata: dict


@dataclass
class RetrievalResult:
    examples: list[DiagramExample]
    distances: list[float]
    query: str


class RAGEngine:
    def __init__(self, db_path: str = "./data/chromadb", collection_name: str = "diagrams"):
        self.db_path = db_path
        self.collection_name = collection_name

        print(f"[RAG] Inițializare ChromaDB la: {db_path}")
        os.makedirs(db_path, exist_ok=True)

        self.client = chromadb.PersistentClient(path=db_path)
        self.ef = DefaultEmbeddingFunction()

        self.collection = self.client.get_or_create_collection(
            name=collection_name,
            embedding_function=self.ef,
            metadata={"hnsw:space": "cosine"},
        )
        print(f"[RAG] Colecție '{collection_name}': {self.collection.count()} documente")

    def index_examples(self, examples: list[DiagramExample]) -> int:
        if not examples:
            return 0

        documents, metadatas, ids = [], [], []

        for ex in examples:
            doc_text = f"[{ex.diagram_type}] {ex.description}\n\n{ex.mermaid_code}"
            documents.append(doc_text)
            metadatas.append({
                "diagram_type": ex.diagram_type,
                "source": ex.source,
                "description": ex.description[:500],
                "mermaid_code": ex.mermaid_code,
            })
            ids.append(ex.doc_id)

        self.collection.upsert(
            documents=documents,
            metadatas=metadatas,
            ids=ids,
        )
        print(f"[RAG] Indexate {len(examples)} diagrame. Total: {self.collection.count()}")
        return len(examples)

    def retrieve(self, query: str, diagram_type: Optional[str] = None, top_k: int = 5) -> RetrievalResult:
        count = self.collection.count()
        if count == 0:
            return RetrievalResult(examples=[], distances=[], query=query)

        where_filter = {"diagram_type": {"$eq": diagram_type}} if diagram_type else None

        results = self.collection.query(
            query_texts=[query],
            n_results=min(top_k, count),
            where=where_filter,
            include=["documents", "metadatas", "distances"],
        )

        examples = []
        distances = results["distances"][0] if results["distances"] else []

        for i, meta in enumerate(results["metadatas"][0]):
            examples.append(DiagramExample(
                doc_id=results["ids"][0][i],
                description=meta.get("description", ""),
                diagram_type=meta.get("diagram_type", "flowchart"),
                mermaid_code=meta.get("mermaid_code", ""),
                source=meta.get("source", "unknown"),
                metadata={},
            ))

        return RetrievalResult(examples=examples, distances=distances, query=query)

    def format_few_shot_context(self, result: RetrievalResult) -> str:
        if not result.examples:
            return "Nu există exemple disponibile în baza de cunoștințe."

        parts = ["=== EXEMPLE RELEVANTE DIN BAZA DE CUNOȘTINȚE ===\n"]
        for i, (ex, dist) in enumerate(zip(result.examples, result.distances), 1):
            similarity = 1 - dist
            parts.append(f"--- Exemplu {i} (similaritate: {similarity:.2f}) ---")
            parts.append(f"Descriere: {ex.description}")
            parts.append(f"Tip: {ex.diagram_type}")
            parts.append(f"Cod Mermaid:\n```mermaid\n{ex.mermaid_code}\n```\n")

        return "\n".join(parts)

    def load_from_jsonl(self, filepath: str) -> int:
        examples = []
        with open(filepath, "r", encoding="utf-8") as f:
            for line_num, line in enumerate(f):
                line = line.strip()
                if not line:
                    continue
                try:
                    data = json.loads(line)
                    examples.append(DiagramExample(
                        doc_id=data.get("id", f"doc_{line_num}"),
                        description=data["description"],
                        diagram_type=data.get("diagram_type", "flowchart"),
                        mermaid_code=data["mermaid_code"],
                        source=data.get("source", "jsonl"),
                        metadata=data.get("metadata", {}),
                    ))
                except (KeyError, json.JSONDecodeError) as e:
                    print(f"[RAG] Eroare linie {line_num}: {e}")

        return self.index_examples(examples)

    def get_stats(self) -> dict:
        count = self.collection.count()
        if count == 0:
            return {"total": 0}

        all_docs = self.collection.get(include=["metadatas"])
        type_counts: dict[str, int] = {}
        source_counts: dict[str, int] = {}

        for meta in all_docs["metadatas"]:
            dt = meta.get("diagram_type", "unknown")
            src = meta.get("source", "unknown")
            type_counts[dt] = type_counts.get(dt, 0) + 1
            source_counts[src] = source_counts.get(src, 0) + 1

        return {"total": count, "by_type": type_counts, "by_source": source_counts}