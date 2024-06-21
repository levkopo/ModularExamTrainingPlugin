package ru.levkopo.plugins.mccisco.tasks.dynamic;

import ru.levkopo.plugins.mccisco.UniqueItems;
import ru.levkopo.plugins.mccisco.tasks.CiscoInterfaceTask;
import ru.levkopo.plugins.mccisco.tasks.Task;
import ru.levkopo.plugins.mccisco.tasks.result.GiveItemResult;
import ru.levkopo.plugins.mccisco.tasks.result.TaskResult;
import ru.levkopo.plugins.mccisco.utils.MaskUtil;
import ru.levkopo.plugins.mccisco.utils.RandomUtil;

import java.util.Objects;

public class ConsoleSetupTask extends DynamicTask {
    public ConsoleSetupTask() {
        super(new GiveItemResult(UniqueItems.MEDIUM_ITEMS));
    }

    @Override
    public Task generateTask(TaskResult result) {
        String ipAddress = RandomUtil.ipAddress();
        String username = RandomUtil.username();
        String password = RandomUtil.password();
        int privilege = RandomUtil.privilege();
        int mask = RandomUtil.mask();
        String fullMask = MaskUtil.fromShortToFull(mask);

        return new CiscoInterfaceTask(
                result,
                RandomUtil.deviceName(),
                CiscoInterfaceTask.Context.NONE,
                "Настройте ip адрес "+ipAddress+"/"+mask+" на интерфейсе GigabitEthernet2/0. " +
                        "Также включите этот интерфейс и защитите консольный порт, создав пользователя "+username+" с паролем "+password+". Дайте пользователю привелегию "+privilege,
                (data) -> data.verifyInterface("GigabitEthernet", 2, 0,
                        inf -> inf.enabled && Objects.equals(inf.ipAddress, ipAddress) && Objects.equals(inf.mask, fullMask))
                        && data.auth(username, password, privilege) && data.lines.get("console").getFirst().login == CiscoInterfaceTask.Line.Login.LOCAL
        );
    }
}
