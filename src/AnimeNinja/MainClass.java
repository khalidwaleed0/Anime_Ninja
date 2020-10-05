package AnimeNinja;

import java.awt.Desktop;
import java.awt.EventQueue;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MainClass {
	
	public static void main(String[] args) {
		OsDetector.detectAndSetup();
		checkUpdates();
		AppSetup appSetup = new AppSetup();
		appSetup.checkInstallationDirectory();
		if(appSetup.checkChromeInstallation() == false)
			System.exit(0);
		if(appSetup.checkChromeDriver() == false)
			showUpdateHelp();
		else
		{
			ScraperSetup scraperSetup = new ScraperSetup();
			Thread setupThread = new Thread(scraperSetup);
			setupThread.start();
			SplashScreen ss = new SplashScreen();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e3) {
				e3.printStackTrace();
			}
			if(scraperSetup.isSessionCreated)
			{
				ss.downloadLocation();
				Downloader dl = new Downloader();
				Thread dlThread = new Thread(dl);
				dlThread.start();
				ss.loadingScreen();
				showGUI();
			}
			else
				showUpdateHelp();
		}

	}

	private static void showGUI()
	{
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e2) {
			e2.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui frame = new Gui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	private static void checkUpdates()
	{
		try {
			Updater.update();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error during update\nPlease download the latest version manually");
			try {
				Desktop.getDesktop().browse(new URI("https://github.com/khalidwaleed0/Anime_Ninja/releases"));
			} catch (IOException | URISyntaxException e1) {
				e1.printStackTrace();
			}
		}
	}
	public static void showUpdateHelp()
	{
		try {
			Runtime.getRuntime().exec("taskkill /F /IM"+" "+OsDetector.driverName);
			File chromeDriver = new File(OsDetector.installationDirectory+File.separator+OsDetector.driverName);
			chromeDriver.delete();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UpdateHelp uh = new UpdateHelp();
					uh.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
