package org.owasp.benchmark.testcode;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/index-00/BenchmarkTest90012")
public class BenchmarkTest90012 extends HttpServlet {

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
         * Safe source:
         * getTheValue() returns constant "bar".
         */

        String param = scr.getTheValue("BenchmarkTest90012");

        int index;

        if ("bar".equals(param)) {

            index = 2;

        } else {

            index = 0;
        }

        String[] records = {"alpha", "beta", "gamma", "delta", "epsilon"};

        /*
         * No attacker-controlled index reaches here.
         */

        String record = records[index];

        response.getWriter().println("Index used: " + index + "<br>");

        response.getWriter().println("Record: " + record);
    }
}
