package ru.levkopo.plugins.mccisco.tasks.dynamic;

import ru.levkopo.plugins.mccisco.UniqueItems;
import ru.levkopo.plugins.mccisco.tasks.CiscoInterfaceTask;
import ru.levkopo.plugins.mccisco.tasks.Task;
import ru.levkopo.plugins.mccisco.tasks.result.GiveItemResult;
import ru.levkopo.plugins.mccisco.tasks.result.TaskResult;
import ru.levkopo.plugins.mccisco.utils.RandomUtil;

import java.util.Objects;
import java.util.function.Predicate;

public class PAgPSetupTask extends DynamicTask {
    public PAgPSetupTask() {
        super(new GiveItemResult(UniqueItems.MEDIUM_ITEMS));
    }

    @Override
    public Task generateTask(TaskResult result) {
        int channelGroup = RandomUtil.random.nextInt(16);
        boolean active = RandomUtil.random.nextBoolean();
        Predicate<CiscoInterfaceTask.NetInterface> verifyInterface = intf -> intf.channelGroup != null &&
                intf.channelGroup.number == channelGroup &&
                (Objects.equals(intf.channelGroup.mode, "desirable") || (active && Objects.equals(intf.channelGroup.mode, "auto")));

        return new CiscoInterfaceTask(
                result,
                RandomUtil.deviceName(),
                CiscoInterfaceTask.Context.NONE,
                "Настройте агрегацию по протоколу PAgP на интерфейсах GigabitEtherne1/0, GigabitEtherne2/0. Гарантируется что на другом устройстве настроен "+
                        (active ? "desirable": "auto")+" режим по "+channelGroup+" группе каналов",
                (data) -> data.verifyInterface("GigabitEthernet", 1, 0, verifyInterface) &&
                        data.verifyInterface("GigabitEthernet", 2, 0, verifyInterface)
        );
    }
}
