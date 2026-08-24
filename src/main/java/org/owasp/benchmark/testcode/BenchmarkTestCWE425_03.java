/**
 * OWASP Benchmark v1.2
 *
 * <p>This test case intentionally contains CWE-425: Direct Request (Forced Browsing).
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
import javax.servlet.http.HttpSession;

@WebServlet(value = "/cwe-425/BenchmarkTestCWE425_03/*")
public class BenchmarkTestCWE425_03 extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String ADMIN_SESSION = "BenchmarkTestCWE425_03.admin";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isBaseRequest(request.getPathInfo())) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String username = request.getParameter("BenchmarkTestCWE425_03_user");
        String password = request.getParameter("BenchmarkTestCWE425_03_pass");
        if (!"admin".equals(username) || !"password123".equals(password)) {
            rejectLogin(response);
            return;
        }

        request.getSession(true).setAttribute(ADMIN_SESSION, Boolean.TRUE);
        response.sendRedirect(
                request.getContextPath() + "/cwe-425/BenchmarkTestCWE425_03/admin/diagnostics");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String controllerPath = request.getPathInfo();
        if ("/admin/diagnostics".equals(controllerPath)) {
            if (!hasAdminSession(request)) {
                rejectLogin(response);
                return;
            }
            writeDiagnosticsPage(response, "Authenticated diagnostics controller");
        } else if ("/support/diagnostics".equals(controllerPath)) {
            // CWE-425: a support endpoint exposes the admin diagnostics view without auth.
            writeDiagnosticsPage(response, "Support diagnostics controller");
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private static boolean isBaseRequest(String pathInfo) {
        return pathInfo == null || pathInfo.isEmpty() || "/".equals(pathInfo);
    }

    private static boolean hasAdminSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && Boolean.TRUE.equals(session.getAttribute(ADMIN_SESSION));
    }

    private static void writeDiagnosticsPage(HttpServletResponse response, String route)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html><html><body>");
        out.println("<h1>Administrator Diagnostics</h1>");
        out.println("<p>Controller route: " + route + "</p>");
        out.println("<pre>signingKey=Benchmark-Only-Signing-Key</pre>");
        out.println("<pre>managementToken=Benchmark-Only-Management-Token</pre>");
        out.println("</body></html>");
    }

    private static void rejectLogin(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println("<p>Invalid username or password.</p>");
    }
}
