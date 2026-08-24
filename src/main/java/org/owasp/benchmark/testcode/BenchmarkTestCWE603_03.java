/**
 * OWASP Benchmark v1.2
 *
 * <p>This test intentionally contains CWE-603. It is vulnerable by design and must not be copied
 * into production.
 */
package org.owasp.benchmark.testcode;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/cwe-603/BenchmarkTestCWE603_03")
public class BenchmarkTestCWE603_03 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String authenticationCookie = cookieValue(request.getCookies(), "CWE603-ClientAuth");
        response.setContentType("text/html;charset=UTF-8");

        // CWE-603: JavaScript creates this unsigned cookie after its own password comparison.
        if ("authenticated".equals(authenticationCookie)) {
            response.getWriter().println("<h3>Authenticated Support Console</h3>");
            response.getWriter().println("<p>Private support reference: BENCHMARK-603-C</p>");
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("<p>Client-side authentication failed.</p>");
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
