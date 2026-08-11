package org.owasp.benchmark.testcode;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebServlet("/auth-00/BenchmarkTestCWE425")
public class BenchmarkTestCWE425 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String statusMessage = executePrivilegedSystemReset();

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("<h1>Admin Dashboard</h1>");
        response.getWriter().println("<p>Status: " + statusMessage + "</p>");
    }

    private String executePrivilegedSystemReset() {
        return "SUCCESS: System configuration reset to default values.";
    }
}
