/**
 * OWASP Benchmark v1.2
 *
 * <p>This test intentionally contains CWE-293: Using Referer Field for Authentication.
 * It uses simulated secrets and must not be copied into production code.
 */
package org.owasp.benchmark.testcode;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/cwe-293/BenchmarkTestCWE293_01")
public class BenchmarkTestCWE293_01 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String referer = request.getHeader("Referer");
        String trustedSuffix =
                request.getContextPath() + "/cwe-293/BenchmarkTestCWE293_01.html";

        // CWE-293: a spoofable Referer suffix is the only authentication decision.
        if (referer == null || !referer.endsWith(trustedSuffix)) {
            rejectRequest(response);
            return;
        }

        writePrivilegedPage(response, "suffix-matched Referer");
    }

    private static void writePrivilegedPage(HttpServletResponse response, String method)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html><html><body>");
        out.println("<h1>Administrator Session Opened</h1>");
        out.println("<p>Authentication source: " + method + ".</p>");
        out.println("<pre>adminToken=Benchmark-Only-Admin-Token-293-01</pre>");
        out.println("</body></html>");
    }

    private static void rejectRequest(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println("<p>Untrusted referring page.</p>");
    }
}
