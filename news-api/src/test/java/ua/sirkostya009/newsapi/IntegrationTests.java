package ua.sirkostya009.newsapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private Controller controller;

    @Test
    public void testFetchNews() throws Exception {
        when(controller.fetchNews(Time.MORNING)).thenReturn(List.of());

        mockMvc.perform(get("/api/news?time=MORNING"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

}
