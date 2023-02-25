package ru.zulvit.service;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@FunctionalInterface
public interface ParsingImageService {
    Optional<String> parse(@NotNull String uri);
}
