package me.tallonscze.muteperms.item;

import com.mojang.logging.LogUtils;
import me.tallonscze.muteperms.Muteperms;
import me.tallonscze.muteperms.network.PacketHandler;
import me.tallonscze.muteperms.network.UpdateNameFormatPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class MuteCoinItem extends Item {

    public static class MuteCoinCurio implements ICurioItem {

        private static final Logger LOGGER = LogUtils.getLogger();

        @Override
        public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
            if(!(slotContext.entity() instanceof Player player)) {
                return;
            }
            CompoundTag persistentData = player.getPersistentData();
            CompoundTag deathPersistentData = persistentData.getCompound(Player.PERSISTED_NBT_TAG);

            deathPersistentData.putBoolean("OnUseCoin", true);
            persistentData.put(Player.PERSISTED_NBT_TAG, deathPersistentData);

            PacketHandler.sendToAllClients(new UpdateNameFormatPacket(player.getUUID(), true));
            Muteperms.PLAYER_MUTE_STATUS.put(player.getUUID(), true);
            player.refreshDisplayName();

            if (!player.level().isClientSide) {
                ServerPlayer serverPlayer = (ServerPlayer) player;
                serverPlayer.refreshTabListName();
            }

            ICurioItem.super.onEquip(slotContext, prevStack, stack);
        }

        @Override
        public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
            if(!(slotContext.entity() instanceof Player player)) {
                return;
            }
            CompoundTag persistentData = player.getPersistentData();
            CompoundTag deathPersistentData = persistentData.getCompound(Player.PERSISTED_NBT_TAG);

            deathPersistentData.putBoolean("OnUseCoin", false);
            persistentData.put(Player.PERSISTED_NBT_TAG, deathPersistentData);

            PacketHandler.sendToAllClients(new UpdateNameFormatPacket(player.getUUID(), false));
            Muteperms.PLAYER_MUTE_STATUS.put(player.getUUID(), false);
            player.refreshDisplayName();

            if (!player.level().isClientSide) {
                ServerPlayer serverPlayer = (ServerPlayer) player;
                serverPlayer.refreshTabListName();
            }

            ICurioItem.super.onUnequip(slotContext, newStack, stack);
        }
    }

    public MuteCoinItem(){
        super(new Properties()
                .rarity(Rarity.EPIC)
                .stacksTo(1)
                .setNoRepair()
                .fireResistant()
        );
    }
}
