package ua.sirkostya009.newsapi;

import java.time.Instant;

public record News(
        String titleUrl,
        String title,
        String authorUrl,
        String author,
        String postedAt,
        String description,
        Instant addedAt
) {
}
