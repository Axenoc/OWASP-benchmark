package org.owasp.benchmark.testcode;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/infoleak-00/BenchmarkTest02767")
public class BenchmarkTest02767 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/xml;charset=UTF-8");

        response.getWriter().println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        response.getWriter()
                .println(
                        "<definitions xmlns=\"http://schemas.xmlsoap.org/wsdl/\" name=\"BenchmarkService\">");
        response.getWriter()
                .println("<!-- Internal endpoint: http://benchmark-internal/service -->");
        response.getWriter().println("<!-- API key: BENCHMARK_FAKE_SECRET_02767 -->");
        response.getWriter().println("</definitions>");
    }
}
