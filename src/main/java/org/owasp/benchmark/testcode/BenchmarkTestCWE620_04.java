/**
 * OWASP Benchmark v1.2
 *
 * <p>False-positive control for CWE-620: the server validates the current password and MFA code.
 */
package org.owasp.benchmark.testcode;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/cwe-620/BenchmarkTestCWE620_04")
public class BenchmarkTestCWE620_04 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String account = request.getParameter("account");
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String mfaCode = request.getParameter("mfaCode");

        boolean authenticated =
                "victim@example.com".equals(account)
                        && "OriginalPassword123!".equals(currentPassword);
        boolean serverVerifiedMfa = "246810".equals(mfaCode);
        boolean validNewPassword = newPassword != null && newPassword.length() >= 12;

        response.setContentType("text/html;charset=UTF-8");
        if (authenticated && serverVerifiedMfa && validNewPassword) {
            response.getWriter().println("<h3>Password changed after server-side MFA verification.</h3>");
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("<p>Current password, valid MFA code, and a strong new password are required.</p>");
        }
    }
}
