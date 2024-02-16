package com.pyding.vp.item.artifacts;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.pyding.vp.VestigesOfPresent;
import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.item.ModItems;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MaskOfDemon extends Vestige{
    public MaskOfDemon(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage) {
        vestigeNumber = 5;
        color = ChatFormatting.BLUE;
        specialCharges = 1;
        specialCd = 1;
        specialMaxTime = 666;
        ultimateMaxTime = 1;
        ultimateCharges = 1;
        ultimateCd = 60;
        super.dataInit(vestigeNumber, color, specialCharges, specialCd, ultimateCharges, ultimateCd, specialMaxTime, ultimateMaxTime, true);
    }

    private Multimap<Attribute, AttributeModifier> createAttributeMap(Player player, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> attributesDefault = HashMultimap.create();
        double attackMultiplier = 4;
        double speedMultiplier = 1;
        if(isStellar(stack)){
            attackMultiplier *= 1.5;
            speedMultiplier *= 1.5;
        }
        float attackScale = (float) ((VPUtil.missingHealth(player) * attackMultiplier) + 1)/100;
        float speedScale = (float) ((VPUtil.missingHealth(player) * speedMultiplier) + 1)/100;

        attributesDefault.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(UUID.fromString("ec62548c-5b26-401e-83fd-693e4aafa532"), "vp:attack_speed_modifier", attackScale, AttributeModifier.Operation.MULTIPLY_TOTAL));
        attributesDefault.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(UUID.fromString("f4ece564-d2c0-40d2-a96a-dc68b493137c"), "vp:speed_modifier", speedScale, AttributeModifier.Operation.MULTIPLY_BASE));

        return attributesDefault;
    }

    @Override
    public void onUnequip(SlotContext context, ItemStack newStack, ItemStack stack) {
        if (context.entity() instanceof Player player) {
            player.getAttributes().removeAttributeModifiers(this.createAttributeMap(player, stack));
        }
        super.onUnequip(context,newStack,stack);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        Player player = (Player) slotContext.entity();
        if(isSpecialActive) {
            if(player.tickCount % 5 == 0)
                player.getAttributes().addTransientAttributeModifiers(this.createAttributeMap(player, stack));
            if (player.tickCount % 20 == 0) {
                if (player.getHealth() > player.getMaxHealth() * 0.05) {
                    player.setHealth((float) (player.getHealth() - player.getMaxHealth() * 0.05));
                }
            }
            for(LivingEntity entity: VPUtil.getEntities(player,30,false)){
                CompoundTag tag = entity.getPersistentData();
                if (tag == null) {
                    tag = new CompoundTag();
                }
                tag.putFloat("VPHealResMask",0-VPUtil.missingHealth(player));
                if(isStellar(stack))
                    tag.putBoolean("MaskStellar",true);
                entity.getPersistentData().merge(tag);
            }
            player.getPersistentData().putFloat("VPHealResMask",0-VPUtil.missingHealth(player));
        } else player.getAttributes().removeAttributeModifiers(this.createAttributeMap(player, stack));
        super.curioTick(slotContext, stack);
    }

    @Override
    public int setSpecialActive(long seconds, Player player) {
        VPUtil.play(player,SoundRegistry.STOLAS1.get());
        if(isSpecialActive) {
            time = 1;
            return 0;
        }
        return super.setSpecialActive(seconds, player);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level) {
        super.doSpecial(seconds, player, level);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level) {
        VPUtil.play(player,SoundRegistry.IMPACT.get());
        float damage = 300;
        float healDebt = player.getMaxHealth()*3;
        if(player.getHealth() <= player.getMaxHealth()*0.5) {
            damage *= 2;
            healDebt *= 2;
        }
        player.getPersistentData().putFloat("HealDebt",player.getPersistentData().getFloat("HealDebt")+healDebt);
        for (LivingEntity entity: VPUtil.ray(player,8,60,false)){
            entity.getPersistentData().putFloat("HealDebt",entity.getPersistentData().getFloat("HealDebt")+healDebt);
            VPUtil.dealDamage(entity,player,DamageSource.playerAttack(player).bypassArmor(),damage);
        }
        super.doUltimate(seconds, player, level);
    }
}
