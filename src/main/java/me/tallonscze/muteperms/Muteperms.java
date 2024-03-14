package me.tallonscze.muteperms;


import me.tallonscze.muteperms.client.ClientSideOnly;
import me.tallonscze.muteperms.item.ItemRegistry;
import me.tallonscze.muteperms.item.MuteCoinItem;
import me.tallonscze.muteperms.network.PacketHandler;
import me.tallonscze.muteperms.server.ServerSideOnly;
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
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;


@Mod(Muteperms.MODID)
public class Muteperms {

    public static final String MODID = "muteperms";
    public static final String PROTOCOL_VERSION = "1";


    public Muteperms() {


        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ItemRegistry.REGISTRY.register(modEventBus);

        modEventBus.addListener(this::enqueueIMC);
        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientSideOnly.INSTANCE::init);
        DistExecutor.unsafeRunWhenOn(Dist.DEDICATED_SERVER, () -> ServerSideOnly.INSTANCE::init);
    }

    private void commonSetup(FMLCommonSetupEvent event)
    {
        event.enqueueWork(PacketHandler::register);

        CuriosApi.registerCurio(ItemRegistry.MUTE_COIN.get(), new MuteCoinItem.MuteCoinCurio());
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.RING.getMessageBuilder().build());
    }

    @SubscribeEvent
    public void onPlayerTabListNameFormat(PlayerEvent.NameFormat event) {
        Player player = event.getEntity();

        if (getMutedCoinOn(player)) {
            event.setDisplayname(Component.literal("lpdd"));
        } else {
            event.setDisplayname(Component.literal("yo"));
        }

        if (!player.level().isClientSide()) {
            ServerPlayer serverPlayer = (ServerPlayer) player;
            serverPlayer.refreshTabListName();
        }
    }

    public static boolean getMutedCoinOn(Player player) {
        return player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG).getBoolean("OnUseCoin");
    }
}
