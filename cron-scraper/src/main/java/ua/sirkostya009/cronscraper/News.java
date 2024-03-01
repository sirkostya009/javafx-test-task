package ua.sirkostya009.cronscraper;

public record News(
        String titleUrl,
        String title,
        String authorUrl,
        String author,
        String postedAt,
        String description
) {
}
