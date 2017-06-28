import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.*;
import java.util.*;
import java.lang.*;
import java.nio.charset.StandardCharsets;
import org.jdom2.JDOMException;
import org.jdom2.input.JDOMParseException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Attribute;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;


import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class TransformXSLXMLJDOM
{
  public static void main(String args[]) throws JDOMException, JDOMParseException, TransformerException, TransformerConfigurationException, FileNotFoundException, IOException    
  {  
    StringWriter sw = new StringWriter();

    TransformerFactory tFactory = TransformerFactory.newInstance();
  
    Transformer transformer = tFactory.newTransformer(new StreamSource(args[0]));

    transformer.transform(new StreamSource(args[1]), new StreamResult(sw));

    //System.out.println("Hello 1");
    String cmarcunicode = sw.toString();


    SAXBuilder saxBuilder = new SAXBuilder();
    
    InputStream transformResult = new ByteArrayInputStream(cmarcunicode.getBytes(StandardCharsets.UTF_8));

    Document document = saxBuilder.build(transformResult);
    
    Element root = document.getRootElement();
    
    List<Element> oneChild = root.getChildren();
    
   
    List<Element> nodes = oneChild.get(0).getChildren();
    
    List<Element> nodesNew = new ArrayList<Element>(nodes);
    
    
    
    
    String nodeName = "";
    String altRepGroup = "";
    String script = "";
    
    String nodeName1 = "";
    String altRepGroup1 = "";
    String script1 = "";
    
    
    
    for(int i=0;i<nodes.size();i++)
    {
      Element node = nodes.get(i);
      
      nodeName = node.getName();
      
      altRepGroup = getAttValue(node.getAttributes(),"altRepGroup");
      
      script = getAttValue(node.getAttributes(),"script");
      
      if(!altRepGroup.equals("not found"))
      {
      
       for(int j=0;j<nodes.size();j++)
       {
        Element node1 = nodes.get(j);
        
        nodeName1 = node1.getName();
        
        if(nodeName1.equals(nodeName))
        {
         altRepGroup1 = getAttValue(node1.getAttributes(),"altRepGroup");
         
         script1 = getAttValue(node1.getAttributes(),"script");
         
         if(!altRepGroup1.equals("not found")&&altRepGroup1.equals(altRepGroup)&&!script1.equals(script))
         {
            
            //System.out.println("before " + nodes.size());
                       
            
            nodesNew.remove(j);
            
            nodesNew.add(j,nodes.get(i).clone().detach());
            
            nodesNew.remove(i);
            
            nodesNew.add(i,nodes.get(j).clone().detach());
            
            //System.out.println("after " + nodes.size());

         }
         
        } 
        
       }
      }
    }
    


    FileInputStream fis = new FileInputStream(args[1]);
    InputStreamReader isr = new InputStreamReader(fis,"utf-8");


    SAXBuilder saxBuilder1 = new SAXBuilder();
    
    Document document1 = saxBuilder1.build(isr);

    Element root1 = document1.getRootElement();

    List<Element> marc = root1.getChildren();

    List<Element> marcs = marc.get(0).getChildren();

    
    String tagName = "";
    String tagAttValue = "";
    
    String value090a ="";
    String value090b ="";
    String value090d ="";
    
    
    String value970l ="";
    String value970t ="";
    String value970c ="";
    String value970p ="";

    String value970 = "";
    
    ArrayList<String> value970s = new ArrayList<String>();
    
    String value090 = "";
    
    String value907 = "";
    
    String codeValue = "";

    //System.out.println("marc.size() " + marcs.size());

    for(int i=0;i<marcs.size();i++)
    {
      tagName = marcs.get(i).getName();
      tagAttValue= marcs.get(i).getAttributeValue("tag");
      
      //System.out.println("tagName " + tagName);
      //System.out.println("tagAttValue " + tagAttValue);
      
      if(tagName.equals("datafield")&&tagAttValue.equals("090"))
      {
       List<Element> marcSubfields = marcs.get(i).getChildren();
       
       for(int j=0;j<marcSubfields.size();j++)
       {
         codeValue = marcSubfields.get(j).getAttributeValue("code");
         
         if(codeValue.equals("a"))
         {
           value090a = marcSubfields.get(j).getValue();
         }
         
         if(codeValue.equals("b"))
         {
           value090b = marcSubfields.get(j).getValue();
         }

         if(codeValue.equals("d"))
         {
           value090d = marcSubfields.get(j).getValue();
         }

       }
       
      }
      
      if(tagName.equals("datafield")&&tagAttValue.equals("907"))
      {
        List<Element> marcSubfields = marcs.get(i).getChildren();
        
        for(int j=0;j<marcSubfields.size();j++)
        {
          value907 += marcSubfields.get(j).getValue();
        }
      }
      
      
      
      if(tagName.equals("datafield")&&tagAttValue.equals("970"))
      {
       value970 = "";
       value970l = "";
       value970t = "";
       value970c = "";
       value970p = "";
       
       List<Element> marcSubfields = marcs.get(i).getChildren();
       
       for(int j=0;j<marcSubfields.size();j++)
       {
         codeValue = marcSubfields.get(j).getAttributeValue("code");
         
         if(codeValue.equals("l"))
         {
           value970l = marcSubfields.get(j).getValue();
         }
         
         if(codeValue.equals("t"))
         {
           value970t = marcSubfields.get(j).getValue();
         }

         if(codeValue.equals("c"))
         {
           value970c = marcSubfields.get(j).getValue();
         }

         if(codeValue.equals("p"))
         {
           value970p = marcSubfields.get(j).getValue();
         }
         
       }
       
       if(!value970l.trim().equals(""))
       {
        value970 = "Chapter " + value970l;
       }
       
       if(!value970t.trim().equals(""))
       {
        if(!value970l.trim().equals(""))
        {
         value970 = value970 + " --- ";
        }
        value970 = value970 + value970t;
       }

       if(!value970c.trim().equals(""))
       {
        if(!value970l.trim().equals("")||!value970t.trim().equals(""))
        {
         value970 = value970 + " / ";
        }
        value970 = value970 + value970c;
       }

       if(!value970p.trim().equals(""))
       {
        if(!value970l.trim().equals("")||!value970t.trim().equals("")||!value970c.trim().equals(""))
        {
         value970 = value970 + " --- ";
        }
        value970 = value970 + " p." + value970p;
       }
       
       value970s.add(value970);

      }

      

    }

    value090 = value090a + value090b + value090d;


    //System.out.println("value090 " + value090);
    //System.out.println("value907 " + value907);

/*
    for(int j=0;j<value970s.size();j++)
    {
      System.out.println("value970 " + value970s.get(j));
    }
*/



    FileOutputStream fosout = new FileOutputStream(args[2]);
    OutputStreamWriter oswout = new OutputStreamWriter(fosout,"UTF-8");

    
    
    
    
    Document updatedDocument = new Document(root.clone());
    List<Element> newMods = updatedDocument.getRootElement().getChildren();
    
    newMods.get(0).removeContent();
    
    for(int i=0;i<nodesNew.size();i++)
    {
     nodesNew.get(i).detach();
     newMods.get(0).addContent(nodesNew.get(i));
    }
    
    Namespace ns = Namespace.getNamespace("http://www.loc.gov/mods/v3");

    Element newRoot = newMods.get(0);

    //System.out.println("newRoot name " + newRoot.getName());
    
    List<Element> locationNodes = newRoot.getChildren("location",ns);
    
    List<Element> locationNodesClone = new ArrayList<Element>();
    
    for(int j=0;j<locationNodes.size();j++)
    {
     locationNodesClone.add(locationNodes.get(j).clone());
    }
    
    
    //System.out.println(locationNodesClone.size());
    
    
    
    newRoot.removeChildren("location",ns);

    //System.out.println(locationNodesClone.size());

    
    Element location = new Element("location",ns);
    
    
    location.addContent(new Element("physicalLocation",ns));
    location.addContent(new Element("shelfLocator",ns));
    location.addContent(new Element("url",ns));
    
    location.getChild("shelfLocator",ns).addContent(value090);
    location.getChild("url",ns).addContent("http://library.cuhk.edu.hk/record=" + value907.substring(1,value907.length()-1));
    
    for(int j=0;j<locationNodesClone.size();j++)
    {
     newRoot.addContent(locationNodesClone.get(j));
    }

    newRoot.addContent(location);

    for(int j=0;j<value970s.size();j++)
    {
     Element tempElement = new Element("tableOfContents",ns);
     tempElement.addContent(value970s.get(j));
     tempElement.setAttribute("displayLabel","Chapters included in book");
     newRoot.addContent(tempElement);
    }

    
    
    XMLOutputter xmlOutput = new XMLOutputter();
    xmlOutput.setFormat(Format.getPrettyFormat());
    xmlOutput.output(updatedDocument,oswout);
    
    
  }
  
  private static String getAttValue(List<Attribute> attributes,String attributeName)
  {
   String altRepGroup = "not found";
   
   for(int j=0;j<attributes.size();j++)
   {
    if(attributes.get(j).getName().equals(attributeName))
    {
      altRepGroup = attributes.get(j).getValue();
    }
   }

   return altRepGroup;
  }
}
