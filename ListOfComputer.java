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
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
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
import static smizsk.Smizsk.computerList;

/**
 *
 * @author madaooo
 */
public class ListOfComputer extends javax.swing.JPanel {

    JPanel listOfComputers;
    JLabel siteCom;
    JPanel computerStatiscic;
    TitledBorder title;
    JPanel computerChart;
    Border blackline;
    JLabel bandwithInformation;
    JLabel actionLabel[] = new JLabel[6];
    JButton left;
    JButton right;
    JTable tableButtons;
    JButton[] computerName;
    Image icons;
    DynamicTimeSeriesCollection bandwithNetwork = null;
    ChartPanel chartPanel = null;
    static JLabel actionComputer[] = new JLabel[6];
    int selcetedComputet = 0;
    mSnmp client;
    Timer timer;
    int tickTime = 1;
    ActualizeParameters acParam = new ActualizeParameters();
    HashMap<Integer, String> sendRecivePackets = new HashMap<>();
    String ComputerBandwith = "";
    String[] cmputer;
    String maxInterfece = "";
    String sysDescr;
    JPanel pane;
    int p = 1;
    int z = 0;

    //constructot
    ListOfComputer() {
        super(true);
        initComponents();
    }

    /**
     * Method have
     *
     * @return none
     */
    public final void initComponents() {
        try {
            blackline = BorderFactory.createLineBorder(Color.black);

            pane = new JPanel(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            // first step create 3 panels
            /*
             [panel1] - list of all computers using in network
             [panel2] - computer ststiscic get on snmp agent
             [panel3] - generate computer chart
             */
            // list of computers
            // start test
            //stop test
            c.gridx = 0;
            c.gridy = 0;
            c.ipadx = 30;
            c.ipady = 0;
            pane.add(generateTable(), c);

            drawComputer();

            //pane.add(statiscic, c);
            // statistic panel
            computerStatiscic = new JPanel(new GridBagLayout());
            computerStatiscic.setSize(new Dimension(800, 140));

            title = BorderFactory.createTitledBorder(blackline, "Statistic");
            title.setTitleJustification(TitledBorder.RIGHT);
            computerStatiscic.setBorder(title);
            generatComputerStatistic();
            c.gridx = 0;
            c.gridy = 1;
            c.ipadx = 500;
            c.ipady = 10;
            pane.add(computerStatiscic, c);

            //chart panel
            computerChart = new JPanel(new GridBagLayout());
            computerChart.setSize(new Dimension(800, 380));
            title = BorderFactory.createTitledBorder(blackline, "Chart Bandwith");
            title.setTitleJustification(TitledBorder.RIGHT);
            computerChart.setBorder(title);
            c.gridx = 0;
            c.gridy = 0;
            c.ipadx = 10;
            c.ipady = 10;
            computerChart.add(graphChart(680, 220), c);
            c.gridx = 0;
            c.gridy = 2;
            c.ipadx = 70;
            c.ipady = 30;
            pane.add(computerChart, c);
            timer = new Timer(10000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    try {
                        if (tickTime <= 1 && tickTime > -1) {

                            //tik czasowy 0 - 1 jak on wynosi jeden
                            //obliczamy maksa a pozniej wrzucamy do funkcji
                            // wyciagamy ip :D
                            //split :D
                            client = new mSnmp(cmputer[1] + "/161");

                            client.start();
                            //pobieramy dane
                            // pobieramy informacje o komputerze - narazie jest to niesitotna informacja

                            sysDescr = client.getAsString(new OID(".1.3.6.1.2.1.1.1.0"));

                            // interfejs maksymalny - wyszukujemy najwiekszy  - max
                            maxInterfece = client.doSnmpwalk(".1.3.6.1.2.1.2.2.1.5");

                            //ile wyslano
                            ComputerBandwith += client.doSnmpwalk(".1.3.6.1.2.1.2.2.1.10");
                            //client.doSnmpwalk(".1.3.6.1.2.1.2.2.1.9");
                            client = null;

                            sendRecivePackets.put(tickTime, ComputerBandwith);
                            tickTime--;

                        }
                        if (tickTime < 0) {

                            // okreslamy max interface
                            int ansAll = acParam.calculateBandtwitch(maxInterfece, sendRecivePackets.get(0), sendRecivePackets.get(1), 10000);
                            actionComputer[2].setText(acParam.getSystem(sysDescr));
                            actionComputer[3].setText(acParam.setMax(ansAll) + " kb/s");
                            actionComputer[4].setText(acParam.setMin(ansAll) + " kb/s");

                            update((float) ansAll);
                            //simpleDateHere.format(new Date());
                            //bandwithNetwork.add(new Minute(), ansAll);
                            // update na rysunku
                            // liczymy przeplywnosc :D
                            // wzor ((t2 - t1) * 8) * 100 / szybkoscInterfejsu * czas (60s)
                            tickTime = 1;
                        }

                    } catch (IOException ex) {
                        Logger.getLogger(ListOfComputer.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (Exception ex) {
                        Logger.getLogger(ListOfComputer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

            add(pane);
            timer.start();
            setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(ListOfComputer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Genrate table with button.
     *
     * @return
     */
    public JPanel generateTable() {
        listOfComputers = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        title = BorderFactory.createTitledBorder(blackline, "Lista Komputerów");
        title.setTitleJustification(TitledBorder.RIGHT);
        listOfComputers.setBorder(title);

        computerName = new JButton[computerList.size()];

        return listOfComputers;
    }

    // method to get parameters by computer
    // name pc, ip, network bandwitch, system
    public void generatComputerStatistic() {
        GridBagConstraints c = new GridBagConstraints();
        bandwithInformation = new JLabel("Nazwa Komputera:");
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 10;
        c.ipady = 12;
        computerStatiscic.add(bandwithInformation, c);
        bandwithInformation = new JLabel("Adres IP:");
        c.gridx = 0;
        c.gridy = 1;
        c.ipadx = 10;
        c.ipady = 12;
        computerStatiscic.add(bandwithInformation, c);
        bandwithInformation = new JLabel("System Operacyjny:");
        c.gridx = 0;
        c.gridy = 2;
        c.ipadx = 10;
        c.ipady = 12;
        computerStatiscic.add(bandwithInformation, c);
        bandwithInformation = new JLabel("Maksymalna przepływność:");
        c.gridx = 0;
        c.gridy = 3;
        c.ipadx = 10;
        c.ipady = 12;
        computerStatiscic.add(bandwithInformation, c);
        bandwithInformation = new JLabel("Minimalna przepływność:");
        c.gridx = 0;
        c.gridy = 4;
        c.ipadx = 10;
        c.ipady = 12;
        computerStatiscic.add(bandwithInformation, c);
        bandwithInformation = new JLabel("Średnia przepływność:");
        cmputer = computerList.get(selcetedComputet).split(";");
        actionComputer[0] = new JLabel(cmputer[0]);
        c.gridx = 1;
        c.gridy = 0;
        c.ipadx = 10;
        c.ipady = 12;
        computerStatiscic.add(actionComputer[0], c);
        actionComputer[1] = new JLabel(cmputer[1]);
        c.gridx = 1;
        c.gridy = 1;
        c.ipadx = 10;
        c.ipady = 12;
        computerStatiscic.add(actionComputer[1], c);
        actionComputer[2] = new JLabel("Ladowanie...");
        c.gridx = 1;
        c.gridy = 2;
        c.ipadx = 10;
        c.ipady = 12;
        computerStatiscic.add(actionComputer[2], c);
        actionComputer[3] = new JLabel("0 kb/s");
        c.gridx = 1;
        c.gridy = 3;
        c.ipadx = 10;
        c.ipady = 12;
        computerStatiscic.add(actionComputer[3], c);
        actionComputer[4] = new JLabel("0 kb/s");
        c.gridx = 1;
        c.gridy = 4;
        c.ipadx = 10;
        c.ipady = 12;
        computerStatiscic.add(actionComputer[4], c);
    }

    /**
     * actually return none, but when I develop i send some data for this method
     *
     * @param w
     * @param h
     * @return
     */
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

    public class strackListLeft implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            try {
                
                if (p > 0 && z > 6) {
                    z -= 8;
                    p--;
                } else {
                    z = 0;
                    p--;
                }

                drawComputer();
            } catch (IOException ex) {
                Logger.getLogger(ListOfComputer.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public class strackListRight implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            try {
                if (computerList.size() / p > z) {
                    z += 8;
                }
                drawComputer();
            } catch (IOException ex) {
                Logger.getLogger(ListOfComputer.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public class changeComputer implements ActionListener {

        int i;

        public changeComputer(int i_) {
            i = i_;
        }

        @Override
        public void actionPerformed(ActionEvent ae) {

            String[] getInfo = computerName[i].getName().split(" ");
            cmputer = computerList.get(Integer.parseInt(getInfo[1])).split(";");

            actionComputer[0].setText(cmputer[0]);
            actionComputer[1].setText(cmputer[1]);
            actionComputer[2].setText("Ladowanie...");
            actionComputer[3].setText("0 kb/s");
            actionComputer[4].setText("0 kb/s");

            computerStatiscic.setVisible(true);

        }
    }

    public void drawComputer() throws IOException {
        GridBagConstraints c = new GridBagConstraints();
        listOfComputers.removeAll();
        try {
            icons = ImageIO.read(getClass().getResource("images/left.png"));
            left = new JButton();
            left.setName("Left");
            left.addActionListener(new strackListLeft());
            left.setIcon(new ImageIcon(icons));
            c.gridx = 0;
            c.gridy = 0;
            c.ipadx = 0;
            c.ipady = 0;
            listOfComputers.add(left, c);
            icons = ImageIO.read(getClass().getResource("images/right.png"));
            right = new JButton();
            right.setName("Right");
            right.addActionListener(new strackListRight());
            right.setIcon(new ImageIcon(icons));
            c.gridx = 1;
            c.gridy = 0;
            c.ipadx = 0;
            c.ipady = 0;
            listOfComputers.add(right, c);
            siteCom = new JLabel(p + "z" + (computerList.size()/5));
           
            c.gridx = 0;
            c.gridy = 1;
            listOfComputers.add(siteCom, c);
            for (int i = z; i < computerList.size(); i++) {

                icons = ImageIO.read(getClass().getResource("images/pc_enable.png"));
                String[] name = computerList.get(i).split(";");
                computerName[i] = new JButton();
                computerName[i].setName("Computer " + i);
                computerName[i].setIcon(new ImageIcon(icons));
                computerName[i].setToolTipText(name[0]);
                computerName[i].addActionListener(new changeComputer(i));
                c.gridx = i + 2;
                c.gridy = 0;
                c.ipadx = 0;
                c.ipady = 0;
                c.gridheight =2;
                listOfComputers.add(computerName[i], c);
                
                if ((i / p) > 6) {
                    
                    p++;
                    break;
                }

                setVisible(true);
                repaint();
            }
        } catch (IOException ex) {
            Logger.getLogger(ListOfComputer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
