package ru.zulvit.service;

import org.springframework.stereotype.Repository;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
public interface AnswerConsumer {
    void consume(SendMessage sendMessage);
}
