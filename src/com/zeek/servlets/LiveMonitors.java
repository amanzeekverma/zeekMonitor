package com.zeek.servlets;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LiveMonitors extends HttpServlet {
	static HashMap<String, String> _properties = new HashMap<String, String>();
	private static int _totalGroups = 0;
	private static String _title = "TITLE";
	//Initialization
	static {
		try {
		DataInputStream dis = new DataInputStream(LiveMonitors.class.getResourceAsStream("/config/chartdisplay.properties"));
		while (dis.available()>0){
			String line = dis.readLine();
			if (!line.contains("=") || line.startsWith("#"))
				continue;
			String[] tokens = line.split("=");
			_properties.put(tokens[0], tokens[1]);
                        System.out.println("Adding property "+tokens[0]+" = "+tokens[1]);
		}
		}catch(Exception e){
			System.out.println("UNABLE TO LOAD LIVE MONITORS, PLEASE CHECK PROPERTIES");
			e.printStackTrace();
			System.exit(1);
		}
		
		_totalGroups = Integer.parseInt(_properties.get("TOTAL.GROUPS"));
		_title = _properties.get("HTML.TITLE");
		
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
                 //DYNATRACE HACK.. FOR DEMO
                 String mode = request.getParameter("mode");
                 if (mode != null && !mode.isEmpty()){
                         String randomString =  returnMeRandomString(mode);
                 }
		 //HACK ENDS
		 response.setContentType("text/html");
		   int _groupNumber = 0;
		   String group = request.getParameter("group");
		   for (int i=0; i<_totalGroups; i++){
			   if (_properties.get("GROUP."+(i+1)+".NAME").equalsIgnoreCase(group)){
				   _groupNumber = (i+1);
				   break;
			   }
		   }
		   String groupPreFix = "GROUP."+_groupNumber;
		   System.out.println("parsing "+groupPreFix+".ROWS"); 
		   int _totalRows = Integer.parseInt(_properties.get(groupPreFix+".ROWS"));
		   
		     PrintWriter out = response.getWriter();
		     out.println("<html>");
		     out.println("<head>");
		     out.println("<title>"+_title+"</title>");
		     out.println("<STYLE TYPE=\"text/css\">BODY{background:BLACK}</STYLE><meta http-equiv='refresh' content='30'/>");
		     out.println("</head>");
		     out.println("<body>");
		     out.println(" <table border=\"2\" width=\"600\" callpadding=\"6\" cellspacing=\"0\">");
			  	for (int row=1; row<=_totalRows; row++){
			  		out.println("<tr><td>");
			  		out.println(" <img src=\""+_properties.get(groupPreFix+".L"+row+".IMG")+"\">");
			  		out.println("</td>");
			  		out.println("<td>");
			  		out.println(" <img src=\""+_properties.get(groupPreFix+".R"+row+".IMG")+"\">");
			  		out.println("</td></tr>");
			  	}
		     out.println("</table>");
		     out.println("</body>");
		     out.println("</html>");

		   
		   
	}

        private String returnMeRandomString(String mode){
              String _mode = mode;
	      Random rand = new Random();
    	      int randomNum = rand.nextInt(100);
              return _mode+randomNum+"_zeekTest";
             
        }

}

