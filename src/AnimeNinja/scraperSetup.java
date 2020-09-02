package AnimeNinja;

import org.openqa.selenium.SessionNotCreatedException;

public class scraperSetup implements Runnable {
	public boolean isSessionCreated = true;
	@Override
	public void run() {
		try{
			scraper.setup();
		}catch(SessionNotCreatedException e) {
			isSessionCreated = false;
		}
	}
}
