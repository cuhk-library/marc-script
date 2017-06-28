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
import org.jdom2.filter.ElementFilter;


import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class TransformXSLXMLJDOMFolder
{
  public static void main(String args[]) throws JDOMException, JDOMParseException, TransformerException, TransformerConfigurationException, FileNotFoundException, IOException    
  {
    
   File curDir = new File(args[1]);
   String filenames [] = getAllXMLFiles(curDir);
  
   //System.out.println("filenames length " + filenames.length);
  
   for(int k=0;k<filenames.length;k++)
   {
    //System.out.println("k " + filenames[k]);
  
    StringWriter sw = new StringWriter();

    TransformerFactory tFactory = TransformerFactory.newInstance();
  
    Transformer transformer = tFactory.newTransformer(new StreamSource(args[0]));

    transformer.transform(new StreamSource(filenames[k]), new StreamResult(sw));

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
    


    FileInputStream fis = new FileInputStream(filenames[k]);
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
    
    String value945 = "";
    String value945c = "";
    
    String value901 = "";
    
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
      
      if(tagName.equals("datafield")&&tagAttValue.equals("945"))
      {
        List<Element> marcSubfields = marcs.get(i).getChildren();
        
        for(int j=0;j<marcSubfields.size();j++)
        {
         codeValue = marcSubfields.get(j).getAttributeValue("code");
         
         if(codeValue.equals("i"))
         {
           value945 = marcSubfields.get(j).getValue();
         }
         
         if(codeValue.equals("c"))
         {
           value945c = marcSubfields.get(j).getValue();
         }

        }
      }
 
      if(tagName.equals("datafield")&&tagAttValue.equals("901"))
      {
        List<Element> marcSubfields = marcs.get(i).getChildren();
        
        for(int j=0;j<marcSubfields.size();j++)
        {
         codeValue = marcSubfields.get(j).getAttributeValue("code");
         
         if(codeValue.equals("a"))
         {
           value901 = marcSubfields.get(j).getValue();
         }
         
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

    String outputFilename = "";
    
    if(!value945.equals(""))
    {
      outputFilename = value945;
    }
    else
    {
      outputFilename = value901;
    }

    if(outputFilename.equals("")||(args.length>2&&args[2]!=null&&args[2].equals("file")))
    {
      outputFilename = filenames[k].substring(0,filenames[k].indexOf("_marc21")) + ".xml";
    }
    else
    {
      outputFilename = args[1] + "\\" + outputFilename + ".xml";
    }

    //System.out.println("outputFilename " + outputFilename);

    FileOutputStream fosout = new FileOutputStream(outputFilename);
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
    
    List<Element> titleInfoNodes = newRoot.getChildren("titleInfo",ns);
    

    
    for(int j=0;j<titleInfoNodes.size();j++)
    {
     //System.out.println("CJK " + titleInfoNodes.get(j).getAttributeValue("script"));

     if(titleInfoNodes.get(j).getAttributeValue("type")==null)
     {
      Element partNumber = new Element("partNumber",ns);
      
      partNumber.addContent(value945c);
     
      titleInfoNodes.get(j).addContent(partNumber);
      
      //System.out.println(titleInfoNodes.get(j).getChild("partNumber",ns));
      
      
     }

     

    }   
    









    //System.out.println("newRoot name " + newRoot.getName());
    
    List<Element> noteNodes = newRoot.getChildren("note",ns);
    
    List<Element> noteNodesClone = new ArrayList<Element>();
    
    for(int j=0;j<noteNodes.size();j++)
    {
     //System.out.println("CJK " + noteNodes.get(j).getAttributeValue("script"));
     if(noteNodes.get(j).getAttributeValue("script")!=null&&noteNodes.get(j).getAttributeValue("script").equals("CJK"))
     {
      noteNodesClone.add(noteNodes.get(j).clone());
     }
    }

    for(int j=0;j<noteNodes.size();j++)
    {
     //System.out.println("non CJK " + noteNodes.get(j).getAttributeValue("script"));
     if(noteNodes.get(j).getAttributeValue("script")==null||!noteNodes.get(j).getAttributeValue("script").equals("CJK"))
     {
      noteNodesClone.add(noteNodes.get(j).clone());
     }
    }
    
    
    //System.out.println(noteNodesClone.size());
    
    newRoot.removeChildren("note",ns);

    //System.out.println(noteNodesClone.size());
    
    for(int j=0;j<noteNodesClone.size();j++)
    {
      newRoot.addContent(noteNodesClone.get(j));
    }

    
    
    
    
    
    List<Element> relatedItemNodes = newRoot.getChildren("relatedItem",ns);
    
    List<Element> relatedItemNodesClone = new ArrayList<Element>();
    
    String scriptValue = "";
    String altRepGroupValue = "";
        
    for(int j=0;j<relatedItemNodes.size();j++)
    {
     //System.out.println("non CJK " + relatedItemNodes.get(j).getAttributeValue("script"));
     if(relatedItemNodes.get(j).getAttributeValue("script")!=null||relatedItemNodes.get(j).getAttributeValue("altRepGroup")!=null)
     {
      
      scriptValue = relatedItemNodes.get(j).getAttributeValue("script");
      altRepGroupValue = relatedItemNodes.get(j).getAttributeValue("altRepGroup");
      
      
      relatedItemNodes.get(j).removeAttribute("script");
      relatedItemNodes.get(j).removeAttribute("altRepGroup");
      
      
      if(scriptValue!=null)
      {
       relatedItemNodes.get(j).getChildren().get(0).setAttribute("script",scriptValue);
      }
      if(altRepGroupValue!=null)
      {
       relatedItemNodes.get(j).getChildren().get(0).setAttribute("altRepGroup",altRepGroupValue);
      }
      
     }
    }

    
    for(int j=0;j<relatedItemNodes.size();j++)
    {
     //System.out.println("CJK " + relatedItemNodes.get(j).getAttributeValue("script"));
     if(relatedItemNodes.get(j).getAttributeValue("script")!=null&&relatedItemNodes.get(j).getAttributeValue("script").equals("CJK"))
     {
      relatedItemNodesClone.add(relatedItemNodes.get(j).clone());
     }
    }

    for(int j=0;j<relatedItemNodes.size();j++)
    {
     //System.out.println("non CJK " + relatedItemNodes.get(j).getAttributeValue("script"));
     if(relatedItemNodes.get(j).getAttributeValue("script")==null||!relatedItemNodes.get(j).getAttributeValue("script").equals("CJK"))
     {
      relatedItemNodesClone.add(relatedItemNodes.get(j).clone());
     }
    }
    
    
    
    
    //System.out.println(relatedItemNodesClone.size());
    
    newRoot.removeChildren("relatedItem",ns);

    //System.out.println(relatedItemNodesClone.size());
    
    for(int j=0;j<relatedItemNodesClone.size();j++)
    {
      newRoot.addContent(relatedItemNodesClone.get(j));
    }





    newRoot.sortContent(new ElementFilter("relatedItem",ns),new SortAltRepGroup());
    newRoot.sortContent(new ElementFilter("relatedItem",ns),new SortScript());
    





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


    Element accessCondition = new Element("accessCondition",ns);
    accessCondition.setAttribute("type","useAndReproduction");
    accessCondition.addContent("Use of this resource is governed by the terms and conditions of the Creative Commons ¡§Attribution-NonCommercial-NoDerivatives 4.0 International¡¨ License (http://creativecommons.org/licenses/by-nc-nd/4.0/)");
        
    newRoot.addContent(accessCondition);



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
  
  private static String[] getAllXMLFiles(File curDir)
  {
    List<String> filenamesList = new ArrayList<String>();
    File[] fileList = curDir.listFiles();
    for(File f : fileList)
    {
     if(f.isDirectory())
     {
       getAllXMLFiles(f);
     }
     
     if(f.isFile())
     {
       if(f.getName().indexOf("_marc21.xml")>-1)
       {
        filenamesList.add(curDir.toString() + "\\" + f.getName());
       }
     }
    }
    
    String filenames [] = filenamesList.toArray(new String[filenamesList.size()]);
    
    return filenames;
  }
  

}


  class SortScript implements Comparator<Element> 
  {

	  public int compare(Element e1, Element e2) 
	  {
      Namespace ns = Namespace.getNamespace("http://www.loc.gov/mods/v3");

	     String script1 = e1.getChildren("titleInfo",ns).get(0).getAttributeValue("script");
	     String script2 = e2.getChildren("titleInfo",ns).get(0).getAttributeValue("script");
	    
	     if(script1==null)
	     {
	      if(script2==null)
	      {
	        return 0;
	      }
	      else
	      {
	        return -1;
	      }
	     }
	     else
	     {
	      if(script2==null)
	      {
	        return 1;
	      }
	      else
	      {
	        return script1.compareTo(script2);
	      }
	     }
	    }
	  
  }
  
  class SortAltRepGroup implements Comparator<Element> 
  {

	  public int compare(Element e1, Element e2) 
	  {
      Namespace ns = Namespace.getNamespace("http://www.loc.gov/mods/v3");

	     String script1 = e1.getChildren("titleInfo",ns).get(0).getAttributeValue("altRepGroup");
	     String script2 = e2.getChildren("titleInfo",ns).get(0).getAttributeValue("altRepGroup");
	    
	     if(script1==null)
	     {
	      if(script2==null)
	      {
	        return 0;
	      }
	      else
	      {
	        return -1;
	      }
	     }
	     else
	     {
	      if(script2==null)
	      {
	        return 1;
	      }
	      else
	      {
	        return script1.compareTo(script2);
	      }
	     }
	    }
	  
  }

