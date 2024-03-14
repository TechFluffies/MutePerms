package me.tallonscze.muteperms.server;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.logging.LogUtils;
import me.tallonscze.muteperms.config.Config;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class MuteCommand {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static List<String> UNMUTED_PLAYERS = null;

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("pmute")
                .then(Commands.argument("player", EntityArgument.player())
                        .requires(cs -> cs.hasPermission(3))
                        .executes(context -> {
                            ServerPlayer player = EntityArgument.getPlayer(context, "player");
                            String uuidStr = player.getUUID().toString();

                            boolean isUnmuted = UNMUTED_PLAYERS.contains(uuidStr);

                            if (isUnmuted) {
                                UNMUTED_PLAYERS.remove(uuidStr);
                            } else {
                                UNMUTED_PLAYERS.add(uuidStr);
                            }

                            Config.save();

                            LOGGER.info(context.getSource().getTextName() + " changed mute to " + (isUnmuted ? "unmuted" : "muted") + " for " + player.getName());

                            return 1;
                        })


                ));
    }
}
