/**
 * OWASP Benchmark v1.2
 *
 * <p>This is a false-positive control for CWE-307. Cookie-based authentication attempts are
 * restricted by a server-side per-client time window.
 */
package org.owasp.benchmark.testcode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/cwe-307/BenchmarkTestCWE307_05")
public class BenchmarkTestCWE307_05 extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final int MAX_ATTEMPTS = 5;
    private static final int TOO_MANY_REQUESTS = 429;
    private static final long WINDOW_MILLIS = 30_000L;
    private static final ConcurrentHashMap<String, AttemptWindow> CLIENT_WINDOWS =
            new ConcurrentHashMap<>();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String encoded = cookieValue(request.getCookies(), "CWE307-Throttled-Auth");
        String[] credentials = decodeCredentials(encoded);
        String clientAddress = request.getRemoteAddr();
        long now = System.currentTimeMillis();
        AttemptWindow window =
                CLIENT_WINDOWS.compute(
                        clientAddress,
                        (ignored, current) ->
                                current == null || now - current.startedAt >= WINDOW_MILLIS
                                        ? new AttemptWindow(now, 1)
                                        : new AttemptWindow(current.startedAt, current.attempts + 1));

        // Enforce the window before checking whether the next password guess is correct.
        if (window.attempts > MAX_ATTEMPTS) {
            long remaining = Math.max(1L, WINDOW_MILLIS - (now - window.startedAt));
            writeThrottled(response, remaining);
            return;
        }

        if ("admin".equals(credentials[0]) && "password123".equals(credentials[1])) {
            CLIENT_WINDOWS.remove(clientAddress);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("<h3>Authenticated Successfully</h3>");
            return;
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().println("<p>Invalid username or password.</p>");
    }

    private static String[] decodeCredentials(String encoded) {
        if (encoded == null) {
            return new String[] {"", ""};
        }
        try {
            String decoded =
                    new String(Base64.getUrlDecoder().decode(encoded), StandardCharsets.UTF_8);
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

    private static void writeThrottled(HttpServletResponse response, long remainingMillis)
            throws IOException {
        long seconds = Math.max(1L, (remainingMillis + 999L) / 1000L);
        response.setStatus(TOO_MANY_REQUESTS);
        response.setHeader("Retry-After", Long.toString(seconds));
        response.getWriter().println("<p>Too many attempts. Try again later.</p>");
    }

    private static final class AttemptWindow {
        private final long startedAt;
        private final int attempts;

        private AttemptWindow(long startedAt, int attempts) {
            this.startedAt = startedAt;
            this.attempts = attempts;
        }
    }
}
