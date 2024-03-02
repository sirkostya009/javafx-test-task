package ua.sirkostya009.javafxui.controller;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import lombok.RequiredArgsConstructor;
import ua.sirkostya009.javafxui.model.News;

import java.util.function.Supplier;

@RequiredArgsConstructor
public class ArticleController {
    @FXML
    private Text descriptionLabel;

    @FXML
    private Text authorLabel;

    @FXML
    private Text postedAtLabel;

    @FXML
    private Text titleLabel;

    private final News news;
    private final Supplier<Void> callback;

    public void initialize() {
        titleLabel.setText(news.title());
        authorLabel.setText(news.author());
        postedAtLabel.setText(news.postedAt());
        descriptionLabel.setText(news.description());
    }

    @FXML
    private void edit() {
        callback.get();
    }
}
