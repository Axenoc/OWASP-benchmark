/**
 * OWASP Benchmark v1.2
 *
 * <p>This test intentionally contains CWE-645: Overly Restrictive Account Lockout
 * Mechanism. It is vulnerable by design and must not be copied into production code.
 */
package org.owasp.benchmark.testcode;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/cwe-645/BenchmarkTestCWE645_01")
public class BenchmarkTestCWE645_01 extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final int MAX_FAILED_ATTEMPTS = 3;
    private static final String VICTIM_USERNAME = "victim@example.com";
    private static final String VALID_PASSWORD = "CorrectPassword123!";

    private static final ConcurrentHashMap<String, AtomicInteger> FAILED_ATTEMPTS =
            new ConcurrentHashMap<>();
    private static final Set<String> LOCKED_ACCOUNTS = ConcurrentHashMap.newKeySet();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Variation 1: attacker-controlled credentials arrive in ordinary form parameters.
        String username = request.getParameter("BenchmarkTestCWE645_01_user");
        String password = request.getParameter("BenchmarkTestCWE645_01_pass");
        processLogin(username, password, response);
    }

    private static void processLogin(
            String username, String password, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        if (username == null || username.trim().isEmpty() || password == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing credentials.");
            return;
        }

        username = username.trim();
        if (LOCKED_ACCOUNTS.contains(username)) {
            writeLockedResponse(response);
            return;
        }

        if (VICTIM_USERNAME.equals(username) && VALID_PASSWORD.equals(password)) {
            FAILED_ATTEMPTS.remove(username);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("<h3>Login Successful</h3>");
            return;
        }

        int attempts =
                FAILED_ATTEMPTS.computeIfAbsent(username, ignored -> new AtomicInteger())
                        .incrementAndGet();
        if (attempts >= MAX_FAILED_ATTEMPTS) {
            // CWE-645: permanent, global lockout can be triggered remotely for any username.
            LOCKED_ACCOUNTS.add(username);
            writeLockedResponse(response);
            return;
        }

        writeFailedResponse(response, MAX_FAILED_ATTEMPTS - attempts);
    }

    private static void writeFailedResponse(HttpServletResponse response, int attemptsRemaining)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().println("<h3>Login Failed</h3>");
        response.getWriter()
                .println("<p>Account locks after " + attemptsRemaining + " more failure(s).</p>");
    }

    private static void writeLockedResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().println("<h3>ACCOUNT LOCKED</h3>");
        response.getWriter().println("<p>The account is permanently locked.</p>");
    }
}
