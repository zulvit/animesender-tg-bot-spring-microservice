package ru.zulvit.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.zulvit.service.ProducerService;

import static ru.zulvit.model.RabbitQueue.ANSWER_MESSAGE_UPDATE;

@Service
@Log4j2
public class ProducerServiceImpl implements ProducerService {
    private final RabbitTemplate rabbitTemplate;

    public ProducerServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void produceAnswer(SendMessage sendMessage) {
        log.debug("produce answer: " + sendMessage.getText());
        rabbitTemplate.convertAndSend(ANSWER_MESSAGE_UPDATE, sendMessage);
    }
}
