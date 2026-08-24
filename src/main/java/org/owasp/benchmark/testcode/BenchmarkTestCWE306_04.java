/**
 * OWASP Benchmark v1.2
 *
 * <p>This is a false-positive control for CWE-306. Attacker-controlled input is validated,
 * and administrator access still requires valid credentials.
 */
package org.owasp.benchmark.testcode;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/cwe-306/BenchmarkTestCWE306_04")
public class BenchmarkTestCWE306_04 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("BenchmarkTestCWE306_04_user");
        String password = request.getParameter("BenchmarkTestCWE306_04_pass");
        String requestedAccess = request.getParameter("access");

        // Safe control: only non-critical, explicitly supported destinations are accepted.
        if (!isAllowedAccess(requestedAccess)) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "The access parameter must be dashboard or reports.");
            return;
        }

        // The validated parameter selects a destination; it never establishes an identity.
        if (!"admin".equals(username) || !"password123".equals(password)) {
            rejectLogin(response);
            return;
        }

        writeAdministratorPage(response, requestedAccess);
    }

    private static boolean isAllowedAccess(String requestedAccess) {
        return "dashboard".equals(requestedAccess) || "reports".equals(requestedAccess);
    }

    private static void writeAdministratorPage(
            HttpServletResponse response, String requestedAccess) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html><html><body>");
        out.println("<h1>Authenticated Successfully</h1>");
        out.println("<p>Administrator access granted to " + requestedAccess + ".</p>");
        out.println("</body></html>");
    }

    private static void rejectLogin(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println("<p>Invalid username or password.</p>");
    }
}
