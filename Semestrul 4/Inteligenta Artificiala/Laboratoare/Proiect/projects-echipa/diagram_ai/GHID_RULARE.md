# 🏗️ Ghid de Rulare — Diagram-as-Code AI Assistant

> Ghid pas-cu-pas pentru instalare, configurare și utilizarea tuturor componentelor proiectului.

---

## 📋 Cuprins

1. [Cerințe sistem](#1-cerințe-sistem)
2. [Instalare](#2-instalare)
3. [Configurare API Key](#3-configurare-api-key)
4. [Inițializare bază de cunoștințe RAG](#4-inițializare-bază-de-cunoștințe-rag)
5. [CLI — Generare diagrame](#5-cli--generare-diagrame)
6. [CLI — Conversie formate](#6-cli--conversie-formate)
7. [REST API](#7-rest-api)
8. [Evaluare automată](#8-evaluare-automată)
9. [Demo pipeline complet](#9-demo-pipeline-complet)
10. [Teste](#10-teste)
11. [Git Hook pre-commit](#11-git-hook-pre-commit)
12. [GitHub Action](#12-github-action)
13. [Notebook-uri Jupyter](#13-notebook-uri-jupyter)
14. [Depanare erori frecvente](#14-depanare-erori-frecvente)

---

## 1. Cerințe sistem

| Componentă | Versiune minimă |
|------------|----------------|
| Python     | 3.11+           |
| pip        | 23+             |
| Git        | 2.x             |

**Verificare:**
```bash
python --version
pip --version
git --version
```

---

## 2. Instalare

### 2.1 Clonare repository

```bash
git clone <url-repo>
cd diagram_ai
```

### 2.2 (Recomandat) Creare mediu virtual

```bash
# Windows
python -m venv .venv
.venv\Scripts\activate

# Linux / macOS
python -m venv .venv
source .venv/bin/activate
```

### 2.3 Instalare dependențe

```bash
pip install -r requirements.txt
```

> ⏱️ Prima instalare durează 2–5 minute (PyTorch, ChromaDB sunt pachete mari).

**Verificare instalare:**
```bash
python -c "import openai, chromadb, click, fastapi, networkx; print('OK')"
```

---

## 3. Configurare API Key (Azure OpenAI)

### 3.1 Creare fișier `.env`

```bash
# Windows
copy .env.example .env

# Linux / macOS
cp .env.example .env
```

### 3.2 Editare `.env`

Deschide fișierul `.env` și completează:

```env
AZURE_OPENAI_API_KEY=<cheia_ta_din_azure_portal>
AZURE_OPENAI_ENDPOINT=https://YOUR-RESOURCE.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-4o
AZURE_OPENAI_API_VERSION=2025-01-01-preview
CHROMA_DB_PATH=./data/chromadb
MAX_RETRIES=3
RAG_TOP_K=5
TEMPERATURE=0.2
```

**Unde găsești aceste valori în Azure for Students:**

1. Intră pe [https://portal.azure.com](https://portal.azure.com) cu contul universitar
2. Mergi la resursa ta **Azure OpenAI** (sau creează una: `Create a resource → Azure OpenAI`)
3. Click **"Keys and Endpoint"** în bara stângă
   - `KEY 1` = `AZURE_OPENAI_API_KEY`
   - `Endpoint` = `AZURE_OPENAI_ENDPOINT`
4. Mergi în **Azure AI Studio** (buton `Go to Azure OpenAI Studio`)
5. `Deployments → + Create deployment`
   - Alege modelul: **gpt-4o** sau **gpt-4o-mini** (disponibil Students)
   - Deployment name (e.g. `gpt-4o`) = `AZURE_OPENAI_DEPLOYMENT`

> ⚠️ Deployment name ≠ model name! Dacă ai creat un deployment cu numele `my-gpt4`, pune `AZURE_OPENAI_DEPLOYMENT=my-gpt4`.

**Verificare cheie:**
```bash
python -c "
from dotenv import load_dotenv; import os
load_dotenv()
key = os.getenv('AZURE_OPENAI_API_KEY')
endpoint = os.getenv('AZURE_OPENAI_ENDPOINT')
print('API Key setat:', 'DA' if key and 'your_azure' not in key else 'NU')
print('Endpoint setat:', 'DA' if endpoint and 'your-resource' not in endpoint else 'NU')
"
```

---

## 4. Inițializare bază de cunoștințe RAG

> **Obligatoriu înainte de prima utilizare!** Indexarea populează ChromaDB cu exemple de diagrame.

### 4.1 Indexare dataset de bază (10 exemple)

```bash
python -m src.agent.cli index data/raw/seed_diagrams.jsonl
```

### 4.2 Indexare dataset extins (10 exemple suplimentare)

```bash
python -m src.agent.cli index data/raw/seed_diagrams_extended.jsonl
```

### 4.3 Verificare statistici RAG

```bash
python -m src.agent.cli stats
```

**Output așteptat:**
```
┌─────────────────────────────────────────┐
│    📚 Statistici bază de cunoștințe RAG │
│ Total diagrame indexate │ 20            │
│   Tip: flowchart        │ 10            │
│   Tip: classDiagram     │ 6             │
│   Tip: sequenceDiagram  │ 4             │
└─────────────────────────────────────────┘
```

---

## 5. CLI — Generare diagrame

Toate comenzile se rulează din directorul rădăcină `diagram_ai/`.

### 5.1 Generare din descriere text

```bash
python -m src.agent.cli generate --text "Un sistem de autentificare cu JWT tokens"
```

### 5.2 Generare cu tip specificat explicit

```bash
# Flowchart
python -m src.agent.cli generate --text "Pipeline CI/CD: push code, run tests, build Docker, deploy" --type flowchart

# Diagramă de clase
python -m src.agent.cli generate --text "Repository pattern cu UserRepository si ProductRepository" --type classDiagram

# Diagramă de secvență
python -m src.agent.cli generate --text "Client trimite request la API Gateway, care autentifică cu Auth Service" --type sequenceDiagram

# C4 Context (nivel înalt)
python -m src.agent.cli generate --text "E-commerce platform cu utilizatori, plăți și inventar" --type C4Context
```

### 5.3 Generare din fișier Python (analiză AST)

```bash
python -m src.agent.cli generate --file src/agent/diagram_agent.py --type classDiagram
python -m src.agent.cli generate --file src/rag_engine/engine.py --type flowchart
```

### 5.4 Salvare output în fișier

```bash
# Salvare Mermaid
python -m src.agent.cli generate --text "Microservices cu RabbitMQ" --output diagrams/architecture.mmd

# Salvare PlantUML
python -m src.agent.cli generate --text "Microservices cu RabbitMQ" --format plantuml --output diagrams/architecture.puml

# Salvare Graphviz DOT
python -m src.agent.cli generate --text "Dependențe între module" --format graphviz --output diagrams/deps.dot

# Salvare Structurizr DSL
python -m src.agent.cli generate --text "Sistem e-commerce cu servicii independente" --format structurizr --output diagrams/workspace.dsl
```

### 5.5 Generare fără RAG (mai rapid, fără context)

```bash
python -m src.agent.cli generate --text "Simple login flow" --type flowchart --no-rag
```

### 5.6 Text + cod sursă combinat

```bash
python -m src.agent.cli generate --text "FastAPI application with authentication" --file src/agent/api_server.py --type classDiagram
```

---

## 6. CLI — Conversie formate

Convertește un fișier `.mmd` existent la alt format, fără a apela Gemini API.

### 6.1 Mermaid → PlantUML

```bash
python -m src.agent.cli convert diagrams/architecture.mmd --format plantuml
python -m src.agent.cli convert diagrams/architecture.mmd --format plantuml --output out.puml
```

### 6.2 Mermaid → Graphviz DOT

```bash
python -m src.agent.cli convert diagrams/architecture.mmd --format graphviz
python -m src.agent.cli convert diagrams/architecture.mmd --format graphviz --output out.dot
```

**Randare SVG din DOT:**
```bash
dot -Tsvg out.dot > out.svg
dot -Tpng out.dot > out.png
```

### 6.3 Mermaid → Structurizr DSL

```bash
python -m src.agent.cli convert diagrams/architecture.mmd --format structurizr
python -m src.agent.cli convert diagrams/architecture.mmd --format structurizr --output workspace.dsl
```

---

## 7. REST API

### 7.1 Pornire server

```bash
python -m src.agent.api_server
# sau explicit:
uvicorn src.agent.api_server:app --host 0.0.0.0 --port 8000 --reload
```

Serverul pornește la: `http://localhost:8000`  
Documentație interactivă (Swagger UI): `http://localhost:8000/docs`

### 7.2 Health check

```bash
curl http://localhost:8000/health
# {"status":"ok","version":"1.0.0"}
```

### 7.3 Generare diagramă via API

```bash
curl -X POST http://localhost:8000/generate -H "Content-Type: application/json" -d '{"text_description": "Sistem de autentificare cu JWT", "diagram_type": "sequenceDiagram"}'
```

**Cu cod sursă:**
```bash
curl -X POST http://localhost:8000/generate -H "Content-Type: application/json" -d '{"text_description": "FastAPI app architecture", "source_code": "class UserService:\n    def get_user(self, id): pass\n    def create_user(self, data): pass", "diagram_type": "classDiagram"}'
```

### 7.4 Analiză cod Python via API

```bash
curl -X POST http://localhost:8000/analyze -H "Content-Type: application/json" -d '{"source_code": "class Animal:\n    def speak(self): pass\nclass Dog(Animal):\n    def fetch(self): pass", "output_format": "classDiagram"}'
```

### 7.5 Statistici RAG via API

```bash
curl http://localhost:8000/stats
```

---

## 8. Evaluare automată

### 8.1 Evaluare pe test set predefinit (5 exemple cu ground truth)

```bash
python eval/run_eval.py
```

### 8.2 Evaluare cu salvare rezultate

```bash
python eval/run_eval.py --output data/processed/eval_results.json
```

### 8.3 Evaluare pe fișier JSONL custom

```bash
# Formatul așteptat în JSONL: {"description":"...", "mermaid_code":"...", "diagram_type":"..."}
python eval/run_eval.py --jsonl data/raw/seed_diagrams.jsonl --output results.json
```

### 8.4 Evaluare rapidă (fără RAG, limitat la 3 teste)

```bash
python eval/run_eval.py --no-rag --limit 3
```

### 8.5 Interpretare rezultate

```
Syntax validity:   90%+  ← % diagrame parsabile sintactic (target >90%)
BLEU score:        0.60+ ← similaritate textuală cu ground truth (target >0.60)
Node F1:           0.70+ ← overlap noduri față de ground truth (target >0.70)
Edge F1:           0.70+ ← overlap relații față de ground truth
```

---

## 9. Demo pipeline complet

Scriptul demo rulează toate componentele sistem și afișează rezultate:

### 9.1 Demo complet (toate secțiunile)

```bash
python scripts/demo_pipeline.py
```

### 9.2 Demo secțiuni individuale

```bash
# Doar generare cu toate tipurile de diagrame
python scripts/demo_pipeline.py --section pipeline

# Doar evaluare cantitativă
python scripts/demo_pipeline.py --section evaluation

# Comparare AST static vs LLM semantic
python scripts/demo_pipeline.py --section comparison

# Demo multi-format output
python scripts/demo_pipeline.py --section formats
```

---

## 10. Teste

### 10.1 Rulare toate testele

```bash
python -m pytest tests/ -v
```

### 10.2 Rulare teste individuale

```bash
# Teste converter multi-format (22 teste)
python -m pytest tests/test_diagram_converter.py -v

# Teste AST analyzer (9 teste)
python -m pytest tests/test_ast_analyzer.py -v

# Teste validator Mermaid (12 teste)
python -m pytest tests/test_validator.py -v
```

### 10.3 Rulare cu raport coverage

```bash
pip install pytest-cov
python -m pytest tests/ --cov=src --cov-report=term-missing
```

### 10.4 Rulare rapidă (fără output verbose)

```bash
python -m pytest tests/ -q
```

**Output așteptat:** `43 passed in 0.27s`

---

## 11. Git Hook pre-commit

Hook-ul validează fișierele Python și diagramele Mermaid din documentație înainte de fiecare commit.

### 11.1 Instalare hook

```bash
python src/integrations/pre_commit_hook.py --install
```

### 11.2 Testare manuală hook (simulare commit)

```bash
python src/integrations/pre_commit_hook.py --run
```

### 11.3 Rulare cu regenerare automată diagrame

```bash
python src/integrations/pre_commit_hook.py --run --auto-regen
```

### 11.4 Dezinstalare hook

```bash
python src/integrations/pre_commit_hook.py --uninstall
```

**Ce face hook-ul:**
- ✅ Detectează fișierele `.py` modificate în staging
- ✅ Rulează analiză AST — dacă există erori de sintaxă Python, blochează commit-ul
- ✅ Validează blocurile `mermaid` din fișierele `.md` modificate
- ⚙️ Opțional: regenerează automat diagramele `.mmd` pentru fișierele modificate

---

## 12. GitHub Action

GitHub Action rulează automat la fiecare Pull Request și push pe `main`.

### 12.1 Configurare secret în GitHub

1. Mergi la `Settings → Secrets and variables → Actions`
2. Click **"New repository secret"**
3. Adaugă **3 secrete** (le găsești în Azure Portal > Keys and Endpoint):

| Nume secret | Valoare |
|------------|--------|
| `AZURE_OPENAI_API_KEY` | Cheia API din Azure Portal |
| `AZURE_OPENAI_ENDPOINT` | `https://YOUR-RESOURCE.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | Numele deployment-ului (ex: `gpt-4o`) |

### 12.2 Ce face Action-ul

**Job `generate-diagrams`** (la PR + push pe main):
- Detectează fișierele `.py` modificate
- Generează diagrame `classDiagram` și `flowchart` pentru fiecare
- Postează un comentariu pe PR cu diagramele generate (actualizează dacă există deja)

**Job `validate-consistency`** (doar la PR):
- Caută toate fișierele `.mmd` și blocurile `mermaid` din `.md`
- Validează sintaxa Mermaid
- Eșuează PR-ul dacă există diagrame invalide

### 12.3 Trigger manual (workflow_dispatch)

Poți adăuga în `.github/workflows/diagram_update.yml`:
```yaml
on:
  workflow_dispatch:  # permite trigger manual din UI GitHub
```

---

## 13. Notebook-uri Jupyter

### 13.1 Pornire Jupyter

```bash
pip install jupyter  # dacă nu e instalat
jupyter notebook
```

Sau în VS Code: instalează extensia **Jupyter** și deschide direct fișierele `.ipynb`.

### 13.2 Notebook 01 — Analiza datelor

```
notebooks/01_data_analysis.ipynb
```

Conține:
- Explorare dataset diagrame (EDA)
- Distribuții tipuri de diagrame
- Analiză complexitate (noduri, edge-uri)
- Vizualizări

**Rulare:** Execută celulele în ordine (Shift+Enter).  
Nu necesită `GEMINI_API_KEY`.

### 13.3 Notebook 02 — Dezvoltare model + Evaluare

```
notebooks/02_model_development.ipynb
```

Conține:
- Arhitectura agentului (pipeline cu 5 etape)
- Demo generare toate tipurile de diagrame
- Evaluare cantitativă (BLEU, Node F1, Edge F1)
- Comparare AST static vs LLM semantic
- Imbunătățiri: retry loop, postprocesare
- Integrare GitHub CI/CD
- Concluzii finale

**Necesită `GEMINI_API_KEY` valid în `.env`.**

**Înainte de rulare**, asigură-te că RAG e indexat:
```bash
python -m src.agent.cli index data/raw/seed_diagrams.jsonl
python -m src.agent.cli index data/raw/seed_diagrams_extended.jsonl
```

**Fix import în notebook (celula 2):**  
Dacă primești `ModuleNotFoundError`, înlocuiește importurile:
```python
# ❌ Greșit (din notebook neactualizat):
from diagram_ai.src.rag_engine.engine import RAGEngine

# ✅ Corect:
from src.rag_engine.engine import RAGEngine
from src.agent.diagram_agent import DiagramAgent, AgentInput, DiagramType
from src.utils.validator import MermaidValidator
from src.ast_analyzer.analyzer import PythonASTAnalyzer
from eval.metrics import DiagramEvaluator
```

---

## 14. Depanare erori frecvente

### ❌ `AZURE_OPENAI_API_KEY nu este setat în .env`

```bash
# Verifică că fișierul .env există și conține cheile:
Get-Content .env | Select-String "AZURE"  # Windows PowerShell
cat .env | grep AZURE                     # Linux/macOS
```

Dacă lipsește fișierul `.env`:
```bash
copy .env.example .env   # Windows
cp .env.example .env     # Linux/macOS
```

Dacă `AZURE_OPENAI_ENDPOINT` conține `your-resource-name`, înlocuiește-l cu endpoint-ul real din Azure Portal.

---

### ❌ `ModuleNotFoundError: No module named 'chromadb'`

```bash
pip install -r requirements.txt
# sau individual:
pip install openai chromadb fastapi uvicorn click rich networkx
```

---

### ❌ `ModuleNotFoundError: No module named 'src'`

Asigură-te că rulezi comenzile **din directorul rădăcină** `diagram_ai/`:

```bash
# ✅ Corect — din diagram_ai/
python -m src.agent.cli generate --text "test"

# ❌ Greșit — din src/
python -m agent.cli generate --text "test"
```

---

### ❌ RAG returnează 0 exemple

Baza de cunoștințe e goală. Indexează:
```bash
python -m src.agent.cli index data/raw/seed_diagrams.jsonl
python -m src.agent.cli index data/raw/seed_diagrams_extended.jsonl
python -m src.agent.cli stats  # verificare
```

---

### ❌ `Connection error` la ChromaDB

Dacă directorul `data/chromadb` e corupt:
```bash
# Windows
rmdir /s /q data\chromadb

# Linux/macOS
rm -rf data/chromadb

# Re-indexare
python -m src.agent.cli index data/raw/seed_diagrams.jsonl
python -m src.agent.cli index data/raw/seed_diagrams_extended.jsonl
```

---

### ❌ Diagrama generată e invalidă sintactic

Agentul încearcă automat până la 3 retry-uri. Dacă tot eșuează:
```bash
# Crește numărul de retry-uri (editeaă .env):
MAX_RETRIES=5

# Sau schimbă la un model mai capabil în Azure AI Studio
# și actualizează .env:
AZURE_OPENAI_DEPLOYMENT=gpt-4o  # cel mai capabil disponibil Students
```

---

### ❌ `git: command not found` în hook

Asigură-te că ești în interiorul unui repository git:
```bash
git status
```

---

### ⚠️ Eroare la conversie Structurizr cu `classDiagram`

Structurizr DSL e optimizat pentru `flowchart` / `C4Context`. Conversia `classDiagram → Structurizr` produce o versiune simplificată. Folosește `--format plantuml` pentru diagrame de clase.

---

## 🔗 Link-uri utile

| Resursă | URL |
|---------|-----|
| Preview Mermaid live | https://mermaid.live |
| Preview PlantUML live | https://www.plantuml.com/plantuml/uml/ |
| Preview Graphviz online | https://dreampuf.github.io/GraphvizOnline/ |
| Preview Structurizr DSL | https://structurizr.com/dsl |
| Azure Portal | https://portal.azure.com |
| Azure AI Studio | https://oai.azure.com |
| Azure for Students | https://azure.microsoft.com/free/students |
| Documentație Swagger (local) | http://localhost:8000/docs |

---

## 📁 Structura fișierelor relevante

```
diagram_ai/
├── .env                    ← Configurare locală (API keys, paths)
├── .env.example            ← Template configurare
├── requirements.txt        ← Dependențe Python
├── GHID_RULARE.md          ← Acest fișier
│
├── data/
│   ├── raw/
│   │   ├── seed_diagrams.jsonl          ← 10 diagrame seed (RAG)
│   │   └── seed_diagrams_extended.jsonl ← 10 diagrame extra (RAG)
│   └── processed/
│       └── ast_canonical.jsonl          ← Dataset AST → Mermaid
│
├── src/
│   ├── agent/
│   │   ├── diagram_agent.py   ← Agentul principal (Azure OpenAI + RAG + AST)
│   │   ├── cli.py             ← Interfața CLI (click)
│   │   └── api_server.py      ← REST API (FastAPI)
│   ├── ast_analyzer/
│   │   └── analyzer.py        ← Parsare cod Python → ModuleInfo
│   ├── rag_engine/
│   │   └── engine.py          ← ChromaDB indexare + retrieval
│   └── utils/
│       ├── validator.py        ← Validare sintaxă Mermaid
│       └── diagram_converter.py ← Conversie Mermaid → PlantUML/Graphviz/Structurizr
│
├── eval/
│   ├── metrics.py             ← BLEU, Node F1, Edge F1
│   └── run_eval.py            ← Script evaluare standalone
│
├── tests/
│   ├── test_ast_analyzer.py   ← 9 teste AST
│   ├── test_validator.py      ← 12 teste validator
│   └── test_diagram_converter.py ← 22 teste converter
│
├── scripts/
│   └── demo_pipeline.py       ← Demo complet pipeline
│
├── notebooks/
│   ├── 01_data_analysis.ipynb      ← Faza I: EDA
│   └── 02_model_development.ipynb  ← Faza II: Agent + Evaluare
│
├── src/integrations/
│   └── pre_commit_hook.py     ← Git hook validare
│
└── .github/workflows/
    └── diagram_update.yml     ← GitHub Action auto-diagrame
```
