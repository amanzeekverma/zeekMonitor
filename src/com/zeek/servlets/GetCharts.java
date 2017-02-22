package com.zeek.servlets;
// Import required java libraries
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

// Extend HttpServlet class
public class GetCharts extends HttpServlet {
 
  private String message;

  public void init() throws ServletException
  {
      // Do required initialization
  }

  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
            throws ServletException, IOException
  {
      response.setContentType("text/html");

  PrintWriter out = response.getWriter();

  out.print("<HTML>");
  out.print("<HEAD><TITLE>Upload Image</TITLE></HEAD>");
  out.print("<BODY>");

  out.print("<img src='/home/nbmuser1/workspace/aman/ZeeKMonitor/out/test.png' alt='image' />");

  out.print("</BODY>");
  out.print("</HTML>");
  out.close();

  }
  
  public void destroy()
  {
      // do nothing.
  }
}
