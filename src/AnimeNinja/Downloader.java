package AnimeNinja;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import javax.swing.filechooser.FileSystemView;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebDriver;

public class Downloader implements Runnable{

	protected static WebDriver driver2;
	private static HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
	protected static ArrayList<String> megaLinks = new ArrayList<String>();
	protected static ArrayList<String> driveLinks = new ArrayList<String>();
	protected static ArrayList<String> zippyShareLinks = new ArrayList<String>();
	private TrayIcon trayIcon;
	private static File defaultDir;
	@Override
	public void run()
	{
		setup();
		if(defaultDir.exists())
			removeOldUncompletedDownloads();
		else
			defaultDir.mkdir();		
		showTrayIcon();
		megaDownloader();
	}
	
	private void zippyShareDownloader()
	{
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		if(zippyShareLinks.size() != 0)
		{
			Gui.lblNewLabel_7.setText("Trying zippyshare Link...");
			guiRefresh();
			driver2.get(zippyShareLinks.get(0));
			zippyShareLinks.remove(0);
			try {
				String epName = driver2.findElement(By.cssSelector(".center font:nth-of-type(3)")).getText().replaceAll("\\[AnimeSanka.com]\\s|\\s\\(.+", "");
				String fullSize = driver2.findElement(By.cssSelector(".center font:nth-of-type(5)")).getText();
				driver2.get(driver2.findElement(By.cssSelector("#dlbutton")).getAttribute("href"));
				Gui.lblNewLabel_3.setVisible(false);
				Gui.lblNewLabel_3.setText("epName : "+epName);
				Gui.lblNewLabel_3.setVisible(true);
				File tmpDownload = getTempDownloadFile();
				double downloadInfo;
				while(true)
				{
					downloadInfo = tmpDownload.length()/(1024*1024);
					if (tmpDownload.exists())
					{
						Gui.lblNewLabel_7.setText(String.valueOf(downloadInfo)+" MB of "+fullSize);
						Gui.lblNewLabel_5.setText("Files left:"+driveLinks.size());
						guiRefresh();
					}
					else
					{
						Gui.lblNewLabel_7.setText("Download Completed");
						guiRefresh();
						notifier("Download Completed",epName);
						break;
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}catch(Exception e) {
				Gui.lblNewLabel_7.setText("Failed to download EP "+(Gui.selectedEpisode+1)+" (Old Links)");
				guiRefresh();
				notifier("Download Failed (Old Links)","EP "+ Gui.selectedEpisode+1);
			}	
		}
	}
	
	private void driveDownloader()
	{
		String epName = null;
		String fullSize = "(full size is unknown)";
		if(driveLinks.size() != 0)
		{
			Gui.lblNewLabel_7.setText("Trying Google Drive Link...");
			guiRefresh();
			driver2.get(driveLinks.get(0));
			driveLinks.remove(0);
			try {
				driver2.findElement(By.tagName("p"));
				try {
					if(driver2.getTitle().contains("Google Drive"))
					{
						driver2.findElement(By.id("uc-download-link")).click();
						String nameAndSize = driver2.findElement(By.cssSelector(".uc-name-size")).getText();
						fullSize = nameAndSize.replaceAll("(.+\\()", "").replaceAll("\\)", "B");
						epName = nameAndSize.replaceAll("\\[[^]]+\\]", "");
					}
					else
						epName = getTempDownloadFile().getName().replaceAll("\\[[^]]+\\]|\\.crdownload", "");
					Gui.lblNewLabel_3.setVisible(false);
					Gui.lblNewLabel_3.setText("epName : "+epName);
					Gui.lblNewLabel_3.setVisible(true);
				}catch(Exception e) {
					zippyShareDownloader();
				}
			}catch(Exception e) {
				zippyShareDownloader();
				return;
			}
			File tmpDownload = getTempDownloadFile();
			double downloadInfo;
			while(true)
			{
				downloadInfo = tmpDownload.length()/(1024*1024);
				if (tmpDownload.exists())
				{
					Gui.lblNewLabel_7.setText(String.valueOf(downloadInfo)+" MB of "+fullSize);
					Gui.lblNewLabel_5.setText("Files left:"+driveLinks.size());
					guiRefresh();
				}
				else
				{
					Gui.lblNewLabel_7.setText("Download Completed");
					guiRefresh();
					notifier("Download Completed",epName);
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private File getTempDownloadFile()
	{
		File tempDownload = null;
		boolean found = false;
		while(!found)
		{
			File[] files = defaultDir.listFiles();
			for(int i=0 ; i<files.length ; i++)
			{
				if(files[i].getName().contains("crdownload"))
				{
					tempDownload = files[i];
					found = true;
					break;
				}
			}
		}
		return tempDownload;
	}
	
	private void removeOldUncompletedDownloads()
	{
		File[] files = defaultDir.listFiles();
		for(int i=0 ; i<files.length ; i++)
		{
			if(files[i].getName().contains("crdownload"))
				files[i].delete();
		}
	}
	
	private void megaDownloader()
	{
		String epName = "";
		while(true)
		{
			if(megaLinks.size()!=0)
			{
				Gui.lblNewLabel_7.setText("Trying mega.nz Link...");
				guiRefresh();
			    driver2.get(megaLinks.get(0));
				megaLinks.remove(0);

			    try {
			    	WebDriverWait wait = new WebDriverWait(driver2, 15);
					wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading")));
				    driver2.findElement(By.cssSelector(".download.big-button.button.download-file.green.transition span")).click();
				}catch(Exception e) {
					driveDownloader();
					continue;
				}
				while(true)
				{
					if(!(driver2.findElement(By.cssSelector(".download.progress-bar")).getAttribute("style").contains("100%")))
					{
						try {
							driver2.findElement(By.cssSelector(".top-login-popup.sign.fm-dialog.pro-register-dialog.hidden"));
						}catch(Exception e) {
							Gui.lblNewLabel_7.setText("Mega.nz reached download limit,please wait...");
							guiRefresh();
						    driver2.close();
						    driver2.quit();
						    setup();
							while(true)
							{
								driveDownloader();
							}
						}
						try {
							String speedInfo = driver2.findElement(By.cssSelector(".dark-numbers")).getText()
									+" "+driver2.findElement(By.cssSelector(".light-txt")).getText();
							epName = driver2.findElement(By.cssSelector(".download.bar-filename")).  ////*without website name*////
									getAttribute("title").replaceAll("\\[[^]]+\\]", "");
							String sizeInfo = driver2.findElement(By.cssSelector(".download.bar-filesize")).getText().replaceFirst("B", "B of ");
							Gui.lblNewLabel_3.setVisible(false);
							Gui.lblNewLabel_3.setText("epName : "+epName);
							Gui.lblNewLabel_3.setVisible(true);
							Gui.lblNewLabel_7.setText(speedInfo+" - "+sizeInfo);
							Gui.lblNewLabel_5.setText("Files left: "+megaLinks.size());
							guiRefresh();
						}catch(Exception e) {
							finishingDownload();
							notifier("Download Completed",epName);
							driveLinks.remove(0);
							break;
						}
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
					else
					{
						finishingDownload();
						notifier("Download Completed",epName);
						driveLinks.remove(0);
						break;
					}
				}
			}else if(megaLinks.size() == 0 && driveLinks.size() != 0)
			{
				driveDownloader();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void guiRefresh()
	{
		Gui.lblNewLabel_7.setVisible(false);
		Gui.lblNewLabel_7.setVisible(true);
		Gui.lblNewLabel_5.setVisible(false);
		Gui.lblNewLabel_5.setVisible(true);
	}

	private void finishingDownload()
	{
		for(int i=13 ; i>=0 ; i--)
		{
			Gui.lblNewLabel_7.setText("finishing download in "+i+" seconds");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			guiRefresh();
		}
		Gui.lblNewLabel_7.setText("Download Completed");
		guiRefresh();
	}

	private void notifier(String downloadState ,String epName)
	{
		try{
		    trayIcon.displayMessage(downloadState,epName, MessageType.NONE);
		}catch(Exception ex){
		    ex.printStackTrace();
		}
	}

	private void showTrayIcon()
	{
		SystemTray tray = SystemTray.getSystemTray();
	    Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/resources/appIcon.png"));
	    trayIcon = new TrayIcon(image, "Java AWT Tray Demo");
	    trayIcon.setImageAutoSize(true);
	    trayIcon.setToolTip("Anime Ninja");
	    try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	private void setup()
	{
		ChromeOptions chromeOptions = new ChromeOptions();
		System.setProperty("webdriver.chrome.driver", OsDetector.installationDirectory +File.separator+OsDetector.driverName);
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("profile.default_content_setting_values.notifications", 2);
		chromePrefs.put("safebrowsing.enabled", "false");
		chromePrefs.put("download.prompt_for_download", "false");
		setDownloadLocation();
		chromeOptions.setExperimentalOption("prefs", chromePrefs);
	    chromeOptions.addArguments("--headless");
	    chromeOptions.addArguments("--disable-gpu");
	    chromeOptions.addArguments("--unlimited-storage");
		driver2 = new ChromeDriver(chromeOptions);
	    driver2.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
	}

	protected static void setDownloadLocation()
	{
		FileReader reader;
		defaultDir = new File(FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath()+File.separator+"Anime Ninja");
		try {
			reader = new FileReader(new File(OsDetector.installationDirectory +File.separator+"Download Location.txt"));
			BufferedReader br = new BufferedReader(reader);
			defaultDir = new File(br.readLine());
			br.close();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		chromePrefs.put("download.default_directory", defaultDir.getAbsolutePath());
	}

	protected static void close()
	{
		driver2.close();
		driver2.quit();
	}
}
