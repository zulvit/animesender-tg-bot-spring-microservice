package ru.zulvit.service;

import ru.zulvit.entity.Search;
import ru.zulvit.entity.Video;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface GettingVideoService {
    List<Video> searchByTitle(@NotNull Search search);
}
