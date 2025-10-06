package com.pts.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/test")
public class TestServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		out.println("<html>");
		out.println("<head><title>Test Servlet</title></head>");
		out.println("<body>");
		out.println("<h1>Servlet Works!</h1>");
		out.println("<p>Application is deployed correctly.</p>");
		out.println("<p>Context Path: " + request.getContextPath() + "</p>");
		out.println("<p><a href='" + request.getContextPath() + "/'>Go to Home</a></p>");
		out.println("</body>");
		out.println("</html>");
	}
}