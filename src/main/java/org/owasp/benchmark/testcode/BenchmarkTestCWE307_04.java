/**
 * OWASP Benchmark v1.2
 *
 * <p>This is a false-positive control for CWE-307. Cookie-based authentication attempts are
 * temporarily restricted per HTTP session.
 */
package org.owasp.benchmark.testcode;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(value = "/cwe-307/BenchmarkTestCWE307_04")
public class BenchmarkTestCWE307_04 extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final int MAX_ATTEMPTS = 5;
    private static final int TOO_MANY_REQUESTS = 429;
    private static final long THROTTLE_MILLIS = 30_000L;
    private static final String ATTEMPTS_KEY = "BenchmarkTestCWE307_04.attempts";
    private static final String THROTTLED_UNTIL_KEY = "BenchmarkTestCWE307_04.until";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true);
        long now = System.currentTimeMillis();

        Long throttledUntil = (Long) session.getAttribute(THROTTLED_UNTIL_KEY);
        if (throttledUntil != null && throttledUntil > now) {
            writeThrottled(response, throttledUntil - now);
            return;
        }
        if (throttledUntil != null) {
            session.removeAttribute(THROTTLED_UNTIL_KEY);
            session.removeAttribute(ATTEMPTS_KEY);
        }

        String combined = cookieValue(request.getCookies(), "CWE307-Limited-Credentials");
        String[] credentials = splitCredentials(combined);
        if ("admin".equals(credentials[0]) && "password123".equals(credentials[1])) {
            session.removeAttribute(ATTEMPTS_KEY);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("<h3>Authenticated Successfully</h3>");
            return;
        }

        int attempts = integerAttribute(session, ATTEMPTS_KEY) + 1;
        if (attempts >= MAX_ATTEMPTS) {
            session.removeAttribute(ATTEMPTS_KEY);
            session.setAttribute(THROTTLED_UNTIL_KEY, now + THROTTLE_MILLIS);
            writeThrottled(response, THROTTLE_MILLIS);
            return;
        }

        session.setAttribute(ATTEMPTS_KEY, attempts);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().println("<p>Invalid username or password.</p>");
    }

    private static String[] splitCredentials(String combined) {
        if (combined == null) {
            return new String[] {"", ""};
        }
        String[] values = combined.split(":", 2);
        return values.length == 2 ? values : new String[] {"", ""};
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

    private static int integerAttribute(HttpSession session, String name) {
        Object value = session.getAttribute(name);
        return value instanceof Integer ? (Integer) value : 0;
    }

    private static void writeThrottled(HttpServletResponse response, long remainingMillis)
            throws IOException {
        long seconds = Math.max(1L, (remainingMillis + 999L) / 1000L);
        response.setStatus(TOO_MANY_REQUESTS);
        response.setHeader("Retry-After", Long.toString(seconds));
        response.getWriter().println("<p>Too many attempts. Try again later.</p>");
    }
}
