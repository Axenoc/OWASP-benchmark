package org.owasp.benchmark.testcode;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/type-00/BenchmarkTest90018")
public class BenchmarkTest90018 extends HttpServlet {

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

        Enumeration<String> headers = request.getHeaders("BenchmarkTest90018");

        if (headers != null && headers.hasMoreElements()) {

            param = headers.nextElement();
        }

        param = java.net.URLDecoder.decode(param, "UTF-8");

        /*
         * Expected type:
         * integer
         *
         * Incorrect type validation:
         *
         * verifies only that at least one digit exists.
         */

        if (!param.matches(".*[0-9].*")) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            response.getWriter().println("User ID must contain a digit");

            return;
        }

        /*
         * Inputs such as:
         *
         * abc123
         * 12xyz
         *
         * incorrectly pass validation.
         */

        String query = "SELECT * FROM users WHERE id = " + param;

        response.getWriter().println("ID accepted: " + param + "<br>");

        response.getWriter().println("Query: " + query);
    }
}
