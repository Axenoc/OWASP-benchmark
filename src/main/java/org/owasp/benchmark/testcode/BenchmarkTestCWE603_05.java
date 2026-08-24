/**
 * OWASP Benchmark v1.2
 *
 * <p>False-positive control for CWE-603: authentication state is held in the server session.
 */
package org.owasp.benchmark.testcode;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(value = "/cwe-603/BenchmarkTestCWE603_05")
public class BenchmarkTestCWE603_05 extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String AUTHENTICATED_SESSION_ATTRIBUTE = "CWE603_SERVER_AUTHENTICATED";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String operation = request.getParameter("operation");
        response.setContentType("text/html;charset=UTF-8");

        if ("login".equals(operation)) {
            login(request, response);
        } else if ("dashboard".equals(operation)) {
            openDashboard(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("<p>Unknown operation.</p>");
        }
    }

    private static void login(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        boolean valid =
                "admin".equals(request.getParameter("username"))
                        && "SessionPassword123!".equals(request.getParameter("password"));
        if (valid) {
            request.getSession(true).setAttribute(AUTHENTICATED_SESSION_ATTRIBUTE, Boolean.TRUE);
            response.getWriter().println("<p>Server authentication succeeded.</p>");
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("<p>Invalid credentials.</p>");
        }
    }

    private static void openDashboard(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String ignoredClientHeader = request.getHeader("X-Client-Authenticated");
        HttpSession session = request.getSession(false);
        boolean serverAuthenticated =
                session != null
                        && Boolean.TRUE.equals(
                                session.getAttribute(AUTHENTICATED_SESSION_ATTRIBUTE));

        if (serverAuthenticated) {
            response.getWriter().println("<h3>Server-session protected dashboard.</h3>");
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println(
                    "<p>Login required; client header was "
                            + (ignoredClientHeader == null ? "absent" : "ignored")
                            + ".</p>");
        }
    }
}
