package com.example.myapplication;

public class Furniture {
    private String name;
    private String colour;
    private String description;
    private String manufacturer;
    private String material;
    private String price;
    private String stock;

    public Furniture(String name, String colour, String description, String manufacturer, String material, String price, String stock) {
        this.name = name;
        this.colour = colour;
        this.description = description;
        this.manufacturer = manufacturer;
        this.material = material;
        this.price = price;
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "Furniture{" +
                "name='" + name + '\'' +
                ", colour='" + colour + '\'' +
                ", description='" + description + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", material='" + material + '\'' +
                ", price='" + price + '\'' +
                ", stock=" + stock +
                '}';
    }
}
