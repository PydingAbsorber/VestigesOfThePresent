package com.pyding.vp.item.vestiges;

import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.util.VPUtil;
import com.pyding.vp.util.Vector3;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.*;

public class Archlinx extends Vestige{
    public Archlinx(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage, ItemStack stack) {
        super.dataInit(25, ChatFormatting.BLUE, 2, 25, 1, 275, 120, 9999, true, stack);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level, ItemStack stack) {
        if(new Random().nextDouble() < 0.5)
            VPUtil.play(player,SoundRegistry.ARROW_READY_1.get());
        else VPUtil.play(player,SoundRegistry.ARROW_READY_2.get());
        VPUtil.spawnSphere(player,ParticleTypes.SNOWFLAKE,35,1.5f,0.2f);
        player.getPersistentData().putInt("VPArchShots",5);
        super.doSpecial(seconds, player, level, stack);
    }

    @Override
    public int setUltimateActive(long seconds, Player player, ItemStack stack) {
        if(isUltimateActive(stack) && !player.getCommandSenderWorld().isClientSide){
            if(currentChargeUltimate(stack) > 0) {
                if(!player.getCommandSenderWorld().isClientSide) {
                    setTimeUlt(1,stack);
                    setUltimateActive(true,stack);
                    //setCdUltimateActive(cdUltimateActive(stack)+ultimateCd(stack),stack);     //time until cd recharges in seconds*tps
                    Random random = new Random();
                    if(!(VPUtil.getSet(player) == 3 && random.nextDouble() < VPUtil.getChance(0.3,player)) || !(VPUtil.getSet(player) == 6 && random.nextDouble() < VPUtil.getChance(0.5,player)) || random.nextDouble() < VPUtil.getChance(player.getPersistentData().getFloat("VPDepth")/10,player))
                        setCurrentChargeUltimate(currentChargeUltimate(stack)-1,stack);
                    long bonus = 1+(long)player.getPersistentData().getFloat("VPDurationBonusDonut")/1000;
                    if(damageType == null)
                        init(stack);
                    this.doUltimate(seconds*bonus, player, player.getCommandSenderWorld(), stack);
                } else this.localSpecial(player);
            }
            return 0;
        }
        return super.setUltimateActive(seconds, player, stack);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level, ItemStack stack) {
        VPUtil.play(player,SoundRegistry.MAGIC_ARROW_1.get());
        VPUtil.spawnParticles(player, ParticleTypes.ENCHANTED_HIT,3,1,0,0.1,0,0.7, 0.5, 1.0);
        super.doUltimate(seconds, player, level, stack);
    }

    @Override
    public void ultimateEnds(Player player, ItemStack stack) {
        VPUtil.play(player,SoundRegistry.MAGIC_ARROW_2.get());
        float damage = player.getPersistentData().getInt("VPArchdamage");
        double range = 6;
        Vector3 target = Vector3.fromEntityCenter(player);
        List<LivingEntity> entities = new ArrayList<>();
        for (int distance = 1; distance < 50; ++distance) {
            target = target.add(new Vector3(player.getLookAngle()).multiply(distance)).add(0.0, 0.5, 0.0);
            VPUtil.spawnParticles(player,ParticleTypes.GLOW_SQUID_INK,target.x,target.y,target.z,10,0,0,0);
            List<LivingEntity> list = player.getCommandSenderWorld().getEntitiesOfClass(LivingEntity.class, new AABB(target.x - range, target.y - range, target.z - range, target.x + range, target.y + range, target.z + range));
            list.removeIf(entity -> entity == player || !player.hasLineOfSight(entity));
            for(LivingEntity entity: list){
                if(!entities.contains(entity)) {
                    entities.add(entity);
                    if(!VPUtil.isProtectedFromHit(player,entity)) {
                        VPUtil.spawnSphere(entity, ParticleTypes.GLOW_SQUID_INK, 20, 2, 0.4f);
                        VPUtil.dealDamage(entity, player, player.damageSources().indirectMagic(player, player), VPUtil.scalePower(damage,25,player), 3, true);
                        VPUtil.addRadiance(this.getClass(),VPUtil.getRadianceUltimate(),player);
                    }
                }
            }
        }
        player.getPersistentData().putFloat("VPArchdamage",0);
        super.ultimateEnds(player, stack);
    }

    @Override
    public void curioSucks(Player player, ItemStack stack) {
        removeModifiers(player);
        super.curioSucks(player, stack);
    }

    public static void removeModifiers(Player player){
        AttributeMap map = player.getAttributes();
        List<Attribute> list = VPUtil.attributeList();
        for(int i = 0; i < list.size(); i++){
            if(map.hasAttribute(BuiltInRegistries.ATTRIBUTE.wrapAsHolder(list.get(i)))) {
                player.getAttributes().removeAttributeModifiers(VPUtil.createAttributeMap(BuiltInRegistries.ATTRIBUTE.wrapAsHolder(list.get(i)), 0, AttributeModifier.Operation.ADD_VALUE, "vp:arch"+list.get(i).getDescriptionId()));
            }
        }
    }
}
