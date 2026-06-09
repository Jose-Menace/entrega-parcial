package com.gamezone.model;

import com.gamezone.model.interfaces.IExportable;
import com.gamezone.model.interfaces.IValidable;

public class DigitalVideoGame extends VideoGame implements IExportable, IValidable {
    private double sizeGB;

    public DigitalVideoGame() {}

    public DigitalVideoGame(String title, double price, int stock, String platform, double sizeGB) {
        super(title, price, stock, platform);
        this.sizeGB = sizeGB;
    }

    public double getSizeGB() { return sizeGB; }
    public void setSizeGB(double sizeGB) { this.sizeGB = sizeGB; }

    @Override
    public double calculateFinalPrice() {
        return (sizeGB > 50) ? price + 5000 : price;
    }

    @Override
    public String exportData() {
        return "Digital|" + title + "|" + price + "|" + stock + "|" + platform + "|" + sizeGB;
    }

    @Override
    public boolean isValid() {
        return sizeGB > 0 && title != null && !title.trim().isEmpty() && price > 0 && stock >= 0;
    }

    @Override
    public String toString() {
        return super.toString() + ", Tamaño: " + sizeGB + " GB, Precio final: " + calculateFinalPrice();
    }
}
