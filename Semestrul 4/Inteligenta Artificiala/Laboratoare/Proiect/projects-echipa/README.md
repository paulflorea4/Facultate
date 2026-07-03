# 🏗️ Diagram-as-Code AI Assistant

> Agent AI care generează automat diagrame de arhitectură software în format Mermaid, pornind din descrieri text sau cod sursă Python.

---

## 👥 Echipa

| Nume | Rol |
|------|-----|
| Feraru Gabriel | AST Analyzer + RAG Engine |
| Florea Paul | Agent AI + Prompting Chain |
| Ghiorghita Gabriel | CI/CD Integration + Evaluation |
---

## 🎯 Problema abordată

Documentația arhitecturală software se **desincronizează rapid** cu implementarea. Diagrame create manual devin obsolete imediat ce codul evoluează, generând confuzie în echipe și dificultăți de onboarding.

**Soluția propusă:** un agent AI care:
1. Analizează cod Python prin parsare AST statică
2. Interoghează o bază de cunoștințe RAG cu diagrame existente
3. Generează automat diagrame Mermaid corecte sintactic
4. Se integrează în fluxul GitHub (PR comments, git hooks)

**Inputuri:** descrieri text, user stories, cod sursă Python, repository-uri  
**Outputuri:** diagrame Mermaid (flowchart, sequenceDiagram, classDiagram, C4Context)

---

## 🏛️ Arhitectura sistemului

```
┌─────────────────────────────────────────────────────────┐
│                      INTRĂRI                            │
│  [Descriere text]  [User story]  [Cod Python / Repo]   │
└──────────────────────────┬──────────────────────────────┘
                           │
          ┌────────────────┴────────────────┐
          │                                 │
┌─────────▼──────────┐           ┌──────────▼──────────┐
│  Analiză statică   │           │   RAG Engine        │
│  AST Python        │           │   (Sentence-BERT    │
│  - servicii        │           │    + ChromaDB)      │
│  - API-uri         │           │   Bază cunoștințe   │
│  - dependențe      │           │   diagrame DSL      │
└─────────┬──────────┘           └──────────┬──────────┘
          │                                 │
          └────────────────┬────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────┐
│              AGENT AI (Azure + SLM)                    │
│  [Lanț prompting] → [Tool use] → [Validare sintaxă]    │
│  - Intent classification                                 │
│  - Context assembly (RAG retrieved examples)             │
│  - Diagram generation                                    │
│  - Self-evaluation + correction                          │
└──────────────────────────┬──────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────┐
│                    OUTPUT                               │
│  [Mermaid DSL]  [Preview SVG]  [GitHub PR comment]     │
│  [CLI/API/Notebook]  [Git hook validation]             │
└─────────────────────────────────────────────────────────┘
```

---

## 📁 Structura proiectului

```
diagram_ai/
├── README.md
├── requirements.txt
├── .env.example
├── data/
│   ├── raw/              # Diagrame colectate brut
│   ├── processed/        # AST-uri canonice + embeddings
│   └── datasets/         # Dataset final pentru fine-tuning
├── src/
│   ├── ast_analyzer/     # Parsare cod Python → graf dependențe
│   ├── rag_engine/       # Indexare + retrieval diagrame
│   ├── agent/            # Orchestrare agent + prompting
│   ├── integrations/     # GitHub Action + git hooks
│   └── utils/            # Validare Mermaid, metrici
├── notebooks/
│   ├── 01_data_analysis.ipynb     # FAZA I: EDA + definire problemă
│   └── 02_model_development.ipynb # FAZA II: Agent + evaluare + Îmbunătățiri
├── eval/
│   └── metrics.py        # BLEU, exact match, validare sintaxă
├── tests/
└── .github/
    └── workflows/
        └── diagram_update.yml
```

---

## 🚀 Instalare și utilizare

```bash
# 1. Clonare și setup
git clone <repo>
cd diagram_ai
pip install -r requirements.txt

# 2. Configurare API key
cp .env.example .env
# Editați .env și adăugați GEMINI_API_KEY=...

# 3. Indexare bază de cunoștințe RAG (obligatoriu prima dată)
python -m src.agent.cli index data/raw/seed_diagrams.jsonl

# 4. Generare diagramă din text
python -m src.agent.cli generate --text "Descrie un sistem de autentificare cu JWT"

# 5. Generare diagramă din cod sursă
python -m src.agent.cli generate --file src/myapp.py --type flowchart

# 6. Salvare output într-un fișier
python -m src.agent.cli generate --text "Microservices cu RabbitMQ" --output diagram.mmd

# 7. Statistici bază de cunoștințe
python -m src.agent.cli stats

# 8. Pornire API server
python -m src.agent.api_server
```

---

## 📊 Evaluare

| Metric | Descriere | Target |
|--------|-----------|--------|
| Syntax validity | % diagrame Mermaid parsabile | >90% |
| Structural accuracy | Nod/edge overlap cu ground truth | >70% |
| BLEU score | Similaritate textuală DSL | >0.6 |
| Retrieval precision@5 | RAG relevance | >80% |

---

## 🌍 Obiective de Dezvoltare Durabilă (SDG) impactate

- **SDG 8** — Muncă decentă și creștere economică: automatizarea documentației reduce timpul neproductiv al echipelor de software
- **SDG 9** — Industrie, inovație și infrastructură: infrastructură software mai bine documentată = mai ușor de menținut și extins
- **SDG 17** — Parteneriate pentru obiective: open-source, favorizeaza colaborarea între echipe distribuite

---

## 📚 Bibliografie

- Clemente & Cândea (2019). Software architecture documentation. *Journal of Systems and Software*
- Devlin et al. (2019). BERT. *NAACL-HLT*
- Reimers & Gurevych (2019). Sentence-BERT. *EMNLP*
- Richards & Ford (2020). *Fundamentals of Software Architecture*. O'Reilly
- Jurafsky & Martin (2023). *Speech and Language Processing* (3rd ed.)
