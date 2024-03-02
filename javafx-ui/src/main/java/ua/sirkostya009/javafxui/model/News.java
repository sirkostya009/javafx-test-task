package ua.sirkostya009.javafxui.model;

public record News(
        String titleUrl,
        String title,
        String authorUrl,
        String author,
        String postedAt,
        String description
) {
}
