package com.bac.bacbackend.data.datasource.scraperUtils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewsMinerMain {
    private static final int threadCount = 5;
    private static final String url = "https://www.reuters.com/world/";
    /**
     * TODO: Rydde opp i den driten her
     *
     * @param args
     */
    public static void main(String[] args) {
        WebCrawler webCrawler = new WebCrawler();
        List<String> articles = webCrawler.startCrawling(10, url);

        System.out.println("Sending articles to scraper");
        startScraping(articles);
        System.out.println("Scraping fullført.");
    }


    private static void startScraping(List<String> articles) {

            ExecutorService executor = Executors.newFixedThreadPool(NewsMinerMain.threadCount);

            for (String url : articles) {
                executor.submit(new Scraper(url));
            }
            executor.shutdown();
            while (!executor.isTerminated()) {
            }
    }
}


