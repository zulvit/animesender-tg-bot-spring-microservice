package ru.zulvit.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.zulvit.service.UpdateProducer;
import ru.zulvit.utils.MessageUtils;


import javax.validation.constraints.NotNull;

import static ru.zulvit.model.RabbitQueue.TEXT_MESSAGE_UPDATE;

@Log4j2
@Component
public class UpdateController {
    private TelegramBot telegramBot;
    private final MessageUtils messageUtils;
    private final UpdateProducer updateProducer;

    public UpdateController(MessageUtils messageUtils, UpdateProducer updateProducer) {
        this.messageUtils = messageUtils;
        this.updateProducer = updateProducer;
    }

    public void registerBot(@NotNull TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void processUpdate(@NotNull Update update) {
        if (update.hasMessage()) {
            distributeMessagesByTypes(update);
        }
    }

    private void distributeMessagesByTypes(@NotNull Update update) {
        var message = update.getMessage();
        if (message.hasText()) {
            processTextMessage(update);
        } else {
            unsupportedMessageType(update);
        }
    }

    private void processTextMessage(@NotNull Update update) {
        updateProducer.produce(TEXT_MESSAGE_UPDATE, update);
    }

    private void unsupportedMessageType(@NotNull Update update) {
        log.debug("unsupported message type in");
        var sendMessage = messageUtils.generateSendMessageWithText(update,
                "Тип сообщения не поддерживается.");
        setView(sendMessage);
    }

    private void setView(SendMessage sendMessage) {
        telegramBot.sendAnswerMessage(sendMessage);
    }
}
