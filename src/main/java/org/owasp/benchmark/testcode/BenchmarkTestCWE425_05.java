/**
 * OWASP Benchmark v1.2
 *
 * <p>This is a false-positive control for CWE-425. A legacy route is redirected to an
 * authenticated controller instead of rendering sensitive data directly.
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

@WebServlet(value = "/cwe-425/BenchmarkTestCWE425_05/*")
public class BenchmarkTestCWE425_05 extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String ADMIN_SESSION = "BenchmarkTestCWE425_05.admin";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isBaseRequest(request.getPathInfo())) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String username = request.getParameter("BenchmarkTestCWE425_05_user");
        String password = request.getParameter("BenchmarkTestCWE425_05_pass");
        if (!"admin".equals(username) || !"password123".equals(password)) {
            rejectLogin(response);
            return;
        }

        request.getSession(true).setAttribute(ADMIN_SESSION, Boolean.TRUE);
        redirectToProtectedExport(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String controllerPath = request.getPathInfo();
        if ("/legacy/admin/export".equals(controllerPath)) {
            // Safe control: the old alias never renders data; it enters the protected route.
            redirectToProtectedExport(request, response);
            return;
        }

        if (!"/admin/exports".equals(controllerPath)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Safe control: both normal and redirected requests must have an authenticated session.
        if (!hasAdminSession(request)) {
            rejectLogin(response);
            return;
        }

        writeExportPage(response);
    }

    private static void redirectToProtectedExport(
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(
                request.getContextPath() + "/cwe-425/BenchmarkTestCWE425_05/admin/exports");
    }

    private static boolean isBaseRequest(String pathInfo) {
        return pathInfo == null || pathInfo.isEmpty() || "/".equals(pathInfo);
    }

    private static boolean hasAdminSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && Boolean.TRUE.equals(session.getAttribute(ADMIN_SESSION));
    }

    private static void writeExportPage(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html><html><body>");
        out.println("<h1>Protected Service Account Export</h1>");
        out.println("<pre>account=malware-scanner</pre>");
        out.println("<pre>apiToken=Benchmark-Only-Service-Token</pre>");
        out.println("</body></html>");
    }

    private static void rejectLogin(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println("<p>Authentication required.</p>");
    }
}
