package org.owasp.benchmark.testcode;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/infoleak-00/BenchmarkTest02755")
public class BenchmarkTest02755 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain;charset=UTF-8");

        String error = "Could not open /WEB-INF/BENCHMARK_SECRET_02755.jsp";

        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, error);
    }
}
