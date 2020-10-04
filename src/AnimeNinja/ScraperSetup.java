package AnimeNinja;

import org.openqa.selenium.SessionNotCreatedException;

public class ScraperSetup implements Runnable {
	public boolean isSessionCreated = true;
	@Override
	public void run() {
		try{
			Scraper.setup();
		}catch(SessionNotCreatedException e) {
			isSessionCreated = false;
		}
	}
}
