package hu.oe.nik.szfmv.model;

import javax.xml.parsers.*;
import java.io.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class Xml {

    private static String type;
    private static float x;
    private static float y;
    private static float m11;
    private static float m12;
    private static float m21;
    private static float m22;
    private static int roadpainting_1;
    private static int roadpainting_2;
    private static int roadpainting_3;
    static List<MainObject> csoport = new ArrayList<MainObject>();

    public static void xmlParse() throws Exception {
        {
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse("C:\\Users\\User\\IdeaProjects\\AutomatedCar-A\\src\\main\\resources\\lane_keeping_test_world.xml");
                //Objektenként szétválaszt
                NodeList objectList = doc.getElementsByTagName("Object");
                for (int i = 0; i < objectList.getLength(); i++) {
                    //Objekteken végigfut
                    Node o = objectList.item(i);
                    if (o.getNodeType() == Node.ELEMENT_NODE) {
                        Element object = (Element) o;
                        type = object.getAttribute("type");
                        //Objekteken belüli leszármazottakon végigfut
                        NodeList posNodes = object.getElementsByTagName("Position");
                        for (int j = 0; j < posNodes.getLength(); j++) {
                            Node n = posNodes.item(j);
                            if (n.getNodeType() == Node.ELEMENT_NODE) {
                                Element name = (Element) n;
                                x = Float.parseFloat(name.getAttribute("x"));
                                y = Float.parseFloat(name.getAttribute("y"));
                            }
                        }
                        NodeList transNodes = object.getElementsByTagName("Transform");
                        for (int j = 0; j < transNodes.getLength(); j++) {
                            Node n = transNodes.item(j);
                            if (n.getNodeType() == Node.ELEMENT_NODE) {
                                Element name = (Element) n;
                                m11 = Float.parseFloat(name.getAttribute("m11"));
                                m12 = Float.parseFloat(name.getAttribute("m12"));
                                m21 = Float.parseFloat(name.getAttribute("m21"));
                                m22 = Float.parseFloat(name.getAttribute("m22"));
                            }
                        }
                        NodeList paraNodes = object.getElementsByTagName("Parameter");
                        for (int j = 0; j < paraNodes.getLength(); j++) {
                            Node n = paraNodes.item(j);
                            if (n.getNodeType() == Node.ELEMENT_NODE) {
                                Element name = (Element) n;
                                if (name.getAttribute("name").contains("1")){
                                    roadpainting_1 = Integer.parseInt(name.getAttribute("value"));
                                }
                                if (name.getAttribute("name").contains("2")){
                                    roadpainting_2 = Integer.parseInt(name.getAttribute("value"));
                                }
                                if (name.getAttribute("name").contains("3")){
                                    roadpainting_3 = Integer.parseInt(name.getAttribute("value"));
                                }
                            }
                        }
                    }
                    csoport.add(new MainObject(type, x, y, m11, m12, m21, m22, roadpainting_1, roadpainting_2, roadpainting_3));
                }
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (MainObject item: csoport
        ) {
            System.out.println(item.type  + ", " + item.x + ", " + item.y + ", " + item.m11 + ", " + item.m12 + ", " + item.m21 + ", " + item.m22 + ", " + item.roadpainting_1 + ", " + item.roadpainting_2 + ", " + item.roadpainting_3);
        }
        System.out.println("ok");
    }
}