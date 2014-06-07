/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smizsk;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.Second;
import org.snmp4j.smi.OID;
import static smizsk.Smizsk.readXMLconf;
import static smizsk.Smizsk.computerList;

/**
 *
 * @author madaooo
 */
public class MainTabSmizsk extends javax.swing.JPanel {

    JLabel filler;
    JPanel statiscic;
    static JPanel chartBandwith;
    static JPanel pane;
    TitledBorder title;
    Border blackline;
    JButton timeButton;
    JLabel bandwithInformation;
    static JLabel actionLabel[] = new JLabel[6];
    DynamicTimeSeriesCollection bandwithNetwork = null;
    static int max = 0, min = 0;
    int delayTime = 10000;
    mSnmp client;
    int tickTime = 1;
    HashMap<Integer, String> sendRecivePackets = new HashMap<>();
    String ComputerBandwith = "";
    String maxInterfece = "";
    ActualizeParameters acParam = new ActualizeParameters();
    Timer timer;

    ChartPanel chartPanel = null;

    /**
     * Creates new form Main_tab_smizsk
     */
    public MainTabSmizsk() {
        initComponents();
    }

    /**
     * Method have
     *
     */
    public final void initComponents() {

        blackline = BorderFactory.createLineBorder(Color.black);

        pane = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 760;
        c.ipady = 140;
        statiscic = new JPanel(new GridBagLayout());
        statiscic.setSize(new Dimension(800, 140));
        title = BorderFactory.createTitledBorder(blackline, "Statistic");
        title.setTitleJustification(TitledBorder.RIGHT);
        statiscic.setBorder(title);
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 10;
        c.ipady = 12;
        //add some elements in statistic
        // compurers in network
        // bandwith
        // computers actives 
        // computers turn off 
        bandwithInformation = new JLabel("PC in network:");

        statiscic.add(bandwithInformation, c);
        bandwithInformation = new JLabel("Maximum Bandwidth:");
        c.gridx = 0;
        c.gridy = 1;
        c.ipadx = 10;
        c.ipady = 12;
        statiscic.add(bandwithInformation, c);
        bandwithInformation = new JLabel("Minimum Bandwidth:");
        c.gridx = 0;
        c.gridy = 2;
        c.ipadx = 10;
        c.ipady = 12;
        statiscic.add(bandwithInformation, c);
        bandwithInformation = new JLabel("Average Bandwidth:");
        c.gridx = 0;
        c.gridy = 3;
        c.ipadx = 10;
        c.ipady = 12;
        statiscic.add(bandwithInformation, c);

        bandwithInformation = new JLabel("Active PC:");
        c.gridx = 0;
        c.gridy = 4;
        c.ipadx = 10;
        c.ipady = 12;
        statiscic.add(bandwithInformation, c);
        bandwithInformation = new JLabel("Deactive PC:");
        c.gridx = 0;
        c.gridy = 5;
        c.ipadx = 10;
        c.ipady = 12;
        statiscic.add(bandwithInformation, c);
        // active labels
        //readXMLconf.
        actionLabel[0] = new JLabel(readXMLconf.sumComputer().toString());
        c.gridx = 1;
        c.gridy = 0;
        c.ipadx = 10;
        c.ipady = 12;
        statiscic.add(actionLabel[0], c);
        actionLabel[1] = new JLabel("0 kb/s");
        c.gridx = 1;
        c.gridy = 1;
        c.ipadx = 10;
        c.ipady = 12;
        statiscic.add(actionLabel[1], c);
        actionLabel[2] = new JLabel("0 kb/s");
        c.gridx = 1;
        c.gridy = 2;
        c.ipadx = 10;
        c.ipady = 12;
        statiscic.add(actionLabel[2], c);
        actionLabel[3] = new JLabel("0 kb/s");
        c.gridx = 1;
        c.gridy = 3;
        c.ipadx = 10;
        c.ipady = 12;
        statiscic.add(actionLabel[3], c);
        actionLabel[4] = new JLabel(readXMLconf.sumComputer().toString());
        c.gridx = 1;
        c.gridy = 4;
        c.ipadx = 10;
        c.ipady = 12;
        statiscic.add(actionLabel[4], c);
        actionLabel[5] = new JLabel("N/A");
        c.gridx = 1;
        c.gridy = 5;
        c.ipadx = 10;
        c.ipady = 12;
        statiscic.add(actionLabel[5], c);

        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 10;
        c.ipady = 12;
        pane.add(statiscic, c);
        chartBandwith = new JPanel(new GridBagLayout());
        chartBandwith.setSize(new Dimension(800, 380));
        title = BorderFactory.createTitledBorder(blackline, "Chart Bandwith");
        title.setTitleJustification(TitledBorder.RIGHT);
        chartBandwith.setBorder(title);
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 10;
        c.ipady = 10;
        c.gridheight = 5;
        chartBandwith.add(graphChart(600, 270), c);
        c.gridx = 0;
        c.gridy = 1;
        c.ipadx = 70;
        c.ipady = 30;
        pane.add(chartBandwith, c);
        
        add(pane);
        //action listners button 
        
        
        timer = new Timer(delayTime, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    
                    if (tickTime <= 1 && tickTime > -1) {
                        //tik czasowy 0 - 1 jak on wynosi jeden 
                        //obliczamy maksa a pozniej wrzucamy do funkcji
                        if (!computerList.isEmpty()) {

                            for (int i = 0; i < computerList.size(); i++) {

                                // wyciagamy ip :D
                                //split :D
                                String[] computer = computerList.get(i).split(";");
                                String ip = computer[1];

                                client = new mSnmp(ip + "/161");
                                client.start();
                                //pobieramy dane 
                                // pobieramy informacje o komputerze - narazie jest to niesitotna informacja

                                String sysDescr = client.getAsString(new OID(".1.3.6.1.2.1.1.1.0"));
                                // interfejs maksymalny - wyszukujemy najwiekszy  - max
                                maxInterfece = client.doSnmpwalk(".1.3.6.1.2.1.2.2.1.5");
                                //ile wyslano
                                ComputerBandwith += client.doSnmpwalk(".1.3.6.1.2.1.2.2.1.10");
                                //client.doSnmpwalk(".1.3.6.1.2.1.2.2.1.9");
                                client = null;

                            }
                            sendRecivePackets.put(tickTime, ComputerBandwith);
                            tickTime--;
                        }
                    }
                    if (tickTime < 0) {
                        // okreslamy max interface
                        int ansAll = acParam.calculateBandtwitch(maxInterfece, sendRecivePackets.get(0), sendRecivePackets.get(1), delayTime);
                        actionLabel[1].setText(acParam.setMax(ansAll) + " kb/s");
                        actionLabel[2].setText(acParam.setMin(ansAll) + " kb/s");
                        actionLabel[3].setText(acParam.calculateAverage() + " kb/s");
                        update((float) ansAll);
                        //simpleDateHere.format(new Date());
                        //bandwithNetwork.add(new Minute(), ansAll);
                        // update na rysunku
                        // liczymy przeplywnosc :D
                        // wzor ((t2 - t1) * 8) * 100 / szybkoscInterfejsu * czas (60s)
                        tickTime = 1;
                    }

                    maxInterfece = "";
                    ComputerBandwith = "";
                } catch (IOException ex) {
                    Logger.getLogger(MainTabSmizsk.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(MainTabSmizsk.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });

        timer.start();
        setVisible(true);

        //pane.add(, c);
    }

    public ChartPanel graphChart(int w, int h) {
        // Create a simple XY chart
        bandwithNetwork = new DynamicTimeSeriesCollection(1, 1000, new Second());
        bandwithNetwork.setTimeBase(new Second());
        bandwithNetwork.addSeries(new float[1], 0, "Przepustowosc sieci");
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Przepustowosc sieci", "Czas(s)", "kb", bandwithNetwork, true, true, false);

        final XYPlot plot = chart.getXYPlot();

        DateAxis axis = (DateAxis) plot.getDomainAxis();

        axis.setAutoRange(true);
        axis.setFixedAutoRange(20000);

        axis.setDateFormatOverride(new SimpleDateFormat("HH:mm:ss"));
        chart.setBackgroundPaint(new Color(142, 143, 145));
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(w, h));
        return chartPanel;
    }

    public void update(float value) throws Exception {

        float[] newData = new float[1];
        newData[0] = value;

        bandwithNetwork.advanceTime();
        bandwithNetwork.appendData(newData);
    }
    

    /**
     * actually return none, but when I develop i send some data for this method
     *
     * @return
     */
}
