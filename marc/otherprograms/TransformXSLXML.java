import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.*;
import java.util.*;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class TransformXSLXML
{
  public static void main(String args[]) throws TransformerException, TransformerConfigurationException, FileNotFoundException, IOException    
  {  
    StringWriter sw = new StringWriter();

    TransformerFactory tFactory = TransformerFactory.newInstance();
  
    Transformer transformer = tFactory.newTransformer(new StreamSource(args[0]));

    transformer.transform(new StreamSource(args[1]), new StreamResult(sw));


    //System.out.println("Hello 1");
    String cmarcunicode = sw.toString();
    
    ArrayList<String> tempList = new ArrayList<String>();
    
    String lines [] = cmarcunicode.split("\n");
    
    
    for(int i=0;i<lines.length;i++)
    {
     if(lines[i].indexOf("altRepGroup")>-1)
     {
      tempList.add(lines[i]); 
     }
    }
    
    
    
    FileInputStream fis = new FileInputStream(args[1]);
    InputStreamReader isr = new InputStreamReader(fis,"utf-8");
    BufferedReader br = new BufferedReader(isr);
    
    String marc21XML = "";

    String str = br.readLine();

    
    while(str!=null)
    {
     marc21XML +=str;
     str = br.readLine();
    }
    
    
    
    
    
    String field090 = "";
    
    String field907 = "";
    
     String value090 = "";
     
     String value090a = "";
     
     String value090b = "";
     
     String value090d = "";
     
     String value907 = "";
     
     String tempValue = "";

     if(marc21XML.indexOf("marc:datafield tag=\"090")>-1)
     {
       field090 = marc21XML.substring(marc21XML.indexOf("marc:datafield tag=\"090"));
       
       field090 = field090.substring(0,field090.indexOf("/marc:datafield"));
       
       System.out.println(field090+"\n");

      
       
       tempValue = field090;
       
       while(tempValue.indexOf("subfield code=\"a\">")>-1)
       {
        tempValue = tempValue.substring(tempValue.indexOf("subfield code=\"a\">")+18);
        tempValue = tempValue.substring(0,tempValue.indexOf("<"));
        value090a +=tempValue;
       }

       tempValue = field090;

       while(tempValue.indexOf("subfield code=\"b\">")>-1)
       {
        tempValue = tempValue.substring(tempValue.indexOf("subfield code=\"b\">")+18);
        tempValue = tempValue.substring(0,tempValue.indexOf("<"));
        value090b +=tempValue;
       }

       tempValue = field090;

       while(tempValue.indexOf("subfield code=\"d\">")>-1)
       {
        tempValue = tempValue.substring(tempValue.indexOf("subfield code=\"d\">")+18);
        tempValue = tempValue.substring(0,tempValue.indexOf("<"));
        value090d +=tempValue;
       }


       /*
       while(field090.indexOf("subfield")>-1)
       {
        field090 = field090.substring(field090.indexOf("subfield")+8);
        tempValue = field090.substring(field090.indexOf(">")+1);
        tempValue = tempValue.substring(0,tempValue.indexOf("<"));
        field090 = field090.substring(field090.indexOf("subfield")+8);
        value090 += tempValue;
       }
     */
     
     
     
      value090 = value090a + value090b + value090d;
      
      value090 = "<shelfLocator>" + value090 + "</shelfLocator>";
     }
     
     
     
         System.out.println(value090+"\n");


     if(marc21XML.indexOf("marc:datafield tag=\"907")>-1)
     {
       field907 = marc21XML.substring(marc21XML.indexOf("marc:datafield tag=\"907"));
       
       field907 = field907.substring(0,field907.indexOf("/marc:datafield"));

    System.out.println(field907+"\n");


       while(field907.indexOf("subfield")>-1)
       {
        field907 = field907.substring(field907.indexOf("subfield")+8);
        tempValue = field907.substring(field907.indexOf(">")+1);
        tempValue = tempValue.substring(0,tempValue.indexOf("<"));
        field907 = field907.substring(field907.indexOf("subfield")+8);
        value907 += tempValue;
       }

     }
    
    value907 = "<url>http://library.cuhk.edu.hk/record=" + value907.substring(1,value907.length()-1) + "</url>";
   
    System.out.println(value907+"\n");
    




    String field970 = "";
    String value970 = "";
    String value970l = "";
    String value970t = "";
    String value970c = "";
    String value970p = "";
    
    String allValue970 = "";
    
    String tempMarc21XML = marc21XML;
    
    while(tempMarc21XML.indexOf("marc:datafield tag=\"970")>-1)
    {
       value970 = "";
       value970l = "";
       value970t = "";
       value970c = "";
       value970p = "";

    
       field970 = tempMarc21XML.substring(tempMarc21XML.indexOf("marc:datafield tag=\"970"));
       
       field970 = field970.substring(0,field970.indexOf("/marc:datafield"));

       tempMarc21XML = tempMarc21XML.substring(tempMarc21XML.indexOf("marc:datafield tag=\"970"));
       
       tempMarc21XML = tempMarc21XML.substring(field970.indexOf("/marc:datafield>")+16);



       System.out.println(field970+"\n");


       tempValue = field970;
       
       while(tempValue.indexOf("subfield code=\"l\">")>-1)
       {
        tempValue = tempValue.substring(tempValue.indexOf("subfield code=\"l\">")+18);
        tempValue = tempValue.substring(0,tempValue.indexOf("<"));
        value970l +=tempValue;
       }

       tempValue = field970;

       while(tempValue.indexOf("subfield code=\"t\">")>-1)
       {
        tempValue = tempValue.substring(tempValue.indexOf("subfield code=\"t\">")+18);
        tempValue = tempValue.substring(0,tempValue.indexOf("<"));
        value970t +=tempValue;
       }

       tempValue = field970;

       while(tempValue.indexOf("subfield code=\"c\">")>-1)
       {
        tempValue = tempValue.substring(tempValue.indexOf("subfield code=\"c\">")+18);
        tempValue = tempValue.substring(0,tempValue.indexOf("<"));
        value970c +=tempValue;
       }

       tempValue = field970;

       while(tempValue.indexOf("subfield code=\"p\">")>-1)
       {
        tempValue = tempValue.substring(tempValue.indexOf("subfield code=\"p\">")+18);
        tempValue = tempValue.substring(0,tempValue.indexOf("<"));
        value970p +=tempValue;
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

       
       allValue970 = allValue970 + "<tableOfContents displayLabel=\"Chapters included in book\">" + value970 + "</tableOfContents>\n";

     }

    
    
    
    
    
    /*
    FileOutputStream fosout = new FileOutputStream(args[2]);
    OutputStreamWriter oswout = new OutputStreamWriter(fosout,"UTF-8");
    BufferedWriter bwout = new BufferedWriter(oswout);
    
    bwout.write(cmarcunicode);
    bwout.flush();
    
    
    
    FileOutputStream fosout1 = new FileOutputStream("altRepGroup.txt");
    OutputStreamWriter oswout1 = new OutputStreamWriter(fosout1,"UTF-8");
    BufferedWriter bwout1 = new BufferedWriter(oswout1);
    
    for(int i=0;i<tempList.size();i++)
    {
     bwout1.write(tempList.get(i));
     bwout1.flush();
    }
    */
    //FileOutputStream fosout2 = new FileOutputStream(args[2].substring(0,args[2].indexOf(".x"))+"_new.xml");
    FileOutputStream fosout2 = new FileOutputStream(args[2]);
    OutputStreamWriter oswout2 = new OutputStreamWriter(fosout2,"UTF-8");
    BufferedWriter bwout2 = new BufferedWriter(oswout2);
    
    String type = "";
    String altRepGroup = "";
    String script = "";

    String type1 = "";
    String altRepGroup1 = "";
    String script1 = "";

    String tempLine = "";
    
    String locationXMLString = "<location>"+"\n";
    locationXMLString += "<physicalLocation>" + "</physicalLocation>" +"\n";
    locationXMLString += value090+"\n";
    locationXMLString += value907+"\n";
    locationXMLString += "</location>"+"\n";

    int locationWritten = 0;
    int value970Written = 0;
    
    for(int i=0;i<lines.length-2;i++)
    {
     if(lines[i].indexOf("altRepGroup")>-1)
     {
      tempLine = lines[i];
     
      type = lines[i].substring(lines[i].indexOf("type=\"")+7);
      type = type.substring(0,type.indexOf("\""));
      
      altRepGroup = lines[i].substring(lines[i].indexOf("altRepGroup=\"")+13);
      altRepGroup = altRepGroup.substring(0,altRepGroup.indexOf("\""));

      script = lines[i].substring(lines[i].indexOf("script=\"")+8);
      script = script.substring(0,script.indexOf("\""));
      
      
      
      for(int j=0;j<tempList.size();j++)
      {
       type1 = tempList.get(j).substring(tempList.get(j).indexOf("type=\"")+7);
       type1 = type1.substring(0,type1.indexOf("\""));
      
       altRepGroup1 = tempList.get(j).substring(tempList.get(j).indexOf("altRepGroup=\"")+13);
       altRepGroup1 = altRepGroup1.substring(0,altRepGroup1.indexOf("\""));

       script1 = tempList.get(j).substring(tempList.get(j).indexOf("script=\"")+8);
       script1 = script1.substring(0,script1.indexOf("\""));



       if(type.equals(type1)&&altRepGroup.equals(altRepGroup1)&&!script.equals(script1))
       {
         tempLine = tempList.get(j);
       }
       
       
      }
      
      
      bwout2.write(tempLine + "\n");
      bwout2.flush();
      
      
     }
     else
     {
      if(lines[i].startsWith("</location>"))
      {
       bwout2.write(lines[i]+"\n");
       bwout2.write(locationXMLString);
       bwout2.flush();
       locationWritten = 1;
      }
      else
      {
       if(lines[i].startsWith("</tableOfContents>"))
       {
        bwout2.write(lines[i]+"\n");
        bwout2.write(allValue970);
        bwout2.flush();
        value970Written = 1;
       }
       else
       {
        bwout2.write(lines[i]+"\n");
        bwout2.flush();
       }
      }
     }
     
     
    }
    
    if(locationWritten==0)
    {
     bwout2.write(locationXMLString);
    }
    if(value970Written==0)
    {
     bwout2.write(allValue970);
    }
    
    bwout2.write(lines[lines.length-2]+"\n");
    bwout2.write(lines[lines.length-1]+"\n");
    bwout2.flush();

    
    
  }
}