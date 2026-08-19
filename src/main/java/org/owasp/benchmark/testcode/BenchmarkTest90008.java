package org.owasp.benchmark.testcode;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/loop-00/BenchmarkTest90008")
public class BenchmarkTest90008 extends HttpServlet {

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

        org.owasp.benchmark.helpers.SeparateClassRequest scr =
                new org.owasp.benchmark.helpers.SeparateClassRequest(request);

        /*
         * Safe source.
         *
         * SeparateClassRequest.getTheValue()
         * returns "bar".
         */

        String param = scr.getTheValue("BenchmarkTest90008");

        int iterations;

        if ("bar".equals(param)) {

            iterations = 100;

        } else {

            iterations = 10;
        }

        /*
         * iterations cannot be influenced
         * by the HTTP parameter.
         */

        long result = 0;

        for (int i = 0; i < iterations; i++) {

            result += (i % 7);
        }

        response.getWriter().println("Iterations completed: " + iterations + "<br>");

        response.getWriter().println("Result: " + result);
    }
}
