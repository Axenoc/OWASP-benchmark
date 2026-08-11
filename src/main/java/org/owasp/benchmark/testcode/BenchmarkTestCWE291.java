package org.owasp.benchmark.testcode;

import java.io.IOException;
import java.net.InetAddress;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/auth-00/BenchmarkTestCWE291")
public class BenchmarkTestCWE291 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String clientIp = request.getParameter("BenchmarkTestCWE291");
        
        if (clientIp == null || clientIp.trim().isEmpty()) {
            clientIp = request.getRemoteAddr();
        }

        
        InetAddress addr = InetAddress.getByName(clientIp);
        String hostName = addr.getHostName(); 
        if (hostName != null && (hostName.endsWith("my.company.com") || hostName.equals("localhost"))) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("<h3>Access Granted</h3>");
            response.getWriter().println("<p>Welcome trusted host: " + hostName + "</p>");
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); 
            response.getWriter().println("<h3>Access Denied</h3>");
            response.getWriter().println("<p>Untrusted domain: " + hostName + "</p>");
        }
    }
}