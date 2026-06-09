package com.gamezone.service;

import com.gamezone.model.*;
import com.gamezone.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

public class GameService {
    private final IGameRepository gameRepo;
    private final JSONSaleRepository saleRepo;

    public GameService() {
        this.gameRepo = new JSONGameRepository();
        this.saleRepo = new JSONSaleRepository();
    }

    public void addGame(VideoGame game) throws Exception {
        if (game.getTitle() == null || game.getTitle().trim().isEmpty())
            throw new Exception("El título no puede ser nulo o vacío");
        if (game.getPrice() <= 0)
            throw new Exception("El precio debe ser mayor a 0");
        if (game.getStock() < 0)
            throw new Exception("El stock no puede ser negativo");
        gameRepo.addGame(game);
    }

    public List<VideoGame> listAllGames() { return gameRepo.findAll(); }
    public VideoGame searchByTitle(String title) { return gameRepo.findByTitle(title); }
    public List<VideoGame> searchByPlatform(String platform) { return gameRepo.findByPlatform(platform); }

    public void updateGame(String oldTitle, VideoGame newGame) throws Exception {
        if (newGame.getTitle() == null || newGame.getTitle().trim().isEmpty())
            throw new Exception("El título no puede ser nulo o vacío");
        if (newGame.getPrice() <= 0)
            throw new Exception("El precio debe ser mayor a 0");
        if (newGame.getStock() < 0)
            throw new Exception("El stock no puede ser negativo");
        gameRepo.updateGame(oldTitle, newGame);
    }

    public void deleteGame(String title) throws Exception { gameRepo.deleteGame(title); }

    public double sellGame(String title, int quantity) throws Exception {
        VideoGame game = gameRepo.findByTitle(title);
        if (game == null) throw new Exception("El videojuego no existe en el catálogo");
        if (game.getStock() < quantity) throw new Exception("Stock insuficiente");
        double total = game.calculateFinalPrice() * quantity;
        game.setStock(game.getStock() - quantity);
        gameRepo.updateGame(title, game);
        saleRepo.saveSale(new Sale(title, quantity, total, LocalDateTime.now()));
        return total;
    }

    public List<Sale> getAllSales() { return saleRepo.getAllSales(); }
}
