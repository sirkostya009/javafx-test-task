package ua.sirkostya009.cronscraper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class DouScraper {
    private final JdbcClient client;
    private final Connection douNews = Jsoup.connect("https://dou.ua/lenta/");

    @EventListener(ApplicationReadyEvent.class)
    @Scheduled(cron = "0 */20 * * * *")
    public void job() throws IOException {
        var document = douNews.get().body();
        var articles = document.getElementsByClass("b-postcard");
        var counter = new AtomicInteger();

        articles.forEach(article -> {
            var titleAnchor = article.select("h2.title a");
            var titleUrl = titleAnchor.attr("href");
            var titleText = titleAnchor.text();

            var authorAnchor = article.select("a.author");
            var authorUrl = authorAnchor.attr("href");
            var authorName = authorAnchor.text();

            var postedAt = article.select("time").text();

            var description = article.select("p.b-typo").text();

            try {
                client.sql("""
                    INSERT INTO news (title_url, title, author_url, author, posted_at, description)
                    VALUES (:titleUrl, :title, :authorUrl, :author, :postedAt, :description)
                    """).param("titleUrl", titleUrl)
                        .param("title", titleText)
                        .param("authorUrl", authorUrl)
                        .param("author", authorName)
                        .param("postedAt", postedAt)
                        .param("description", description)
                        .update();
            } catch (DuplicateKeyException ignored) { // by design
                counter.incrementAndGet();
            } catch (Exception e) {
                log.error("Error while inserting news", e);
                counter.incrementAndGet();
            }
        });

        if (counter.get() == articles.size()) {
            log.info("No new articles");
        } else {
            log.info("Inserted {} new articles", articles.size() - counter.get());
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void cleanup() {
        client.sql("DELETE FROM news WHERE NOW() - added_at >= interval 1 day").update();
        log.info("Cleared news table");
    }
}
