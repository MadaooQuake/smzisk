/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smizsk;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;


/**
 *
 * @author madaooo
 */
public class config {

    File fXmlFile = new File("config.xml"); //statyczniie ustawiony plik konfiguracyjny

    DocumentBuilderFactory dbFactory;
    DocumentBuilder dBuilder;
    Document doc;
    HashMap<Integer,String> computerList = new HashMap<> ();
    int sumCom;

    public void load_file() {
        try {
            
            dbFactory = DocumentBuilderFactory.newInstance();
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(fXmlFile);
        } catch (IOException | ParserConfigurationException | SAXException exception) {

            // add log loggers abaout bad config file format
            System.err.printf(exception.getMessage());
            System.exit(0);
        }
    }

    public HashMap<Integer,String> read_elements() {
        doc.getDocumentElement().normalize();

        // i teraz sie zaczyna akcja
        // wczytujemy wszystkie komputery
        NodeList nList = doc.getElementsByTagName("computer");

        //wyciagamy wszystko i bedziemy dodawac do listy
        sumCom = nList.getLength();
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                //zappis danych

                // dla testu
                Element eElement = (Element) nNode;
                computerList.put(temp, eElement.getAttribute("name").toString() 
                        + ";" + eElement.getElementsByTagName("ip").item(0).getTextContent().toString());
                // elements to string 
            }
        }
        return computerList;
    }
    
    public Integer sumComputer() {
        return sumCom;
    }
}
