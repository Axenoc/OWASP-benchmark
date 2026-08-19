package org.owasp.benchmark.testcode;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/loop-00/BenchmarkTest90005")
public class BenchmarkTest90005 extends HttpServlet {

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

        String param = request.getParameter("BenchmarkTest90005");

        long iterations;

        try {

            iterations = Long.parseLong(param);

        } catch (NumberFormatException e) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            response.getWriter().println("Invalid iteration count");

            return;
        }

        /*
         * CWE-606
         *
         * User-controlled value is used directly
         * as the loop condition.
         *
         * No upper-bound validation exists.
         */

        long result = 0;

        for (int i = 0; i < iterations; i++) {

            /*
             * Some CPU work so excessive iteration
             * has an observable cost.
             */

            result += (i % 7);
        }

        response.getWriter().println("Iterations completed: " + iterations + "<br>");

        response.getWriter().println("Result: " + result);
    }
}
