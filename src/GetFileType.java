import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.Enumeration;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
 
public class GetFileType {
	private static JTextField txtDir1;
	private static JTextField txtDir2;
	private static JTable table;
	
	public static void main(String[] args)
    {
		
		 JFrame f = new JFrame("A JFrame");
		 f.setResizable(false);
	      f.setSize(1280, 720);
	      f.setLocation(300,200);
	      f.getContentPane().setLayout(null);
	      
	      JPanel panel = new JPanel();
	      panel.setBounds(0, 0, 1274, 682);
	      f.getContentPane().add(panel);
	      panel.setLayout(null);
	      
	      JLabel lblDir1 = new JLabel("Directory 1:");
	      lblDir1.setFont(new Font("Tahoma", Font.BOLD, 18));
	      lblDir1.setBounds(12, 13, 120, 22);
	      panel.add(lblDir1);
	      
	      JLabel lblDir2 = new JLabel("Directory 2:");
	      lblDir2.setFont(new Font("Tahoma", Font.BOLD, 18));
	      lblDir2.setBounds(12, 90, 120, 22);
	      panel.add(lblDir2);
	      
	      txtDir1 = new JTextField();
	      txtDir1.setFont(new Font("Tahoma", Font.PLAIN, 18));
	      txtDir1.setBounds(12, 49, 1250, 28);
	      panel.add(txtDir1);
	      txtDir1.setColumns(10);
	      
	      txtDir2 = new JTextField();
	      txtDir2.setFont(new Font("Tahoma", Font.PLAIN, 18));
	      txtDir2.setColumns(10);
	      txtDir2.setBounds(12, 126, 1250, 28);
	      panel.add(txtDir2);
	      
	      JButton btnGenerate = new JButton("Generate");
	      btnGenerate.addMouseListener(new MouseAdapter() {
	      	@Override
	      	public void mouseClicked(MouseEvent e) {
	      		String dir1 = txtDir1.getText();
	      		String dir2 = txtDir2.getText();
					try {
						findAllFiles1(dir1);
					} catch (IOException e1) {
						System.out.println("Error on Mouse Clicked - findAllFiles1");
					}
					try {
						findAllFiles2(dir2);
					} catch (IOException e1) {
						System.out.println("Error on Mouse Clicked - findAllFiles2");
					}			
	      	}
	      });
	     
	      btnGenerate.setFont(new Font("Tahoma", Font.BOLD, 20));
	      btnGenerate.setBounds(532, 167, 146, 54);
	      panel.add(btnGenerate);
	      
	      JScrollPane scrollPane = new JScrollPane();
	      scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	      scrollPane.setBounds(12, 253, 1250, 402);
	      panel.add(scrollPane);
    
	      table = new JTable();
	      table.setModel(new DefaultTableModel(
	      	new Object[][] {
	      	},
	      	new String[] {
	      		
	      	}
	      ));
	      scrollPane.setViewportView(table);
	      f.setVisible(true);

    }
	
	public static void findAllFiles1 (String dir1)throws IOException{

	        File f = new File(dir1);
	        File[] fl = f.listFiles();
	        
	        for (int i = 0; i < fl.length; i++) {
	            if (fl[i].isDirectory() && !fl[i].isHidden()) {
	            	System.out.println( );
	                System.out.println("Folder: " + fl[i].getAbsolutePath());
	                findAllFiles1(fl[i].getAbsolutePath());
	            } else{
	            	Date dateModified = new Date(fl[i].lastModified());
	    			SimpleDateFormat df = new SimpleDateFormat ("MM/dd/yyyy  hh:mm:ss a");
	    			System.out.println("Filename: " + fl[i].getName() + " Size: " + fl[i].length() + " Date Modified: " + df.format(dateModified));
	                if (fl[i].getName().toLowerCase().endsWith(".zip")){
	                	ZipFile zipFile = new ZipFile(fl[i].getAbsolutePath().toString().replace("\\", "\\\\"));
	                	Enumeration<?> enu = zipFile.entries();
						System.out.println( );
						while (enu.hasMoreElements()) {
							ZipEntry zipEntry = (ZipEntry) enu.nextElement();
							Date zipDateModified = new Date(zipEntry.getTime());
							System.out.println("Filename: " + zipEntry.getName() + " Size: " + zipEntry.getSize() + " Date Modified: " + df.format(zipDateModified));
						}	System.out.println( );
	                 } 
	              }
	        }
	}
	
	
	public static void findAllFiles2 (String dir2)throws IOException{

        File f = new File(dir2);
        File[] fl = f.listFiles();
        
        for (int i = 0; i < fl.length; i++) {
            if (fl[i].isDirectory() && !fl[i].isHidden()) {
            	System.out.println( );
                System.out.println(fl[i].getAbsolutePath());
                String path = fl[i].getAbsolutePath();
                findAllFiles2("Folder: " + fl[i].getAbsolutePath());
            } else{
            	Date dateModified = new Date(fl[i].lastModified());
    			SimpleDateFormat df = new SimpleDateFormat ("MM/dd/yyyy  hh:mm:ss a");
    			String filename1 = fl[i].getName();
    			long size1 = fl[i].length();
    			String date1 = df.format(dateModified);
    			System.out.println("Filename: " + fl[i].getName() + " Size: " + fl[i].length() + " Date Modified: " + df.format(dateModified));
                if (fl[i].getName().toLowerCase().endsWith(".zip")){
                	ZipFile zipFile = new ZipFile(fl[i].getAbsolutePath().toString().replace("\\", "\\\\"));
                	Enumeration<?> enu = zipFile.entries();
					while (enu.hasMoreElements()) {
						ZipEntry zipEntry = (ZipEntry) enu.nextElement();
						Date zipDateModified = new Date(zipEntry.getTime());
						String filename2 = zipEntry.getName();
		    			long size2 = zipEntry.getSize();
		    			String date2 = df.format(zipDateModified);
						System.out.println("Filename: " + zipEntry.getName() + " Size: " + zipEntry.getSize() + " Date Modified: " + df.format(zipDateModified));
					}	System.out.println( );
                 } 
              }
        }
	}
	
}