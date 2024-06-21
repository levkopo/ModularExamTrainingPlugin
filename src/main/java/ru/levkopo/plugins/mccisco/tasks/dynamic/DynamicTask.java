package ru.levkopo.plugins.mccisco.tasks.dynamic;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.levkopo.plugins.mccisco.tasks.Task;
import ru.levkopo.plugins.mccisco.tasks.result.TaskResult;

public abstract class DynamicTask extends Task {
    public DynamicTask(TaskResult result) {
        super(result);
    }

    private Task currentTask;

    public abstract Task generateTask(TaskResult result);

    @Override
    public void onStart(Player player) {
        super.onStart(player);
        currentTask = generateTask(this.result);
        currentTask.onStart(player);
    }

    @Override
    public void done(boolean negative) {}

    @Override
    public void renderMessage(@NotNull Player player, @NotNull Component message) {
        currentTask.renderMessage(player, message);
    }
}
