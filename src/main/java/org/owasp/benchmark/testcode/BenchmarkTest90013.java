package org.owasp.benchmark.testcode;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/syntax-00/BenchmarkTest90013")
public class BenchmarkTest90013 extends HttpServlet {

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

        String param = request.getParameter("BenchmarkTest90013");

        /*
         * Expected syntax:
         *
         * name=<letters>;count=<digits>
         *
         * Example:
         *
         * name=alice;count=10
         *
         * CWE-1286:
         * no syntactic validation is performed.
         */

        String[] fields = param.split(";");

        String name = fields[0].split("=", 2)[1];

        String count = fields[1].split("=", 2)[1];

        response.getWriter().println("Parsed successfully.<br>");

        response.getWriter().println("Name: " + name + "<br>");

        response.getWriter().println("Count: " + count);
    }
}
