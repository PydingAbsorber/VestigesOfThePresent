package com.pyding.vp.mixin;

import com.pyding.vp.effects.VipEffect;
import com.pyding.vp.util.GradientUtil;
import com.pyding.vp.util.VPUtil;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.effect.MobEffect;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ServerGamePacketListenerImpl.class, priority = 0)
public abstract class ServerGamePacketListenerMixin {


    @Shadow public ServerPlayer player;

    @Shadow @Final private MinecraftServer server;

    @Inject(method = "handleClientCommand",at = @At("HEAD"),cancellable = true, require = 1)
    private void command(ServerboundClientCommandPacket packet, CallbackInfo ci){
        ci.cancel();
        PacketUtils.ensureRunningOnSameThread(packet, (ServerGamePacketListener) this, this.player.serverLevel());
        this.player.resetLastActionTime();
        ServerboundClientCommandPacket.Action serverboundclientcommandpacket$action = packet.getAction();
        switch (serverboundclientcommandpacket$action) {
            case PERFORM_RESPAWN:
                if (this.player.wonGame) {
                    this.player.wonGame = false;
                    this.player = this.server.getPlayerList().respawn(this.player, true);
                    CriteriaTriggers.CHANGED_DIMENSION.trigger(this.player, Level.END, Level.OVERWORLD);
                } else {
                    Player player = this.player;
                    VPUtil.printTrack("Was Command Revive",player);
                    if(!VPUtil.canTeleport(player))
                        VPUtil.antiTp(player,-1);
                    if(!VPUtil.canResurrect(player))
                        VPUtil.antiResurrect(player,-1);
                    if(VPUtil.isRoflanEbalo(player))
                        VPUtil.setRoflanEbalo(player,-1);
                    this.player = this.server.getPlayerList().respawn(this.player, false);
                    if (this.server.isHardcore()) {
                        this.player.setGameMode(GameType.SPECTATOR);
                        this.player.level().getGameRules().getRule(GameRules.RULE_SPECTATORSGENERATECHUNKS).set(false, this.server);
                    }
                }
                break;
            case REQUEST_STATS:
                this.player.getStats().sendStats(this.player);
        }
        /*if(packet.getAction() == ServerboundClientCommandPacket.Action.PERFORM_RESPAWN){

        }*/
    }
}
