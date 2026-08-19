/**
 * OWASP Benchmark v1.2
 *
 * <p>This test case intentionally contains CWE-645: Overly Restrictive Account Lockout
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
import org.owasp.esapi.ESAPI;

@WebServlet(value = "/cwe-645/BenchmarkTest90023")
public class BenchmarkTest90023 extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final int MAX_FAILED_ATTEMPTS = 3;
    private static final String VALID_USERNAME = "victim@example.com";
    private static final String VALID_PASSWORD = "CorrectPassword123!";

    private static final ConcurrentHashMap<String, AtomicInteger> FAILED_ATTEMPTS =
            new ConcurrentHashMap<>();
    private static final Set<String> LOCKED_ACCOUNTS = ConcurrentHashMap.newKeySet();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.getRequestDispatcher("/cwe-645/BenchmarkTestCWE645.html")
                .include(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        Credentials credentials = parseCredentials(request.getParameter("BenchmarkTestCWE645"));
        if (credentials == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("<h3>Invalid input</h3>");
            response.getWriter().println("<p>Use the format username,password.</p>");
            return;
        }

        String username = credentials.username;
        String safeUsername = ESAPI.encoder().encodeForHTML(username);
        if (LOCKED_ACCOUNTS.contains(username)) {
            writeLockedResponse(response, safeUsername);
            return;
        }

        if (VALID_USERNAME.equals(username) && VALID_PASSWORD.equals(credentials.password)) {
            FAILED_ATTEMPTS.remove(username);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("<h3>Login Successful</h3>");
            return;
        }

        int attempts =
                FAILED_ATTEMPTS.computeIfAbsent(username, ignored -> new AtomicInteger())
                        .incrementAndGet();
        if (attempts >= MAX_FAILED_ATTEMPTS) {
            LOCKED_ACCOUNTS.add(username);
            writeLockedResponse(response, safeUsername);
            return;
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().println("<h3>Login Failed</h3>");
        response.getWriter()
                .println(
                        "<p>Account '"
                                + safeUsername
                                + "' will be locked after "
                                + (MAX_FAILED_ATTEMPTS - attempts)
                                + " more failed attempt(s).</p>");
    }

    private static Credentials parseCredentials(String input) {
        if (input == null) {
            return null;
        }

        String[] values = input.split(",", 2);
        if (values.length != 2 || values[0].trim().isEmpty()) {
            return null;
        }
        return new Credentials(values[0].trim(), values[1]);
    }

    private static void writeLockedResponse(HttpServletResponse response, String safeUsername)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().println("<h3>ACCOUNT LOCKED</h3>");
        response.getWriter()
                .println(
                        "<p>Account '"
                                + safeUsername
                                + "' is locked due to excessive failed attempts.</p>");
    }

    private static final class Credentials {
        private final String username;
        private final String password;

        private Credentials(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }
}
