package org.owasp.benchmark.testcode;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/cwe-651/BenchmarkTest02768")
public class BenchmarkTest02768 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/xml;charset=UTF-8");

        response.getWriter().println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        response.getWriter()
                .println(
                        "<definitions xmlns=\"http://schemas.xmlsoap.org/wsdl/\" name=\"BenchmarkService\">");
        response.getWriter().println("<!-- Public service -->");
        response.getWriter().println("</definitions>");
    }
}
