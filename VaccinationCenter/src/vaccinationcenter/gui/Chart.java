/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vaccinationcenter.gui;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;
import javax.swing.JPanel;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;

/**
 *
 * @author davidpavlicko
 */
public class Chart {
    
    private DrawingThread drawingThread;
    private final XYChart chart;
    private final JPanel pnlPlot;    
    
    private final IntSupplier xValue;
    private final DoubleSupplier yValue;
   
    public Chart(String title, String xTitle, String yTitle, IntSupplier xValue, DoubleSupplier yValue) {
        
        this.xValue = xValue;
        this.yValue = yValue;
        
        this.chart = QuickChart.getChart(title, xTitle, yTitle, "datapoints", new double[] { 0 }, new double[] { 0 });                
        this.chart.getStyler().setLegendVisible(false);        
        this.chart.getStyler().setXAxisTicksVisible(true);
        this.chart.getStyler().setXAxisDecimalPattern("### ### ### ### ###");
        this.chart.getStyler().setYAxisDecimalPattern("0.00");

        this.pnlPlot = new XChartPanel(chart);                        
    }        
    
    public JPanel getPlot() {                
        return this.pnlPlot;
    }
    
    public void startThreads() {
        this.drawingThread = new DrawingThread();
        this.drawingThread.start();        
    }
    
    public void stopThreads() {        
        this.drawingThread.cancel();
        try {            
            this.drawingThread.join();                        
        } catch (InterruptedException ex) { throw new RuntimeException(ex); }
        this.drawingThread = null;
    }
    
    public void afterReplication() {
        this.drawingThread.newData();        
    }
    
    private class DrawingThread extends Thread {
        
        private final ArrayList<Integer> xAxis;
        private final ArrayList<Double> yAxis;
        private final AtomicBoolean isCancelled = new AtomicBoolean(false);
        
        public DrawingThread() {
            
            this.xAxis = new ArrayList();
            this.yAxis = new ArrayList();            
        }
        
        public void cancel() {
            this.isCancelled.set(true);
            
            synchronized (xAxis) {
               synchronized (yAxis) {
                    chart.updateXYSeries("datapoints", xAxis, yAxis, null);  
                }
            }                                                      
            pnlPlot.repaint();   
        }
        
        public void newData() {
            synchronized (xAxis) {
                synchronized (yAxis) { 
                    this.xAxis.add(xValue.getAsInt());    
                    this.yAxis.add(yValue.getAsDouble()); 
                }
            }
        }
        
        public void run() {
            while (!isCancelled.get()) {  
                
                synchronized (xAxis) {
                    synchronized (yAxis) {
                        chart.updateXYSeries("datapoints", xAxis, yAxis, null);  
                    }
                }                                                      
                pnlPlot.repaint();                

                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                }
            }
        }            
    }
    
}
