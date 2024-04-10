package com.pyding.vp.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.CombatTracker;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

import static com.mojang.text2speech.Narrator.LOGGER;

@Mixin(value = LivingEntity.class)
public abstract class DeadInsideMixin {

   /* @Shadow @Nullable public abstract LivingEntity getKillCredit();

    @Shadow protected boolean dead;

    @Shadow protected abstract void dropAllDeathLoot(DamageSource p_21192_);


    @Inject(method = "die",at = @At("HEAD"),cancellable = true, require = 1)
    private void die(DamageSource p_21014_, CallbackInfo info){
        System.out.println("mixin die!!!");
        if(this.getKillCredit() != null && this.getKillCredit().getPersistentData().getLong("VPDeadInsider") >= System.currentTimeMillis()) {
            System.out.println("mixin yes");
            Entity victim = p_21014_.getDirectEntity();
            this.dead = true;
            Level level = victim.level();
            if (level instanceof ServerLevel && victim instanceof LivingEntity livingEntity) {
                ServerLevel serverlevel = (ServerLevel)level;
                if (victim == null || victim.killedEntity(serverlevel, livingEntity)) {
                    victim.gameEvent(GameEvent.ENTITY_DIE);
                    this.dropAllDeathLoot(p_21014_);
                }

                victim.level().broadcastEntityEvent(victim, (byte)3);
            }
            victim.setPose(Pose.DYING);
            info.cancel();
        }
    }*/

}
