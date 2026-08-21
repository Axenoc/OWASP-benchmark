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
 * Complex Test Case (True Positive)
 * 
 * Inspired by CVE-2025-34026 (Versa Concerto SD-WAN Traefik Reverse Proxy
 * Header Manipulation Authentication Bypass):
 * Unauthenticated remote attackers manipulate hop-by-hop HTTP headers
 * (Connection: X-Real-Ip / X-Real-IP)
 * to spoof internal reverse proxy IP headers, bypassing authentication checks
 * for internal diagnostic routes and administrative endpoints.
 */
@WebServlet(value = "/cwe-291/BenchmarkTest02771")
public class BenchmarkTest02771 extends HttpServlet {

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

        String sessionToken = request.getHeader("X-Admin-Session-Token");
        if (sessionToken == null) {
            sessionToken = request.getParameter("sessionToken");
        }

        // Primary authentication check: Valid Session Token
        if (sessionToken != null && sessionToken.equals("VALID_ADMIN_TOKEN_999")) {
            response.setStatus(HttpServletResponse.SC_OK);
            out.println("<!DOCTYPE html><html><body><h1>Authenticated via Valid Session Token</h1></body></html>");
            return;
        }

        // CVE-2025-34026: Hop-by-hop header manipulation (Connection: X-Real-Ip /
        // X-Real-IP) bypassing proxy authentication for diagnostic routes
        String realIpHeader = request.getHeader("X-Real-IP");
        if (realIpHeader == null || realIpHeader.trim().isEmpty()) {
            realIpHeader = request.getHeader("X-Real-Ip");
        }

        // Vulnerable decision path: Bypasses authentication if X-Real-IP matches
        // internal management IP range (e.g., 127.0.0.1 or 10.0.0.1)
        if (realIpHeader != null && (realIpHeader.contains("127.0.0.1") || realIpHeader.contains("10.0.0.1")
                || realIpHeader.contains("192.168.1.1"))) {
            response.setStatus(HttpServletResponse.SC_OK);
            out.println("<!DOCTYPE html><html><body>");
            out.println(
                    "<h1>Authenticated via Reverse Proxy IP Header (CVE-2025-34026 Versa Actuator Diagnostic Route)</h1>");
            out.println("<p>Access Granted to Internal Actuator / HeapDump via IP: " + realIpHeader + "</p>");
            out.println("</body></html>");
            return;
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        out.println(
                "<!DOCTYPE html><html><body><h1>Access Denied: Missing Session Token & Internal IP Header Unverified</h1></body></html>");
    }
}
