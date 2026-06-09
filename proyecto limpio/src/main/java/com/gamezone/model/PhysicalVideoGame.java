package com.gamezone.model;

public class PhysicalVideoGame extends VideoGame {
    private String condition;

    public PhysicalVideoGame() {}

    public PhysicalVideoGame(String title, double price, int stock, String platform, String condition) {
        super(title, price, stock, platform);
        this.condition = condition;
    }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    @Override
    public double calculateFinalPrice() {
        return condition.equalsIgnoreCase("usado") ? price * 0.75 : price;
    }

    @Override
    public String toString() {
        return super.toString() + ", Condición: " + condition + ", Precio final: " + calculateFinalPrice();
    }
}
