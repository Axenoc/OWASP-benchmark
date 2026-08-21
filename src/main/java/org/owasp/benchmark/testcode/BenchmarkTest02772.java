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
 * Simple Test Case (False Positive - Safe)
 * 
 * The application logs the client's remote IP address for audit purposes only.
 * Authentication relies strictly on valid credentials (Username/Password), and
 * the IP address
 * is NEVER used to grant access or bypass credential verification.
 */
@WebServlet(value = "/cwe-291/BenchmarkTest02772")
public class BenchmarkTest02772 extends HttpServlet {

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

        // Passive IP retrieval for security audit logging ONLY
        String clientIp = request.getRemoteAddr();
        getServletContext().log("Login attempt from IP: " + clientIp);

        String username = request.getParameter("BenchmarkTest02772_user");
        String password = request.getParameter("BenchmarkTest02772_pass");

        // Safe: Authentication relies strictly on valid credentials. IP address is not
        // used for auth decision.
        if ("admin".equals(username) && "securePassword123!".equals(password)) {
            response.setStatus(HttpServletResponse.SC_OK);
            out.println("<!DOCTYPE html><html><body>");
            out.println("<h1>Authenticated Successfully</h1>");
            out.println("<p>Logged in user: " + username + " (Audit IP: " + clientIp + ")</p>");
            out.println("</body></html>");
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.println("<!DOCTYPE html><html><body><h1>Access Denied: Invalid Credentials</h1></body></html>");
        }
    }
}
