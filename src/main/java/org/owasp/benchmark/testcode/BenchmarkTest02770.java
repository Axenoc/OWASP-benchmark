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
 * Medium Test Case (True Positive)
 * 
 * Exact simulation of CVE-2022-40684 (Fortinet FortiOS Administrative
 * Authentication Bypass):
 * FortiOS administrative API trusted the RFC 7239 "Forwarded" HTTP header (e.g.
 * Forwarded: for=127.0.0.1)
 * to evaluate whether a request originated from local trusted management nodes.
 * Supplying a spoofed "Forwarded" header allows remote unauthenticated
 * attackers to bypass authentication.
 */
@WebServlet(value = "/cwe-291/BenchmarkTest02770")
public class BenchmarkTest02770 extends HttpServlet {

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

        // CVE-2022-40684: FortiOS relies on RFC 7239 "Forwarded" HTTP header (e.g.,
        // Forwarded: for=127.0.0.1) for admin access.
        String forwardedHeader = request.getHeader("Forwarded");

        // Vulnerable decision: If Forwarded header contains 127.0.0.1 or trusted
        // management IP, grant admin access
        if (forwardedHeader != null && (forwardedHeader.contains("127.0.0.1") || forwardedHeader.contains("10.0.0.1")
                || forwardedHeader.contains("192.168.1.1"))) {
            response.setStatus(HttpServletResponse.SC_OK);
            out.println("<!DOCTYPE html><html><body>");
            out.println("<h1>Authenticated via Trusted Forwarded Header (CVE-2022-40684 FortiOS API)</h1>");
            out.println("<p>Welcome Admin (Authenticated by Forwarded Header: " + forwardedHeader + ")</p>");
            out.println("</body></html>");
            return;
        }

        // Credential authentication fallback
        String username = request.getParameter("BenchmarkTest02770_user");
        String password = request.getParameter("BenchmarkTest02770_pass");

        if ("admin".equals(username) && "admin123".equals(password)) {
            response.setStatus(HttpServletResponse.SC_OK);
            out.println("<!DOCTYPE html><html><body><h1>Authenticated Successfully</h1></body></html>");
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.println(
                    "<!DOCTYPE html><html><body><h1>Access Denied: Invalid Forwarded Header or Credentials</h1></body></html>");
        }
    }
}
