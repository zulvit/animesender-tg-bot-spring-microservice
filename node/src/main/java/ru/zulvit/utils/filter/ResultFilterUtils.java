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
        ArrayList<Video> arrayList = new ArrayList<>();
        for (Video video : videos) {
            boolean flag = false;
            for (Video value : arrayList) {
                if (value.getTitle().equals(video.getTitle())) {
                    flag = true;
                    break;
                }
            }
            if (!flag) arrayList.add(video);
        }
        return arrayList;
    }
}
