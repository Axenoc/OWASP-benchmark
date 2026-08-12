package org.owasp.benchmark.testcode;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/cwe-644/BenchmarkTest02742")
public class BenchmarkTest02742 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String input = request.getParameter("BenchmarkTest02742");

        if (input == null) {
            input = "";
        }

        String safeInput = input.replaceAll("[^a-zA-Z0-9._ -]", "");

        response.setHeader("X-Benchmark-Test", safeInput);

        response.getWriter().println("BenchmarkTest02742");
    }
}
