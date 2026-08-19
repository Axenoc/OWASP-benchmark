package org.owasp.benchmark.testcode;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebServlet("/cwe-302/BenchmarkTest90024")
public class BenchmarkTest90024 extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
        doPost(request,response);
    }
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException{
        String authHeader = request.getHeader("Authorization");
        Cookie[] cookies = request.getCookies();
        if (authHeader == null || !authHeader.startsWith("Basic ")&&authenticateCookie(cookies) ) {
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
            Cookie cookie = new Cookie("authenticated","1");
            Cookie cookie2 = new Cookie("username","admin");
            response.addCookie(cookie);
            response.addCookie(cookie2);
            PrintWriter out = response.getWriter();
            out.println("<!DOCTYPE html><html><body>");
            out.println("<h1>Authenticated Successfully</h1>");
            out.println("<p>Welcome, " + username + "!</p>");
            out.println("</body></html>");

    }
    else if(authenticateCookie(cookies)){
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
  private boolean authenticateCookie(Cookie[] cookie){
    String authenticated = null;
    String username = null;
    if (cookie != null){
        for (Cookie cookies : cookie) {
            if ("authenticated".equals(cookies.getName())) {
                authenticated = cookies.getValue(); 
            } else if ("username".equals(cookies.getName())) {
                username = cookies.getValue();     
            }
        }
    }
    if (authenticated.equals("1")&&username.equals("admin")){
        return true;
    }
    return false;
  }

}
