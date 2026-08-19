package org.owasp.benchmark.testcode;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/index-00/BenchmarkTest90010")
public class BenchmarkTest90010 extends HttpServlet {

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

        Enumeration<String> headers = request.getHeaders("BenchmarkTest90010");

        if (headers != null && headers.hasMoreElements()) {

            param = headers.nextElement();
        }

        param = java.net.URLDecoder.decode(param, "UTF-8");

        int index;

        try {

            index = Integer.parseInt(param);

        } catch (NumberFormatException e) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            response.getWriter().println("Invalid index");

            return;
        }

        String[] records = {"alpha", "beta", "gamma", "delta", "epsilon"};

        /*
         * Incomplete validation.
         *
         * Upper bound is checked,
         * but lower bound is missing.
         */

        if (index >= records.length) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            response.getWriter().println("Index too large");

            return;
        }

        /*
         * index = -1 passes:
         *
         * -1 >= 5
         *
         * false
         *
         * CWE-1285
         */

        String record = records[index];

        response.getWriter().println("Index accepted: " + index + "<br>");

        response.getWriter().println("Record: " + record);
    }
}
