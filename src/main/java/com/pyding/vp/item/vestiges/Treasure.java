package com.pyding.vp.item.vestiges;

import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.network.packets.ParticlePacket;
import com.pyding.vp.util.ConfigHandler;
import com.pyding.vp.util.VPUtil;
import com.pyding.vp.util.VPUtilParticles;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import top.theillusivec4.curios.api.SlotContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Treasure extends Vestige{
    public Treasure(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage, ItemStack stack) {
        super.dataInit(26, ChatFormatting.GOLD, 3, 60, 1, 145, 3, 30, true, stack);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level, ItemStack stack) {
        stack.getOrCreateTag().putInt("VPTicks",player.tickCount);
        stack.getOrCreateTag().putInt("VPEarthquake",0);
        super.doSpecial(seconds, player, level, stack);
    }

    public int getRadius(ItemStack stack){
        return Math.min(11,Math.max(1,stack.getOrCreateTag().getInt("VPRadius")));
    }

    @Override
    public void whileSpecial(Player player, ItemStack stack) {
        if((player.tickCount-stack.getOrCreateTag().getInt("VPTicks")) % 20 == 0){
            int earthquake = stack.getOrCreateTag().getInt("VPEarthquake");
            if(earthquake < 6)
                stack.getOrCreateTag().putInt("VPEarthquake",earthquake+1);
            for(LivingEntity entity: VPUtil.getEntitiesAround(player,Math.max(1,3 * earthquake),Math.max(1,earthquake),Math.max(1,3 * earthquake))){
                if(entity != player) {
                    VPUtil.dealDamage(entity,player,player.damageSources().inWall(),player.getArmorValue()*(4+earthquake),2,true);
                }
            }
            if (player instanceof ServerPlayer serverPlayer) {
                createOvalHoleUnderPlayer(serverPlayer, Math.max(1,3 * earthquake), Math.max(1,earthquake));
            }
        }
        super.whileSpecial(player, stack);
    }

    @Override
    public void whileUltimate(Player player, ItemStack stack) {
        int radius = stack.getOrCreateTag().getInt("VPRadiusOst");
        while (radius > 10){
            radius -= 10;
            stack.getOrCreateTag().putInt("VPRadius", (stack.getOrCreateTag().getInt("VPRadius") + 1));
        }
        stack.getOrCreateTag().putInt("VPRadiusOst",radius);
        super.whileUltimate(player, stack);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level, ItemStack stack) {
        stack.getOrCreateTag().putInt("VPRadius",0);
        super.doUltimate(seconds, player, level, stack);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);
        if(slotContext.entity() instanceof Player player){
            if(player.getMainHandItem().getItem() instanceof PickaxeItem || player.getOffhandItem().getItem() instanceof PickaxeItem){
                float armor = VPUtil.scalePower(getOres(player)/20,26,player);
                float tough = VPUtil.scalePower(getMinerals(player)/32,26,player);
                player.getAttributes().addTransientAttributeModifiers(VPUtil.createAttributeMap(player, Attributes.ARMOR, UUID.fromString("e6fdfccb-e294-481c-bd65-f464a9982e3f"),armor, AttributeModifier.Operation.ADDITION,"vp.treasure.armor"));
                player.getAttributes().addTransientAttributeModifiers(VPUtil.createAttributeMap(player, Attributes.ARMOR_TOUGHNESS, UUID.fromString("c692ceea-e05b-441f-8c98-0ff7842fa89e"),tough, AttributeModifier.Operation.ADDITION,"vp.treasure.armor"));
            } else if(player.getAttributes().hasModifier(Attributes.ARMOR,UUID.fromString("e6fdfccb-e294-481c-bd65-f464a9982e3f"))){
                player.getAttributes().removeAttributeModifiers(VPUtil.createAttributeMap(player, Attributes.ARMOR, UUID.fromString("e6fdfccb-e294-481c-bd65-f464a9982e3f"),0, AttributeModifier.Operation.ADDITION,"vp.treasure.armor"));
                player.getAttributes().removeAttributeModifiers(VPUtil.createAttributeMap(player, Attributes.ARMOR_TOUGHNESS, UUID.fromString("c692ceea-e05b-441f-8c98-0ff7842fa89e"),0, AttributeModifier.Operation.ADDITION,"vp.treasure.armor"));
            }
        }
    }

    @Override
    public void curioSucks(Player player, ItemStack stack) {
        super.curioSucks(player, stack);
        if(player.getAttributes().hasModifier(Attributes.ARMOR,UUID.fromString("e6fdfccb-e294-481c-bd65-f464a9982e3f"))){
            player.getAttributes().removeAttributeModifiers(VPUtil.createAttributeMap(player, Attributes.ARMOR, UUID.fromString("e6fdfccb-e294-481c-bd65-f464a9982e3f"),0, AttributeModifier.Operation.ADDITION,"vp.treasure.armor"));
            player.getAttributes().removeAttributeModifiers(VPUtil.createAttributeMap(player, Attributes.ARMOR_TOUGHNESS, UUID.fromString("c692ceea-e05b-441f-8c98-0ff7842fa89e"),0, AttributeModifier.Operation.ADDITION,"vp.treasure.armor"));
        }
    }

    public static float getOres(Player player){
        float ores = 0;
        for(ItemStack stack: player.getInventory().items){
            if(VPUtil.getOres().contains(stack.getDescriptionId()))
                ores += stack.getCount();
        }
        return ores;
    }

    public static float getMinerals(Player player){
        float ores = 0;
        for(ItemStack stack: player.getInventory().items){
            for(String name: ConfigHandler.COMMON.mineralCluster.get().toString().split(",")){
                if(stack.getDescriptionId().equals(name))
                    ores += stack.getCount();
            }
        }
        return ores;
    }

    public static void createOvalHoleUnderPlayer(ServerPlayer player, int radius, int depth) {
        ServerLevel level = (ServerLevel) player.level();
        BlockPos playerPos = player.blockPosition();
        BlockPos center = playerPos.below();
        int minX = center.getX() - radius;
        int maxX = center.getX() + radius;
        int minZ = center.getZ() - radius;
        int maxZ = center.getZ() + radius;
        int minY = Math.max(level.getMinBuildHeight(), center.getY() - depth);
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                if (isPointInEllipse(x, z, center.getX(), center.getZ(), radius, radius)) {
                    digColumnToDepth(level, new BlockPos(x, center.getY(), z), minY,player);
                }
            }
        }
        spawnDestructionEffects(level, center, radius, player);
    }

    private static boolean isPointInEllipse(int x, int z, int centerX, int centerZ,
                                            int radiusX, int radiusZ) {
        double dx = (x - centerX) / (double) radiusX;
        double dz = (z - centerZ) / (double) radiusZ;
        return dx * dx + dz * dz <= 1.0;
    }

    private static void digColumnToDepth(ServerLevel level, BlockPos topPos, int minY,ServerPlayer serverPlayer) {
        for (int y = topPos.getY(); y >= minY; y--) {
            BlockPos currentPos = new BlockPos(topPos.getX(), y, topPos.getZ());
            BlockState state = level.getBlockState(currentPos);
            if (state.is(Blocks.BEDROCK)) {
                break;
            }
            if (!state.isAir()) {
                level.playSound(null, currentPos, state.getSoundType().getBreakSound(),
                        SoundSource.BLOCKS, 0.8F, 1.0F);
                serverPlayer.gameMode.destroyBlock(currentPos);
            }
        }
    }

    private static void spawnDestructionEffects(ServerLevel level, BlockPos center, int radius, ServerPlayer serverPlayer) {
        for (int i = 0; i < 360; i += 15) {
            double angle = Math.toRadians(i);
            double x = center.getX() + radius * Math.cos(angle);
            double z = center.getZ() + radius * Math.sin(angle);
            level.sendParticles(ParticleTypes.POOF,
                    x, center.getY() + 1, z,
                    10, 0.5, 0.1, 0.5, 0.05);
        }
        PacketHandler.sendToClient(new ParticlePacket(666,center.getX(), center.getY(), center.getZ(), 0, 0, 0),serverPlayer);
    }
}
