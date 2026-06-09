package com.gamezone.model;

import java.time.LocalDateTime;

public class Sale {
    private String gameTitle;
    private int quantity;
    private double total;
    private LocalDateTime date;

    public Sale() {}

    public Sale(String gameTitle, int quantity, double total, LocalDateTime date) {
        this.gameTitle = gameTitle;
        this.quantity = quantity;
        this.total = total;
        this.date = date;
    }

    // Getters y Setters
    public String getGameTitle() { return gameTitle; }
    public void setGameTitle(String gameTitle) { this.gameTitle = gameTitle; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    @Override
    public String toString() {
        return "Venta: " + gameTitle + " x" + quantity + " = $" + total + " (" + date + ")";
    }
}
