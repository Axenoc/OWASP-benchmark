# CVE-to-CWE Benchmark Extension

## Project

This repository extends OWASP Benchmark with additional CWE-focused
testcases based on real CVE vulnerability patterns.

Existing repository conventions must be preserved.

Important locations:

- Java testcases:
  `src/main/java/org/owasp/benchmark/testcode/`

- CWE HTML forms:
  `src/main/webapp/cwe-<id>/`

- CWE suite pages:
  `src/main/webapp/cwe<id>.html`

- Ground truth:
  `expectedresults-1.2.csv`

- Build:
  Maven WAR project

- Local deployment:
  Tomcat through the Cargo Maven plugin

## Workflow

Every new CWE/CVE task follows exactly:

Research -> Implementation -> Validation

Do not skip stages.

Each stage consumes the approved output of the previous stage.

## Research rules

Research must:

- verify the CWE definition
- identify authoritative CVE evidence
- distinguish direct mapping from inferred mapping
- identify the vulnerability root cause
- identify attacker-controlled input
- identify observable impact
- identify patched/safe behavior
- determine whether the scenario can be represented safely in Benchmark

Research must not:

- modify Java implementation files
- modify HTML testcase files
- modify expectedresults-1.2.csv
- invent CVE/CWE mappings without evidence

The output contract is `research.yaml`.

## Implementation rules

Implementation must:

- consume an approved `research.yaml`
- follow the selected CVE and scenario exactly
- inspect neighboring Benchmark testcases before implementing
- preserve existing project conventions
- implement both vulnerable and safe cases when specified
- update Java, HTML, CWE suite links, and CSV ground truth as required
- run the Maven build after implementation

Implementation must not:

- select another CVE
- reinterpret the vulnerability without returning to Research
- silently modify the approved research specification
- use real credentials, tokens, victims, or external targets

## Validation rules

Validation is independent of Implementation.

Validation must:

- inspect the approved research specification
- inspect the implementation diff
- run the Maven build
- start the local Benchmark deployment when required
- execute HTTP-level assertions
- test vulnerable and safe behavior
- test repeated execution when relevant
- test isolation and cleanup when relevant
- verify HTML/form/servlet parameter consistency
- verify expectedresults-1.2.csv

Validation must not modify implementation code to make a test pass.

Failures must be classified as either:

- `implementation`
- `research`

## Feedback routing

Return to Implementation when the specification is valid but the code
does not implement it correctly.

Return to Research when the CVE/CWE mapping, vulnerability model,
safe behavior, or benchmark scenario itself is invalid.

## Safety and repository rules

- Never run `git push`.
- Never commit unless explicitly requested.
- Never rewrite repository history.
- Never use real secrets or credentials.
- Never interact with external vulnerable systems.
- Keep vulnerability demonstrations local and reproducible.
- Prefer minimal changes.
- Do not modify unrelated existing testcases.
- Show the diff before declaring implementation complete.

## Completion

A CWE task is complete only when:

1. Research is approved.
2. Implementation builds successfully.
3. Independent Validation passes.