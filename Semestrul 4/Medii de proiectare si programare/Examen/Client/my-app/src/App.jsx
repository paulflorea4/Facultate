import { useState } from "react";

const BASE_URL = "http://localhost:8080";

export default function App() {
  const [values, setValues] = useState("");
  const [extra, setExtra] = useState("");
  const [loading, setLoading] = useState(false);
  const [alert, setAlert] = useState(null); // { type: "error"|"success", msg }
  const [result, setResult] = useState(null);

  async function handleSubmit() {
    setAlert(null);
    setResult(null);

    if (!values.trim()) {
      setAlert({ type: "error", msg: "Values cannot be empty." });
      return;
    }

    let extraObj = {};
    if (extra.trim()) {
      try { extraObj = JSON.parse(extra); }
      catch { setAlert({ type: "error", msg: "Extra fields is not valid JSON." }); return; }
    }

    setLoading(true);
    try {
      const res = await fetch(`${BASE_URL}/api/configurations`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ values, ...extraObj }),
      });

      const contentType = res.headers.get("content-type") || "";
      const data = contentType.includes("application/json")
        ? await res.json()
        : await res.text();

      if (!res.ok) {
        setAlert({ type: "error", msg: `${res.status} — ${typeof data === "string" ? data : JSON.stringify(data)}` });
      } else {
        setAlert({ type: "success", msg: "Configuration saved!" });
        if (typeof data === "object") setResult(data);
      }
    } catch (e) {
      setAlert({ type: "error", msg: `Network error: ${e.message}` });
    } finally {
      setLoading(false);
    }
  }

  return (
    <div style={{ maxWidth: 520, margin: "2rem auto", fontFamily: "sans-serif" }}>
      <h2>Add Configuration</h2>

      <div style={{ marginTop: "1rem" }}>
        <label>Values</label>
        <textarea
          value={values}
          onChange={e => setValues(e.target.value)}
          placeholder="e.g. 1,2,3,4"
          style={{ display: "block", width: "100%", marginTop: 4 }}
        />
      </div>

      <div style={{ marginTop: "1rem" }}>
        <label>Extra fields (optional JSON)</label>
        <textarea
          value={extra}
          onChange={e => setExtra(e.target.value)}
          placeholder='{"name": "my-config"}'
          style={{ display: "block", width: "100%", marginTop: 4 }}
        />
      </div>

      {alert && (
        <p style={{ color: alert.type === "error" ? "red" : "green", marginTop: "1rem" }}>
          {alert.msg}
        </p>
      )}

      <button onClick={handleSubmit} disabled={loading} style={{ marginTop: "1rem" }}>
        {loading ? "Saving…" : "Save"}
      </button>

      {result && (
        <pre style={{ marginTop: "1rem", background: "#f4f4f4", padding: "1rem" }}>
          {JSON.stringify(result, null, 2)}
        </pre>
      )}
    </div>
  );
}