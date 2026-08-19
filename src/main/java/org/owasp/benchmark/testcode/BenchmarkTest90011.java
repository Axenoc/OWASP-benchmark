package org.owasp.benchmark.testcode;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/index-00/BenchmarkTest90011")
public class BenchmarkTest90011 extends HttpServlet {

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

        String param = request.getParameter("BenchmarkTest90011");

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
         * Correct full bounds validation.
         */

        if (index < 0 || index >= records.length) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            response.getWriter().println("Index must be between 0 and " + (records.length - 1));

            return;
        }

        /*
         * Safe path constraint:
         *
         * 0 <= index < records.length
         */

        String record = records[index];

        response.getWriter().println("Index accepted: " + index + "<br>");

        response.getWriter().println("Record: " + record);
    }
}
