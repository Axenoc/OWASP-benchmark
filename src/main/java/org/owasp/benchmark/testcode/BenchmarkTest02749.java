package org.owasp.benchmark.testcode;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/infoleak-00/BenchmarkTest02749")
public class BenchmarkTest02749 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain;charset=UTF-8");

        String input = request.getParameter("BenchmarkTest02749");

        if (!"valid".equals(input)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            // This message is written by the servlet.
            response.getWriter().println("Login failed for key: BENCHMARK_SECRET_02749");
        } else {
            response.getWriter().println("Login successful");
        }
    }
}
