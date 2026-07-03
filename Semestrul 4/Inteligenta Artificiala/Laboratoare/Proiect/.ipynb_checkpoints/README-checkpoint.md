# 🤖 Diagram-as-Code AI Assistant
**Proiect de Inteligență Artificială - Analiză Arhitecturală Automată**

---

### 👥 Echipa:
* Florea Paul | Feraru Gabriel | Ghiorghiță Gabriel

### 🎯 Problema Abordată:
Menținerea documentației tehnice sincronizate cu implementarea codului este o provocare majoră în ingineria software. Proiectul nostru dezvoltă un **Agent AI hibrid** care extrage semantica unui cod sursă Python (folosind AST) și generează automat diagrame UML (Mermaid/PlantUML) prin intermediul unui **Small Language Model (SLM)** finetuned pe date de arhitectură.

### 📐 Soluția Propusă (Schema):

```mermaid
graph TD
    %% Intrări diferite
    InCode[Cod Sursă Python] --> B{Analizor Static AST}
    InText[User Story / Limbaj Natural] --> D[AI Agent - SLM]

    %% Fluxul de Cod
    B -- Extracție Structurală --> C[Metadata JSON]
    C --> D

    %% Partea de Inteligență AI
    E[Bază de Cunoștințe RAG - Sintaxă UML] --> D
    D -- Generare DSL --> F[Cod Mermaid / PlantUML]
    F --> G[Diagramă Vizuală / Doc Sincronizată]