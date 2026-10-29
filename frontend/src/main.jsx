
import React, { useState } from "react";
import { createRoot } from "react-dom/client";
import ReactMarkdown from "react-markdown";
import remarkBreaks from "remark-breaks";
import "./style.css";

const API = "http://localhost:8080/api";

// Convert skill strings into separate items.
function toSkillList(value) {
  if (value === null || value === undefined || value === "") {
    return [];
  }

  if (Array.isArray(value)) {
    return value
      .map((item) => String(item).trim())
      .filter(Boolean);
  }

  return String(value)
    .split(/[,;\n]+/)
    .map((item) => item.trim())
    .filter(Boolean);
}

// Convert an API response into JSON safely.
async function safeJson(response) {
  try {
    return await response.json();
  } catch {
    return {};
  }
}

function App() {
  const [file, setFile] = useState(null);
  const [resumeText, setResumeText] = useState("");
  const [job, setJob] = useState("");
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");

  async function uploadPdf(selectedFile) {
    const form = new FormData();
    form.append("file", selectedFile);

    const response = await fetch(`${API}/resume/upload`, {
      method: "POST",
      body: form,
    });

    const data = await safeJson(response);

    if (!response.ok) {
      throw new Error(data.message || "Upload failed");
    }

    setResumeText(data.extractedText || "");
    setMessage(
      `✓ ${data.filename || selectedFile.name} uploaded and text extracted.`
    );
  }

  async function handleFile(event) {
    const selectedFile = event.target.files?.[0];

    if (!selectedFile) return;

    setFile(selectedFile);
    setResult(null);
    setMessage("Uploading PDF...");

    try {
      await uploadPdf(selectedFile);
    } catch (error) {
      setMessage(`Error: ${error.message}`);
    }
  }

  async function analyze(event) {
    event.preventDefault();

    if (!resumeText.trim()) {
      setMessage("Please upload a PDF or paste your resume text.");
      return;
    }

    if (!job.trim()) {
      setMessage("Please enter a job description.");
      return;
    }

    setLoading(true);
    setMessage("");
    setResult(null);

    try {
      const body = new URLSearchParams();

      body.append("resumeText", resumeText);
      body.append("jobDescription", job);
      body.append("fileName", file?.name || "Pasted Resume");

      const response = await fetch(`${API}/resume/analyze`, {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
        body,
      });

      const data = await safeJson(response);

      if (!response.ok) {
        throw new Error(data.message || "Analysis failed");
      }

      setResult(data);
    } catch (error) {
      setMessage(`Error: ${error.message}`);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="app">
      <header>
        <div className="brand">AI Resume Analyzer</div>
        <span>Java Full Stack Project</span>
      </header>

      <main>
        <section className="hero">
          <p className="eyebrow">SMART CAREER TOOL</p>

          <h1>
            Resume Analyzer &
            <br />
            <b>Job Matcher</b>
          </h1>

          <p>
            Upload your resume, compare it with a job description, and discover
            your skill gaps.
          </p>
        </section>

        <form className="card" onSubmit={analyze}>
          <label>1. Upload your resume (PDF)</label>

          <div className="upload">
            <input
              type="file"
              accept=".pdf,application/pdf"
              onChange={handleFile}
            />

            {file && <small>{file.name}</small>}
          </div>

          <label>Or paste resume text</label>

          <textarea
            value={resumeText}
            onChange={(event) => setResumeText(event.target.value)}
            placeholder="Paste your resume text here..."
          />

          <label>2. Job description</label>

          <textarea
            value={job}
            onChange={(event) => setJob(event.target.value)}
            placeholder="Paste the job description here..."
          />

          {message && <div className="message">{message}</div>}

          <button type="submit" disabled={loading}>
            {loading ? "Analyzing..." : "Analyze Resume"}
          </button>
        </form>

        {result && (
          <section className="results">
            <h2>Analysis Results</h2>

            <div className="scores">
              <div className="score">
                <strong>{result.resumeScore ?? 0}%</strong>
                <span>Resume Score</span>
              </div>

              <div className="score">
                <strong>{result.matchPercentage ?? 0}%</strong>
                <span>Job Match</span>
              </div>
            </div>

            <div className="grid">
              <ResultCard
                title="Matching Skills"
                items={toSkillList(result.matchingSkills)}
                chips
              />

              <ResultCard
                title="Missing Skills"
                items={toSkillList(result.missingSkills)}
                chips
              />

              <ResultCard
                title="Strengths"
                text={result.strengths}
              />

              <ResultCard
                title="Recommendations"
                text={result.recommendations}
              />
            </div>
          </section>
        )}
      </main>

      <footer>Developed by Shaik Mohammed Mustak</footer>
    </div>
  );
}

function ResultCard({ title, items, text, chips = false }) {
  return (
    <div className="result-card">
      <h3>{title}</h3>

      {chips ? (
        Array.isArray(items) && items.length > 0 ? (
          <div className="chips">
            {items.map((skill, index) => (
              <span key={`${skill}-${index}`} className="skill-chip">
                {String(skill)}
              </span>
            ))}
          </div>
        ) : (
          <p>None detected.</p>
        )
      ) : (
        <div className="markdown">
          <ReactMarkdown remarkPlugins={[remarkBreaks]}>
            {typeof text === "string" ? text : ""}
          </ReactMarkdown>
        </div>
      )}
    </div>
  );
}
createRoot(document.getElementById("root")).render(<App />);
