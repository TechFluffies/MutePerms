package me.tallonscze.muteperms.network;

import com.mojang.logging.LogUtils;
import me.tallonscze.muteperms.Muteperms;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import org.slf4j.Logger;

import java.util.UUID;
import java.util.function.Supplier;

public class UpdateNameFormatPacket {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final UUID playerUUID;
    private final boolean muted;

    public UpdateNameFormatPacket(UUID playerUUID, boolean muted) {
        this.playerUUID = playerUUID;
        this.muted = muted;
    }

    public UpdateNameFormatPacket(FriendlyByteBuf buf) {
        this.playerUUID = buf.readUUID();
        this.muted = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf)
    {
        buf.writeUUID(playerUUID);
        buf.writeBoolean(muted);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();

        Muteperms.PLAYER_MUTE_STATUS.put(playerUUID, muted);

        if (Minecraft.getInstance().level == null)
            return;

        Player player = Minecraft.getInstance().level.getPlayerByUUID(playerUUID);

        if (player == null)
            return;
        player.refreshDisplayName();

        context.setPacketHandled(true);
    }
}
