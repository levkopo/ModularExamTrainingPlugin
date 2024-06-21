package ru.levkopo.plugins.mccisco.tasks.dynamic;

import ru.levkopo.plugins.mccisco.tasks.CiscoInterfaceTask;
import ru.levkopo.plugins.mccisco.tasks.Task;
import ru.levkopo.plugins.mccisco.tasks.result.TaskResult;
import ru.levkopo.plugins.mccisco.tasks.result.UpgradeWorldBorder;
import ru.levkopo.plugins.mccisco.utils.RandomUtil;

public class HostnameSetupTask extends DynamicTask {
    public HostnameSetupTask() {
        super(new UpgradeWorldBorder(100));
    }

    @Override
    public Task generateTask(TaskResult result) {
        String hostname = RandomUtil.deviceName();

        return new CiscoInterfaceTask(
                result,
                RandomUtil.deviceName(),
                CiscoInterfaceTask.Context.NONE,
                "Настройте на коммутаторе имя устройства "+hostname,
                (data) -> data.hostname.equals(hostname)
        );
    }
}
