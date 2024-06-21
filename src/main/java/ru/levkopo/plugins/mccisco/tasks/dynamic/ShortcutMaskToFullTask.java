package ru.levkopo.plugins.mccisco.tasks.dynamic;

import ru.levkopo.plugins.mccisco.UniqueItems;
import ru.levkopo.plugins.mccisco.tasks.CiscoInterfaceTask;
import ru.levkopo.plugins.mccisco.tasks.QuestionTask;
import ru.levkopo.plugins.mccisco.tasks.Task;
import ru.levkopo.plugins.mccisco.tasks.result.GiveItemResult;
import ru.levkopo.plugins.mccisco.tasks.result.TaskResult;
import ru.levkopo.plugins.mccisco.tasks.result.UpgradeWorldBorder;
import ru.levkopo.plugins.mccisco.utils.MaskUtil;
import ru.levkopo.plugins.mccisco.utils.RandomUtil;

import java.util.Objects;

public class ShortcutMaskToFullTask extends DynamicTask {
    public ShortcutMaskToFullTask() {
        super(new UpgradeWorldBorder(50));
    }

    @Override
    public Task generateTask(TaskResult result) {
        String ipAddress = RandomUtil.ipAddress();
        int mask = RandomUtil.mask();
        String fullMask = MaskUtil.fromShortToFull(mask);

        return new QuestionTask(
                "Как выглядит маска для ip адреса "+ipAddress+"/"+mask+"?",
                fullMask,
                result
        );
    }
}
