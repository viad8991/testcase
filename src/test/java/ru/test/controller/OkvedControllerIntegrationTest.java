package ru.test.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import ru.test.ApplicationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class OkvedControllerIntegrationTest extends ApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void checkSuccessRequest() throws Exception {
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
