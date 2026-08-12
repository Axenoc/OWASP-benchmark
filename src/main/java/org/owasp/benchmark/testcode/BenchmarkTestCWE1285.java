package org.owasp.benchmark.testcode;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Có thể dùng cho 129
@WebServlet(value = "/cwe-1285/BenchmarkTestCWE1285")
public class BenchmarkTestCWE1285 extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String[] USER_ROLES = {"GUEST", "USER", "MODERATOR", "ADMIN"};

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        String indexStr = req.getParameter("BenchmarkTestCWE1285");
        int index = 0;
        try {
            if (indexStr != null) {
                index = Integer.parseInt(indexStr);
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            resp.getWriter().println("Error: " + e.toString());
        }
        StringBuilder output = new StringBuilder();
        try {
            output.append("<h3> Role: ").append(USER_ROLES[index]).append("</h3>");
            resp.getWriter().println(output.toString());
        } catch (ArrayIndexOutOfBoundsException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println("Error: " + e.toString());
        }
    }
}
