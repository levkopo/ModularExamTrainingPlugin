package ru.levkopo.plugins.mccisco.tasks;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import ru.levkopo.plugins.mccisco.ModularExamTrainingPlugin;
import ru.levkopo.plugins.mccisco.tasks.result.TaskResult;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public abstract class Task  {
    public final TaskResult result;
    private Player player;
    private boolean done;

    protected Task(TaskResult result) {
        this.result = result;
    }

    public Player getPlayer() {
        return player;
    }

    public void onStart(Player player) {
        this.player = player;
        this.done = false;
    }

    public void done(boolean negative) {
        this.done = true;

        String resultDescription = result.getResultDescription(negative) + player.getName();
        if(negative) {
            player.getServer().broadcast(
                    text(player.getName() + " провалил испытание. ", RED)
                            .append(text(resultDescription, WHITE))
            );
        }else{
            player.getServer().broadcast(
                    text(player.getName() + " прошел испытание. ", GREEN)
                            .append(text(resultDescription, WHITE))
            );
        }

        if(negative == result.negative) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    result.run(player);
                }
            }.runTask(ModularExamTrainingPlugin.instance);
        }

        TasksProvider.stopTask();
    }

    public void renderMessage(@NotNull Player player, @NotNull Component message) {}

    public final void render(@NotNull Player player, @NotNull Component message) {
        if(this.done) {
            return;
        }

        renderMessage(player, message);
    }
}
