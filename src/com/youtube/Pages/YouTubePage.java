package com.youtube.Pages;

import com.youtube.Tests.VerifyYouTubeVideo;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;

/**
 * All locators of YouTube web page.
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

    public YouTubePage(WebDriver driver){
        this.driver = driver;
        this.executor = (JavascriptExecutor) this.driver;
        this.wait60 = new WebDriverWait(driver, Duration.ofSeconds(60));
        PageFactory.initElements(driver, this);
    }

    public void clickOnExploreIcon() throws InterruptedException {
        logger.info("Clicking \"Explore\" icon");
        executor.executeScript("arguments[0].click();", explore_icon);
    }

    public String getTitleFromTrendingVideosListByIndex(int elementIndex){
        wait60.until(ExpectedConditions.visibilityOf(trending_topics_title));
        logger.info("Get Title of second video under \"Trending Videos\".. ");
        return trending_videos_list.get(elementIndex).getAttribute("title").trim();
    }

    public void clickTrendingVideosByIndex(int elementIndex){
        logger.info("Clicking Second Video under \"trending Videos\" section..");
        trending_videos_list.get(elementIndex).click();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(90));
    }

    public String getCurrentlyPlayingVideoTitle(){
        wait60.until(ExpectedConditions.visibilityOf(playing_video_title));
        logger.info("Get Title of currently playing trending video..");
        return playing_video_title.getText().trim();
    }

    public String getPageTitle(){
        return driver.getTitle();
    }

    public String getPageUrl(){
        logger.info("Getting current page url from browser..");
        return driver.getCurrentUrl();
    }
}
