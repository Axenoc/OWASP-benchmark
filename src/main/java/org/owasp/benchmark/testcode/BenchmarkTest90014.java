package org.owasp.benchmark.testcode;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/syntax-00/BenchmarkTest90014")
public class BenchmarkTest90014 extends HttpServlet {

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

        Enumeration<String> headers = request.getHeaders("BenchmarkTest90014");

        if (headers != null && headers.hasMoreElements()) {

            param = headers.nextElement();
        }

        param = java.net.URLDecoder.decode(param, "UTF-8");

        /*
         * Expected:
         *
         * name=<letters>;count=<digits>
         *
         * Incomplete syntactic validation.
         */

        if (!param.contains("name=") || !param.contains(";")) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            response.getWriter().println("Invalid syntax");

            return;
        }

        /*
         * The validator does NOT verify:
         *
         * - exact field order
         * - presence of count=
         * - field count
         * - allowed characters
         * - complete grammar
         */

        String[] fields = param.split(";");

        String name = fields[0].split("=", 2)[1];

        String count = fields[1].split("=", 2)[1];

        response.getWriter().println("Parsed successfully.<br>");

        response.getWriter().println("Name: " + name + "<br>");

        response.getWriter().println("Count: " + count);
    }
}
