package com.gamezone.persistence;

import com.gamezone.model.VideoGame;
import java.util.List;

public interface IGameRepository {
    void addGame(VideoGame game) throws Exception;
    void updateGame(String title, VideoGame newGame) throws Exception;
    void deleteGame(String title) throws Exception;
    List<VideoGame> findAll();
    VideoGame findByTitle(String title);
    List<VideoGame> findByPlatform(String platform);
}
