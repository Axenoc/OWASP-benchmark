/**
 * OWASP Benchmark v1.2
 *
 * <p>This is a false-positive control for CWE-645. Failed attempts temporarily throttle only
 * the originating network client; they never lock a username globally.
 */
package org.owasp.benchmark.testcode;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/cwe-645/BenchmarkTestCWE645_05")
public class BenchmarkTestCWE645_05 extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final int MAX_FAILED_ATTEMPTS = 3;
    private static final int TOO_MANY_REQUESTS = 429;
    private static final long THROTTLE_MILLIS = 30_000L;

    private static final ConcurrentHashMap<String, Integer> CLIENT_FAILURES =
            new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Long> CLIENT_THROTTLED_UNTIL =
            new ConcurrentHashMap<>();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String clientAddress = request.getRemoteAddr();
        long now = System.currentTimeMillis();

        Long throttledUntil = CLIENT_THROTTLED_UNTIL.get(clientAddress);
        if (throttledUntil != null && throttledUntil > now) {
            writeThrottleResponse(response, throttledUntil - now);
            return;
        }
        if (throttledUntil != null) {
            CLIENT_THROTTLED_UNTIL.remove(clientAddress, throttledUntil);
            CLIENT_FAILURES.remove(clientAddress);
        }

        // Variation: credentials arrive in headers, but the throttle key is server-derived.
        String username = request.getHeader("X-Benchmark-Username");
        String password = request.getHeader("X-Benchmark-Password");
        if ("victim@example.com".equals(username)
                && "CorrectPassword123!".equals(password)) {
            CLIENT_FAILURES.remove(clientAddress);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("<h3>Login Successful</h3>");
            return;
        }

        int attempts = CLIENT_FAILURES.merge(clientAddress, 1, Integer::sum);
        if (attempts >= MAX_FAILED_ATTEMPTS) {
            CLIENT_FAILURES.remove(clientAddress);
            CLIENT_THROTTLED_UNTIL.put(clientAddress, now + THROTTLE_MILLIS);
            writeThrottleResponse(response, THROTTLE_MILLIS);
            return;
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().println("<h3>Login Failed</h3>");
    }

    private static void writeThrottleResponse(HttpServletResponse response, long remainingMillis)
            throws IOException {
        long remainingSeconds = Math.max(1L, (remainingMillis + 999L) / 1000L);
        response.setStatus(TOO_MANY_REQUESTS);
        response.setHeader("Retry-After", Long.toString(remainingSeconds));
        response.getWriter().println("<h3>Network Client Temporarily Throttled</h3>");
        response.getWriter().println("<p>No user account has been locked.</p>");
    }
}
