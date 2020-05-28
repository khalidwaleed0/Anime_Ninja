package AnimeNinja;

import java.awt.Desktop;
import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class mainClass {
	
	public static void main(String[] args) {
		checkChromeInstallation();
		File folder = new File(System.getenv("SystemDrive")+"\\Program Files\\Anime Ninja");
		File chromeDriver = new File(folder+"\\chromedriver83.exe");
		if(folder.exists())
		{
			if(!chromeDriver.exists())
				extractChromeDriver(chromeDriver);
		}
		else
		{
			folder.mkdir();
			extractChromeDriver(chromeDriver);
		}
		scraperSetup setup = new scraperSetup();
		Thread setupThread = new Thread(setup);
		setupThread.start();

		splashScreen ss = new splashScreen();
		ss.downloadLocation();
		downloader dl = new downloader();
		Thread dlThread = new Thread(dl);
		dlThread.start();
		ss.loadingScreen();
		
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e2) {
			e2.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					gui frame = new gui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private static void checkChromeInstallation()
	{
		File chromePath32 = new File("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");
		File chromePath64 = new File("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe");
		if(!(chromePath32.exists() || chromePath64.exists()))
		{
			JOptionPane.showMessageDialog(null, "This program requires Google Chrome to be installed\\nPlease install it and try again"
					, "Anime Ninja", JOptionPane.WARNING_MESSAGE);
			try {
				Desktop.getDesktop().browse(new URI("https://www.google.com/intl/en_us/chrome/"));
			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
			}
			System.exit(0);
		}
	}
	
	private static void extractChromeDriver(File chromeDriver)
	{
		InputStream input = (mainClass.class.getResourceAsStream("/chromedriver83.exe"));
  	     try {
				Files.copy(input, chromeDriver.toPath());
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}
