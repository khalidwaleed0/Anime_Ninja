# Anime_Ninja
 A program to download 1080p anime episodes with one click(with arabic subtitles).<br/>
 supports Linux and Windows.
 <br>
 ### Program is Deprecated (as the website owner now gets the decoded url from an external source)
 ### Alternative Solution :
 1- Add ublock origin extension to your browser(firefox is preferred).
 <br>
 2- Open the dashboard in the extension and go to "My Filters".
 <br>
 3- Add these lines and press "Apply Changes".
  ```
pr.animesanka.club##^script:has-text(window.location.replace)
prwd.animesanka.club##^script:has-text(window.location.replace)
||www.gstatic.com/firebasejs/7.10.0/firebase-app.js$script,domain=pr.animesanka.club|prwd.animesanka.club
||pr.animesanka.club/p/blog-page_15.html$document,domain=pr.animesanka.club
||www.gstatic.com/firebasejs/7.10.0/firebase-analytics.js$script,domain=prwd.animesanka.club
||prwd.animesanka.club/p/blog-page_15.html$document,domain=prwd.animesanka.club

  ```
 4- Now you can browse the premium version of AnimeSanka for free [AnimeSanka](https://pr.animesanka.club/)
 <br><br>
 ![screenshot](https://user-images.githubusercontent.com/54943086/78883666-166b5680-7a5a-11ea-8e03-78b3e9cb4195.png)
### Requirements :
 1- java 11 or higher <br/>
 2- Google chrome must be installed.

### How to install :
####  Easy Way :
 * Download the exe or jar file from here [Anime Ninja](https://github.com/khalidwaleed0/Anime_Ninja/releases)
 * Follow the instructions provided by the program.
####  Hard Way :
 * If you want to run the code on your IDE or make your own jar file, you should :
   * Put chromedriver.exe in the resources.
   * add selenium libraries ,[jLayer library](http://www.javazoom.net/javalayer/sources.html) and jsoup library to the build path(project libraries).
   * Class (scraper) contains some arabic string make sure your ide uses UTF-8 so that it can read it properly.
### How does the program work ?
 It uses selenium libraries and chromedriver to scrap the website [animesanka.net](https://www.animesanka.net).
