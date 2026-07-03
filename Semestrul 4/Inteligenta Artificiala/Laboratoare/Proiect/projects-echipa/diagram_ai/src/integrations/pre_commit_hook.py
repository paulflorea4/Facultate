#!/usr/bin/env python3
"""
integrations/pre_commit_hook.py
Git hook pre-commit care:
1. Detectează fișierele Python modificate în staging
2. Rulează analiza AST pe ele
3. Verifică că diagramele Mermaid existente în documentație sunt încă valide
4. Poate regenera automat diagramele (cu --auto-regen flag)

Instalare:
  python src/integrations/pre_commit_hook.py --install
  
Dezinstalare:
  python src/integrations/pre_commit_hook.py --uninstall
"""

import subprocess
import sys
import os
import argparse
from pathlib import Path


HOOK_CONTENT = '''#!/usr/bin/env python3
import subprocess, sys
result = subprocess.run(
    [sys.executable, "src/integrations/pre_commit_hook.py", "--run"],
    capture_output=False
)
sys.exit(result.returncode)
'''


def get_staged_python_files() -> list[str]:
    """Returnează lista de fișiere Python în staging."""
    result = subprocess.run(
        ["git", "diff", "--cached", "--name-only", "--diff-filter=ACM"],
        capture_output=True, text=True
    )
    files = [
        f for f in result.stdout.strip().split("\n")
        if f.endswith(".py") and os.path.exists(f)
        and not any(skip in f for skip in ["test_", "_test.py", "migrations/"])
    ]
    return files


def check_mermaid_in_docs() -> tuple[bool, list[str]]:
    """Verifică sintaxa Mermaid în fișierele .md din staging."""
    import re

    result = subprocess.run(
        ["git", "diff", "--cached", "--name-only", "--diff-filter=ACM"],
        capture_output=True, text=True
    )
    md_files = [f for f in result.stdout.strip().split("\n") if f.endswith(".md") and os.path.exists(f)]

    errors = []

    sys.path.insert(0, str(Path(__file__).parent.parent.parent))
    from src.utils.validator import MermaidValidator
    validator = MermaidValidator()

    for fpath in md_files:
        with open(fpath, "r", encoding="utf-8") as f:
            content = f.read()

        blocks = re.findall(r'```mermaid\s*(.*?)\s*```', content, re.DOTALL)
        for i, block in enumerate(blocks, 1):
            is_valid, errs = validator.validate(block)
            if not is_valid:
                errors.append(f"{fpath} (bloc {i}): {'; '.join(errs)}")

    return len(errors) == 0, errors


def analyze_changed_files(files: list[str]) -> dict:
    """Analizează fișierele Python modificate și returnează statistici."""
    sys.path.insert(0, str(Path(__file__).parent.parent.parent))
    from src.ast_analyzer.analyzer import PythonASTAnalyzer

    analyzer = PythonASTAnalyzer()
    results = {}

    for fpath in files:
        try:
            module_info = analyzer.analyze_file(fpath)
            results[fpath] = {
                "classes": len(module_info.classes),
                "functions": len(module_info.functions),
                "imports": len(module_info.imports),
            }
        except SyntaxError as e:
            results[fpath] = {"error": str(e)}

    return results


def run_hook(auto_regen: bool = False) -> int:
    """Execută hook-ul. Returnează exit code (0 = ok, 1 = eroare)."""
    print("🏗️  Diagram-as-Code: Verificare pre-commit...")

    staged_files = get_staged_python_files()

    if staged_files:
        print(f"📁 Fișiere Python modificate: {len(staged_files)}")
        analysis = analyze_changed_files(staged_files)

        total_classes = sum(v.get("classes", 0) for v in analysis.values())
        total_functions = sum(v.get("functions", 0) for v in analysis.values())
        syntax_errors = [f for f, v in analysis.items() if "error" in v]

        if syntax_errors:
            print(f"❌ Erori sintaxă Python în: {syntax_errors}")
            return 1

        print(f"   ✓ {total_classes} clase, {total_functions} funcții detectate")

        # Regenerare diagrame (opțional)
        if auto_regen and os.getenv("AZURE_OPENAI_API_KEY"):
            print("🔄 Regenerare diagrame automate...")
            for fpath in staged_files[:3]:  # max 3 fișiere pentru viteză
                out_path = fpath.replace(".py", ".mmd")
                try:
                    subprocess.run(
                        [sys.executable, "-m", "src.agent.cli", "generate",
                         "--file", fpath, "--output", out_path, "--no-rag"],
                        timeout=30, capture_output=True
                    )
                    if os.path.exists(out_path):
                        subprocess.run(["git", "add", out_path])
                        print(f"   ✓ Regenerat: {out_path}")
                except subprocess.TimeoutExpired:
                    print(f"   ⚠️  Timeout pentru {fpath}")

    # Verificare diagrame Mermaid în documentație
    docs_ok, doc_errors = check_mermaid_in_docs()
    if not docs_ok:
        print(f"❌ Diagrame Mermaid invalide în documentație:")
        for err in doc_errors:
            print(f"   • {err}")
        print("\n💡 Tip: Rulați 'python -m src.agent.cli generate' pentru a regenera diagramele")
        return 1

    print("✅ Toate verificările au trecut!")
    return 0


def install_hook():
    """Instalează hook-ul în .git/hooks/pre-commit."""
    git_dir = subprocess.run(
        ["git", "rev-parse", "--git-dir"],
        capture_output=True, text=True
    ).stdout.strip()

    if not git_dir:
        print("❌ Nu ești într-un repository git")
        return False

    hook_path = Path(git_dir) / "hooks" / "pre-commit"
    hook_path.write_text(HOOK_CONTENT)
    hook_path.chmod(0o755)
    print(f"✅ Hook instalat la: {hook_path}")
    return True


def uninstall_hook():
    """Dezinstalează hook-ul."""
    git_dir = subprocess.run(
        ["git", "rev-parse", "--git-dir"],
        capture_output=True, text=True
    ).stdout.strip()

    hook_path = Path(git_dir) / "hooks" / "pre-commit"
    if hook_path.exists():
        hook_path.unlink()
        print(f"✅ Hook dezinstalat: {hook_path}")
    else:
        print("ℹ️  Hook nu era instalat")


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Git pre-commit hook pentru diagrame arhitecturale")
    parser.add_argument("--install", action="store_true", help="Instalează hook-ul")
    parser.add_argument("--uninstall", action="store_true", help="Dezinstalează hook-ul")
    parser.add_argument("--run", action="store_true", help="Rulează verificările (folosit de git)")
    parser.add_argument("--auto-regen", action="store_true", help="Regenerează diagrame automat")
    args = parser.parse_args()

    if args.install:
        install_hook()
    elif args.uninstall:
        uninstall_hook()
    elif args.run:
        sys.exit(run_hook(auto_regen=args.auto_regen))
    else:
        parser.print_help()
