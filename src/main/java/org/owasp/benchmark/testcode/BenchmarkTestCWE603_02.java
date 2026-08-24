/**
 * OWASP Benchmark v1.2
 *
 * <p>This test intentionally contains CWE-603. It is vulnerable by design and must not be copied
 * into production.
 */
package org.owasp.benchmark.testcode;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/cwe-603/BenchmarkTestCWE603_02")
public class BenchmarkTestCWE603_02 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String authenticationHeader = request.getHeader("X-Client-Authenticated");
        response.setContentType("text/html;charset=UTF-8");

        // CWE-603: a modified client can forge the result header without checking credentials.
        if ("yes".equalsIgnoreCase(authenticationHeader)) {
            response.getWriter().println("<h3>Authenticated Billing Portal</h3>");
            response.getWriter().println("<p>Private invoice reference: BENCHMARK-603-B</p>");
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("<p>Client-side authentication failed.</p>");
        }
    }
}
