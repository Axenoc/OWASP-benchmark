package org.owasp.benchmark.testcode;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/index-00/BenchmarkTest90009")
public class BenchmarkTest90009 extends HttpServlet {

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

        String param = request.getParameter("BenchmarkTest90009");

        int index;

        try {

            index = Integer.parseInt(param);

        } catch (NumberFormatException e) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            response.getWriter().println("Invalid index");

            return;
        }

        String[] records = {
            "public-record-0",
            "public-record-1",
            "public-record-2",
            "public-record-3",
            "public-record-4"
        };

        /*
         * CWE-1285
         *
         * User-controlled index is used directly.
         *
         * Missing:
         *
         * index >= 0
         * index < records.length
         */

        String record = records[index];

        response.getWriter().println("Selected index: " + index + "<br>");

        response.getWriter().println("Record: " + record);
    }
}
