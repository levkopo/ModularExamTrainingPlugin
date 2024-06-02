package ru.levkopo.plugins.mccisco.tasks.result;

import org.bukkit.entity.Player;

public abstract class TaskResult {
    public final boolean negative;
    public final String description;
    protected TaskResult(boolean negative, String description) {
        this.negative = negative;
        this.description = description;
    }

    public void run(Player player) {}
    public String getResultDescription(boolean negative) {
        return "";
    }
}
