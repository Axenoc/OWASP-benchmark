package org.owasp.benchmark.testcode;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/type-00/BenchmarkTest90020")
public class BenchmarkTest90020 extends HttpServlet {

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

        String value = scr.getTheValue("BenchmarkTest90020");

        /*
         * Safe source.
         *
         * The helper returns a known safe value.
         */

        int userId;

        if ("bar".equals(value)) {

            userId = 123;

        } else {

            userId = 1;
        }

        response.getWriter().println("User ID accepted: " + userId);
    }
}
