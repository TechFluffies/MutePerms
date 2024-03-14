package me.tallonscze.muteperms.client;

import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.slf4j.Logger;

@OnlyIn(Dist.CLIENT)
public class ClientSideOnly {

    public static final ClientSideOnly INSTANCE = new ClientSideOnly();

    public void init() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent event) {
        Player player = event.getEntity();
        if (player != null) {
            //player.setCustomName(Component.literal("jjjjj"));
            //player.refreshDisplayName();
        }
    }

    private static final Logger LOGGER = LogUtils.getLogger();

    /*@SubscribeEvent
    public void onPlayerTagName(RenderNameTagEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Player player)
        {
            if (getMutedCoinOn(player))
            {
                event.setContent(Component.literal("muted."));
                LOGGER.info(player.getName() + " muted");
            }
            else {
                event.setContent(Component.literal("?."));
                LOGGER.info(player.getName() + " not muted");
            }
        }

    }*/

    public static boolean getMutedCoinOn(Player player) {
        return player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG).getBoolean("OnUseCoin");
    }

    /*@SubscribeEvent
    public void onPlayerTabListNameFormat(PlayerEvent.TabListNameFormat event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();

        if (getMutedCoinOn(player)) {
            event.setDisplayName(Component.literal("§k" + event.getEntity().getName()));
        } else {
            event.setDisplayName(event.getEntity().getName());
        }

        player.setCustomName(Component.literal("yo"));
        player.setCustomNameVisible(true);

        Scoreboard sc = player.getScoreboard();

        Objective objective = sc.getDisplayObjective(2);

        sc.getObjective("bellowName").setDisplayName(Component.literal("ojoj"));

        if (objective != null)
            objective.setDisplayName(Component.literal("§k" + event.getEntity().getName()));
    }*/

    public void scoreBoardCreate() {
        Scoreboard sc = new Scoreboard();
        Objective obj = new Objective(sc, "bellowName", ObjectiveCriteria.DUMMY, Component.literal(" "), ObjectiveCriteria.RenderType.byId("2"));
    }

}
