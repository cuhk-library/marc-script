import java.io.*;
import java.util.*;
import java.lang.*;

public class GenerateBatch {

  public static void main(String args[]) throws Exception
  {
    FileInputStream fis = new FileInputStream("config.txt");
    InputStreamReader isr = new InputStreamReader(fis);
    BufferedReader br = new BufferedReader(isr);
    
    
    String marceditLocation = "";
    String marcLocation = "";
    String batchFilename = "";
    
    String str = br.readLine();
    
    while(str!=null)
    {
      if(str.startsWith("marceditLocation ")&&str.indexOf(" ")>-1)
      {
        marceditLocation = str.substring(str.indexOf(" ")+1);
      }
      
      if(str.startsWith("marcLocation ")&&str.indexOf(" ")>-1)
      {
        marcLocation = str.substring(str.indexOf(" ")+1);
      }

      if(str.startsWith("batchFilename ")&&str.indexOf(" ")>-1)
      {
        batchFilename = str.substring(str.indexOf(" ")+1);
      }

    }
    
    if(marceditLocation.trim().equals("")||marcLocation.trim().equals("")||batchFilename.trim().equals(""))
    {
      System.out.println("config file not ready, please check");
      System.exit(1);
    }
    
    FileOutputStream fos = new FileOutputStream(batchFilename);
    OutputStreamWriter osw = new OutputStreamWriter(fos);
    BufferedWriter bw = new BufferedWriter(osw);
    
    File curDir = new File(marcLocation);
    String filenames [] = getAllFiles(curDir);
    
    for(int i=0;i<filenames.length;i++)
    {
      
    }
  }
  
  private static String[] getAllFiles(File curDir)
  {
    List<String> filenamesList = new ArrayList<String>();
    File[] fileList = curDir.listFiles();
    for(File f : fileList)
    {
     if(f.isDirectory())
     {
       getAllFiles(f);
     }
     
     if(f.isFile())
     {
       filenamesList.add(curDir.toString() + "\\" + f.getName());
     }
    }
    
    String filenames [] = filenamesList.toArray(new String[filenamesList.size()]);
    
    return filenames;
  }
}