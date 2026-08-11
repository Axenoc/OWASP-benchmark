package org.owasp.benchmark.testcode;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/inputval-00/BenchmarkTestCWE1287")
public class BenchmarkTestCWE1287 extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String input = request.getParameter("BenchmarkTestCWE1287");

        try {
            int length = input.length();
            StringBuilder output = new StringBuilder();
            output.append("<h3> Length is: ").append(length).append("</h3>");
            response.getWriter().println(output.toString());

        } catch (NullPointerException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("<p> Error: " + e.toString() + "</p>");
        }
    }
}
