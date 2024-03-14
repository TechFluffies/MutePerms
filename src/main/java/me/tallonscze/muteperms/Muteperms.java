package me.tallonscze.muteperms;


import com.mojang.logging.LogUtils;
import me.tallonscze.muteperms.client.ClientSideOnly;
import me.tallonscze.muteperms.item.ItemRegistry;
import me.tallonscze.muteperms.item.MuteCoinItem;
import me.tallonscze.muteperms.network.PacketHandler;
import me.tallonscze.muteperms.network.UpdateNameFormatPacket;
import me.tallonscze.muteperms.server.ServerSideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

import java.util.*;
import java.util.logging.Level;


@Mod(Muteperms.MODID)
public class Muteperms {

    public static final String MODID = "muteperms";
    public static final String PROTOCOL_VERSION = "1";

    private static final Logger LOGGER = LogUtils.getLogger();


    public Muteperms() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ItemRegistry.REGISTRY.register(modEventBus);

        modEventBus.addListener(this::enqueueIMC);
        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        PacketHandler.register();

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientSideOnly.INSTANCE::init);
        DistExecutor.unsafeRunWhenOn(Dist.DEDICATED_SERVER, () -> ServerSideOnly.INSTANCE::init);
    }

    private void commonSetup(FMLCommonSetupEvent event)
    {
        CuriosApi.registerCurio(ItemRegistry.MUTE_COIN.get(), new MuteCoinItem.MuteCoinCurio());
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.RING.getMessageBuilder().build());
    }

    public static Map<UUID, Boolean> PLAYER_MUTE_STATUS = new HashMap<>();

    @SubscribeEvent
    public void onPlayerTabListNameFormat(PlayerEvent.NameFormat event) {
        Player player = event.getEntity();

        if (!PLAYER_MUTE_STATUS.containsKey(player.getUUID()))
            return;

        if (PLAYER_MUTE_STATUS.get(player.getUUID())) {
            event.setDisplayname(Component.literal("§kmuted"));
        } else {
            event.setDisplayname(event.getDisplayname());
        }

        if (!player.level().isClientSide()) {
            ServerPlayer serverPlayer = (ServerPlayer) player;
            serverPlayer.refreshTabListName();
        }
    }
}
