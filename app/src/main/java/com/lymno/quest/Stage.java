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
    private String question;

    public Stage(int id, int level, int questId, String name, String description, String question) {
        this.id = id;
        this.level = level;
        this.questId = questId;
        this.name = name;
        this.description = description;
        this.question = question;
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

    public String getQuestion() {
        return question;
    }
}
