package ua.sirkostya009.javafxui;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Pagination;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class MainController {
    @FXML
    private ComboBox<Time> timeBox;
    @FXML
    private Pagination pagination;

    private final String newsUrl = "http://localhost:8080/api/news";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient client = HttpClient.newHttpClient();

    private final List<News> newsList = new ArrayList<>();

    private boolean isEditing = false;

    public void initialize() throws IOException, InterruptedException {
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        pagination.setPageFactory(this::articleViewPage);
        timeBox.setItems(FXCollections.observableArrayList(Time.values()));
        requestNews();
    }

    private Node articleViewPage(int pageIndex) {
        var loader = new FXMLLoader(Main.class.getResource("article-view.fxml"));
        loader.setControllerFactory($ -> new ArticleController(newsList.get(pageIndex), () -> {
            forceRefresh();
            return null;
        }));
        try {
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Node articleEditPage(int pageIndex) {
        var loader = new FXMLLoader(Main.class.getResource("article-edit.fxml"));
        loader.setControllerFactory($ -> new EditController(newsList.get(pageIndex), news -> {
            newsList.set(pageIndex, news);
            forceRefresh();
            try {
                updateNews(news);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, news -> {
            newsList.remove(pageIndex);
            forceRefresh();
            try {
                deleteNews(news);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, i -> forceRefresh()));
        try {
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void forceRefresh() {
        isEditing = !isEditing;
        pagination.setPageFactory(isEditing ? this::articleEditPage : this::articleViewPage);
    }

    @FXML
    private void requestNews() throws IOException, InterruptedException {
        var response = client.send(
                HttpRequest.newBuilder(URI.create(newsUrl + (timeBox.getValue() != null ? "?time=" + timeBox.getValue() : ""))).GET().build(),
                HttpResponse.BodyHandlers.ofInputStream()
        );

        newsList.clear();
        newsList.addAll(objectMapper.readValue(response.body(), new TypeReference<>() {}));

        if (newsList.isEmpty()) {
            newsList.add(new News("", "", "", "", "", ""));
        }

        pagination.setPageCount(newsList.size());
        pagination.setCurrentPageIndex(0);
    }

    private void updateNews(News news) throws IOException, InterruptedException {
        client.send(
                HttpRequest.newBuilder(URI.create(newsUrl + "?url=" + news.titleUrl()))
                        .header("Content-Type", "application/json")
                        .PUT(HttpRequest.BodyPublishers.ofByteArray(objectMapper.writeValueAsBytes(news)))
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        );
    }

    private void deleteNews(News news) throws IOException, InterruptedException {
        client.send(
                HttpRequest.newBuilder(URI.create(newsUrl + "?url=" + news.titleUrl()))
                        .header("Content-Type", "application/json")
                        .DELETE()
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        );
    }
}
