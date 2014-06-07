/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package smizsk;

import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author madaooo
 */
public class Smizsk {
    
    static config readXMLconf=null;
    static HashMap<Integer,String> computerList = new HashMap<> ();
    
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        readXMLconf = new config();
        readXMLconf.load_file();
        computerList = readXMLconf.read_elements();
        
        // test snmp connection?
        /*
        mSnmp client = new mSnmp("udp:127.0.0.1/161");
        client.start();
        String sysDescr = client.getAsString(new OID(".1.3.6.1.2.1.1.1.0"));
        client.doSnmpwalk(".1.3.6.1.2.1.2.2.1.5"); // interfejs - max
        client.doSnmpwalk(".1.3.6.1.2.1.2.2.1.10"); //ile wyslano
        client.doSnmpwalk(".1.3.6.1.2.1.2.2.1.9");
        // .1.3.6.1.2.1.2.2.1.9 - czas
                */
        //System.out.println(computerList);
        //System.out.println(sysDescr);
        
        //Run gui for this application
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Gui().setVisible(true);
            }
        });
    }
    
}
