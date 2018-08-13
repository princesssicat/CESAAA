import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
 

import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

public class Compare {
	private static JTextField txtDir1;
	private static JTextField txtDir2;
	public static List<String> allZipFiles = new ArrayList<String>();
    public static String fileExtension = ".zip";
	/**
	 * @wbp.parser.entryPoint
	 */
    
  //This can be any folder locations which you want to compare
  		File dir1 = new File(txtDir1.toString());
  		File dir2 = new File(txtDir2.toString());
  		public static void main(String ...args) 
  		{
  			
  		// TODO Auto-generated method stub

  			
  		  JFrame DialogBox1 = new JFrame("A JFrame");
  		  DialogBox1.setTitle("iCRM QA Build Verifier");
  	      DialogBox1.setSize(902, 267);
  	      DialogBox1.setLocation(300,200);
  	      DialogBox1.getContentPane().setLayout(null);
  	      
  	      JLabel lblPleaseInputThe = new JLabel("Please Input the path for Current Build:");
  	      lblPleaseInputThe.setFont(new Font("Tahoma", Font.PLAIN, 16));
  	      lblPleaseInputThe.setBounds(12, 76, 297, 16);
  	      DialogBox1.getContentPane().add(lblPleaseInputThe);
  	      
  	      JLabel label = new JLabel("Please Input the path for Previous Build:");
  	      label.setFont(new Font("Tahoma", Font.PLAIN, 16));
  	      label.setBounds(12, 13, 320, 16);
  	      DialogBox1.getContentPane().add(label);
  	      
  	      txtDir1 = new JTextField();
  	      txtDir1.setBounds(12, 41, 858, 22);
  	      DialogBox1.getContentPane().add(txtDir1);
  	      txtDir1.setColumns(10);
  	      
  	      txtDir2 = new JTextField();
  	      txtDir2.setColumns(10);
  	      txtDir2.setBounds(12, 105, 858, 22);
  	      DialogBox1.getContentPane().add(txtDir2);
  	      
  	      JButton btnCompare = new JButton("Compare");

  	      btnCompare.addMouseListener(new MouseAdapter() {
  			@Override
  	      	public void mouseClicked(MouseEvent arg0) {
  				Compare compare = new Compare();
  	  			try
  	  			{
  	  				compare.getDiff(compare.dir1,compare.dir2);
  	  			}
  	  			catch(IOException ie)
  	  			{
  	  				ie.printStackTrace();
  	  			}
  	  			
  	      	}
  	      });
  	  
  	      btnCompare.setFont(new Font("Tahoma", Font.BOLD, 17));
  	      btnCompare.setBounds(12, 142, 134, 60);
  	      DialogBox1.getContentPane().add(btnCompare);
  	      DialogBox1.setVisible(true);
  	
  		}
  		
  		public void getDiff(File dirA, File dirB) throws IOException
  		{
  			File[] fileList1 = dirA.listFiles();
  			File[] fileList2 = dirB.listFiles();
  			Arrays.sort(fileList1);
  			Arrays.sort(fileList2);
  			HashMap<String, File> map1;
  			if(fileList1.length < fileList2.length)
  			{
  				map1 = new HashMap<String, File>();
  				for(int i=0;i<fileList1.length;i++)
  				{
  					map1.put(fileList1[i].getName(),fileList1[i]);
  				}
  				
  				compareNow(fileList2, map1);
  			}
  			else
  			{
  				map1 = new HashMap<String, File>();
  				for(int i=0;i<fileList2.length;i++)
  				{
  					map1.put(fileList2[i].getName(),fileList2[i]);
  				}
  				compareNow(fileList1, map1);
  			}
  		}
  		
  		public void compareNow(File[] fileArr, HashMap<String, File> map) throws IOException
  		{
  			for(int i=0;i<fileArr.length;i++)
  			{
  				String fName = fileArr[i].getName();
  				File fComp = map.get(fName);
  				map.remove(fName);
  				if(fComp!=null)
  				{
  					if(fComp.isDirectory())
  					{
  						getDiff(fileArr[i], fComp);
  					}
  					else
  					{
  						String cSum1 = checksum(fileArr[i]);
  						String cSum2 = checksum(fComp);
  						if(!cSum1.equals(cSum2))
  						{
  							System.out.println(fileArr[i].getName()+"\t\t"+ "different");
  						}
  						else
  						{
  							System.out.println(fileArr[i].getName()+"\t\t"+"identical");
  						}
  					}
  				}
  				else
  				{
  					if(fileArr[i].isDirectory())
  					{
  						traverseDirectory(fileArr[i]);
  					}
  					else
  					{
  						System.out.println(fileArr[i].getName()+"\t\t"+"only in "+fileArr[i].getParent());
  					}
  				}
  			}
  			Set<String> set = map.keySet();
  			Iterator<String> it = set.iterator();
  			while(it.hasNext())
  			{
  				String n = it.next();
  				File fileFrmMap = map.get(n);
  				map.remove(n);
  				if(fileFrmMap.isDirectory())
  				{
  					traverseDirectory(fileFrmMap);
  				}
  				else
  				{
  					System.out.println(fileFrmMap.getName() +"\t\t"+"only in "+ fileFrmMap.getParent());
  				}
  			}
  		}
  		
  		public void traverseDirectory(File dir)
  		{
  			File[] list = dir.listFiles();
  			for(int k=0;k<list.length;k++)
  			{
  				if(list[k].isDirectory())
  				{
  					traverseDirectory(list[k]);
  				}
  				else
  				{
  					System.out.println(list[k].getName() +"\t\t"+"only in "+ list[k].getParent());
  				}
  			}
  		}
  		
  		public String checksum(File file) 
  		{
  			try 
  			{
  			    InputStream fin = new FileInputStream(file);
  			    java.security.MessageDigest md5er = MessageDigest.getInstance("MD5");
  			    byte[] buffer = new byte[1024];
  			    int read;
  			    do 
  			    {
  			    	read = fin.read(buffer);
  			    	if (read > 0)
  			    		md5er.update(buffer, 0, read);
  			    } while (read != -1);
  			    fin.close();
  			    byte[] digest = md5er.digest();
  			    if (digest == null)
  			      return null;
  			    String strDigest = "0x";
  			    for (int i = 0; i < digest.length; i++) 
  			    {
  			    	strDigest += Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1).toUpperCase();
  			    }
  			    return strDigest;
  			} 
  			catch (Exception e) 
  			{
  			    return null;
  			}
  		}
  	
  }	
