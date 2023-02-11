package ru.zulvit.service.impl;

import lombok.extern.log4j.Log4j2;
import org.apache.http.client.methods.HttpPost;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import ru.zulvit.entity.Search;
import ru.zulvit.entity.Video;
import ru.zulvit.service.GettingVideoService;

import javax.validation.constraints.NotNull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Log4j2
@Service
public class GettingVideoServiceImpl implements GettingVideoService {
    private static final int CONNECT_TIMEOUT = 250;
    private static final int READ_TIMEOUT = 250;
    private static final int NOT_FOUND = -1;
    private final JSONParser jsonParser = new JSONParser();


    /**
     * @param search название аниме
     * @return лист с ответом: ссылки на плеер, озвучки, картинки
     */
    @Override
    public List<Video> searchByTitle(@NotNull Search search) {
        var request = prepareRequest(search);
        log.warn(request);
        var response = getResponse(request);

        List<Video> videos = new ArrayList<>();
        if (getSearchTotal(response) != NOT_FOUND) {
            long countAnime = getSearchTotal(response);
            System.out.println("Найдено всего: " + countAnime);
            for (int i = 0; i < countAnime; i++) {
                Video video = new Video.Builder()
                        .id(Objects.requireNonNull(getId(response)).get(i))
                        .link(Objects.requireNonNull(getLinks(response)).get(i))
                        .title(Objects.requireNonNull(getTitle(response)).get(i))
                        .translation(Objects.requireNonNull(getTranslation(response)).get(i))
                        .worldArtLink(Objects.requireNonNull(getWorldArtLink(response)).get(i))
                        .build();
                videos.add(video);
            }
        return videos;
        } else {
            return null;
        }
    }

    /**
     * @return результат поиска на сервере
     */
    private String getResponse(@NotNull String request) {
        try {
            HttpURLConnection connection = (HttpURLConnection) (new URL(request)).openConnection();
            connection.setRequestMethod(HttpPost.METHOD_NAME); //"POST"
            connection.setUseCaches(false);
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.connect();
            StringBuilder stringBuilder = new StringBuilder();
            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                    stringBuilder.append("\n");
                }
                log.debug(stringBuilder);
                return stringBuilder.toString();
            }
            System.out.printf("HTTP SENDER: FAIL %s\n", connection.getResponseCode());
        } catch (IOException var6) {
            var6.printStackTrace();
        }
        return null;
    }

    /**
     * @return подготовленный для отправки на сервер запрос
     */
    private String prepareRequest(@NotNull Search search) {
        return "https://kodikapi.com/search?" +
                "token=" + search.getToken() +
                "&with_page_links=" + search.isWithPageLinks() +
                "&with_episodes=" + search.isWithEpisodes() +
                "&with_seasons=" + search.isWithSeasons() +
                "&limit=" + search.getLimit() +
                "&types=" + search.getType() +
                "&title=" + textToFormat(search.getTitle()) +
                "&season=" + search.getSeason();
    }

    /**
     * @param text отформатированный запрос в формате "word%20word"
     */
    private String textToFormat(@NotNull String text) {
        String[] array = text.split(" ");

        StringBuilder textBuilder = new StringBuilder();
        for (String s : array) {
            textBuilder.append(s).append("%20");
        }
        text = textBuilder.toString();
        return text;
    }

    /**
     * @return количество найденных в БД аниме
     */
    public long getSearchTotal(@NotNull String path) {
        long total = NOT_FOUND;
        try {
            JSONObject rootJsonObject = (JSONObject) jsonParser.parse(path);
            total = (long) rootJsonObject.get("total");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return total;
    }

    /**
     * @return Лист со всеми названиями
     */
    private ArrayList<String> getTitle(@NotNull String path) {
        ArrayList<String> listTitles = new ArrayList<>();

        try {
            JSONObject rootJsonObject = (JSONObject) jsonParser.parse(path);
            JSONArray jsonArray = (JSONArray) rootJsonObject.get("results");
            for (Object o : jsonArray) {
                JSONObject titleObject = (JSONObject) o;
                String title = titleObject.get("title").toString();
                listTitles.add(title);
            }
            return listTitles;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return лист со всеми ID
     */
    private ArrayList<String> getId(@NotNull String path) {
        ArrayList<String> listId = new ArrayList<>();

        try {
            JSONObject rootJsonObject = (JSONObject) jsonParser.parse(path);
            JSONArray jsonArray = (JSONArray) rootJsonObject.get("results");
            for (Object o : jsonArray) {
                JSONObject idObject = (JSONObject) o;
                String id = idObject.get("id").toString();
                listId.add(id);
            }
            return listId;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return лист со всеми ссылками
     */
    private ArrayList<String> getLinks(@NotNull String path) {
        ArrayList<String> listLinks = new ArrayList<>();

        try {
            JSONObject rootJsonObject = (JSONObject) jsonParser.parse(path);
            JSONArray jsonArray = (JSONArray) rootJsonObject.get("results");
            for (Object o : jsonArray) {
                JSONObject linkObject = (JSONObject) o;
                String link = linkObject.get("link").toString();
                listLinks.add(link);
            }
            return listLinks;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return лист со всеми озвучками
     */
    private ArrayList<String> getTranslation(@NotNull String path) {
        ArrayList<String> listTranslation = new ArrayList<>();

        try {
            JSONObject rootJsonObject = (JSONObject) jsonParser.parse(path);
            JSONArray jsonArray = (JSONArray) rootJsonObject.get("results");
            for (Object o : jsonArray) {
                JSONObject translationObject = (JSONObject) o;
                String translation = translationObject.get("translation").toString();
                listTranslation.add(translation);
            }
            return listTranslation;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return ссылку на world-art для парсинга обложек
     */
    private ArrayList<String> getWorldArtLink(@NotNull String path) {
        ArrayList<String> worldArtLink = new ArrayList<>();

        try {
            JSONObject rootJsonObject = (JSONObject) jsonParser.parse(path);
            JSONArray jsonArray = (JSONArray) rootJsonObject.get("results");
            for (Object o : jsonArray) {
                JSONObject translationObject = (JSONObject) o;
                if (translationObject.get("worldart_link") != null) {
                    String artLink = translationObject.get("worldart_link").toString();
                    worldArtLink.add(artLink);
                } else {
                    worldArtLink.add(String.valueOf(NOT_FOUND));
                }
            }
            return worldArtLink;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
