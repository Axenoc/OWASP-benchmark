/**
 * OWASP Benchmark v1.2
 *
 * <p>This is a false-positive control for CWE-293. Referer validation is defense in depth;
 * valid server-side credentials are independently required.
 */
package org.owasp.benchmark.testcode;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/cwe-293/BenchmarkTestCWE293_05")
public class BenchmarkTestCWE293_05 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("BenchmarkTestCWE293_05_user");
        String password = request.getParameter("BenchmarkTestCWE293_05_pass");

        // Safe control: spoofing Referer alone cannot satisfy this credential check.
        if (!"admin".equals(username) || !"password123".equals(password)) {
            rejectLogin(response);
            return;
        }

        String referer = request.getHeader("Referer");
        String expectedSuffix =
                request.getContextPath() + "/cwe-293/BenchmarkTestCWE293_05.html";
        if (referer == null || !referer.endsWith(expectedSuffix)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unexpected navigation source.");
            return;
        }

        writePrivilegedPage(response);
    }

    private static void writePrivilegedPage(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html><html><body>");
        out.println("<h1>Authenticated Administrator</h1>");
        out.println("<pre>serviceToken=Benchmark-Only-Service-Token-293-05</pre>");
        out.println("</body></html>");
    }

    private static void rejectLogin(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println("<p>Invalid username or password.</p>");
    }
}
