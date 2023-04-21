package cn.ChengZhiYa;

import cn.ChengZhiYa.Listener.FriendMessage;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;

public final class ChengBot extends JavaPlugin {
    public static final ChengBot INSTANCE = new ChengBot();

    private ChengBot() {
        super(new JvmPluginDescriptionBuilder("cn.ChengZhiYa.ChengBot", "0.0.1")
                .name("ChengBot")
                .author("ChengZhiYa")
                .build());
    }

    @Override
    public void onEnable() {
        getLogger().info(getDataFolderPath().toString());
        GlobalEventChannel.INSTANCE.registerListenerHost(new FriendMessage());
    }
}