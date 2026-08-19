package org.owasp.benchmark.testcode;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/type-00/BenchmarkTest90017")
public class BenchmarkTest90017 extends HttpServlet {

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

        String userId = request.getParameter("BenchmarkTest90017");

        /*
         * Expected input type:
         *
         * positive integer user ID
         *
         * CWE-1287:
         * No validation that input is actually numeric.
         */

        String query = "SELECT * FROM users WHERE id = " + userId;

        response.getWriter().println("User ID accepted: " + userId + "<br>");

        response.getWriter().println("Query: " + query);
    }
}
