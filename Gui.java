/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smizsk;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 *
 * @author madaooo
 */
public class Gui extends javax.swing.JFrame {

    JTabbedPane tabbedPane;


    /**
     * Creates new form Gui
     */
    public Gui() {

        // main element of gui  panels, menus etc..
        mainGuiGenerate();

        // build element to menu

        //generate all elements 
        guiGenerate();

        setVisible(true);
    }

    private void mainGuiGenerate() {
        try {
            UIManager.put("nimbusBase", new Color(87, 73, 73));
            UIManager.put("nimbusBlueGrey", new Color(99, 67, 67));
            UIManager.put("control", new Color(142, 143, 145));
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }

        setTitle("System monitorowania i zarządzania siecią v1.0.0");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //Create the menu bar.

        // tabbed panels
        tabbedPane = new JTabbedPane();
        MainTabSmizsk panel1 = new MainTabSmizsk();
        //JComponent panel1 = makeTextPanel("Panel #1");
        ListOfComputer panel2 = new ListOfComputer();
        tabbedPane.addTab("Ogólne", panel1);
        tabbedPane.addTab("Siec", panel2);
        // Create Panels :D

    }

    private void guiGenerate() {
        
        add(tabbedPane);
    }

    //only for Design
    protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }
}
