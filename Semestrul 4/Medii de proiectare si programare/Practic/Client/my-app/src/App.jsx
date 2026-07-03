import { useState } from "react";

const BASE_URL = "http://localhost:8080";

export default function App() {
  const [valoare, setValoare] = useState("");
  const [indiciu, setIndiciu] = useState("");
  const [complexitate, setComplexitate] = useState("");
  const [loading, setLoading] = useState(false);
  const [alert, setAlert] = useState(null); // { type: "error"|"success", msg }
  const [result, setResult] = useState(null);

  async function handleSubmit() {
    setAlert(null);
    setResult(null);

    setLoading(true);
    try {
      const res = await fetch(`${BASE_URL}/api/cuvinte`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ 
            valoare : valoare.trim(),
            indiciu: indiciu.trim(),
            complexitate : complexitate.trim()
         }),
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
    <div style={{ maxWidth: 520, margin: "2rem auto", fontFamily: "sans-serif", background: "#f5f5f5", padding: "2rem", borderRadius: "4px" }}>
      <h2>Adaugare Cuvant</h2>

      <div style={{ marginTop: "1rem" }}>
        <label style={{color:"#000000"}}>Valoare</label>
        <textarea
          value={valoare}
          onChange={e => setValoare(e.target.value)}
          placeholder="e.g. masina"
          style={{ display: "block", width: "100%", marginTop: 4 }}
        />
      </div>

      <div style={{ marginTop: "1rem" }}>
        <label style={{color:"#000000"}}>Indiciu</label>
        <textarea
          value={indiciu}
          onChange={e => setIndiciu(e.target.value)}
          placeholder='e.g. are roti'
          style={{ display: "block", width: "100%", marginTop: 4 }}
        />
      </div>

      <div style={{ marginTop: "1rem" }}>
        <label style={{color:"#000000"}}>Complexitate</label>
        <textarea
          value={complexitate}
          onChange={e => setComplexitate(e.target.value)}
          placeholder='e.g. usor, mediu, ridicat, foarte ridicat'
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
        <pre style={{ marginTop: "1rem", background: "#131313", padding: "1rem", color: "#ffffff", borderRadius: "4px" }}>
          {JSON.stringify(result, null, 2)}
        </pre>
      )}
    </div>
  );
}