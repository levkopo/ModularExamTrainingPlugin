package ru.levkopo.plugins.mccisco.tasks.dynamic;

import ru.levkopo.plugins.mccisco.UniqueItems;
import ru.levkopo.plugins.mccisco.tasks.CiscoInterfaceTask;
import ru.levkopo.plugins.mccisco.tasks.Task;
import ru.levkopo.plugins.mccisco.tasks.result.GiveItemResult;
import ru.levkopo.plugins.mccisco.tasks.result.TaskResult;
import ru.levkopo.plugins.mccisco.utils.MaskUtil;
import ru.levkopo.plugins.mccisco.utils.RandomUtil;

import java.util.Objects;

public class TelnetSetupTask extends DynamicTask {
    public TelnetSetupTask() {
        super(new GiveItemResult(UniqueItems.ULTIMATE_ITEMS));
    }

    @Override
    public Task generateTask(TaskResult result) {
        String domainName = RandomUtil.domainName();
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
                "Настройте ip адрес "+ipAddress+"/"+mask+" на интерфейсе GigabitEthernet2/0. Настройте доменное имя "+domainName+". " +
                        "Включите этот интерфейс и настройте удаленный доступ по telnet, создав пользователя "+username+" с паролем "+password+". Дайте пользователю привелегию "+privilege,                (data) -> data.verifyInterface("GigabitEthernet", 2, 0,
                        inf -> inf.enabled && Objects.equals(inf.ipAddress, ipAddress) && Objects.equals(inf.mask, fullMask))
                        && data.auth(username, password, privilege)
                        && Objects.equals(data.domainName, domainName)
                        && data.verifyLines("vty", line -> line.login == CiscoInterfaceTask.Line.Login.LOCAL && Objects.equals(line.transportInput, "telnet"))
        );
    }
}
