import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.*;
import java.util.*;
import java.lang.*;



public class MarcEditFolder
{
  public static void main(String args[]) throws FileNotFoundException, IOException    
  {
    
   File curDir = new File(args[0]);
   String filenames [] = getAllMRCFiles(curDir);
  
   //System.out.println("filenames length " + filenames.length);
  
  
   Process tr ;
   BufferedReader br;
   String line = "";
  
   for(int k=0;k<filenames.length;k++)
   {
    System.out.println("\"..\\MarcEdit 5.0\\cmarcedit.exe\" -s " + filenames[k].substring(0,filenames[k].indexOf(".")) + ".mrc -d " + filenames[k].substring(0,filenames[k].indexOf(".")) + "_marc21.xml -marcxml");
    tr = Runtime.getRuntime().exec("\"..\\MarcEdit 5.0\\cmarcedit.exe\" -s " + filenames[k].substring(0,filenames[k].indexOf(".")) + ".mrc -d " + filenames[k].substring(0,filenames[k].indexOf(".")) + "_marc21.xml -marcxml");
    br = new BufferedReader(new InputStreamReader(tr.getInputStream()));
    while((line = br.readLine())!=null)
    {
      System.out.println(line);
    }
   }
  }
  
  private static String[] getAllMRCFiles(File curDir)
  {
    List<String> filenamesList = new ArrayList<String>();
    File[] fileList = curDir.listFiles();
    for(File f : fileList)
    {
     if(f.isDirectory())
     {
       getAllMRCFiles(f);
     }
     
     if(f.isFile())
     {
       if(f.getName().indexOf(".mrc")>-1)
       {
        filenamesList.add(curDir.toString() + "\\" + f.getName());
       }
     }
    }
    
    String filenames [] = filenamesList.toArray(new String[filenamesList.size()]);
    
    return filenames;
  }

}
