package org.owasp.benchmark.testcode;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/loop-00/BenchmarkTest90006")
public class BenchmarkTest90006 extends HttpServlet {

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

        String param = "";

        Enumeration<String> headers = request.getHeaders("BenchmarkTest90006");

        if (headers != null && headers.hasMoreElements()) {

            param = headers.nextElement();
        }

        param = java.net.URLDecoder.decode(param, "UTF-8");

        int iterations;

        try {

            iterations = Integer.parseInt(param);

        } catch (NumberFormatException e) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            response.getWriter().println("Invalid iteration count");

            return;
        }

        /*
         * Validation exists...
         */

        if (iterations <= 0) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            response.getWriter().println("Iteration count must be positive");

            return;
        }

        /*
         * ...but there is NO upper bound.
         *
         * 1,000,000,000 passes validation.
         *
         * CWE-606
         */

        long result = 0;

        for (int i = 0; i < iterations; i++) {

            result += (i % 13);
        }

        response.getWriter().println("Loop completed.<br>");

        response.getWriter().println("Iterations: " + iterations + "<br>");

        response.getWriter().println("Result: " + result);
    }
}
