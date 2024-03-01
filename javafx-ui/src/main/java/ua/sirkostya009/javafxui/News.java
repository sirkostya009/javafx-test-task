package ua.sirkostya009.javafxui;

public record News(
        String titleUrl,
        String title,
        String authorUrl,
        String author,
        String postedAt,
        String description
) {
}
