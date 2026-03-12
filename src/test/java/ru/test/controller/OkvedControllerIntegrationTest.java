package ru.test.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.ResourceUtils;
import ru.test.ApplicationTest;
import ru.test.dto.Okved;
import ru.test.service.OkvedLoader;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class OkvedControllerIntegrationTest extends ApplicationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OkvedLoader mockOkvedLoader;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() throws IOException {
        List<Okved> okveds = objectMapper.readValue(
                ResourceUtils.getFile("classpath:okved.json"),
                new TypeReference<>() {
                });

        when(mockOkvedLoader.getOkveds()).thenReturn(okveds.stream()
                // TODO Дублирование кода из ru.test.service.impl.OkvedLoaderImpl.getOkveds. Нужен WireMock.
                .flatMap(okved -> okved.items().stream())
                .toList());
    }

    @Test
    void checkSuccessRequest() throws Exception {
        mockMvc.perform(post("/api/v1/okved/find")
                        .param("phone", "9000011101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phoneNormalized").value("+79000011101"))
                .andExpect(jsonPath("$.okvedCode").value("01.11"))
                .andExpect(jsonPath("$.okvedName").value("Выращивание зерновых (кроме риса), зернобобовых культур и семян масличных культур"))
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
                .andExpect(content().string("Некорректный номер телефона: '6600011101'"))
                .andDo(print());

        mockMvc.perform(post("/api/v1/okved/find"))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Required parameter 'phone' is not present."))
                .andDo(print());
    }

}
