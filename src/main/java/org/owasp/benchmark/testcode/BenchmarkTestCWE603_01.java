/**
 * OWASP Benchmark v1.2
 *
 * <p>This test intentionally contains CWE-603: Use of Client-Side Authentication. It is
 * vulnerable by design and must not be copied into production.
 */
package org.owasp.benchmark.testcode;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/cwe-603/BenchmarkTestCWE603_01")
public class BenchmarkTestCWE603_01 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String clientAuthenticated = request.getParameter("clientAuthenticated");
        response.setContentType("text/html;charset=UTF-8");

        // CWE-603: the server accepts the result of a JavaScript-only credential check.
        if ("true".equalsIgnoreCase(clientAuthenticated)) {
            response.getWriter().println("<h3>Authenticated Account Dashboard</h3>");
            response.getWriter().println("<p>Private account reference: BENCHMARK-603-A</p>");
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("<p>The client did not authenticate the user.</p>");
        }
    }
}
