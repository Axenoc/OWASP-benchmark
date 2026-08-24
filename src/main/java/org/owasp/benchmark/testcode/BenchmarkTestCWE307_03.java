/**
 * OWASP Benchmark v1.2
 *
 * <p>This test intentionally contains CWE-307: Improper Restriction of Excessive
 * Authentication Attempts. It is vulnerable by design and must not be copied into production.
 */
package org.owasp.benchmark.testcode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/cwe-307/BenchmarkTestCWE307_03")
public class BenchmarkTestCWE307_03 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String encodedCredentials = cookieValue(request.getCookies(), "CWE307-Auth");
        String[] credentials = decodeCredentials(encodedCredentials);

        // CWE-307: every encoded-cookie attempt is processed with no rate or failure limit.
        authenticate(credentials[0], credentials[1], response);
    }

    private static String[] decodeCredentials(String encodedCredentials) {
        if (encodedCredentials == null) {
            return new String[] {"", ""};
        }
        try {
            String decoded =
                    new String(
                            Base64.getUrlDecoder().decode(encodedCredentials),
                            StandardCharsets.UTF_8);
            String[] values = decoded.split(":", 2);
            return values.length == 2 ? values : new String[] {"", ""};
        } catch (IllegalArgumentException exception) {
            return new String[] {"", ""};
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

    private static void authenticate(
            String username, String password, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        if ("admin".equals(username) && "password123".equals(password)) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("<h3>Authenticated Successfully</h3>");
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("<p>Invalid username or password.</p>");
        }
    }
}
