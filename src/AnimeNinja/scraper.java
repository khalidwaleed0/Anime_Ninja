package AnimeNinja;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.swing.JOptionPane;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class scraper {
	
	private static WebDriver driver;
	private static List <WebElement> seasons = new ArrayList<WebElement>();
	private static List <String> seasonLinks = new ArrayList<String>();
	private static List <File> seasonPhotos = new ArrayList<File>();
	private static List <String> similarLinks = new ArrayList<String>();
	private static List <File> similarPhotos = new ArrayList<File>();
	protected static List <WebElement> episodes;
	protected static boolean showSimilar = false;
	protected static boolean onlyOneSimilar = false;
	protected static List <String> similarTexts = new ArrayList<String>();
	
	protected static void setup()
	{
		System.setProperty("webdriver.chrome.driver", System.getenv("SystemDrive")+"\\Program Files\\Anime Ninja\\chromedriver79.exe");
		ChromeOptions chromeOptions = new ChromeOptions();
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("profile.default_content_setting_values.notifications", 2);
		chromeOptions.setExperimentalOption("prefs", chromePrefs);
	    chromeOptions.addArguments("--headless");
	    chromeOptions.addArguments("--disable-gpu");
	    chromeOptions.addArguments("--unlimited-storage");
		driver = new ChromeDriver(chromeOptions);
		driver.manage().window().setSize(new Dimension(1900,980));
	    driver.get("https://www.animesanka.net/search?q=haikyuu");
	}
	
	protected static void close()
	{
		driver.close();
		driver.quit();
	}
	
	protected static File getPhoto(int num,boolean isSimilar)
	{
		if(isSimilar)
			return similarPhotos.get(num);
		else
			return seasonPhotos.get(num);
	}
	
	protected static boolean search(String animeName)			//returns whether or not anime exists
	{
		seasons.removeAll(seasons);
		driver.get("https://www.animesanka.net/search?q="+animeName.trim().replaceAll("\\s", "+"));
		JavascriptExecutor js1 = ((JavascriptExecutor) driver);
		js1.executeScript("window.scrollTo(0, document.body.scrollHeight)");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		seasons = driver.findElements(By.cssSelector(".entry-content.rct-cards"));
		if(seasons.size() != 0)
			return true;
		else
			return false;
	}
	
	private static void clearOldData()
	{
		seasonLinks.removeAll(seasonLinks);
		seasonPhotos.removeAll(seasonPhotos);
		similarTexts.removeAll(similarTexts);
		similarLinks.removeAll(similarLinks);
		similarPhotos.removeAll(similarPhotos);
	}
	
	protected static List<String> getSeasons()						// returns number of seasons (in order) (without ova , specials)
	{
		clearOldData();
		Boolean isArabicOrder = false;
		List <String> seasonNames = new ArrayList<String>();
		for(int i=0; i<seasons.size() ; i++)									//gets seasonNames without ova,specials or movies
		{
			seasonNames.add(seasons.get(i).findElement(By.tagName("h2")).getText());
			if(seasonNames.get(i).contains("الموسم"))
				isArabicOrder = true;
			else if(seasonNames.get(i).contains("season") || seasonNames.get(i).contains("Season"))
			{}
			else if(seasonNames.get(i).contains("أوفا") || seasonNames.get(i).toLowerCase().contains("movie") || seasonNames.get(i).contains("فيلم")||
					seasonNames.get(i).contains("الخاصة")|| seasonNames.get(i).contains("الخاصه"))
			{
				seasonNames.remove(i);
				seasons.remove(i);
				i--;
			}
			else
			{
				similarTexts.add(seasonNames.get(i).replaceAll("(season \\d|FHD|BluRay|1080P|[^\\w\\s])", "").trim());
				similarLinks.add(seasons.get(i).findElement(By.className("RecentThumb")).getAttribute("href"));
				similarPhotos.add(seasons.get(i).findElement(By.className("RecentThumb")).getScreenshotAs(OutputType.FILE));
				seasons.remove(i);
				seasonNames.remove(i);
				i--;
			}
		}
		ArrayList<String> seasonOrder = orderArrange(isArabicOrder,seasonNames);
		return seasonReturn(seasonOrder);
	}

	private static ArrayList<String> orderArrange(Boolean arabicOrder,List<String> seasonNames)			// puts seasons in order because they are not ordered
	{																									// in the website
		ArrayList<String> seasonOrder;
		if(arabicOrder)
			seasonOrder = new ArrayList<String>(Arrays.asList(new String[]{"الأول","الثان","الثالث","الرابع","الخامس","السادس"}));
		else
			seasonOrder = new ArrayList<String>(Arrays.asList(new String[]{"season 1","season 2","season 3","season 4","season 5","season 6"}));
		for(int i=0 ; i<seasons.size() ; i++)
		{
			for(int j=0 ; j<seasons.size() ; j++)
			{
				if(seasonNames.get(j).toLowerCase().contains(seasonOrder.get(i)))
				{
					seasonLinks.add(seasons.get(j).findElement(By.className("RecentThumb")).getAttribute("href"));
					seasonPhotos.add(seasons.get(j).findElement(By.className("RecentThumb")).getScreenshotAs(OutputType.FILE));
					break;
				}
			}
		}
		if(arabicOrder)
			seasonOrder = new ArrayList<String>(Arrays.asList(new String[]{"season 1","season 2","season 3","season 4","season 5","season 6"}));
		return seasonOrder;
	}
	
	private static List<String> seasonReturn(ArrayList<String> seasonOrder)
	{
		if(seasons.size()!=0)
		{
			if(similarTexts.size()!=0)
			{
				similarTexts.add(0, "Choose");
				showSimilar = true;
			}
			seasonOrder.add(0,"Choose");
			return seasonOrder.subList(0, seasonLinks.size()+1);
		}
		else
		{
			if(similarTexts.size()==1)
			{
				onlyOneSimilar = true;
				similarTexts.add(0, "Choose");
				return similarTexts;
			}
			else if(similarTexts.size() > 1)
			{
				similarTexts.add(0, "Choose");
				showSimilar = true;
				return seasonOrder;
			}
			else
			{
				JOptionPane.showMessageDialog(null, "NO Result");
				return seasonOrder;	
			}
		}
	}
	
	protected static void selectSeason(int seasonNumber)
	{
		driver.get(seasonLinks.get(seasonNumber));
	}
	
	protected static void selectSimilar(int num)
	{
		driver.get(similarLinks.get(num));
	}
	
	protected static ArrayList<String> getEpisodes()		//returns an Array containing episode numbers
	{
		try {
				driver.get(driver.findElement(By.cssSelector(".tab-content-sanka.content3-sanka .ibtn.iNor.ibtn-4")).getAttribute("href"));
		}catch(Exception e) {
			
		}
		ArrayList<String> episodeNumbers = new ArrayList<String>();
		downloader.megaLinks.removeAll(downloader.megaLinks);
		episodes = driver.findElements(By.cssSelector(".selective select option"));
		Collections.reverse(episodes);
		for(int i=0 ; i<episodes.size() ; i++)
		{
			episodeNumbers.add(episodes.get(i).getAttribute("value").replaceAll("\\D+", ""));
		}
		episodeNumbers.add(0, "Choose");
		return episodeNumbers;
	}
}
