package ru.zulvit.entity;

public class Video {
    private final String title;
    private final String id;
    private final String link;
    private final String description;
    private final String translation;
    private final String worldArtLink;

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public String getLink() {
        return link;
    }

    public String getTranslation() {
        return translation;
    }

    public String getWorldArtLink() {
        return worldArtLink;
    }

    public static class Builder {
        public String description = "не указано";
        public String title = "не указано";
        public String id = "не указан";
        public String link = "не указана";
        public String translation = "не указана";
        public String worldArtLink = "не указана";

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder link(String link) {
            this.link = link;
            return this;
        }

        public Builder translation(String translation) {
            this.translation = translation;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder worldArtLink(String worldArtLink) {
            this.worldArtLink = worldArtLink;
            return this;
        }

        public Video build() {
            return new Video(this);
        }
    }

    @Override
    public String toString() {
        return "Аниме:\n" +
                "Название: " + title + '\n' +
                "ID: " + id + '\n' +
                "Ссылка на просмотр: " + link + '\n' +
                "Ссылка на превью: " + worldArtLink + "\n\n\n";
    }

    public Video(Builder builder) {
        title = builder.title;
        id = builder.id;
        link = builder.link;
        description = builder.description;
        translation = builder.translation;
        worldArtLink = builder.worldArtLink;
    }
}
