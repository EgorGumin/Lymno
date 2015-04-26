package com.lymno.quest;

/**
 * Created by Roman Belkov on 16.04.2015.
 */

public class Stage {
    private int id;
    private int level;
    private int questId;
    private String name;
    private String description;
    private double x;
    private double y;
    private String question;
    private String answer;

    public Stage(int id, int level, int questId, String name, String description, double x,
                 double y, String question, String answer) {
        this.id = id;
        this.level = level;
        this.questId = questId;
        this.name = name;
        this.description = description;
        this.x = x;
        this.y = y;
        this.question = question;
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public int getQuestId() {
        return questId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}
