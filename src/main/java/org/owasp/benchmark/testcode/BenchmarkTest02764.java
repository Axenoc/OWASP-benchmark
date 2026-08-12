package org.owasp.benchmark.testcode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/cwe-538/BenchmarkTest02764")
public class BenchmarkTest02764 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain;charset=UTF-8");

        Path file = Files.createTempFile("benchmark-02764-", ".txt");

        try {
            Files.write(file, "BENCHMARK_SECRET_02764".getBytes(StandardCharsets.UTF_8));
            response.getWriter().println("File saved");
        } finally {
            Files.deleteIfExists(file);
        }
    }
}
