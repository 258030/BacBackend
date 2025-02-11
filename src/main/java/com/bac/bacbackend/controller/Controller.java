package com.bac.bacbackend.controller;

import com.bac.bacbackend.data.scraper.WebCrawler;
import com.bac.bacbackend.domain.model.Article;
import com.bac.bacbackend.data.scraper.Bot;
import com.bac.bacbackend.data.repository.ArticleRepository;
import com.bac.bacbackend.domain.service.OpenAi;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class Controller {

    private final Bot bot;
    private final ArticleRepository repo;
    private final OpenAi ai;
    private final WebCrawler webCrawler;
    @Value("${ai.command.city}")
    private String coCity;
    @Value("${ai.command.category}")
    private String coCategory;
    @Value("${ai.summary}")
    private String coSummary;

    private Controller (Bot bot, ArticleRepository repo, OpenAi ai, WebCrawler webCrawler) {
        this.bot = bot;
        this.repo = repo;
        this.ai = ai;
        this.webCrawler = webCrawler;
    }

    @RequestMapping("/")
    public String index() { return "Application is running"; }

    @RequestMapping("/read")
    public String read() { return repo.findById("1").toString(); }

    @RequestMapping("/save")
    public String save() {
        Article a = new Article();
        a.setSourceName("a jerk");
        a.setId("www.yourmom.com");
        a.setTitle("nice");
        a.setImgUrl("www.nty.io");
        repo.save(a);
        return a.toString();
    }

    @RequestMapping("/start")
    public String start() {
        bot.start();
        return "Scraping started";
    }

    @RequestMapping("/ai")
    public String oai() {
        System.out.println(ai.prompt(coCategory, coSummary));
        return "Ai started";
    }

    @RequestMapping ("/crawl")
    public String crawl() {
        String url = "https://www.reuters.com/world/";
        webCrawler.startCrawling(1, url);
        return "Crawling started";
    }


    @GetMapping("/news")
    public List<Article> getNews() {
     return (List<Article>) repo.findAll();
    }

    @GetMapping("/location")
    public List<LocationResponse> getLocations() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        File file = Paths.get("src/main/java/com/bac/bacbackend/repository/Markers.JSON").toFile();

        return objectMapper.readValue(file, new TypeReference<>() {});
    }

    public static class LocationResponse {
        private double x;
        private double y;
        private String html;

        public LocationResponse() {}

        public double getX() { return x; }
        public void setX(double x) { this.x = x; }

        public double getY() { return y; }
        public void setY(double y) { this.y = y; }

        public String getHtml() { return html; }
        public void setHtml(String html) { this.html = html; }
    }
}