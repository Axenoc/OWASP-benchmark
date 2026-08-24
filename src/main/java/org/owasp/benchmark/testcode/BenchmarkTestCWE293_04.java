/**
 * OWASP Benchmark v1.2
 *
 * <p>This is a false-positive control for CWE-293. Referer data is sanitized for auditing and
 * never used to authenticate the requester.
 */
package org.owasp.benchmark.testcode;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/cwe-293/BenchmarkTestCWE293_04")
public class BenchmarkTestCWE293_04 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String auditedReferer = sanitizeForAudit(request.getHeader("Referer"));
        response.setHeader("X-Benchmark-Referer-Audit-Length", Integer.toString(auditedReferer.length()));

        String username = request.getParameter("BenchmarkTestCWE293_04_user");
        String password = request.getParameter("BenchmarkTestCWE293_04_pass");

        // Safe control: authentication depends only on credentials, never on Referer.
        if (!"admin".equals(username) || !"password123".equals(password)) {
            rejectLogin(response);
            return;
        }

        writePrivilegedPage(response);
    }

    private static String sanitizeForAudit(String referer) {
        if (referer == null) {
            return "";
        }
        String singleLine = referer.replace("\r", "").replace("\n", "");
        return singleLine.length() <= 200 ? singleLine : singleLine.substring(0, 200);
    }

    private static void writePrivilegedPage(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html><html><body>");
        out.println("<h1>Authenticated Administrator</h1>");
        out.println("<pre>adminToken=Benchmark-Only-Admin-Token-293-04</pre>");
        out.println("</body></html>");
    }

    private static void rejectLogin(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println("<p>Invalid username or password.</p>");
    }
}
