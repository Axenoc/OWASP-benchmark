/**
 * OWASP Benchmark v1.2
 *
 * <p>This is a false-positive control for CWE-306. Attacker-controlled input is sanitized and
 * validated, and administrator access still requires valid credentials.
 */
package org.owasp.benchmark.testcode;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/cwe-306/BenchmarkTestCWE306_05")
public class BenchmarkTestCWE306_05 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("BenchmarkTestCWE306_05_user");
        String password = request.getParameter("BenchmarkTestCWE306_05_pass");
        String originalRole = request.getHeader("X-Requested-Role");
        String requestedRole = sanitizeRole(originalRole);

        // Safe control: reject missing, altered, or non-allow-listed header values.
        if (requestedRole == null
                || !requestedRole.equals(originalRole)
                || !isAllowedRole(requestedRole)) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "The X-Requested-Role header must be viewer or auditor.");
            return;
        }

        // The sanitized header only selects a view; credentials remain mandatory.
        if (!"admin".equals(username) || !"password123".equals(password)) {
            rejectLogin(response);
            return;
        }

        writeAdministratorPage(response, requestedRole);
    }

    private static String sanitizeRole(String role) {
        return role == null ? null : role.replaceAll("[^A-Za-z0-9_-]", "");
    }

    private static boolean isAllowedRole(String role) {
        return "viewer".equals(role) || "auditor".equals(role);
    }

    private static void writeAdministratorPage(HttpServletResponse response, String role)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html><html><body>");
        out.println("<h1>Authenticated Successfully</h1>");
        out.println("<p>Administrator opened the validated " + role + " view.</p>");
        out.println("</body></html>");
    }

    private static void rejectLogin(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println("<p>Invalid username or password.</p>");
    }
}
