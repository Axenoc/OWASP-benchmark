package org.owasp.benchmark.testcode;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/auth-00/BenchmarkTestCWE41")
public class BenchmarkTestCWE41 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String fileName = request.getParameter("BenchmarkTestCWE41");

        if (fileName == null || fileName.isEmpty()) {
            fileName = "welcome.txt";
        }

        if (fileName.contains("..") || fileName.equalsIgnoreCase("secret.txt")) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
            response.getWriter().println("Access Denied: Restricted File.");
            return;
        }
        File requestedFile = new File("/var/www/uploads/", fileName);

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("<h3>File Requested: " + requestedFile.getName() + "</h3>");
        response.getWriter().println("<p>File payload loaded successfully.</p>");
    }
}
