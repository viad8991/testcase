package ru.test.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.ResourceUtils;
import ru.test.ApplicationTest;
import ru.test.dto.Node;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OkvedControllerIntegrationTest extends ApplicationTest {

    @MockitoBean
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private WireMockServer wireMockServer;

    @BeforeAll
    public static void setUp() throws IOException {
        List<Node> nodes = new ObjectMapper().readValue(
                ResourceUtils.getFile("classpath:okved.json"),
                new TypeReference<>() {
                });


        // when(objectMapper.readValue(ArgumentMatchers.any(URL.class), Mockito.<TypeReference<List<Node>>>any()))
        //       .thenReturn(List.of());
        // wireMockServer = new WireMockServer(WireMockConfiguration.options().port(8080));
        // wireMockServer.start();
        // WireMock.configureFor("localhost", 8080);
    }

    @AfterEach
    public void shutDown() {
        // if (wireMockServer != null && wireMockServer.isRunning()) {
        //     wireMockServer.stop();
        //     wireMockServer.shutdownServer();
        // }
    }

    @Test
    void checkSuccessRequest() throws Exception {
        List<Node> nodes = new ObjectMapper().readValue(
                ResourceUtils.getFile("classpath:okved.json"),
                new TypeReference<>() {
                });

        when(objectMapper.readValue(ArgumentMatchers.any(URL.class), Mockito.<TypeReference<List<Node>>>any()))
                .thenReturn(nodes);

        /*stubFor(get(urlEqualTo("/mock/okved.json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBodyFile("okved.json"))
        );*/

        mockMvc.perform(post("/api/v1/okved/find")
                        .param("phone", "9000011101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phoneNormalized").value("+79000011101"))
                .andExpect(jsonPath("$.okvedCode").value("11.01"))
                .andExpect(jsonPath("$.okvedName").value("Перегонка, очистка и смешивание спиртов"))
                .andExpect(jsonPath("$.matchLength").value("4"))
                .andDo(print());

        mockMvc.perform(post("/api/v1/okved/find")
                        .param("phone", "89000011112"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phoneNormalized").value("+79000011112"))
                .andExpect(jsonPath("$.okvedCode").value("01.11.12"))
                .andExpect(jsonPath("$.okvedName").value("Выращивание ячменя"))
                .andExpect(jsonPath("$.matchLength").value("6"))
                .andDo(print());

        mockMvc.perform(post("/api/v1/okved/find")
                        .param("phone", "89000001134"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phoneNormalized").value("+79000001134"))
                .andExpect(jsonPath("$.okvedCode").value("01.13.4"))
                .andExpect(jsonPath("$.okvedName").value("Выращивание семян овощных культур, за исключением семян сахарной свеклы"))
                .andExpect(jsonPath("$.matchLength").value("5"))
                .andDo(print());

        mockMvc.perform(post("/api/v1/okved/find")
                        .param("phone", "79000400000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phoneNormalized").value("+79000400000"))
                .andExpect(jsonPath("$.okvedCode").value("unknown"))
                .andExpect(jsonPath("$.okvedName").value("Неизвестный код."))
                .andExpect(jsonPath("$.matchLength").value("0"))
                .andDo(print());
    }

    @Test
    void checkBadRequest() throws Exception {
        // TODO нормализовать ошибку. Или body, или reason.
        mockMvc.perform(post("/api/v1/okved/find")
                        .param("phone", "6600011101"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Некорректный номер телефона"))
                .andDo(print());

        mockMvc.perform(post("/api/v1/okved/find"))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Required parameter 'phone' is not present."))
                .andDo(print());
    }

}
