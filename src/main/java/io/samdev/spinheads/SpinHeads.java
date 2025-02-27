package io.samdev.spinheads;

import io.samdev.actionutil.ActionUtil;
import io.samdev.spinheads.command.MainCommand;
import io.samdev.spinheads.data.DataManager;
import io.samdev.spinheads.head.HeadManager;
import io.samdev.spinheads.util.Message;
import org.bukkit.plugin.java.JavaPlugin;

public class SpinHeads extends JavaPlugin
{
    private ActionUtil actionUtil;
    private HeadManager headManager;
    private DataManager dataManager;

    @Override
    public void onEnable()
    {
        preInit();

        actionUtil = ActionUtil.init(this);
        
        headManager = new HeadManager(this);
        dataManager = new DataManager(this);

        getCommand("spinheads").setExecutor(new MainCommand(this));
    }

    @Override
    public void onDisable()
    {
        getDataManager().save();
    }

    private void preInit()
    {
        saveDefaultConfig();
        Message.init(this);
    }

    public DataManager getDataManager()
    {
        return dataManager;
    }

    public HeadManager getHeadManager()
    {
        return headManager;
    }

    public ActionUtil getActionUtil() {
        return actionUtil;
    }
}
