package com.pyding.vp.item.vestiges;

import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.event.EventHandler;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.network.packets.PlayerFlyPacket;
import com.pyding.vp.util.VPUtil;
import com.pyding.vp.util.Vector3;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.VibrationParticleOption;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.phys.AABB;
import top.theillusivec4.curios.api.SlotContext;

import java.util.*;

public class Archlinx extends Vestige{
    public Archlinx(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage, ItemStack stack) {
        super.dataInit(25, ChatFormatting.BLUE, 2, 25, 1, 120, 120, 9999, true, stack);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level, ItemStack stack) {
        if(new Random().nextDouble() < 0.5)
            VPUtil.play(player,SoundRegistry.ARROW_READY_1.get());
        else VPUtil.play(player,SoundRegistry.ARROW_READY_2.get());
        VPUtil.spawnParticles(player, ParticleTypes.SNOWFLAKE,3,1,0,0.1,0,0.4, 0.7, 1.0);
        player.getPersistentData().putInt("VPArchShots",5);
        super.doSpecial(seconds, player, level, stack);
    }

    @Override
    public int setUltimateActive(long seconds, Player player, ItemStack stack) {
        if(isUltimateActive(stack) && !player.getCommandSenderWorld().isClientSide){
            setTimeUlt(1,stack);
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
                    VPUtil.spawnSphere(entity, ParticleTypes.GLOW_SQUID_INK,20,2,0.4f);
                    VPUtil.dealDamage(entity,player,player.damageSources().indirectMagic(player,player),damage,3,true);
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
            if(map.hasAttribute(list.get(i))) {
                player.getAttributes().addTransientAttributeModifiers(VPUtil.createAttributeMap(player,list.get(i), archUUIDs.get(i),0, AttributeModifier.Operation.ADDITION, "vp:arch"+list.get(i).getDescriptionId()));
            }
        }
    }

    public static List<UUID> archUUIDs = Arrays.asList(
            UUID.fromString("d2850e71-4de0-46b4-baeb-0ba28fdc54a5"),
            UUID.fromString("f0a7a122-3f55-4d8f-a18c-31520f849f63"),
            UUID.fromString("9dcf47de-6a44-4dd7-a1b7-7cd9a7f42114"),
            UUID.fromString("4ec6182d-4af2-4b67-bd0e-c3a60a826cb2"),
            UUID.fromString("f6e8c51e-68d9-4e0b-b8d9-b05f7b1c9b9b"),
            UUID.fromString("c41b8a9b-23f0-4e08-b7e2-64f5178beff8"),
            UUID.fromString("0a1f0d5e-7ff4-4c64-99cd-9acda29416d6"),
            UUID.fromString("35727d98-1b21-453e-9e75-fcae1173e0b7"),
            UUID.fromString("6d96373c-02e7-4933-9734-7b5b1d35df10"),
            UUID.fromString("e7032e0f-f9aa-409b-a28a-6318e358964a"),
            UUID.fromString("02491729-4d26-4f7b-bd90-cdd2b1c3d4ea")
    );
}
