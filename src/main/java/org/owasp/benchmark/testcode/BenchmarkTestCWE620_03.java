/**
 * OWASP Benchmark v1.2
 *
 * <p>This test intentionally contains CWE-620. It is vulnerable by design and must not be copied
 * into production.
 */
package org.owasp.benchmark.testcode;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/cwe-620/BenchmarkTestCWE620_03")
public class BenchmarkTestCWE620_03 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String account = request.getParameter("account");
        String newPassword = request.getParameter("newPassword");
        String mfaCookie = cookieValue(request.getCookies(), "CWE620-MFA");

        response.setContentType("text/html;charset=UTF-8");
        // CWE-620: the unsigned MFA cookie can be created or changed by the attacker.
        if ("passed".equals(mfaCookie)
                && account != null
                && newPassword != null
                && !newPassword.isEmpty()) {
            response.getWriter().println("<h3>Password changed for the requested account.</h3>");
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().println("<p>MFA verification is required.</p>");
        }
    }

    private static String cookieValue(Cookie[] cookies, String name) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
