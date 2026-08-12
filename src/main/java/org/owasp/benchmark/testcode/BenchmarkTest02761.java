package org.owasp.benchmark.testcode;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/cwe-548/BenchmarkTest02761")
public class BenchmarkTest02761 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain;charset=UTF-8");

        response.getWriter().println("Index of /benchmark-files/");
        response.getWriter().println("backup.sql");
        response.getWriter().println("config.properties");
        response.getWriter().println("secret.txt");
    }
}
