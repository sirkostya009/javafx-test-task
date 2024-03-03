package ua.sirkostya009.newsapi;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class Controller {
    private final JdbcClient client;

    @GetMapping("/api/news")
    public List<News> fetchNews(@RequestParam(required = false) Time time) {
        var query = "SELECT * FROM news";
        if (time != null) switch (time) {
            case MORNING -> query += " WHERE HOUR(added_at) < 12";
            case DAY -> query += " WHERE HOUR(added_at) >= 12 AND HOUR(added_at) < 18";
            case EVENING -> query += " WHERE HOUR(added_at) >= 18";
        }
        return client.sql(query + " ORDER BY added_at DESC").query(News.class).list();
    }

    @PutMapping("/api/news")
    public void updateNews(@RequestParam String url, @RequestBody News news) {
        client.sql("""
                UPDATE news
                SET title = COALESCE(:title, title),
                    description = COALESCE(:description, description),
                    author = COALESCE(:author, author),
                    posted_at = COALESCE(:postedAt, posted_at),
                    author_url = COALESCE(:authorUrl, author_url)
                WHERE title_url = :url
                """)
                .param("title", news.title())
                .param("description", news.description())
                .param("author", news.author())
                .param("postedAt", news.postedAt())
                .param("authorUrl", news.authorUrl())
                .param("url", url)
                .update();
    }

    @DeleteMapping("/api/news")
    public void deleteNews(@RequestParam String url) {
        client.sql("DELETE FROM news WHERE title_url = :url")
                .param("url", url)
                .update();
    }
}
