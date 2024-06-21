package ru.levkopo.plugins.mccisco.tasks.dynamic;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.levkopo.plugins.mccisco.Cables;
import ru.levkopo.plugins.mccisco.tasks.CiscoInterfaceTask;
import ru.levkopo.plugins.mccisco.tasks.Task;
import ru.levkopo.plugins.mccisco.tasks.result.TaskResult;
import ru.levkopo.plugins.mccisco.tasks.result.UpgradeWorldBorder;
import ru.levkopo.plugins.mccisco.utils.RandomUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;
import static net.kyori.adventure.text.format.TextDecoration.UNDERLINED;

public class CableLabelQuestionsSetupTask extends Task {
    private Map.Entry<String, String> questionPart;
    private int trueVariantNumber;

    public CableLabelQuestionsSetupTask() {
        super(new UpgradeWorldBorder(100));
    }

    @Override
    public void renderMessage(@NotNull Player player, @NotNull Component message) {
        TextComponent text = (TextComponent) message;
        player.getServer().broadcast(
                Component.text("Правильный ответ: ")
                        .decorate(BOLD)
                        .append(Component.text(trueVariantNumber + ": " + questionPart.getValue()))
        );

        done(!text.content().toLowerCase().equals(String.valueOf(trueVariantNumber + 1)));
    }

    @Override
    public void onStart(Player player) {
        super.onStart(player);

        List<Map.Entry<String, String>> cable = RandomUtil.fromList(
                RandomUtil.fromList(List.of(
                        Cables.coaxial, Cables.optical, Cables.symmetrical
                ))
        );

        this.questionPart = RandomUtil.fromList(cable.stream().filter(it -> !it.getValue().equals("skip")).toList());
        Component component = Component
                .text("[Ох уж эти маркировки]", RED)
                .appendSpace()
                .append(Component.text("Определите что означает выделеный сегмент в маркировке кабеля: ", WHITE));

        for (Map.Entry<String, String> cablePart : cable) {
            if (cablePart == questionPart) {
                component = component.append(
                        Component.text(cablePart.getKey(), BLUE).decorate(BOLD, UNDERLINED)
                );
            } else {
                component = component.append(
                        Component.text(cablePart.getKey(), WHITE)
                );
            }
        }

        List<Map.Entry<String, String>> variants = new ArrayList<>(RandomUtil.fromList(
                RandomUtil.fromList(List.of(
                        Cables.coaxial, Cables.optical, Cables.symmetrical
                ))
        ).stream().filter(it -> !it.getValue().equals(questionPart.getValue())).toList());

        if (variants.size() > 3) {
            variants = variants.subList(0, 3);
        }

        trueVariantNumber = RandomUtil.random.nextInt(3);
        variants.set(trueVariantNumber, questionPart);

        component = component
                .appendNewline()
                .appendNewline()
                .append(Component.text("Варианты: ", WHITE));

        for (int i = 0; i < variants.size(); i++) {
            component = component
                    .append(Component.text((i + 1) + ") " + variants.get(i).getValue(), WHITE))
                    .appendNewline();
        }

        component = component.appendNewline()
                .append(text("Мы верим что " + player.getName() + " знает правильный ответ", WHITE));


        player.getServer().broadcast(component);
    }
}
