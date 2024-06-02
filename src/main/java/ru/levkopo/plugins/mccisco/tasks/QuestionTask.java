package ru.levkopo.plugins.mccisco.tasks;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.levkopo.plugins.mccisco.tasks.result.TaskResult;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class QuestionTask extends Task {
    public final String question;
    public final String answer;

    public QuestionTask(String question, String answer, TaskResult result) {
        super(result);
        this.question = question;
        this.answer = answer.toLowerCase();
    }

    @Override
    public void renderMessage(@NotNull Player player, @NotNull Component message) {
        TextComponent text = (TextComponent) message;
        done(!text.content().toLowerCase().equals(answer));
    }

    @Override
    public void onStart(Player player) {
        super.onStart(player);
        player.getServer().broadcast(
                text("[ВОПРОС]:", RED)
                        .appendSpace()
                        .append(text(question, WHITE))
                        .appendNewline()
                        .append(text("Если игрок "+player.getName()+" ответит", WHITE))
                        .appendSpace()
                        .append(text((result.negative ? "неверно": "верно"), WHITE))
                        .appendSpace()
                        .append(text("то,", WHITE))
                        .appendSpace()
                        .append(text(result.description, WHITE))
        );
    }
}
