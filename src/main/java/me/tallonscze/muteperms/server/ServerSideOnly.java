package me.tallonscze.muteperms.server;

import com.mojang.logging.LogUtils;
import me.tallonscze.muteperms.Muteperms;
import me.tallonscze.muteperms.network.PacketHandler;
import me.tallonscze.muteperms.network.UpdateNameFormatPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.slf4j.Logger;

@OnlyIn(Dist.DEDICATED_SERVER)
public class ServerSideOnly {

    public static final ServerSideOnly INSTANCE = new ServerSideOnly();

    public void init() {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new MuteCommand());
    }

    @SubscribeEvent
    public void onServerChat(ServerChatEvent event) {
        if (!MuteCommand.UNMUTED_PLAYERS.contains(event.getPlayer().getUUID().toString()))
        {
            event.setCanceled(true);
        }

        //event.setCanceled(true);
        //event.getPlayer().sendSystemMessage(Component.literal("<"+event.getPlayer().getName().getString()+">"+event.getMessage().getString()));
        //event.getPlayer().refreshTabListName();
    }

    @SubscribeEvent
    public void onDimensionChanged(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();

        PacketHandler.sendToAllClients(new UpdateNameFormatPacket(player.getUUID(), getMutedCoinOn(player)));
    }

    @SubscribeEvent
    public void onPlayerTabListNameFormat(PlayerEvent.TabListNameFormat event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();

        if (getMutedCoinOn(player))
        {
            event.setDisplayName(Component.literal(Muteperms.MUTED_NAME));
        }
        else
        {
            event.setDisplayName(event.getEntity().getName());
        }
    }

    public static boolean getMutedCoinOn(Player player) {
        return player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG).getBoolean("OnUseCoin");
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Muteperms.PLAYER_MUTE_STATUS.forEach((uuid, bool) -> {
            PacketHandler.sendToClient((ServerPlayer) event.getEntity(), new UpdateNameFormatPacket(uuid, bool));
        });
    }

}
