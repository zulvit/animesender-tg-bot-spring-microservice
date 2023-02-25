package ru.zulvit.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.zulvit.dao.AppUserDAO;
import ru.zulvit.dao.RawDataDAO;
import ru.zulvit.entity.AppUser;
import ru.zulvit.entity.RawData;
import ru.zulvit.entity.Search;
import ru.zulvit.service.GettingVideoService;
import ru.zulvit.service.MainService;
import ru.zulvit.service.ParsingImageService;
import ru.zulvit.service.ProducerService;
import ru.zulvit.service.enums.ServiceCommands;
import ru.zulvit.utils.filter.ResultFilterUtils;

import javax.validation.constraints.NotNull;

import static ru.zulvit.entity.enums.UserState.BASIC_STATE;
import static ru.zulvit.entity.enums.UserState.WAIT_EMAIL_CONFIRMATION_STATE;
import static ru.zulvit.service.enums.ServiceCommands.*;

@Service
@Log4j2
public class MainServiceImpl implements MainService {
    private final RawDataDAO rawDataDAO;
    private final ProducerService producerService;
    private final AppUserDAO appUserDAO;
    private final GettingVideoService gettingVideoService;
    private final ParsingImageService parsingImageService;

    public MainServiceImpl(RawDataDAO rawDataDAO, ProducerService producerService,
                           AppUserDAO appUserDAO, GettingVideoService gettingVideoService, ParsingImageService parsingImageService) {
        this.rawDataDAO = rawDataDAO;
        this.producerService = producerService;
        this.appUserDAO = appUserDAO;
        this.gettingVideoService = gettingVideoService;
        this.parsingImageService = parsingImageService;
    }

    @Override
    public void processTextMessage(Update update) {
        saveRawData(update);
        var appUser = findOrSaveUser(update);
        var userState = appUser.getUserState();
        var text = update.getMessage().getText();
        log.debug("userState: " + userState + " text:" + text);
        var output = "";

        var serviceCommands = ServiceCommands.fromValue(text);
        if (CANCEL.equals(serviceCommands)) {
            output = cancelProcess(appUser);
        } else if (BASIC_STATE.equals(userState)) {
            output = processServiceCommand(appUser, text);
        } else if (WAIT_EMAIL_CONFIRMATION_STATE.equals(userState)) {
            //TODO add registration system
        } else {
            log.error("Unknown user state : " + userState);
            output = "unknown error. state:" + userState + " contact administrator";
        }
        var chatId = update.getMessage().getChatId();
        sendAnswer(output, chatId);
    }

    private void sendAnswer(String output, Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(output);
        producerService.produceAnswer(sendMessage);
    }

    private String processServiceCommand(AppUser appUser, String command) {
        var serviceCommand = ServiceCommands.fromValue(command);
        if (REGISTRATION.equals(serviceCommand)) {
            //TODO added registration
            return "sorry, registration doesn't work. Please, wait ~1 month =)";
        } else if (HELP.equals(serviceCommand)) {
            return help();
        } else if (START.equals(serviceCommand)) {
            return "Hello. Let's start! Enter /help";
        } else {
            return search(command);
        }
    }

    private String search(@NotNull String title) {
        Search search = new Search(title, 1);
        var searchResult = gettingVideoService.searchByTitle(search);
        var filteredSearchResult = ResultFilterUtils.filterRepeat(searchResult);

        //TODO переписать этот код, написан только для теста
        for (ru.zulvit.entity.Video video : filteredSearchResult) {
            var imagesSearchResult = parsingImageService.parse(video.getWorldArtLink());
            log.debug(imagesSearchResult);
        }
        log.debug(searchResult);
        return filteredSearchResult.toString();
    }

    private String help() {
        return "List of commands: /help, /cancel";
    }

    private String cancelProcess(AppUser appUser) {
        appUser.setUserState(BASIC_STATE);
        appUserDAO.save(appUser);
        return "go back!";
    }

    private AppUser findOrSaveUser(Update update) {
        var telegramUser = update.getMessage().getFrom();
        AppUser persistentAppUser = appUserDAO.findAppUserByTelegramUserId(telegramUser.getId());
        if (persistentAppUser == null) {
            AppUser transientAppUser = AppUser.builder()
                    .telegramUserId(telegramUser.getId())
                    .userName(telegramUser.getUserName())
                    .firstName(telegramUser.getFirstName())
                    .lastName(telegramUser.getLastName())
                    .isActive(true) //TODO rewrite default values after creating registration system
                    .userState(BASIC_STATE)
                    .build();
            return appUserDAO.save(transientAppUser);
        }
        return persistentAppUser;
    }

    private void saveRawData(Update update) {
        RawData rawData = RawData.builder()
                .event(update)
                .build();
        rawDataDAO.save(rawData);
    }

    @Override
    public void processDocMessage(Update update) {

    }

    @Override
    public void processPhotoMessage(Update update) {

    }
}
