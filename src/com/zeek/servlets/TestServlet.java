package com.zeek.servlets;

import java.io.IOException; // Required Servlet Imports
import java.io.OutputStream;
import javax.servlet.*;
import javax.servlet.http.*;
import java.awt.Color; // Imports related to chart production
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

public class TestServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		    doTestPieChart(request, response); // Will produce chart
	} // End Method
	
	
	protected void doTestPieChart(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
			OutputStream out = response.getOutputStream();
			try {
			DefaultPieDataset dataset = new DefaultPieDataset();
			dataset.setValue("Graphic 1", 192);
			dataset.setValue("Graphic 2", 123);
			dataset.setValue("Graphic 3", 20);
			
			JFreeChart chart = ChartFactory.createPieChart("Check pie",
			dataset, true, false, false);
			chart.setBackgroundPaint(Color.white); // Customize chart
                        System.out.println("I am creating chart");
			response.setContentType("image/png");
			ChartUtilities.writeChartAsPNG(out, chart, 400, 300);
			}
			catch (Exception e) {
			System.out.println(e.toString());
			}
			finally {
			out.close();
			}
			} // End Method
	
	
	
} // End Class
