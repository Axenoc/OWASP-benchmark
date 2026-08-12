package org.owasp.benchmark.testcode;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/cwe-211/BenchmarkTest02751")
public class BenchmarkTest02751 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain;charset=UTF-8");

        String input = request.getParameter("BenchmarkTest02751");
        String address = "https://benchmark.invalid/BENCHMARK_SECRET_02751/" + input;

        try {
            URI uri = new URI(address);
            response.getWriter().println("The URL is valid");
        } catch (URISyntaxException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println(e.getMessage());
        }
    }
}
