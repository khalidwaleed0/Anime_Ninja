package AnimeNinja;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Font;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Updater {
	
	private static JLabel lblDownloadInfo;
	private static JDialog dialog;
	
	protected static void update() throws IOException
	{
		Document doc = Jsoup.connect("https://github.com/khalidwaleed0/Anime_Ninja/releases").get();
		Element latestReleaseName = doc.selectFirst(".f1.flex-auto.min-width-0.text-normal a");
		if(!latestReleaseName.text().equals("v1.2.0"))
		{
			showUpdateWindow();
			String whatsNew = doc.selectFirst(".markdown-body p").wholeText();
			String latestReleaseLink = "https://github.com/"+doc.selectFirst(".Box.Box--condensed.mt-3 a[href$="+"\""+OsDetector.appName+"\""+"]")
					.attr("href");
			String fullSize = doc.selectFirst(".pl-2.text-gray.flex-shrink-0").text();
			File downloadedFile = new File(System.getProperty("user.home")
					+File.separator+"Desktop"+File.separator+OsDetector.appName.replace("Ninja","Ninja(Latest)"));
			try (BufferedInputStream inputStream = new BufferedInputStream(new URL(latestReleaseLink).openStream());
				FileOutputStream fileOS = new FileOutputStream(downloadedFile)) {
					byte data[] = new byte[1024];
					int byteContent;
					while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
						fileOS.write(data, 0, byteContent);
						lblDownloadInfo.setVisible(false);
						lblDownloadInfo.setText("Downloaded : "+ String.format("%.2f", (float)downloadedFile.length()/(1024*1024)) + " MB of "+fullSize);
						lblDownloadInfo.setVisible(true);
					}
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Error while downloading latest Version\nPlease download it manually");
					try {
						Desktop.getDesktop().browse(new URI("https://github.com/khalidwaleed0/Anime_Ninja/releases"));
					} catch (URISyntaxException e1) {
						e1.printStackTrace();
					}
				  }
				lblDownloadInfo.setText("Download Completed");
				dialog.dispose();
				JOptionPane.showMessageDialog(null, whatsNew,"What's New",JOptionPane.INFORMATION_MESSAGE);
				extractBatch();
				openBatch();
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
		lblTheNewVersion.setBounds(10, 87, 430, 14);
		contentPanel.add(lblTheNewVersion);
	}

	private static void extractBatch()
	{
		InputStream input = (MainClass.class.getResourceAsStream("/resources/"+OsDetector.updaterBatchName));
		try {
			Files.copy(input, new File(OsDetector.updaterBatchName).toPath());
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static void openBatch()
	{
		ProcessBuilder pb = new ProcessBuilder(OsDetector.runShellCommands);
		try {
			pb.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
