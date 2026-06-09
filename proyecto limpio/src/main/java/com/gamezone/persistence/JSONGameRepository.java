package com.gamezone.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.gamezone.model.VideoGame;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JSONGameRepository implements IGameRepository {
    private final File file = new File("videojuegos.json");
    private final ObjectMapper mapper;

    public JSONGameRepository() {
        var ptv = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType("com.gamezone.model")
                .build();
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE);
    }

    private List<VideoGame> readAllFromFile() throws IOException {
        if (!file.exists()) return new ArrayList<>();
        return mapper.readValue(file, new TypeReference<ArrayList<VideoGame>>() {});
    }

    private void writeAllToFile(List<VideoGame> games) throws IOException {
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, games);
    }

    @Override
    public void addGame(VideoGame game) throws Exception {
        List<VideoGame> games = readAllFromFile();
        boolean exists = games.stream().anyMatch(g -> g.getTitle().equalsIgnoreCase(game.getTitle()));
        if (exists) throw new Exception("El videojuego ya existe en el catálogo");
        games.add(game);
        writeAllToFile(games);
    }

    @Override
    public void updateGame(String title, VideoGame newGame) throws Exception {
        List<VideoGame> games = readAllFromFile();
        boolean found = false;
        for (int i = 0; i < games.size(); i++) {
            if (games.get(i).getTitle().equalsIgnoreCase(title)) {
                games.set(i, newGame);
                found = true;
                break;
            }
        }
        if (!found) throw new Exception("Juego no encontrado para actualizar");
        writeAllToFile(games);
    }

    @Override
    public void deleteGame(String title) throws Exception {
        List<VideoGame> games = readAllFromFile();
        boolean removed = games.removeIf(g -> g.getTitle().equalsIgnoreCase(title));
        if (!removed) throw new Exception("Juego no encontrado para eliminar");
        writeAllToFile(games);
    }

    @Override
    public List<VideoGame> findAll() {
        try { return readAllFromFile(); } catch (IOException e) { return new ArrayList<>(); }
    }

    @Override
    public VideoGame findByTitle(String title) {
        try {
            return readAllFromFile().stream()
                    .filter(g -> g.getTitle().equalsIgnoreCase(title))
                    .findFirst().orElse(null);
        } catch (IOException e) { return null; }
    }

    @Override
    public List<VideoGame> findByPlatform(String platform) {
        try {
            return readAllFromFile().stream()
                    .filter(g -> g.getPlatform().equalsIgnoreCase(platform))
                    .collect(Collectors.toList());
        } catch (IOException e) { return new ArrayList<>(); }
    }
}
