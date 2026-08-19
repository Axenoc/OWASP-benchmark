package org.owasp.benchmark.testcode;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Cookie;

import org.springframework.web.client.HttpClientErrorException.Unauthorized;

@WebServlet("/cwe-603/BenchmarkTest90026")
public class BenchmarkTest90026 extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
            response.setStatus(HttpServletResponse.SC_OK); 
            response.setContentType("text/html;charset=UTF-8");
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("Missing or invalid Authorization header.");
            return;
        }
        String base64Credentials = authHeader.substring("Basic ".length()).trim();
        byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(decodedBytes, "UTF-8");
        if (credentials.equals("1480707")){
            PrintWriter out = response.getWriter();
            out.println("<!DOCTYPE html><html><body>");
            out.println("<h1>Authenticated Successfully</h1>");
            out.println("<p>Welcome, " + "admin" + "!</p>");
            out.println("</body></html>");
        }
        else{
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
