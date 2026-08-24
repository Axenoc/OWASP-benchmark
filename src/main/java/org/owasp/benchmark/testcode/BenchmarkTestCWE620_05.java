/**
 * OWASP Benchmark v1.2
 *
 * <p>False-positive control for CWE-620: MFA state is stored and consumed on the server.
 */
package org.owasp.benchmark.testcode;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(value = "/cwe-620/BenchmarkTestCWE620_05")
public class BenchmarkTestCWE620_05 extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String MFA_SESSION_ATTRIBUTE = "CWE620_SERVER_MFA_VERIFIED";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String operation = request.getParameter("operation");
        response.setContentType("text/html;charset=UTF-8");

        if ("verifyMfa".equals(operation)) {
            verifyMfa(request, response);
        } else if ("changePassword".equals(operation)) {
            changePassword(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("<p>Unknown operation.</p>");
        }
    }

    private static void verifyMfa(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if ("135790".equals(request.getParameter("mfaCode"))) {
            request.getSession(true).setAttribute(MFA_SESSION_ATTRIBUTE, Boolean.TRUE);
            response.getWriter().println("<p>MFA verified on the server for this session.</p>");
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("<p>Invalid MFA code.</p>");
        }
    }

    private static void changePassword(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        boolean serverVerified =
                session != null && Boolean.TRUE.equals(session.getAttribute(MFA_SESSION_ATTRIBUTE));
        String newPassword = request.getParameter("newPassword");

        if (serverVerified && newPassword != null && newPassword.length() >= 12) {
            session.removeAttribute(MFA_SESSION_ATTRIBUTE);
            response.getWriter().println("<h3>Password changed; the one-time MFA state was consumed.</h3>");
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().println("<p>A server-verified MFA session and strong password are required.</p>");
        }
    }
}
