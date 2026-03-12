package ru.test.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.test.dto.Okved;
import ru.test.service.OkvedLoader;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Сервис, который отвечает за хранение океведов в памяти. Решение вместе репозитория.
 */
@Service
public class OkvedLoaderImpl implements OkvedLoader {

    private final Logger log = LoggerFactory.getLogger(OkvedLoaderImpl.class);

    private final ObjectMapper objectMapper;
    private final String okvedsDataUrl;

    private AtomicReference<List<Okved>> okvedsCache = new AtomicReference<>(); //TODO БД

    public OkvedLoaderImpl(
            ObjectMapper objectMapper,
            @Value("${okved.source.url}")
            String okvedsDataUrl
    ) {
        this.objectMapper = objectMapper;
        this.okvedsDataUrl = okvedsDataUrl;
    }

    @PostConstruct
    public void loadOnStart() {
        refreshOkvedData();
    }

    @Scheduled(cron = "0 0 3 * * ?")
    public void refreshOkvedData() {
        log.info("Начало обновления ОКВЕДов");
        log.debug("Из источника: {}", okvedsDataUrl);

        try {
            // TODO Нужно преобразовывать данные сразу, а не каждый рвз в getOkveds(). Еще раз попытаться через WireMock.
            // List<Okved> data = objectMapper.readValue(url, new TypeReference<>() {
            // });
            // okvedsCache = data.stream()
            //         .flatMap(okved -> okved.items().stream())
            //         .toList();

            URL url = URI.create(okvedsDataUrl).toURL();
            List<Okved> data = objectMapper.readValue(url, new TypeReference<>() {
            });

            okvedsCache.set(data);

            log.info("ОКВЕДы были обновлены");
        } catch (IOException ex) {
            log.error("Возникла ошибка при обновлении ОКВЕДов", ex);
        }
    }

    @NonNull
    @Override
    public List<Okved> getOkveds() {
        return new ArrayList<>(okvedsCache.get()).stream()
                .flatMap(okved -> okved.items().stream())
                .toList();
    }
}
