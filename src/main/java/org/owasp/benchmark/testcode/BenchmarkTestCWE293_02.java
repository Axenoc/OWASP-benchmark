/**
 * OWASP Benchmark v1.2
 *
 * <p>This test intentionally contains CWE-293: Using Referer Field for Authentication.
 * It uses simulated secrets and must not be copied into production code.
 */
package org.owasp.benchmark.testcode;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/cwe-293/BenchmarkTestCWE293_02")
public class BenchmarkTestCWE293_02 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String referer = request.getHeader("Referer");

        // CWE-293: parsing the attacker-controlled header does not make it authentic.
        if (!hasTrustedRefererPath(referer, request.getContextPath())) {
            rejectRequest(response);
            return;
        }

        writePrivilegedPage(response, "parsed Referer path");
    }

    private static boolean hasTrustedRefererPath(String referer, String contextPath) {
        if (referer == null) {
            return false;
        }
        try {
            URI refererUri = new URI(referer);
            return refererUri.getPath() != null
                    && refererUri.getPath().startsWith(contextPath + "/cwe-293/");
        } catch (URISyntaxException exception) {
            return false;
        }
    }

    private static void writePrivilegedPage(HttpServletResponse response, String method)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html><html><body>");
        out.println("<h1>Service Account Export</h1>");
        out.println("<p>Authentication source: " + method + ".</p>");
        out.println("<pre>serviceToken=Benchmark-Only-Service-Token-293-02</pre>");
        out.println("</body></html>");
    }

    private static void rejectRequest(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println("<p>Untrusted referring path.</p>");
    }
}
