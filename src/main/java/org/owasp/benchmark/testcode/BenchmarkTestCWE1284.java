package org.owasp.benchmark.testcode;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Có thể dùng cho CWE-606 luôn
@WebServlet(value = "/cwe-1284/BenchmarkTestCWE1284")
public class BenchmarkTestCWE1284 extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        Integer count = Integer.parseInt(request.getParameter("BenchmarkTestCWE1284"));
        StringBuilder output = new StringBuilder();
        output.append("<h3>Processing").append(count).append("items</h3><br>");
        for (int i = 0; i < count; i++) {
            output.append("Item #").append(i).append("</br>");
        }
        response.getWriter().println(output.toString());
    }
}
