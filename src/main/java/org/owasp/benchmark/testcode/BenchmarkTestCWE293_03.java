/**
 * OWASP Benchmark v1.2
 *
 * <p>This test intentionally contains CWE-293: Using Referer Field for Authentication.
 * It uses simulated secrets and must not be copied into production code.
 */
package org.owasp.benchmark.testcode;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/cwe-293/BenchmarkTestCWE293_03")
public class BenchmarkTestCWE293_03 extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Pattern TRUSTED_REFERER =
            Pattern.compile(
                    "^https?://[^/]+/benchmark/cwe-293/BenchmarkTestCWE293_03\\.html(?:\\?.*)?$");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String referer = request.getHeader("Referer");

        // CWE-293: a strict format check still trusts a header controlled by the requester.
        if (referer == null || !TRUSTED_REFERER.matcher(referer).matches()) {
            rejectRequest(response);
            return;
        }

        writePrivilegedPage(response, "regular-expression Referer check");
    }

    private static void writePrivilegedPage(HttpServletResponse response, String method)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html><html><body>");
        out.println("<h1>Administrator Diagnostics</h1>");
        out.println("<p>Authentication source: " + method + ".</p>");
        out.println("<pre>signingKey=Benchmark-Only-Signing-Key-293-03</pre>");
        out.println("</body></html>");
    }

    private static void rejectRequest(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println("<p>Untrusted referring origin.</p>");
    }
}
