package me.tallonscze.muteperms.item;

import me.tallonscze.muteperms.Muteperms;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, Muteperms.MODID);
    public static final RegistryObject<Item> MUTE_COIN = REGISTRY.register("mutecoin", MuteCoinItem::new);
}
