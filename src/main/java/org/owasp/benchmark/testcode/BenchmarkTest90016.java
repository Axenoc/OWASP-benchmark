package org.owasp.benchmark.testcode;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/syntax-00/BenchmarkTest90016")
public class BenchmarkTest90016 extends HttpServlet {

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
         * getTheValue() is a safe source.
         *
         * It returns constant "bar".
         */

        String safeValue = scr.getTheValue("BenchmarkTest90016");

        /*
         * Internally construct syntactically
         * valid input.
         */

        String param = "name=" + safeValue + ";count=3";

        String[] fields = param.split(";");

        String name = fields[0].split("=", 2)[1];

        String count = fields[1].split("=", 2)[1];

        response.getWriter().println("Parsed successfully.<br>");

        response.getWriter().println("Name: " + name + "<br>");

        response.getWriter().println("Count: " + count);
    }
}
