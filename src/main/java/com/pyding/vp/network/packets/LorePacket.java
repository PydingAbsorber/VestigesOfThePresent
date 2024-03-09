package com.pyding.vp.network.packets;

import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class LorePacket {
    private final int number;

    public LorePacket(int number) {
        this.number = number;
    }

    public static void encode(LorePacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.number);
    }

    public static LorePacket decode(FriendlyByteBuf buf) {
        return new LorePacket(buf.readInt());
    }

    public static void handle(LorePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            handle2(msg.number);
        });

        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handle2(int number) {
        LocalPlayer player = Minecraft.getInstance().player;
        if(player == null)
            return;
        String name = VPUtil.getRainbowString(VPUtil.generateRandomString("entity".length())) + ": ";
        String playerName = player.getDisplayName().getString();
        playerName += ": ";

        if(number == 1)
            player.sendSystemMessage(Component.translatable("vp.lore.1"));
        else if(number == 2)
            player.sendSystemMessage(Component.translatable("vp.lore.2"));
        else if(number == 3) {
            player.sendSystemMessage(Component.literal(name).append(Component.translatable("vp.lore.3.1")).withStyle(ChatFormatting.DARK_PURPLE));
            player.sendSystemMessage(Component.literal(playerName).append(Component.translatable("vp.lore.3.2")));
            player.sendSystemMessage(Component.literal(name).append(Component.translatable("vp.lore.3.3")).withStyle(ChatFormatting.DARK_PURPLE));
            player.sendSystemMessage(Component.literal(playerName).append(Component.translatable("vp.lore.3.4")));
            player.sendSystemMessage(Component.literal(name).append(Component.translatable("vp.lore.3.5")).withStyle(ChatFormatting.DARK_PURPLE));
            player.sendSystemMessage(Component.translatable("vp.lore.3.6"));
        }
        else if(number == 4)
            player.sendSystemMessage(Component.translatable("vp.lore.4"));
        else if(number == 5)
            player.sendSystemMessage(Component.translatable("vp.lore.5"));
        else if(number == 6)
            player.sendSystemMessage(Component.translatable("vp.lore.6"));
        else if(number == 7) {
            player.sendSystemMessage(Component.translatable("vp.lore.7"));
            player.sendSystemMessage(Component.literal(name).append(Component.translatable("vp.lore.7.1")).withStyle(ChatFormatting.DARK_PURPLE));
            player.sendSystemMessage(Component.literal(playerName).append(Component.translatable("vp.lore.7.2")));
            player.sendSystemMessage(Component.literal(name).append(Component.translatable("vp.lore.7.3")).withStyle(ChatFormatting.DARK_PURPLE));
            player.sendSystemMessage(Component.literal(playerName).append(Component.translatable("vp.lore.7.4")));
            player.sendSystemMessage(Component.literal(name).append(Component.translatable("vp.lore.7.5")).withStyle(ChatFormatting.DARK_PURPLE));
            player.sendSystemMessage(Component.translatable("vp.lore.7.6"));
            player.sendSystemMessage(Component.translatable("vp.lore.7.7"));
        }
        else if(number == 8)
            player.sendSystemMessage(Component.translatable("vp.lore.8"));
        else if(number == 9) {
            player.sendSystemMessage(Component.literal(name).append(Component.translatable("vp.lore.9.1")).withStyle(ChatFormatting.DARK_PURPLE)); //Component.literal(playerName).append()
            player.sendSystemMessage(Component.literal(playerName).append(Component.translatable("vp.lore.9.2")));
            player.sendSystemMessage(Component.literal(name).append(Component.translatable("vp.lore.9.3")).withStyle(ChatFormatting.DARK_PURPLE));
            player.sendSystemMessage(Component.literal(playerName).append(Component.translatable("vp.lore.9.4")));
            player.sendSystemMessage(Component.literal(name).append(Component.translatable("vp.lore.9.5")).withStyle(ChatFormatting.DARK_PURPLE));
            player.sendSystemMessage(Component.literal(playerName).append(Component.translatable("vp.lore.9.6")));
            player.sendSystemMessage(Component.literal(name).append(Component.translatable("vp.lore.9.7")).withStyle(ChatFormatting.DARK_PURPLE));
            player.sendSystemMessage(Component.literal(playerName).append(Component.translatable("vp.lore.9.8")));
            player.sendSystemMessage(Component.literal(name).append(Component.translatable("vp.lore.9.9")).withStyle(ChatFormatting.DARK_PURPLE));
            player.sendSystemMessage(Component.translatable("vp.lore.9.10"));
        }
    }
}
