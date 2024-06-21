package ru.levkopo.plugins.mccisco.tasks.dynamic;

import ru.levkopo.plugins.mccisco.tasks.CiscoInterfaceTask;
import ru.levkopo.plugins.mccisco.tasks.Task;
import ru.levkopo.plugins.mccisco.tasks.result.TaskResult;
import ru.levkopo.plugins.mccisco.tasks.result.TimeUpgradeWorldBorder;
import ru.levkopo.plugins.mccisco.tasks.result.UpgradeWorldBorder;
import ru.levkopo.plugins.mccisco.utils.RandomUtil;

import java.util.Objects;

public class DomainNameSetupTask extends DynamicTask {
    public DomainNameSetupTask() {
        super(new UpgradeWorldBorder(100));
    }

    @Override
    public Task generateTask(TaskResult result) {
        String domainName = RandomUtil.domainName();

        return new CiscoInterfaceTask(
                result,
                RandomUtil.deviceName(),
                CiscoInterfaceTask.Context.NONE,
                "Настройте доменное имя "+domainName+" на роутере",
                (data) -> Objects.equals(data.domainName, domainName)
        );
    }
}
