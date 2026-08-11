package org.owasp.benchmark.testcode;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/auth-00/BenchmarkTestCWE645")
public class BenchmarkTestCWE645 extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final ConcurrentHashMap<String, Integer> failedAttempts =
            new ConcurrentHashMap<>();

    private static final int MAX_ALLOWED_ATTEMPTS = 1;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String input = request.getParameter("BenchmarkTestCWE645");
        String[] list = input.split(",");
        String username = list[0];
        String password = list[1];
        if (username == null || username.trim().isEmpty()) {
            username = "default_user";
        }

        int attempts = failedAttempts.getOrDefault(username, 0);

        if (attempts >= MAX_ALLOWED_ATTEMPTS) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().println("<h3>ACCOUNT LOCKED</h3>");
            response.getWriter()
                    .println(
                            "<p>Account '"
                                    + username
                                    + "' is locked due to excessive failed attempts.</p>");
            return;
        }

        if ("admin".equals(username) && "CorrectPassword123!".equals(password)) {
            failedAttempts.remove(username); 
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("<h3>Login Successful</h3>");
        } else {
            failedAttempts.put(username, attempts + 1);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("<h3>Login Failed</h3>");
        }
    }
}
