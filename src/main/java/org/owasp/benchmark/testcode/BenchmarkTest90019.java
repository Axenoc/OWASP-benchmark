package org.owasp.benchmark.testcode;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/type-00/BenchmarkTest90019")
public class BenchmarkTest90019 extends HttpServlet {

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

        String param = request.getParameter("BenchmarkTest90019");

        /*
         * Strict type validation.
         *
         * Expected type:
         * integer represented entirely by digits.
         */

        if (param == null || !param.matches("[0-9]+")) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            response.getWriter().println("User ID must be numeric");

            return;
        }

        int userId;

        try {

            userId = Integer.parseInt(param);

        } catch (NumberFormatException e) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            response.getWriter().println("User ID is outside integer range");

            return;
        }

        /*
         * Only correctly typed input reaches here.
         */

        response.getWriter().println("User ID accepted: " + userId);
    }
}
