/**
 * OWASP Benchmark v1.2
 *
 * <p>This test case intentionally contains CWE-306: Missing Authentication for Critical
 * Function. It is vulnerable by design and must not be copied into production code.
 */
package org.owasp.benchmark.testcode;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/cwe-306/BenchmarkTestCWE306_02")
public class BenchmarkTestCWE306_02 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("BenchmarkTestCWE306_02_user");
        String password = request.getParameter("BenchmarkTestCWE306_02_pass");

        boolean validCredentials =
                "admin".equals(username) && "password123".equals(password);

        // CWE-306: the application trusts a header that any direct client can supply.
        boolean headerBypass =
                "admin".equals(request.getHeader("X-Authenticated-User"));

        if (validCredentials || headerBypass) {
            writeAdministratorPage(response, headerBypass ? "HTTP header" : "credentials");
        } else {
            rejectLogin(response);
        }
    }

    private static void writeAdministratorPage(HttpServletResponse response, String loginMethod)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html><html><body>");
        out.println("<h1>Authenticated Successfully</h1>");
        out.println("<p>Administrator configuration access granted via " + loginMethod + ".</p>");
        out.println("</body></html>");
    }

    private static void rejectLogin(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println("<p>Invalid username or password.</p>");
    }
}
