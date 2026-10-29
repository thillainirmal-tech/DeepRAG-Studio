# DeepRAG Studio

An offline Java laboratory that makes six advanced retrieval patterns visible and testable.

[![CI](https://github.com/thillainirmal-tech/DeepRAG-Studio/actions/workflows/ci.yml/badge.svg)](https://github.com/thillainirmal-tech/DeepRAG-Studio/actions/workflows/ci.yml)

Patterns: query rewriting, multi-query retrieval, HyDE, metadata filtering, reciprocal-rank fusion with re-ranking, and contextual compression.

Run `mvn spring-boot:run`, open http://localhost:8086, and verify using `mvn verify`.

The deterministic corpus means every experiment works without an API key or vector database. The service returns citations and a full transformation trace, making it useful for learning, evaluation and regression testing.

Roadmap: pluggable embedding adapters, golden evaluation sets, nDCG/MRR metrics, latency comparison, and pgvector.

Apache-2.0. Synthetic corpus only.
