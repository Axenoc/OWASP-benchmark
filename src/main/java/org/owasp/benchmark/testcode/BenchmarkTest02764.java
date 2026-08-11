package org.owasp.benchmark.testcode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/infoleak-00/BenchmarkTest02764")
public class BenchmarkTest02764 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain;charset=UTF-8");

        File file = File.createTempFile("benchmark-02764-", ".txt");
        file.deleteOnExit();

        FileWriter writer = new FileWriter(file);
        writer.write("BENCHMARK_SECRET_02764");
        writer.close();

        response.getWriter().println("File saved");

        file.delete();
    }
}
