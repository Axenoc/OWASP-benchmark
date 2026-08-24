/**
 * OWASP Benchmark v1.2
 *
 * <p>False-positive control for CWE-603: credentials are authenticated by the server.
 */
package org.owasp.benchmark.testcode;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/cwe-603/BenchmarkTestCWE603_04")
public class BenchmarkTestCWE603_04 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String ignoredClientClaim = request.getParameter("clientAuthenticated");

        // The client claim is untrusted metadata. Authentication is repeated on the server.
        boolean clientSentClaim = "true".equalsIgnoreCase(ignoredClientClaim);
        boolean serverAuthenticated =
                "admin".equals(username) && "ServerPassword123!".equals(password);

        response.setContentType("text/html;charset=UTF-8");
        if (serverAuthenticated) {
            response.getWriter().println("<h3>Authenticated by the server.</h3>");
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println(
                    "<p>Invalid credentials; client claim was "
                            + (clientSentClaim ? "ignored" : "absent")
                            + ".</p>");
        }
    }
}
