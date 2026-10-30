# DeepRAG Studio

An offline-first Java retrieval service that makes six advanced RAG patterns visible, persistent and testable.

[![CI](https://github.com/thillainirmal-tech/DeepRAG-Studio/actions/workflows/ci.yml/badge.svg)](https://github.com/thillainirmal-tech/DeepRAG-Studio/actions/workflows/ci.yml)

Patterns: query rewriting, multi-query retrieval, HyDE, metadata filtering, reciprocal-rank fusion with re-ranking, and contextual compression.

Run `mvn spring-boot:run`, open http://localhost:8086, and verify using `mvn verify`.

`POST /api/documents` ingests real text into an atomically persisted corpus; `POST /api/run` returns citations and a complete six-stage trace; `POST /api/evaluate` calculates recall@4 and reciprocal rank against expected document IDs. The deterministic retriever works without an API key, making production regression tests repeatable instead of token-dependent. Mount `/data` or use the supplied Compose stack so ingestion survives restarts.

Roadmap: pluggable embedding/reranking adapters, nDCG and latency comparison, and pgvector for large corpora.

Apache-2.0. Never commit confidential corpus data; inject production content through the authenticated deployment boundary.
