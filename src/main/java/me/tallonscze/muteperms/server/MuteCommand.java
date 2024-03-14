package me.tallonscze.muteperms.server;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MuteCommand {
    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("pmute")
                .then(Commands.argument("player", EntityArgument.player())
                        .requires(cs -> cs.hasPermission(3))
                        .executes(context -> {
                            ServerPlayer player = EntityArgument.getPlayer(context, "player");


                            return 1;
                        })


                ));
    }
}
