package org.owasp.benchmark.testcode;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/infoleak-00/BenchmarkTest02759")
public class BenchmarkTest02759 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain;charset=UTF-8");

        response.getWriter().println("hostname=benchmark-internal-host");
        response.getWriter().println("internalIp=10.10.10.10");
        response.getWriter().println("installPath=/opt/benchmark/internal");
        response.getWriter().println("apiKey=BENCHMARK_FAKE_KEY_02759");
    }
}
