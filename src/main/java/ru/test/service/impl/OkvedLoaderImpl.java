package ru.test.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.test.dto.Node;
import ru.test.dto.Okved;
import ru.test.service.OkvedLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * Сервис, который отвечает за хранение океведов в памяти. Решение вместе репозитория.
 */
@Service
public class OkvedLoaderImpl implements OkvedLoader {

    private final Logger log = LoggerFactory.getLogger(OkvedLoaderImpl.class);

    private final Resource okvedResource;
    private final ObjectMapper objectMapper;

    private final AtomicReference<Map<String, Okved>> okvedsCache = new AtomicReference<>(Map.of()); //TODO БД

    public OkvedLoaderImpl(
            @Value("${okved.source.uri}")
            Resource okvedResource,
            ObjectMapper objectMapper
    ) {
        this.okvedResource = okvedResource;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void loadOnStart() {
        log.debug("Инициализация ОКВЕДов");
        refreshOkvedData();
    }

    @NonNull
    @Override
    public Map<String, Okved> getOkveds() {
        return okvedsCache.get();
    }

    @Scheduled(cron = "0 0 3 * * ?")
    public void refreshOkvedData() {
        log.info("Начало обновления ОКВЕДов из: {}", okvedResource);

        try (InputStream inputStream = okvedResource.getInputStream()) {
            List<Node> nodes = objectMapper.readValue(inputStream, new TypeReference<>() {
            });

            if (nodes != null) {
                okvedsCache.set(toOkvedCodeMap(nodes));
                log.info("ОКВЕДы успешно обновлены");
            }
        } catch (IOException ex) {
            log.error("Ошибка при чтении или десериализации ОКВЕДов", ex);
        } catch (Exception ex) {
            log.error("Непредвиденная ошибка при обновлении ОКВЕДов", ex);
        }
    }

    @NonNull
    private Map<String, Okved> toOkvedCodeMap(@NonNull List<Node> nodes) {
        HashMap<String, Okved> result = new HashMap<>();

        for (Node node : nodes) {
            String normalizeCode = node.code().replace(".", "");
            result.put(normalizeCode, new Okved(node.code(), node.name()));

            List<Node> items = node.items();
            if (!isEmpty(items)) {
                result.putAll(toOkvedCodeMap(items));
            }
        }

        return result;
    }
}
