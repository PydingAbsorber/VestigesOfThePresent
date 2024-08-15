package com.pyding.vp.entity;

import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.util.VPUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class ShellHealEntity extends Projectile {

    public ShellHealEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    public ShellHealEntity(Level pLevel) {
        this(ModEntities.SHELLHEAL.get(), pLevel);
    }
    @Override
    public void refreshDimensions() {
        super.refreshDimensions();
        this.setBoundingBox(new AABB(this.getX(), this.getY(), this.getZ(), this.getX() + 0.1, this.getY() + 0.1, this.getZ() + 0.1));
        this.makeBoundingBox();
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void tick() {
        super.tick();
        if(this.getCommandSenderWorld().isClientSide)
            return;
        Player player = this.getCommandSenderWorld().getNearestPlayer(this,1);
        if(player != null){
            this.discard();
            switch (random.nextInt(6)){
                case 0: {
                    VPUtil.play(player, SoundRegistry.BUBBLEPOP.get());
                    break;
                }
                case 1: {
                    VPUtil.play(player, SoundRegistry.BUBBLE1.get());
                    break;
                }
                case 2: {
                    VPUtil.play(player, SoundRegistry.BUBBLE2.get());
                    break;
                }
                case 3: {
                    VPUtil.play(player, SoundRegistry.BUBBLE3.get());
                    break;
                }
                case 4: {
                    VPUtil.play(player, SoundRegistry.BUBBLE4.get());
                    break;
                }
                default: {
                    VPUtil.play(player, SoundRegistry.BUBBLE5.get());
                }
            }
            for(LivingEntity livingEntity: VPUtil.getEntitiesAround(this,20,20,20,false)) {
                if (livingEntity instanceof SillySeashell sillySeashell) {
                    sillySeashell.getPersistentData().putInt("VPShellHeal",sillySeashell.getPersistentData().getInt("VPShellHeal")+1);
                }
            }
        }
    }


    @Override
    public boolean displayFireAnimation() {
        return false;
    }
}
