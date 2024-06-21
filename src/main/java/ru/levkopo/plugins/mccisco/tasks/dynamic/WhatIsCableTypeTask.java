package ru.levkopo.plugins.mccisco.tasks.dynamic;

import ru.levkopo.plugins.mccisco.Cables;
import ru.levkopo.plugins.mccisco.tasks.QuestionTask;
import ru.levkopo.plugins.mccisco.tasks.Task;
import ru.levkopo.plugins.mccisco.tasks.result.TaskResult;
import ru.levkopo.plugins.mccisco.tasks.result.UpgradeWorldBorder;
import ru.levkopo.plugins.mccisco.utils.RandomUtil;

import java.util.List;
import java.util.Map;

public class WhatIsCableTypeTask extends DynamicTask {
    public WhatIsCableTypeTask() {
        super(new UpgradeWorldBorder(50));
    }

    @Override
    public Task generateTask(TaskResult result) {
        String domainName = RandomUtil.domainName();
        String type = RandomUtil.fromArray(new String[]{"оптический", "симметричный", "коаксиальный"});
        List<Map.Entry<String, String>> cableLabel = switch (type) {
            case "оптический" -> RandomUtil.fromList(Cables.optical);
            case "симметричный" -> RandomUtil.fromList(Cables.symmetrical);
            case "коаксиальный" -> RandomUtil.fromList(Cables.coaxial);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };


        return new QuestionTask(
                "К какому типу относится кабель с маркировкой " + Cables.getShortcut(cableLabel) + ": оптический, симметричный, коаксиальный?",
                type,
                new UpgradeWorldBorder(80)
        );
    }
}
