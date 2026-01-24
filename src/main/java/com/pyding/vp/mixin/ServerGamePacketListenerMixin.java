package com.pyding.vp.mixin;

import com.pyding.vp.util.VPUtil;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ServerGamePacketListenerImpl.class, priority = 0)
public abstract class ServerGamePacketListenerMixin {


    @Shadow public ServerPlayer player;

    @Inject(method = "handleClientCommand",at = @At("HEAD"),cancellable = true, require = 1)
    private void command(ServerboundClientCommandPacket packet, CallbackInfo ci){
        ci.cancel();
        PacketUtils.ensureRunningOnSameThread(packet, (ServerGamePacketListenerImpl)(Object)this, this.player.serverLevel());
        this.player.resetLastActionTime();
        MinecraftServer server = this.player.getServer();
        ServerboundClientCommandPacket.Action serverboundclientcommandpacket$action = packet.getAction();
        switch (serverboundclientcommandpacket$action) {
            case PERFORM_RESPAWN:
                if (this.player.wonGame) {
                    this.player.wonGame = false;
                    this.player = server.getPlayerList().respawn(this.player, true, Entity.RemovalReason.CHANGED_DIMENSION);
                    CriteriaTriggers.CHANGED_DIMENSION.trigger(this.player, Level.END, Level.OVERWORLD);
                } else {
                    if (this.player.getHealth() > 0.0F) {
                        return;
                    }

                    this.player = server.getPlayerList().respawn(this.player, false, Entity.RemovalReason.KILLED);
                    if (server.isHardcore()) {
                        this.player.setGameMode(GameType.SPECTATOR);
                        ((GameRules.BooleanValue)this.player.level().getGameRules().getRule(GameRules.RULE_SPECTATORSGENERATECHUNKS)).set(false, server);
                    }
                }
                break;
            case REQUEST_STATS:
                this.player.getStats().sendStats(this.player);
        }
    }
}
