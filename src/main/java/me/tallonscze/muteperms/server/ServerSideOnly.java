package me.tallonscze.muteperms.server;

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.DEDICATED_SERVER)
public class ServerSideOnly {

    public static final ServerSideOnly INSTANCE = new ServerSideOnly();

    private MinecraftServer server;

    public void init() {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new MuteCommand());
    }

    public MinecraftServer getServer() {
        return server;
    }

    @SubscribeEvent
    public void onServerChat(ServerChatEvent event) {
        event.setCanceled(true);
        //event.getPlayer().sendSystemMessage(Component.literal("<"+event.getPlayer().getName().getString()+">"+event.getMessage().getString()));
        //event.getPlayer().refreshTabListName();
    }

    @SubscribeEvent
    public void onPlayerTabListNameFormat(PlayerEvent.TabListNameFormat event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();

        if (getMutedCoinOn(player))
        {
            event.setDisplayName(Component.literal("muted"));
            player.setCustomName(Component.literal("muted."));
            player.setCustomNameVisible(true);
        }
        else
        {
            event.setDisplayName(Component.literal("yoooo"));
            player.setCustomName(Component.literal("yoooo."));
            player.setCustomNameVisible(true);
        }
    }

    public static boolean getMutedCoinOn(Player player) {
        return player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG).getBoolean("OnUseCoin");
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {

    }

    @SubscribeEvent
    public void onServerStarted(ServerStartingEvent event) {
        server = event.getServer();
    }

}
