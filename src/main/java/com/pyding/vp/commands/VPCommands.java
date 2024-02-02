package com.pyding.vp.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import com.pyding.vp.capability.PlayerCapabilityVP;
import com.pyding.vp.event.EventHandler;
import com.pyding.vp.item.artifacts.Vestige;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.util.ICuriosHelper;

import java.awt.*;
import java.util.List;

public class VPCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("vestige")
                .then(Commands.literal("clear")
                        .requires(sender -> sender.hasPermission(2))
                        .then(Commands.literal("progress")
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                                        cap.clearAllProgress(player);
                                    });
                                    player.sendSystemMessage(Component.literal("Progress cleared successfully"));
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                        .then(Commands.literal("cooldown")
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    ICuriosHelper api = CuriosApi.getCuriosHelper();
                                    List list = api.findCurios(player, (stackInSlot) -> {
                                        if(stackInSlot.getItem() instanceof Vestige vestige) {
                                            vestige.refresh(player);
                                            return true;
                                        }
                                        return false;
                                    });
                                    player.sendSystemMessage(Component.literal("Cooldowns refreshed successfully"));
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
                .then(Commands.literal("progress")
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                                for(int i = 1; i < PlayerCapabilityVP.totalVestiges; i++){
                                    int progress = cap.getChallenge(i);
                                    if(i == 12)
                                        progress = EventHandler.getCurses(player);
                                    player.sendSystemMessage(Component.literal("Current progress for Vestige ").append(Component.translatable("vp.name."+i)));
                                    player.sendSystemMessage(Component.translatable("vp.progress").withStyle(ChatFormatting.DARK_GREEN)
                                            .append(Component.literal(" " + progress))
                                            .append(Component.literal(" / " + PlayerCapabilityVP.getMaximum(i))));
                                }
                                player.sendSystemMessage(Component.literal("Current chance for " + VPUtil.getRainbowString("Stellar:") + " " + cap.getChance()));
                            });
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .then(Commands.literal("deadInside").requires(sender -> sender.hasPermission(2))
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            VPUtil.deadInside(player,player);
                            return Command.SINGLE_SUCCESS;
                        })
                )
        );
    }


    public enum CommandAction {
        SET,
        ADD,
        TAKE
    }
}
