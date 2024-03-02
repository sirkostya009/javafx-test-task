package ua.sirkostya009.javafxui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import lombok.RequiredArgsConstructor;
import ua.sirkostya009.javafxui.model.News;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class EditController {
    private final News news;
    private final Consumer<News> onSave;
    private final Consumer<News> onDelete;
    private final Consumer<Void> onCancel;

    @FXML
    private TextField titleLabel;
    @FXML
    private Text authorLabel;
    @FXML
    private Text postedAtLabel;
    @FXML
    private TextField descriptionLabel;

    public void initialize() {
        titleLabel.setText(news.title());
        authorLabel.setText(news.author());
        postedAtLabel.setText(news.postedAt());
        descriptionLabel.setText(news.description());
    }

    public void save() {
        onSave.accept(new News(
                this.news.titleUrl(),
                titleLabel.getText(),
                this.news.authorUrl(),
                this.news.author(),
                this.news.postedAt(),
                descriptionLabel.getText()
        ));
    }

    public void delete() {
        onDelete.accept(news);
    }

    public void cancel() {
        onCancel.accept(null);
    }
}
