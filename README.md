# OWASP Benchmark for Java
The OWASP Benchmark Project is a Java test suite designed to verify the speed and accuracy of vulnerability detection tools. It is a fully runnable open source web application that can be analyzed by any type of Application Security Testing (AST) tool, including SAST, DAST (like <a href="https://www.zaproxy.org/">ZAP</a>), and IAST tools. The intent is that all the vulnerabilities deliberately included in and scored by the Benchmark are actually exploitable so it's a fair test for any kind of application vulnerability detection tool.

The Benchmark project also includes scorecard generators for numerous open source and commercial AST tools, and the set of supported tools is growing all the time. This scoring capability is implemented in the BenchmarkUtils project, which is at: https://github.com/OWASP-Benchmark/BenchmarkUtils.

The project documentation is all on the OWASP site at the <a href="https://owasp.org/www-project-benchmark">OWASP Benchmark</a> project pages. Please refer to that site for all the project details.

The current latest release is v1.2. Note that all the releases that are available here: https://github.com/OWASP-Benchmark/BenchmarkJava/releases, are historical. The latest release is always available live by simply cloning or pulling the head of this repository (i.e., git pull).

## Added CWE Test Cases

Each CWE below has one vulnerable test case and one safe test case.

| CWE | Vulnerable / Positive | Safe / Negative |
| --- | --- | --- |
| CWE-644 | BenchmarkTest02741 | BenchmarkTest02742 |
| CWE-598 | BenchmarkTest02743 | BenchmarkTest02744 |
| CWE-204 | BenchmarkTest02745 | BenchmarkTest02746 |
| CWE-209 | BenchmarkTest02747 | BenchmarkTest02748 |
| CWE-210 | BenchmarkTest02749 | BenchmarkTest02750 |
| CWE-211 | BenchmarkTest02751 | BenchmarkTest02752 |
| CWE-535 | BenchmarkTest02753 | BenchmarkTest02754 |
| CWE-536 | BenchmarkTest02755 | BenchmarkTest02756 |
| CWE-537 | BenchmarkTest02757 | BenchmarkTest02758 |
| CWE-497 | BenchmarkTest02759 | BenchmarkTest02760 |
| CWE-548 | BenchmarkTest02761 | BenchmarkTest02762 |
| CWE-538 | BenchmarkTest02763 | BenchmarkTest02764 |
| CWE-615 | BenchmarkTest02765 | BenchmarkTest02766 |
| CWE-651 | BenchmarkTest02767 | BenchmarkTest02768 |

Running Benchmark Itself:
* runBenchmark.sh - run the Benchmark Web Application (accessible via local machine only)
* runRemoteAccessibleBenchmark.sh - like the above but allows port 8443 to be accessible outside the machine Benchmark is running on.
