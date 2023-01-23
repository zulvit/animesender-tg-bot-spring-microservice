package ru.zulvit.service;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateProducer {
    void produce(String rabbitQueue, Update update);
}