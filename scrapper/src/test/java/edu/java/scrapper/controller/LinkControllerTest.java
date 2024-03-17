package edu.java.scrapper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.core.dto.AddLinkRequest;
import edu.java.core.dto.LinkResponse;
import edu.java.core.dto.RemoveLinkRequest;
import edu.java.scrapper.service.impl.jdbc.JdbcLinkServiceImpl;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LinkController.class)
class LinkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JdbcLinkServiceImpl jdbcLinkServiceImpl;

    @Autowired
    private ObjectMapper objectMapper;

    private final String tgChatId = "12345";
    private final LinkResponse linkResponse = new LinkResponse(1L, URI.create("http://example.com"));

    @BeforeEach
    void setUp() {
        List<LinkResponse> links = Collections.singletonList(linkResponse);
        given(jdbcLinkServiceImpl.listAll(anyLong())).willReturn(links);
        given(jdbcLinkServiceImpl.add(anyLong(), any(AddLinkRequest.class))).willReturn(linkResponse);
        given(jdbcLinkServiceImpl.remove(anyLong(), any())).willReturn(linkResponse);
    }

    @Test
    void getLinksTest() throws Exception {
        mockMvc.perform(get("/v0/chats/links")
                .header("Tg-Chat-Id", tgChatId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void addLinkTest() throws Exception {
        AddLinkRequest addLinkRequest = new AddLinkRequest();
        addLinkRequest.setLink("http://example.com");
        mockMvc.perform(post("/v0/chats/links")
                .header("Tg-Chat-Id", tgChatId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addLinkRequest)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.url").value("http://example.com"));
    }

    @Test
    void removeLinkTest() throws Exception {
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest("http://example.com");
        mockMvc.perform(delete("/v0/chats/links")
                .header("Tg-Chat-Id", tgChatId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(removeLinkRequest)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.url").value("http://example.com"));
    }
}
