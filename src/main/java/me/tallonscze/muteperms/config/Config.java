package me.tallonscze.muteperms.config;

import me.tallonscze.muteperms.Muteperms;
import me.tallonscze.muteperms.server.MuteCommand;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = Muteperms.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> UNMUTED_PLAYERS = BUILDER
            .comment("List of players that are enabled to chat")
            .defineListAllowEmpty("unmuted_players", new ArrayList<>(), Config::validateUUID);

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    private static final Pattern UUID_REGEX = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    private static boolean validateUUID(Object obj)
    {
        return obj instanceof String str && UUID_REGEX.matcher(str).matches();
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        MuteCommand.UNMUTED_PLAYERS = new ArrayList<>(UNMUTED_PLAYERS.get());
    }

    public static void save()
    {
        UNMUTED_PLAYERS.set(List.copyOf(MuteCommand.UNMUTED_PLAYERS));
        UNMUTED_PLAYERS.save();
    }

}
