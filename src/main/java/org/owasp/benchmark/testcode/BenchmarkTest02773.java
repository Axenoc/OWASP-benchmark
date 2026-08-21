package org.owasp.benchmark.testcode;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * CWE-291: Reliance on IP Address for Authentication
 * Complex Test Case (False Positive - Safe)
 * 
 * Defense-in-depth implementation:
 * 1. Uses socket remote address (request.getRemoteAddr()), ignoring all
 * untrusted HTTP headers.
 * 2. Performs Forward-Confirmed Reverse DNS (FCRDNS) verification (reverse PTR
 * lookup matched with forward A lookup).
 * 3. MANDATORY credential verification: IP and hostname checks serve only as
 * network access restriction (firewall layer).
 * Valid username and password credentials are ALWAYS strictly required to
 * obtain access.
 */
@WebServlet(value = "/cwe-291/BenchmarkTest02773")
public class BenchmarkTest02773 extends HttpServlet {

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

        // 1. Direct TCP socket remote IP (ignores spoofable client HTTP headers)
        String directSocketIp = request.getRemoteAddr();

        // 2. Network level restriction check with FCRDNS
        boolean networkAccessAllowed = verifyNetworkAndFcrdns(directSocketIp);

        if (!networkAccessAllowed) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            out.println("<!DOCTYPE html><html><body><h1>Access Denied: Network Policy Restriction</h1></body></html>");
            return;
        }

        // 3. SAFE: Credential authentication is ALWAYS mandatory regardless of IP check
        // result
        String username = request.getParameter("BenchmarkTest02773_user");
        String password = request.getParameter("BenchmarkTest02773_pass");

        if ("admin".equals(username) && "ComplexSecurePass2026!".equals(password)) {
            response.setStatus(HttpServletResponse.SC_OK);
            out.println("<!DOCTYPE html><html><body>");
            out.println("<h1>Authenticated Successfully</h1>");
            out.println("<p>Welcome " + username + " (Network Verified: " + directSocketIp + ")</p>");
            out.println("</body></html>");
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.println("<!DOCTYPE html><html><body><h1>Access Denied: Invalid Credentials</h1></body></html>");
        }
    }

    /**
     * Performs Forward-Confirmed Reverse DNS (FCRDNS) verification.
     * Verifies that reverse DNS lookup IP -> Hostname matches forward DNS lookup
     * Hostname -> IP.
     */
    private boolean verifyNetworkAndFcrdns(String ip) {
        if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip) || "localhost".equals(ip)) {
            return true;
        }
        try {
            InetAddress addrByIp = InetAddress.getByName(ip);
            String hostName = addrByIp.getHostName(); // PTR lookup
            if (hostName == null || hostName.trim().isEmpty() || hostName.equals(ip)) {
                return false;
            }

            // Forward DNS lookup to confirm (FCRDNS)
            InetAddress[] forwardAddrs = InetAddress.getAllByName(hostName);
            for (InetAddress fwd : forwardAddrs) {
                if (fwd.getHostAddress().equals(ip)) {
                    return true;
                }
            }
        } catch (Exception e) {
            // Fail safe on DNS error
            return false;
        }
        return false;
    }
}
