package org.owasp.benchmark.testcode;

import java.io.IOException;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/syntax-00/BenchmarkTest90015")
public class BenchmarkTest90015 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Pattern VALID_SYNTAX =
            Pattern.compile("name=[A-Za-z]{1,20};count=[0-9]{1,4}");

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String param = request.getParameter("BenchmarkTest90015");

        /*
         * Full syntactic validation.
         *
         * Expected grammar:
         *
         * name=<1-20 letters>;count=<1-4 digits>
         */

        if (param == null || !VALID_SYNTAX.matcher(param).matches()) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            response.getWriter().println("Input does not conform " + "to expected syntax");

            return;
        }

        /*
         * Parser is now only reachable when
         * the syntax is valid.
         */

        String[] fields = param.split(";");

        String name = fields[0].split("=", 2)[1];

        String count = fields[1].split("=", 2)[1];

        response.getWriter().println("Parsed successfully.<br>");

        response.getWriter().println("Name: " + name + "<br>");

        response.getWriter().println("Count: " + count);
    }
}
