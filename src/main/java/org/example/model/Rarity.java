package org.example.model;

public enum Rarity {
    COMMON("Обычный", 67),
    RARE("Редкий", 20),
    EPIC("Эпический", 10),
    LEGENDARY("Легендарный", 3);

    private final String title;
    private final int weight;

    Rarity(String title, int weight) {
        this.title = title;
        this.weight = weight;
    }

    public String getTitle() {
        return title;
    }

    public int getWeight() {
        return weight;
    }
}