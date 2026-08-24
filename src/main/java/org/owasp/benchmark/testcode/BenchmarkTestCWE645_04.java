/**
 * OWASP Benchmark v1.2
 *
 * <p>This is a false-positive control for CWE-645. Failed attempts temporarily throttle only
 * the attacker's HTTP session; they never lock the user account globally.
 */
package org.owasp.benchmark.testcode;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(value = "/cwe-645/BenchmarkTestCWE645_04")
public class BenchmarkTestCWE645_04 extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final int MAX_FAILED_ATTEMPTS = 3;
    private static final int TOO_MANY_REQUESTS = 429;
    private static final long THROTTLE_MILLIS = 30_000L;
    private static final String ATTEMPTS_KEY = "BenchmarkTestCWE645_04.attempts";
    private static final String THROTTLED_UNTIL_KEY = "BenchmarkTestCWE645_04.throttledUntil";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true);
        long now = System.currentTimeMillis();

        Long throttledUntil = (Long) session.getAttribute(THROTTLED_UNTIL_KEY);
        if (throttledUntil != null && throttledUntil > now) {
            writeThrottleResponse(response, throttledUntil - now);
            return;
        }
        if (throttledUntil != null) {
            session.removeAttribute(THROTTLED_UNTIL_KEY);
            session.removeAttribute(ATTEMPTS_KEY);
        }

        String username = request.getParameter("BenchmarkTestCWE645_04_user");
        String password = request.getParameter("BenchmarkTestCWE645_04_pass");
        if ("victim@example.com".equals(username)
                && "CorrectPassword123!".equals(password)) {
            session.removeAttribute(ATTEMPTS_KEY);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("<h3>Login Successful</h3>");
            return;
        }

        int attempts = integerAttribute(session, ATTEMPTS_KEY) + 1;
        if (attempts >= MAX_FAILED_ATTEMPTS) {
            session.removeAttribute(ATTEMPTS_KEY);
            session.setAttribute(THROTTLED_UNTIL_KEY, now + THROTTLE_MILLIS);
            writeThrottleResponse(response, THROTTLE_MILLIS);
            return;
        }

        session.setAttribute(ATTEMPTS_KEY, attempts);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().println("<h3>Login Failed</h3>");
    }

    private static int integerAttribute(HttpSession session, String name) {
        Object value = session.getAttribute(name);
        return value instanceof Integer ? (Integer) value : 0;
    }

    private static void writeThrottleResponse(HttpServletResponse response, long remainingMillis)
            throws IOException {
        long remainingSeconds = Math.max(1L, (remainingMillis + 999L) / 1000L);
        response.setStatus(TOO_MANY_REQUESTS);
        response.setHeader("Retry-After", Long.toString(remainingSeconds));
        response.getWriter().println("<h3>Client Session Temporarily Throttled</h3>");
        response.getWriter().println("<p>The user account remains available to other sessions.</p>");
    }
}
