package com.pyding.vp.entity;

import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.item.ModItems;
import com.pyding.vp.item.Vortex;
import com.pyding.vp.item.vestiges.Vestige;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.network.packets.SendEntityNbtToClient;
import com.pyding.vp.util.ConfigHandler;
import com.pyding.vp.util.VPUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class VortexEntity extends Projectile {

    public VortexEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public List<String> items = new ArrayList<>();
    public int frags = 0;

    public VortexEntity(Level pLevel, LivingEntity owner) {
        this(ModEntities.VORTEX.get(), pLevel);
        setOwner(owner);
    }
    @Override
    public void refreshDimensions() {
        super.refreshDimensions();
        this.setBoundingBox(new AABB(this.getX(), this.getY(), this.getZ(), this.getX() + 1.0, this.getY() + 1.0, this.getZ() + 1.0));
        this.makeBoundingBox();
    }

    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        return EntityDimensions.scalable((1+items.size()) * 2.0F, (1+items.size()) * 2.0F);
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void tick() {
        super.tick();
        if(getCommandSenderWorld().isClientSide)
            return;
        float r = 5;
        Player player = (Player) getOwner();
        int maxSize = Vortex.getItems().size()-ConfigHandler.COMMON.vortexReduction.get();
        if(player == null) {
            discard();
            kill();
            return;
        }
        if(tickCount <= 2 && !getCommandSenderWorld().isClientSide)
            PacketHandler.sendToClients(PacketDistributor.TRACKING_ENTITY.with(() -> this), new SendEntityNbtToClient(getPersistentData(),getId()));
        getPersistentData().putLong("VPAntiTP",System.currentTimeMillis()+10000);
        setGlowingTag(true);
        for(ItemEntity itemEntity: getCommandSenderWorld().getEntitiesOfClass(ItemEntity.class, new AABB(getX()+r,getY()+r,getZ()+r,getX()-r,getY()-r,getZ()-r))){
            if(items.size() < maxSize) {
                boolean add = false;
                for (String element : ConfigHandler.COMMON.repairObjects.get().toString().split(",")) {
                    if (itemEntity.getItem().getDescriptionId().contains(element)) {
                        add = true;
                        for(String item: items){
                            if(item.equals(itemEntity.getItem().getDescriptionId()))
                                add = false;
                        }
                    }
                }
                if(add) {
                    items.add(itemEntity.getItem().getDescriptionId());
                    itemEntity.discard();
                }
                if(itemEntity.getItem().getItem() instanceof Vestige vestige){
                    items.add(itemEntity.getItem().getDescriptionId());
                    itemEntity.discard();
                    if(vestige.isTripleStellar(itemEntity.getItem()))
                        frags += 120;
                    else if(vestige.isDoubleStellar(itemEntity.getItem()))
                        frags += 80;
                    else if(vestige.isStellar(itemEntity.getItem()))
                        frags += 10;
                    else frags += 2;
                }
            }
            else {
                if(tickCount % 20 == 0)
                    VPUtil.spawnParticles(player, ParticleTypes.GLOW_SQUID_INK,4,4,4,1,0,0,0);
                if(itemEntity.getItem().isDamageableItem()){
                    ItemStack stack = itemEntity.getItem();
                    stack.setDamageValue(0);
                    stack.getOrCreateTag().putBoolean("VPUnbreak",true);
                    VPUtil.giveStack(stack,player);
                    itemEntity.discard();
                    discard();
                    kill();
                    if(frags > 0)
                        VPUtil.giveStack(new ItemStack(ModItems.STELLAR.get(),frags),player);
                }
            }
        }
        this.getPersistentData().putString("VPVortexList",VPUtil.filterString(items.toString()));
        if ((tickCount - 1) % loopSoundDurationInTicks == 0) {
            this.playSound(SoundRegistry.BLACK_HOLE.get(), items.size()+5, 1);
        }
        VPUtil.syncEntity(this);
        if(tickCount > 5*60*20) {
            discard();
            kill();
            player.addItem(new ItemStack(ModItems.VORTEX.get()));
        }
    }

    private static final int loopSoundDurationInTicks = 6 * 20;

    @Override
    public boolean displayFireAnimation() {
        return false;
    }
}
