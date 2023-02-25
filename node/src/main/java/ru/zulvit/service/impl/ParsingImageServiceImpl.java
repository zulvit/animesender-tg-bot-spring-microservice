package ru.zulvit.service.impl;

import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import ru.zulvit.service.ParsingImageService;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Log4j2
@Service
public class ParsingImageServiceImpl implements ParsingImageService {
    @Override
    public Optional<String> parse(@NotNull String uri) {
        try {
            Document document = Jsoup.connect(uri)
                    .userAgent("Chrome/4.0.149.0 Safari/532")
                    .referrer("https:www.google.com")
                    .get();
            Elements elements = document.select("img");
            if (elements.size() > 1) {
                Element element = elements.get(1);
                log.debug(element.attr("src"));
                return Optional.of(element.attr("src"));
            }
        } catch (Exception e) {
            log.error(e.toString());
            return Optional.empty();
        }
        return Optional.empty();
    }
}