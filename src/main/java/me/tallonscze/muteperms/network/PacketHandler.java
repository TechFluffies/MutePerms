package me.tallonscze.muteperms.network;

import me.tallonscze.muteperms.Muteperms;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(Muteperms.MODID,
                    "play"),
            () -> Muteperms.PROTOCOL_VERSION,
            Muteperms.PROTOCOL_VERSION::equals,
            Muteperms.PROTOCOL_VERSION::equals);

    public static void register()
    {
        CHANNEL.messageBuilder(UpdateNameFormatPacket.class, 1, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(UpdateNameFormatPacket::encode)
                .decoder(UpdateNameFormatPacket::new)
                .consumerMainThread(UpdateNameFormatPacket::handle)
                .add();
    }

    public static void sendToServer(Object obj)
    {
        CHANNEL.sendToServer(obj);
    }

    public static void sendToAllClients(Object obj)
    {
        CHANNEL.send(PacketDistributor.ALL.noArg(), obj);
    }

}
