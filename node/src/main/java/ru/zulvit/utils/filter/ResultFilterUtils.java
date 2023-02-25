package ru.zulvit.utils.filter;

import ru.zulvit.entity.Video;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class ResultFilterUtils {
    /**
     * @param videos весь список видео
     * @return отфильтрованный список, без повторов по названию
     */
    public static List<Video> filterRepeat(@NotNull List<Video> videos) {
        List<Video> filteredVideoList = new ArrayList<>();
        for (Video video : videos) {
            boolean flag = false;
            for (Video value : filteredVideoList) {
                if (value.getTitle().equals(video.getTitle())) {
                    flag = true;
                    break;
                }
            }
            if (!flag) filteredVideoList.add(video);
        }
        return filteredVideoList;
    }
}
