package org.owasp.benchmark.testcode;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/quantity-00/BenchmarkTest90004")
public class BenchmarkTest90004 extends HttpServlet {

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
         * Safe source.
         *
         * SeparateClassRequest.getTheValue()
         * always returns "bar".
         */

        String param = scr.getTheValue("BenchmarkTest90004");

        /*
         * The quantity is derived from trusted data,
         * not from the HTTP request.
         */

        int quantity;

        if ("bar".equals(param)) {
            quantity = 3;
        } else {
            quantity = 1;
        }

        int unitPrice = 20;

        int total = unitPrice * quantity;

        response.getWriter().println("Quantity accepted: " + quantity + "<br>");

        response.getWriter().println("Total charge: " + total);
    }
}
