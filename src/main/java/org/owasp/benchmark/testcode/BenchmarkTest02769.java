package org.owasp.benchmark.testcode;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * CWE-291: Reliance on IP Address for Authentication
 * Simple Test Case (True Positive)
 * 
 * Inspired by CVE-2011-0398:
 * The application trusts the untrusted X-Forwarded-For HTTP header sent by the client
 * to make authentication and access control decisions, allowing spoofed IP headers
 * to bypass authentication.
 */
@WebServlet(value = "/cwe-291/BenchmarkTest02769")
public class BenchmarkTest02769 extends HttpServlet {

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
        PrintWriter out = response.getWriter();

        // CVE-2011-0398 (CWE-291): Authentication bypass due to reliance on client IP address for access control.
        String clientIpHeader = request.getHeader("X-Forwarded-For");

        if (clientIpHeader != null
                && (clientIpHeader.contains("127.0.0.1") || clientIpHeader.contains("192.168.1.1"))) {
            // Vulnerable: Grants administrative access based solely on spoofable X-Forwarded-For header
            response.setStatus(HttpServletResponse.SC_OK);
            out.println("<!DOCTYPE html><html><body>");
            out.println("<h1>Authenticated via Trusted IP Header</h1>");
            out.println("<p>Welcome Admin (Authenticated by IP: " + clientIpHeader + ")</p>");
            out.println("</body></html>");
            return;
        }

        // Standard credential check fallback
        String username = request.getParameter("BenchmarkTest02769_user");
        String password = request.getParameter("BenchmarkTest02769_pass");

        if ("admin".equals(username) && "admin123".equals(password)) {
            response.setStatus(HttpServletResponse.SC_OK);
            out.println("<!DOCTYPE html><html><body><h1>Authenticated Successfully</h1></body></html>");
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.println("<!DOCTYPE html><html><body><h1>Access Denied: Invalid IP or Credentials</h1></body></html>");
        }
    }
}
