package ru.zulvit.service.impl;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.zulvit.controller.UpdateController;
import ru.zulvit.service.AnswerConsumer;

import static ru.zulvit.model.RabbitQueue.ANSWER_MESSAGE_UPDATE;

public class AnswerConsumerImpl implements AnswerConsumer {
    private final UpdateController updateController;

    public AnswerConsumerImpl(UpdateController updateController) {
        this.updateController = updateController;
    }

    @Override
    @RabbitListener(queues = ANSWER_MESSAGE_UPDATE)
    public void consume(SendMessage sendMessage) {
        updateController.setView(sendMessage);
    }
}
