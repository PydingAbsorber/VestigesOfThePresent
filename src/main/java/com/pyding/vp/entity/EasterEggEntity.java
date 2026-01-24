package com.pyding.vp.entity;

import com.pyding.vp.item.ModItems;
import com.pyding.vp.util.VPUtil;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class EasterEggEntity extends Projectile {

    public EasterEggEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    public EasterEggEntity(Level pLevel) {
        this(ModEntities.EASTER_EGG_ENTITY.get(), pLevel);
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
            VPUtil.giveStack(new ItemStack(ModItems.EASTER_EGG.get()),player);
            VPUtil.play(player, SoundEvents.CHICKEN_EGG);
        }
    }


    @Override
    public boolean displayFireAnimation() {
        return false;
    }
}
