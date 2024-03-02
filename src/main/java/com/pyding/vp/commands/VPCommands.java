package com.pyding.vp.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import com.pyding.vp.capability.PlayerCapabilityVP;
import com.pyding.vp.event.EventHandler;
import com.pyding.vp.item.artifacts.Vestige;
import com.pyding.vp.util.ConfigHandler;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.util.ICuriosHelper;

import java.awt.*;
import java.util.List;

public class VPCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("vestiges")
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
                                    player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                                        cap.clearCoolDown(player);
                                    });
                                    player.sendSystemMessage(Component.literal("Cooldowns of Vestiges and Challenges refreshed successfully! \nNote! That command will trigger ''cd ends'' that affect some Vestiges like Trigon or SweetDonut"));
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
                .then(Commands.literal("info")
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            player.sendSystemMessage(Component.translatable("vp.info.mechanics"));
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .then(Commands.literal("progress")
                        .then(Commands.literal("show")
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
                        .then(Commands.literal("set").requires(sender -> sender.hasPermission(2))
                                .then(Commands.argument("challengeNumber", IntegerArgumentType.integer())
                                    .then(Commands.argument("amount", IntegerArgumentType.integer())
                                        .executes(context -> {
                                            int challenge = IntegerArgumentType.getInteger(context, "challengeNumber");
                                            int amount = IntegerArgumentType.getInteger(context, "amount");
                                            if (challenge > PlayerCapabilityVP.totalVestiges) {
                                                context.getSource().sendFailure(Component.literal("There is no such challenge number! >:("));

                                                return 0;
                                            }
                                            ServerPlayer player = context.getSource().getPlayerOrException();
                                            player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                                                cap.setChallenge(challenge,amount,player);
                                            });
                                            player.sendSystemMessage(Component.literal("Progress for challenge " + challenge + " has been set to " + amount));
                                            return Command.SINGLE_SUCCESS;
                                        })
                                    )
                                )
                        )
                )
                .then(Commands.literal("deadInside").requires(sender -> sender.hasPermission(2))
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            VPUtil.deadInside(player,player);
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .then(Commands.literal("damage").requires(sender -> sender.hasPermission(2))
                        .then(Commands.argument("amount", FloatArgumentType.floatArg())
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    float amount = FloatArgumentType.getFloat(context, "amount");
                                    player.hurt(DamageSource.GENERIC,amount);
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
                .then(Commands.literal("debug")
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                                cap.setDebug(player);
                                player.sendSystemMessage(Component.literal("Debug now " + cap.getDebug()));
                            });
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .then(Commands.literal("itemInHandsFullInfo")
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            ItemStack stack = player.getMainHandItem();
                            if(stack != null){
                                player.sendSystemMessage(Component.literal("Enchantments list: " + stack.getEnchantmentTags()));
                                for(Enchantment enchantment: stack.getAllEnchantments().keySet()) {
                                    player.sendSystemMessage(Component.literal("Ench name: " + enchantment.getDescriptionId()));
                                    player.sendSystemMessage(Component.literal("Ench min lvl: " + enchantment.getMinLevel()));
                                    player.sendSystemMessage(Component.literal("Ench max lvl: " + enchantment.getMaxLevel()));
                                    player.sendSystemMessage(Component.literal("Ench lvl: " + stack.getEnchantmentLevel(enchantment)));
                                    if(enchantment instanceof ProtectionEnchantment)
                                        player.sendSystemMessage(Component.literal("Ench damage protection: " + enchantment.getDamageProtection(stack.getEnchantmentLevel(enchantment),DamageSource.GENERIC)));
                                }
                                player.sendSystemMessage(Component.literal("Tags: " + stack.getOrCreateTag()));
                                player.sendSystemMessage(Component.literal("Damage: " + stack.getDamageValue()));
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .then(Commands.literal("calculateChance")
                        .then(Commands.argument("playerHP", FloatArgumentType.floatArg())
                                .then(Commands.argument("entityMaxHP", FloatArgumentType.floatArg())
                                        .then(Commands.argument("entityCurrentHP", FloatArgumentType.floatArg())
                                            .executes(context -> {
                                                ServerPlayer player = context.getSource().getPlayerOrException();
                                                float playerHealth = FloatArgumentType.getFloat(context, "playerHP");
                                                float entityMaxHP = FloatArgumentType.getFloat(context, "entityMaxHP");
                                                float entityCurrentHP = FloatArgumentType.getFloat(context, "entityCurrentHP");
                                                double chance = VPUtil.calculateCatchChance(playerHealth,entityMaxHP,entityCurrentHP);
                                                player.sendSystemMessage(Component.literal("For arguments where taken \n§cplayer HP: " + playerHealth + " \n§2entity maximum HP: " + entityMaxHP + " \n§eentity current HP: " + entityCurrentHP));
                                                player.sendSystemMessage(Component.literal("Chance for capture: §5" + String.format("%.15f", chance*100) + "%"));
                                                return Command.SINGLE_SUCCESS;
                                            })
                                        )
                                )
                        )
                )
                .then(Commands.literal("addShields").requires(sender -> sender.hasPermission(2))
                        .then(Commands.argument("shields", FloatArgumentType.floatArg())
                                .then(Commands.argument("overshields", FloatArgumentType.floatArg())
                                        .executes(context -> {
                                            ServerPlayer player = context.getSource().getPlayerOrException();
                                            float shields = FloatArgumentType.getFloat(context, "shields");
                                            float overshields = FloatArgumentType.getFloat(context, "overshields");
                                            if(shields > 0)
                                                VPUtil.addShield(player,shields,true);
                                            if(overshields > 0)
                                                VPUtil.addOverShield(player,overshields);
                                            return Command.SINGLE_SUCCESS;
                                        })
                                )
                        )
                )
                .then(Commands.literal("enableHardcore").requires(sender -> sender.hasPermission(2))
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            if(ConfigHandler.COMMON.hardcore.get()) {
                                ConfigHandler.COMMON.hardcore.set(false);
                                player.sendSystemMessage(Component.literal("Hardcore mode disabled. Please type /reload if the changes have not been applied."));
                            }
                            else {
                                ConfigHandler.COMMON.hardcore.set(true);
                                player.sendSystemMessage(Component.literal("Hardcore mode enabled, all bosses hp now is x10 and attack is x2. Please type /reload if the changes have not been applied."));
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                )
        );
    }
}
