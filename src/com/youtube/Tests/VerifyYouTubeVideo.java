package com.youtube.Tests;

import com.youtube.Pages.YouTubePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
import org.testng.asserts.SoftAssert;

public class VerifyYouTubeVideo {
    WebDriver driver;
    Logger logger = LogManager.getLogger(VerifyYouTubeVideo.class);

    /**
     * setUp() methopd initializes web driver and opens the url before every test.
     */
    @BeforeMethod
    public void setUp(){
        String driverLocation = System.getProperty("user.dir");

        System.setProperty("webdriver.gecko.driver", driverLocation+"/lib/firefoxdriver/geckodriver");

        driver = new FirefoxDriver();

        logger.info("Starting test execution..");

        driver.manage().window().maximize();
        logger.info("Browser Maximized");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(90));
        driver.get("https://www.youtube.com");
        logger.info("Opening url https://www.youtube.com");
    }

    /**
     * tearDown() method executes after every test and closes the browser window.
     */
    @AfterMethod
    public void tearDown(){
        logger.info("Closing Browser...");
        driver.quit();
    }

    /**
     * verifyTrendingVideoTest() is the test method that clicks "explore" icon in left panel,
     * Asserts "Tending videos" list is displayed, get the text of second video in "Trending Videos" list,
     * then clicks the second video in Trending videos list to play the video,
     * verifies that the playing video title matches with the one in Trending videos list,
     * captures the url of the video that is playing.
     * Then search the same title in search box and click on 1st search result video,
     * captures the url of the video that is playing and compare with earlier url. Pass test if urls are match.
     */
    @Test
    public void verifyTrendingVideoTest() throws InterruptedException {

        SoftAssert sa = new SoftAssert();

        logger.info("Starting Test : verifyTrendingVideoTest");
        YouTubePage you_tube_page = PageFactory.initElements(driver, YouTubePage.class);

        String title = you_tube_page.getPageTitle();
        Assert.assertTrue(title.equalsIgnoreCase("YouTube"),
                "Title of page was not \"YouTube\" but was "+title);
        logger.info("Page successfully loaded and title was "+title);

        try {
            you_tube_page.clickOnExploreIcon();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        String url = you_tube_page.getPageUrl();
        logger.info("Page url: "+url);
        Assert.assertTrue(url.equalsIgnoreCase("https://www.youtube.com/feed/explore"));
        logger.info("\"Explore\" icon in left panel clicked successfully!");

        String titleOfSecondVideoInList = you_tube_page.getTitleFromTrendingVideosListByIndex(1);
        logger.info("Title of second video from Trending Videos list is :  " + titleOfSecondVideoInList);
        you_tube_page.clickTrendingVideoByIndex(1);
        String playingVideoTitle = you_tube_page.getCurrentlyPlayingVideoTitle();
        logger.info("Title of currently playing video is : "+playingVideoTitle);

        Assert.assertTrue(titleOfSecondVideoInList.equalsIgnoreCase(playingVideoTitle),
                "Title of Main you tube page and playing video did not match:" + titleOfSecondVideoInList +" and " + playingVideoTitle);

        url = you_tube_page.getPageUrl();
        logger.info("URL of currently playing trending video is :  "+url);

        try {
            you_tube_page.searchVideo(titleOfSecondVideoInList);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String titleFirstVideo = you_tube_page.getTitleOfFirstSearchedVideos();
        logger.info("Title of first video from searched Videos list is :  " + titleFirstVideo);
        sa.assertEquals(titleOfSecondVideoInList.equalsIgnoreCase(titleFirstVideo),
                "Title of first search result video did not match searched text : "+titleFirstVideo + " and "+titleOfSecondVideoInList);

        you_tube_page.clickFirstVideoInSerachResultsList();
        playingVideoTitle = you_tube_page.getCurrentlyPlayingVideoTitle();
        logger.info("Title of currently playing video is : "+playingVideoTitle);

        String searched_video_url = you_tube_page.getPageUrl();
        logger.info("Title of currently playing video is : "+ searched_video_url);

        Assert.assertTrue(url.equalsIgnoreCase(searched_video_url),
                "URL of trending video and URL of search top result video did not match:" + url + " and " + searched_video_url);
        logger.info("Test execution completed!");

        sa.assertAll();
    }

}