package me.tallonscze.muteperms.network;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import org.slf4j.Logger;

import java.util.UUID;
import java.util.function.Supplier;

public class UpdateNameFormatPacket {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final UUID playerUUID;

    public UpdateNameFormatPacket(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public UpdateNameFormatPacket(FriendlyByteBuf buf) {
        this.playerUUID = buf.readUUID();
    }

    public void encode(FriendlyByteBuf buf)
    {
        buf.writeUUID(playerUUID);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();

        ServerPlayer serverPlayer = Minecraft.getInstance().player.getServer().getPlayerList().getPlayer(playerUUID);
        serverPlayer.refreshDisplayName();

        LOGGER.info("Got update packet from " + context.getSender().getName() + " to " + Minecraft.getInstance().player.getName());


        context.setPacketHandled(true);
    }
}
