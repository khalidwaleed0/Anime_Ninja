package AnimeNinja;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Font;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class updater {
	
	private static JLabel lblDownloadInfo;
	private static JDialog dialog;
	
	protected static void update() throws IOException
	{
		Document doc = Jsoup.connect("https://github.com/khalidwaleed0/Anime_Ninja/releases").get();
		Element latestReleaseName = doc.selectFirst(".f1.flex-auto.min-width-0.text-normal a");
		if(!latestReleaseName.text().equals("1.3.0"))
		{
			showUpdateWindow();
			String whatsNew = doc.selectFirst(".markdown-body p").wholeText();
			String latestReleaseLink = "https://github.com/"+doc.selectFirst(".d-flex.flex-items-center.min-width-0").attr("href");
			String fullSize = doc.selectFirst(".pl-2.text-gray.flex-shrink-0").text();
			File downloadedFile = new File(System.getProperty("user.home")+"\\Desktop\\Anime.Ninja(Latest).exe");
			try (BufferedInputStream inputStream = new BufferedInputStream(new URL(latestReleaseLink).openStream());
				FileOutputStream fileOS = new FileOutputStream(downloadedFile)) {
					byte data[] = new byte[1024];
					int byteContent;
					while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
						fileOS.write(data, 0, byteContent);
						System.out.println("downloaded : " + String.format("%.2f", (float)downloadedFile.length()/(1024*1024)) +" MB");
						lblDownloadInfo.setVisible(false);
						lblDownloadInfo.setText("Downloaded : "+ String.format("%.2f", (float)downloadedFile.length()/(1024*1024)) + " MB of "+fullSize);
						lblDownloadInfo.setVisible(true);
					}
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Error while downloading latest Version\nPlease download it manually");
					try {
						Desktop.getDesktop().browse(new URI("https://github.com/khalidwaleed0/Anime_Ninja/releases"));
					} catch (URISyntaxException e1) {
					}
				  }
				lblDownloadInfo.setText("Download Completed");
				dialog.dispose();
				JOptionPane.showMessageDialog(null, whatsNew);
				try {
					Desktop.getDesktop().open(batch());
				} catch (IOException e) {
				}
				System.exit(0);
			}
	}
	
	private static void showUpdateWindow()
	{
		dialog = new JDialog();
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
		
		lblDownloadInfo = new JLabel("Downloading");
		lblDownloadInfo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblDownloadInfo.setBounds(10, 45, 380, 21);
		contentPanel.add(lblDownloadInfo);
		
		JLabel lblTheNewVersion = new JLabel("The new Version will be on desktop and will open automatically");
		lblTheNewVersion.setBounds(10, 87, 360, 14);
		contentPanel.add(lblTheNewVersion);
	}
	
	private static File batch()
	{
		File batch = new File(System.getProperty("user.home")+"\\Desktop\\updater.bat");
		try {
			PrintWriter writer = new PrintWriter(batch);
			writer.print("@echo off\r\n" + 
					"echo installing a new version of Anime Ninja...\r\n" + 
					"timeout 2 >nul\r\n" + 
					"TASKKILL /im javaw.exe\r\n" + 
					"DEL Anime.Ninja.exe\r\n" + 
					"DEL \"Anime Ninja.exe\"\r\n" + 
					"rename Anime.Ninja(Latest).exe Anime.Ninja.exe"+"\r\n"+
					"Anime.Ninja.exe\r\n" + 
					"del updater.bat\r\n" + 
					"");
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return batch;
	}
}
