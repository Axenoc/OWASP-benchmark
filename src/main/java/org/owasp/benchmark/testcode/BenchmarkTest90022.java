package org.owasp.benchmark.testcode;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.HashSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/cwe-291/BenchmarkTest90022")
public class BenchmarkTest90022 extends HttpServlet {
    HashSet<String> set = new HashSet<>();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
        doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
        String[] values = credentials.split(":", 2);

        String username = values[0];
        String password = values.length > 1 ? values[1] : "";

        if ("admin".equals(username) && "password123".equals(password)) {
            response.setStatus(HttpServletResponse.SC_OK); 
            response.setContentType("text/html;charset=UTF-8");
            set.add(request.getRemoteAddr());
            PrintWriter out = response.getWriter();
            out.println("<!DOCTYPE html><html><body>");
            out.println("<h1>Authenticated Successfully</h1>");
            out.println("<p>Welcome, " + username + "!</p>");
            out.println("</body></html>");
        }
        else if (IpAdress.authorizeIpAdress(set,request.getRemoteAddr() )){
                        response.setStatus(HttpServletResponse.SC_OK); 
            response.setContentType("text/html;charset=UTF-8");
            
            PrintWriter out = response.getWriter();
            out.println("<!DOCTYPE html><html><body>");
            out.println("<h1>Authenticated Successfully</h1>");
            out.println("<p>Welcome, " + username + "!</p>");
            out.println("</body></html>");
        }
        else{
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); 
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<p>Invalid username or password.</p>");
        } 

    }
}
class IpAdress{
    public static boolean authorizeIpAdress(HashSet<String> set, String ip){
        if (set.contains(ip)){
            return true;
        }
        return false;
    }

}