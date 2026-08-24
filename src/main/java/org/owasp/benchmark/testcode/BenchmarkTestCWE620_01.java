/**
 * OWASP Benchmark v1.2
 *
 * <p>This test intentionally contains CWE-620: Unverified Password Change. It is vulnerable by
 * design and must not be copied into production.
 */
package org.owasp.benchmark.testcode;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/cwe-620/BenchmarkTestCWE620_01")
public class BenchmarkTestCWE620_01 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String account = request.getParameter("account");
        String newPassword = request.getParameter("newPassword");
        String mfaVerified = request.getParameter("mfaVerified");

        response.setContentType("text/html;charset=UTF-8");
        // CWE-620: an attacker can add mfaVerified=true to a crafted form request.
        if ("true".equalsIgnoreCase(mfaVerified)
                && account != null
                && newPassword != null
                && !newPassword.isEmpty()) {
            response.getWriter().println("<h3>Password changed for the requested account.</h3>");
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().println("<p>MFA verification is required.</p>");
        }
    }
}
