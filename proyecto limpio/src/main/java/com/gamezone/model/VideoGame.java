package com.gamezone.model;

public abstract class VideoGame {
    protected String title;
    protected double price;
    protected int stock;
    protected String platform;

    public VideoGame() {}

    public VideoGame(String title, double price, int stock, String platform) {
        this.title = title;
        this.price = price;
        this.stock = stock;
        this.platform = platform;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }

    public abstract double calculateFinalPrice();

    @Override
    public String toString() {
        return "Título: " + title + ", Precio base: " + price + ", Stock: " + stock + ", Plataforma: " + platform;
    }
}
