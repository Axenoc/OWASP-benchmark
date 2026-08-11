package org.owasp.benchmark.testcode;

import java.io.IOException;
import java.io.StringReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.InputSource;

@WebServlet(value = "/inputval-00/BenchmarkTestCWE1286")
public class BenchmarkTestCWE1286 extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String xmlPayload = request.getParameter("BenchmarkTestCWE1286");
        if (xmlPayload == null) xmlPayload = "";

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            builder.parse(new InputSource(new StringReader(xmlPayload)));

            response.getWriter().println("<p>XML parsed successfully!</p>");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("<p>Syntax Error: " + e.toString() + "</p>");
        }
    }
}
