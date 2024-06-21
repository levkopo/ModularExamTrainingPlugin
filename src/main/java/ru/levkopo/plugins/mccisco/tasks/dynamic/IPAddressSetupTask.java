package ru.levkopo.plugins.mccisco.tasks.dynamic;

import ru.levkopo.plugins.mccisco.UniqueItems;
import ru.levkopo.plugins.mccisco.tasks.CiscoInterfaceTask;
import ru.levkopo.plugins.mccisco.tasks.Task;
import ru.levkopo.plugins.mccisco.tasks.result.GiveItemResult;
import ru.levkopo.plugins.mccisco.tasks.result.TaskResult;
import ru.levkopo.plugins.mccisco.utils.RandomUtil;
import ru.levkopo.plugins.mccisco.utils.MaskUtil;

import java.util.Objects;

public class IPAddressSetupTask extends DynamicTask {
    public IPAddressSetupTask() {
        super(new GiveItemResult(UniqueItems.SIMPLE_ITEMS));
    }

    @Override
    public Task generateTask(TaskResult result) {
        String ipAddress = RandomUtil.ipAddress();
        int mask = RandomUtil.mask();
        String fullMask = MaskUtil.fromShortToFull(mask);

        return new CiscoInterfaceTask(
                result,
                RandomUtil.deviceName(),
                CiscoInterfaceTask.Context.NONE,
                "Настройте ip адрес "+ipAddress+"/"+mask+" на интерфейсе GigabitEthernet2/0. " +
                        "Также включите этот интерфейс",
                (data) -> data.verifyInterface("GigabitEthernet", 2, 0,
                        inf -> inf.enabled && Objects.equals(inf.ipAddress, ipAddress) && Objects.equals(inf.mask, fullMask))
        );
    }
}
