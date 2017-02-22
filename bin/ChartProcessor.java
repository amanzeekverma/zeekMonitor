import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.SymbolicAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.general.SeriesException;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class ChartProcessor {
	static HashMap<String, String> properties = new HashMap<String, String>(16);
	static int _totalCharts = 0;
	//First argument is chart.properties.
	public static void main(String a[]) throws Exception{
		DataInputStream dis = new DataInputStream(new FileInputStream(new File(a[0])));
		//loading all properties
		while (dis.available()>0){
			String line = dis.readLine();
			if (line.startsWith("#"))
				continue;
			String[] propToken = line.split("=");
			properties.put(propToken[0], propToken[1]);
		}
		_totalCharts = Integer.parseInt(properties.get("chart.count"));
		for (int _i=0; _i<_totalCharts; _i++){
			final int i = _i;
			new Thread("ChartProcessorThread-"+i){
				public void run(){
					String title = properties.get("chart."+i+".name");
					String[] _columns = properties.get("chart."+i+".columns").split(",");
					String outPNG = properties.get("chart."+i+".png"); 
					String inData = properties.get("chart."+i+".rawdata")+"_xtrct";
					int refreshTime = Integer.parseInt(properties.get("chart."+i+".refresh"));
					 DataInputStream dis = null;
					 DataOutputStream outStream = null;
						try {
							 //dis = new DataInputStream (new FileInputStream (new File(inData)));
							 //outStream = new DataOutputStream(new FileOutputStream(new File(outPNG)));
						}catch(Exception fex){
							System.out.println("File Exceptions.. please check input/output file or location");
							fex.printStackTrace();
						}
					while (true){
						try {
                                                 try{dis.close();}catch(Exception e){}//TP Line
                                                 dis = new DataInputStream (new FileInputStream (new File(inData)));
                                                 outStream = new DataOutputStream(new FileOutputStream(new File(outPNG)));
						new ChartGenerator(title, _columns, dis, outStream).createChart();
						}catch(Exception ex){
							ex.printStackTrace();
						}
					//createChart(title, _columns, dis, outStream);
						try {
							Thread.sleep(refreshTime * 1000);
						}catch (Exception ex) {}	// loled out!
					}
				}

								
				
			}.start();
		}
		
	}
}
class ChartGenerator{
	private String[] xArrayItems = null;
	private String xLabel = "X Axis";
	private double maxMean = 0;
	private String title = "";
	private String[] columns = null;
	private DataInputStream dis = null;
	private DataOutputStream out = null;
	
	public ChartGenerator(String title, String[] columns, DataInputStream dis, DataOutputStream outStream) {
		this.title = title;
		this.columns = columns;
		this.dis = dis;
		this.out = outStream;
	}
	public void createChart() throws Exception {
		XYDataset dataset = createDataset(dis);
	    JFreeChart chart = createChart(dataset, title);
	    ChartUtilities.writeChartAsPNG(out, chart, 600, 300);
	    out.close();
	}

	private JFreeChart createChart(XYDataset dataset, String title) 
	{    

	    JFreeChart chart = ChartFactory.createXYLineChart(title,          // chart title
	        "Value", 
	        "Activation", 
	        dataset,                // data
	        PlotOrientation.HORIZONTAL, 
	        true,                   // include legend
	        true,
	        false);
	    
	   


	    XYPlot plot = (XYPlot) chart.getPlot();
	    //plot.setForegroundAlpha(0.5f);

	    SymbolicAxis rangeAxis = new SymbolicAxis(xLabel, xArrayItems);
	    int tick = 1;
	    int len = xArrayItems.length ;
	    while (len > 100){
	    	tick+=5;
	    	len-=100;
	    }
	    rangeAxis.setTickUnit(new NumberTickUnit(tick));
            //System.out.println("xArrayItem size = "+xArrayItems.length);
	    rangeAxis.setRange(0,xArrayItems.length-1);
	    rangeAxis.setVerticalTickLabels(true);
	    plot.setRangeAxis(rangeAxis);
	    plot.getDomainAxis().setMaximumAxisValue(maxMean*2);
            plot.getDomainAxis().setMinimumAxisValue(0);

	    return chart;
	}
	private XYDataset createDataset(DataInputStream dis) {

		try {
		String[] headers = this.columns;
		xLabel = headers[0];
		int totalSeries =headers.length - 1;
		
		DefaultTableXYDataset result = new DefaultTableXYDataset();
		XYSeriesCollection collection = new XYSeriesCollection();
		HashMap<XYSeries, List<Double>> listOfDomainsPerSeries = new HashMap<XYSeries, List<Double>>();
	    List<String> xPoints = new ArrayList<String>();
	    int[] _i = new int[totalSeries];
	    
		for (int _x=0; _x<totalSeries; _x++){
			XYSeries series = new XYSeries(headers[_x+1],false, false);
			collection.addSeries(series);
		}
	  //  XYSeries series1 = new XYSeries(headers[1],false, false);
		
	    
	    
	    for (int c =0 ; c<totalSeries; c++)
	    	_i[c]=0;
	    
	    while(dis.available() > 0){
	    	String[] line = dis.readLine().split(",");
	    	xPoints.add(line[0]);
	    	for (int x=0; x<totalSeries; x++){
	    		//System.out.println("adding in series "+(x+1));
	    		XYSeries _series = collection.getSeries(x);
	    		
	    		double val = Double.parseDouble(line[x+1]);
	    		
	    		List<Double> domainList = listOfDomainsPerSeries.get(_series);
	    		if (domainList == null){
	    			domainList = new ArrayList<Double>();
	    			listOfDomainsPerSeries.put(_series, domainList);
	    		}
	    		domainList.add(val);
	    		
	    		//System.out.println("adding : "+line[x+1]+"  "+_i[x]);
	    		while (true){
	    			try {
	    					_series.add(val, _i[x]);
	    					listOfDomainsPerSeries.put(_series, domainList);
	    					break;
	    			}catch(SeriesException e){
	    				val+=.00001;
	    		}
	    	}
	    	_i[x]++;
	      }
	    }
	    
	    List<Double> means = new ArrayList<Double>();
	    for (XYSeries s : listOfDomainsPerSeries.keySet()){
	    	List<Double> list = listOfDomainsPerSeries.get(s);
	    	Collections.sort(list);
	    	int _95thIndex = (list.size()*95/100);;
	    	double sum=0;
	    	for (int index=0; index < _95thIndex; index++){
	    		Double d = list.get(index);
	    		sum+=d;
	    	}
	    	//System.out.println("sum = "+sum);
	    	 double mean = sum/(list.size()*0.95);
	    	 means.add(mean);
	    }
    	for (double _mean : means)
    		if (_mean > maxMean)
    			maxMean = _mean;
    	
    	
	     xArrayItems = xPoints.toArray(new String[0]);
	     //System.out.println("--> X Array items "+xPoints); 
	     for (int x=0; x<totalSeries; x++){
	    	 result.addSeries(collection.getSeries(x));
	     }
	    
	    return result;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	
	}

	
}
