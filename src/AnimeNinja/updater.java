package AnimeNinja;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class updater {
	
	private static File downloadLocation;
	
	protected static void update()
	{
		downloader.driver2.get("https://github.com/khalidwaleed0/Anime_Ninja/releases");
	    List<WebElement> releases = downloader.driver2.findElements(By.cssSelector(".pl-2.flex-auto.min-width-0.text-bold"));
	    if(releases.size() > 11)
	    {
	    	String whatsNew = downloader.driver2.findElement(By.cssSelector(".markdown-body")).getText();
	    	String fullSize = downloader.driver2.findElement(By.cssSelector(".d-flex.flex-justify-between.flex-items-center.py-1.py-md-2.Box-body.px-2 small")).getText();
	    	releases.get(0).click();
			JDialog dialog = new JDialog();
			JPanel contentPanel = new JPanel();
			dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			dialog.setTitle("New Update");
			dialog.setAlwaysOnTop(true);
			dialog.setVisible(true);
			dialog.setBounds(100, 100, 450, 151);
			dialog.getContentPane().setLayout(new BorderLayout());
			contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
			dialog.getContentPane().add(contentPanel, BorderLayout.CENTER);
			contentPanel.setLayout(null);
			dialog.setLocationRelativeTo(null);
			
			JLabel lblDownloadInfo = new JLabel("Downloading");
			lblDownloadInfo.setFont(new Font("Tahoma", Font.PLAIN, 12));
			lblDownloadInfo.setBounds(10, 45, 380, 21);
			contentPanel.add(lblDownloadInfo);
			
			JLabel lblTheNewVersion = new JLabel("The new Version will be on desktop and will open automatically");
			lblTheNewVersion.setBounds(10, 87, 360, 14);
			contentPanel.add(lblTheNewVersion);
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			File[] files = downloadLocation.listFiles();
			File tempFile = null;
			boolean found = false;
			while(!found)
			{
				for(int i=0 ; i < files.length ; i++)
				{
					if(files[i].getName().contains("crdownload"))
					{
						tempFile = files[i];
						found = true;
					}
				}
			}
			double downloadedSize;
			while(true)
			{
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				downloadedSize = tempFile.length()/(1024*1024);
				if (tempFile.exists())
				{
					lblDownloadInfo.setVisible(false);
					lblDownloadInfo.setText("Downloaded : "+downloadedSize+" MB of "+fullSize);
					lblDownloadInfo.setVisible(true);
				}
				else
				{
					lblDownloadInfo.setText("Download Completed");
					break;
				}
			}
			scraper.close();
			downloader.close();
			dialog.dispose();
			JOptionPane.showMessageDialog(null, whatsNew);
			dialog.dispose();
			try {
				Desktop.getDesktop().open(batch());
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.exit(0);
	    }
	}
	
	private static File batch()
	{
		File batch = new File("updater.bat");
		try {
			PrintWriter writer = new PrintWriter(batch);
			writer.print("@echo off\r\n" + 
					"echo installing a new version of Anime Ninja...\r\n" + 
					"timeout 2 >nul\r\n" + 
					"TASKKILL /im javaw.exe\r\n" + 
					"DEL Anime.Ninja.exe\r\n" + 
					"DEL \"Anime Ninja.exe\"\r\n" + 
					"move " +"\""+downloadLocation+"\\Anime.Ninja.exe"+"\" "+ "\"" + "%USERPROFILE%\\Desktop\\Anime.Ninja.exe" +"\"\r\n"+
					"\""+"%USERPROFILE%\\Desktop\\Anime.Ninja.exe\"\r\n" + 
					"del updater.bat\r\n" + 
					"");
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return batch;
	}
	
	protected static void getdownloadLocation(File dl)
	{
		downloadLocation = dl;
	}
}
