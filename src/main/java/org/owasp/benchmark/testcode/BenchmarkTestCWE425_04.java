/**
 * OWASP Benchmark v1.2
 *
 * <p>This is a false-positive control for CWE-425. Direct requests to the administrator
 * controller are protected by the same server-side session check as normal navigation.
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

@WebServlet(value = "/cwe-425/BenchmarkTestCWE425_04/*")
public class BenchmarkTestCWE425_04 extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String ADMIN_SESSION = "BenchmarkTestCWE425_04.admin";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isBaseRequest(request.getPathInfo())) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String username = request.getParameter("BenchmarkTestCWE425_04_user");
        String password = request.getParameter("BenchmarkTestCWE425_04_pass");
        if (!"admin".equals(username) || !"password123".equals(password)) {
            rejectLogin(response);
            return;
        }

        request.getSession(true).setAttribute(ADMIN_SESSION, Boolean.TRUE);
        response.sendRedirect(
                request.getContextPath() + "/cwe-425/BenchmarkTestCWE425_04/admin");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!"/admin".equals(request.getPathInfo())) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Safe control: forced browsing cannot skip this server-side authorization check.
        if (!hasAdminSession(request)) {
            rejectLogin(response);
            return;
        }

        writeAdminPage(response);
    }

    private static boolean isBaseRequest(String pathInfo) {
        return pathInfo == null || pathInfo.isEmpty() || "/".equals(pathInfo);
    }

    private static boolean hasAdminSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && Boolean.TRUE.equals(session.getAttribute(ADMIN_SESSION));
    }

    private static void writeAdminPage(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html><html><body>");
        out.println("<h1>Protected Administrator Configuration</h1>");
        out.println("<pre>databaseUser=benchmark_admin</pre>");
        out.println("<pre>databasePassword=Benchmark-Only-DB-Secret</pre>");
        out.println("</body></html>");
    }

    private static void rejectLogin(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println("<p>Authentication required.</p>");
    }
}
