package org.owasp.benchmark.testcode;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/loop-00/BenchmarkTest90007")
public class BenchmarkTest90007 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final int MAX_ITERATIONS = 1000;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String param = request.getParameter("BenchmarkTest90007");

        int iterations;

        try {

            iterations = Integer.parseInt(param);

        } catch (NumberFormatException e) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            response.getWriter().println("Invalid iteration count");

            return;
        }

        /*
         * Complete range validation.
         */

        if (iterations < 1 || iterations > MAX_ITERATIONS) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            response.getWriter()
                    .println("Iteration count must be " + "between 1 and " + MAX_ITERATIONS);

            return;
        }

        /*
         * Safe:
         *
         * path constraint:
         *
         * 1 <= iterations <= 1000
         */

        long result = 0;

        for (int i = 0; i < iterations; i++) {

            result += (i % 7);
        }

        response.getWriter().println("Iterations completed: " + iterations + "<br>");

        response.getWriter().println("Result: " + result);
    }
}
