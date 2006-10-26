package org.wyona.jspwiki;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Category;
import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import com.ecyrd.jspwiki.WikiContext;
import com.ecyrd.jspwiki.WikiEngine;
import com.ecyrd.jspwiki.WikiPage;

/**
 *
 */
public class WikiParser extends org.wyona.wikiparser.WikiParser {
    private static Category log = Category.getInstance(WikiParser.class);
    
    private InputStream inputStream = null;
    private Properties properties = null;
    private Element bodyElement = null; 
    
    public WikiParser(InputStream is) {
        init(is);
    }
    
    /**
     *
     */
    public static void main(String[] args) {
        FileInputStream fstream;
        try {
            fstream = new FileInputStream(args[0]);
            new WikiParser(fstream);
        } catch (FileNotFoundException e) {
            log.error(e);
        }
    }
    
    private void init(InputStream is) {
        inputStream = is;
        loadProperties();
    }    
    
    public void parse(InputStream inputStream) {
        transformHtml2Xml();
    }
    
    /**
     * this method does mainly the job it reads the file from the repository transform it to 
     * html and calls the Html2WikiXmlTransformer to create a wiki xml file which can be edited with yulup
     */
    private void transformHtml2Xml() {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            
            WikiEngine engine = new WikiEngine(properties);
            WikiPage page = new WikiPage(engine, "PAGE");
            WikiContext context = new WikiContext(engine, page);
            
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = null;
            StringBuffer stringBuffer = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line + "\n");                
            }
            StringBuffer createdHtml = new StringBuffer();
            createdHtml.append("<html><body>");
            createdHtml.append(engine.textToHTML(context, stringBuffer.toString()));
            createdHtml.append("</body></html>");
            
            ByteArrayInputStream inStream = new ByteArrayInputStream(createdHtml.toString().getBytes());
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(inStream);
            Element root = doc.getRootElement();
            bodyElement = root.getChild("body");
            List listRootElements = bodyElement.getChildren();
            for(int i=0; i<listRootElements.size(); i++) {
                Element element  = (Element)(listRootElements.get(i));
                if(element.getName().equalsIgnoreCase("ul") || element.getName().equalsIgnoreCase("ol")) {
                    treeWalker(element, 1);
                }
            }
            
            XMLOutputter outputter = new XMLOutputter();
            String modifiedHtml = outputter.outputString(doc);
            log.debug("####################################");
            log.debug(modifiedHtml);
            log.debug("####################################");
            Html2WikiXmlTransformer html2WikiXml = new Html2WikiXmlTransformer();
            SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
            saxParser.parse(new java.io.ByteArrayInputStream(modifiedHtml.getBytes()), html2WikiXml);
            setResultAsInputStream(html2WikiXml.getInputStream());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void treeWalker(Content node, int counter) {
        if(node instanceof Element) {
            Element element = (Element) node;
            if(!("true").equals(element.getAttributeValue("moved"))) {
                if(element.getName().equals("li")) {
                    element.setAttribute("depth", "" + counter);
                    counter++;
                }    
                List listChildren = element.getChildren();
                for(int i=0; i<listChildren.size(); i++) {
                    treeWalker((Element) listChildren.get(i), counter);
                }
                if(element.getName().equals("ul")) {
                    moveElement(element, "ul");
                }
                if(element.getName().equals("ol")) {
                    moveElement(element, "ol");
                }
                element.setAttribute("moved", "true");
            }
        }
    }
    
    public void moveElement(Element element, String name) {
        int position = 0;
        Vector keepThisKids = new Vector();
        Element grandParentElement = element.getParentElement().getParentElement();
        if(grandParentElement.getName().equals(name)) {
            Element temp = element.getParentElement();
            List movingChildren = element.getChildren();
            for(int i=0; i<movingChildren.size(); i++) {
                keepThisKids.add(movingChildren.get(i));
                ((Element)(movingChildren.get(i))).detach();
            }
            element.detach();
            position = grandParentElement.indexOf(temp) + 1;
            grandParentElement.addContent(position, keepThisKids);
        } else 
        if(name.equals("ol") && grandParentElement.getName().equals("ul") || name.equals("ul") && grandParentElement.getName().equals("ol")) {
            Element subRoot = getActualRootsChild(element);
            List movingChildren = element.getChildren();
            for(int i=0; i<movingChildren.size(); i++) {
                keepThisKids.add(movingChildren.get(i));
                ((Element)(movingChildren.get(i))).detach();
            }
            element.detach();
            position = bodyElement.indexOf(subRoot) + 1;
            bodyElement.addContent(position, keepThisKids);
        }
        //TODO handle this bug DONT know where to attach this 
    }
    
    public Element getActualRootsChild(Element element) {
        if(element.getParentElement().equals(bodyElement)) return element;
        else return getActualRootsChild(element.getParentElement());
    }
    
    /**
     * this method loads the properties for the jspWikiParser
     */
    private void loadProperties() {
        try {
            properties = new Properties();
            InputStream is = getClass().getResourceAsStream( "/parser.properties" );
            properties.load(is);    
        } catch(IOException e) {
            log.error("Could not load PropertyFile: ", e);
        }
    }
    
    /**
     * this method loads the InputStream which is needed for the WikiResource
     * @param inputStream
     */
    private void setResultAsInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
    
    /**
     * this method returns the Result of the Page as serialized Xml in an InputStream
     * @return WikiXml of Page as InputStream 
     */
    public InputStream getInputStream() {
        return this.inputStream;
    }
    
    /**
     * this method simply prints data from the inputStream into the logfile
     *
     */
    public void debugInputStream() {
        try {
            InputStreamReader inR = new InputStreamReader(inputStream);
            BufferedReader buf = new BufferedReader(inR);
            String line;
            while ((line = buf.readLine()) != null) {
                log.debug(line);
            }
        } catch (Exception e) {
            log.error(e);
        }
    }
}
