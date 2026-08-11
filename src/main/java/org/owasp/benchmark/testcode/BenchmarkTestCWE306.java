package org.owasp.benchmark.testcode;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/authen-00/BenchmarkTestCWE306")
public class BenchmarkTestCWE306 extends HttpServlet {

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

        String amountStr = request.getParameter("BenchmarkTestCWE306");
        try {
            double amount = Double.parseDouble(amountStr);
            String transactionResult = withdrawFunds("ACC-99201", amount);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("<h3>Transaction Complete</h3>");
            response.getWriter().println("<p>" + transactionResult + "</p>");
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("<p>Invalid withdrawal amount provided.</p>");
        }
    }
    private String withdrawFunds(String accountId, double amount) {
        return "Successfully withdrew $" + amount + " from account " + accountId;
    }
}