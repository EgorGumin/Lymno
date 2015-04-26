package com.lymno.quest;

/**
 * Created by Roman Belkov on 16.04.2015.
 */

public class Quest {
    private int    id;
    private String name;
    private String description;
    private int    authorId;
    private int    startTime;
    private int    amountStages;
    private double x;
    private double y;
    private double length;
    private int    averageTime;

    public Quest(int id, String name, String description, int authorId, int startTime,
                 int amountStages, double x, double y, double length, int averageTime) {
        this.name = name;
        this.id = id;
        this.description = description;
        this.authorId = authorId;
        this.startTime = startTime;
        this.amountStages = amountStages;
        this.x = x;
        this.y = y;
        this.length = length;
        this.averageTime = averageTime;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getAuthorId() {
        return authorId;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getAmountStages() {
        return amountStages;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getLength() {
        return length;
    }

    public int getAverageTime() {
        return averageTime;
    }
}
