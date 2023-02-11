package ru.zulvit.entity;

import lombok.Getter;

@Getter
public class Search {
    private final String token = "6aca623ad7da67b960eefffbf1a8ac00";
    private final boolean withPageLinks = true;
    private final boolean withEpisodes = false;
    private final boolean withSeasons = true;
    private final int limit = Integer.MAX_VALUE;
    private final String type = "anime-serial";
    private final String title;
    private final int season;


    public Search(String title, int season) {
        this.title = title;
        this.season = season;
    }
}
