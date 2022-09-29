package com.youtube.Pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;

/**
 * All locators of YouTube web page and methods to access these elements are avaiable in this page model class.
 */
public class YouTubePage {
    WebDriver driver;
    private WebDriverWait wait60;
    JavascriptExecutor executor;
    Logger logger = LogManager.getLogger(YouTubePage.class);

    @FindBy(xpath="//a[@id='endpoint']//span[contains(text(),'Explore')]")
    WebElement explore_icon;

    @FindBy(xpath = "//div[@id='contents']//span[contains(text(),'Trending videos')]")
    WebElement trending_topics_title;

    @FindBy(xpath = "//div[@id='contents']//a[@id='video-title']")
    List<WebElement>  trending_videos_list;

    @FindBy(xpath = "//h1[contains(@class,'ytd-video-primary-info-renderer')]")
    WebElement playing_video_title;

    @FindBy(xpath = "//h2[contains(@class,'ytd-reel-player-header-renderer')]")
    WebElement playing_reel_title;

    @FindBy(xpath = "//input[@id='search']")
    WebElement text_search_box;

    @FindBy(xpath = "//*[@id='button']//*[@id='text']")
    WebElement icon_filters;

    @FindBy(xpath = "//ytd-two-column-search-results-renderer[1]//ytd-section-list-renderer[1]//ytd-item-section-renderer[1]/div[3]/ytd-video-renderer[1]/div[1]/div[1]/div[1]/div[1]/h3[1]/a[1]/yt-formatted-string[1]")
    WebElement searchVideoFirst;

    public YouTubePage(WebDriver driver){
        this.driver = driver;
        this.executor = (JavascriptExecutor) this.driver;
        this.wait60 = new WebDriverWait(driver, Duration.ofSeconds(60));
        PageFactory.initElements(driver, this);
    }

    public void clickOnExploreIcon() throws InterruptedException {
        logger.info("Clicking \"Explore\" icon");
        executor.executeScript("arguments[0].click();", explore_icon);
        wait60.until(ExpectedConditions.visibilityOf(trending_topics_title));
    }

    public String getTitleFromTrendingVideosListByIndex(int elementIndex){
        logger.info("Getting Title of \"Trending Video\" "+ (elementIndex+1) + " listed on page.. ");
        return trending_videos_list.get(elementIndex).getAttribute("title").trim();
    }

    public void clickTrendingVideoByIndex(int elementIndex)  {
        logger.info("Clicking video # " + (elementIndex+1) + " from \"Trending Videos\" list to play video..");
        trending_videos_list.get(elementIndex).click();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(90));
    }

    public String getCurrentlyPlayingVideoTitle() throws InterruptedException {
        Thread.sleep(15000);
        logger.info("Get Title of video or reel playing");
        if (playing_video_title.isDisplayed()){
            logger.info("Get Title of currently playing trending video..");
            return playing_video_title.getText().trim();
        } else {
            //wait60.until(ExpectedConditions.visibilityOf(playing_reel_title));
            playing_reel_title.isDisplayed();
            logger.info("Get Title of currently playing trending reel video..");
            return playing_reel_title.getText().trim();
        }
    }

    public String getPageTitle(){
        return driver.getTitle();
    }

    public String getPageUrl(){
        logger.info("Getting current page url from browser..");
        return driver.getCurrentUrl();
    }

    public void searchVideo(String txt) throws InterruptedException {
        logger.info("Entering search text " + txt + " and searching video..");
        wait60.until(ExpectedConditions.visibilityOf(text_search_box));
        text_search_box.click();
        text_search_box.clear();
        text_search_box.sendKeys(txt);
        logger.info(" search text typed ");
        text_search_box.sendKeys(Keys.RETURN);
        Thread.sleep(10000);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(90));
    }

    public String getTitleOfFirstSearchedVideos(){
        logger.info("Getting Title of First Video in \"Searched Videos\" list");
        return searchVideoFirst.getText().trim();
    }

    public void clickFirstVideoInSerachResultsList() {
        logger.info("Clicking first video from \"Search Results \" list to play the video..");
        executor.executeScript("arguments[0].click();", searchVideoFirst);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(90));
    }

}
