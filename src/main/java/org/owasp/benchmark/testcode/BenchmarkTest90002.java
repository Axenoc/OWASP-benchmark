package org.owasp.benchmark.testcode;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/quantity-00/BenchmarkTest90002")
public class BenchmarkTest90002 extends HttpServlet {

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

        Enumeration<String> headers = request.getHeaders("BenchmarkTest90002");

        if (headers != null && headers.hasMoreElements()) {
            param = headers.nextElement();
        }

        param = java.net.URLDecoder.decode(param, "UTF-8");

        int amount;

        try {
            amount = Integer.parseInt(param);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            response.getWriter().println("Invalid transaction amount");

            return;
        }

        /*
         * Incomplete quantity validation.
         *
         * Upper bound exists,
         * but lower bound is missing.
         */

        if (amount > 1000) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            response.getWriter().println("Transaction amount too large");

            return;
        }

        int balance = 1000;

        /*
         * CWE-1284:
         *
         * amount = -500
         *
         * newBalance =
         * 1000 - (-500)
         *
         * = 1500
         */

        int newBalance = balance - amount;

        response.getWriter().println("Transaction accepted.<br>");

        response.getWriter().println("Amount: " + amount + "<br>");

        response.getWriter().println("New balance: " + newBalance);
    }
}
