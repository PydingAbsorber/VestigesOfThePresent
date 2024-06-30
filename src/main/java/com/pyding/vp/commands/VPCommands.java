package com.pyding.vp.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;

public class VPCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("vestiges")
                .then(Commands.literal("clear").requires(sender -> sender.hasPermission(2))
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
                                    for(ItemStack stack: VPUtil.getVestigeList(player)){
                                        if(stack.getItem() instanceof Vestige vestige) {
                                            vestige.refresh(player, stack);
                                        }
                                    }
                                    player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                                        cap.clearCoolDown(player);
                                    });
                                    player.sendSystemMessage(Component.literal("Cooldowns of Vestiges and Challenges refreshed successfully! \nNote! That command will trigger ''cd ends'' that affect some Vestiges like Trigon or SweetDonut"));
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
                .then(Commands.literal("friend")
                        .then(Commands.literal("add")
                                .then(Commands.argument("creature Id or Display Name", StringArgumentType.string())
                                        .executes(context -> {
                                            String name = StringArgumentType.getString(context, "creature Id or Display Name");
                                            ServerPlayer player = context.getSource().getPlayerOrException();
                                            player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                                                cap.addFriend(name,player);
                                            });
                                            player.sendSystemMessage(Component.literal("You have added " + name + " as a friend!").withStyle(ChatFormatting.GREEN));
                                            return Command.SINGLE_SUCCESS;
                                        })
                                )
                        )
                        .then(Commands.literal("remove")
                                .then(Commands.argument("creature Id or Display Name", StringArgumentType.string())
                                        .executes(context -> {
                                            String name = StringArgumentType.getString(context, "creature Id or Display Name");
                                            ServerPlayer player = context.getSource().getPlayerOrException();
                                            player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                                                cap.removeFriend(name,player);
                                            });
                                            player.sendSystemMessage(Component.literal(name + " is no friend anymore :((("));
                                            return Command.SINGLE_SUCCESS;
                                        })
                                )
                        )
                        .then(Commands.literal("seeFriends")
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                                        player.sendSystemMessage(Component.literal("This is your friends list:"));
                                        player.sendSystemMessage(Component.literal(cap.getFriends()));
                                    });
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                        .then(Commands.literal("information")
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    player.sendSystemMessage(Component.literal("Friends can't take any damage or bad effects from you and your Vestiges").withStyle(ChatFormatting.GREEN));
                                    player.sendSystemMessage(Component.literal("To know creature's Id type command /vestiges getType"));
                                    player.sendSystemMessage(Component.literal("If you type ,,cow,, it will count all mobs that has cow in it's id"));
                                    player.sendSystemMessage(Component.literal("If you type entity.minecraft.cow from /vestiges getType it will count only vanilla cow"));
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
                                                .append(Component.literal(" / " + PlayerCapabilityVP.getMaximum(i,player))));
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
                .then(Commands.literal("hurt").requires(sender -> sender.hasPermission(2))
                        .then(Commands.argument("amount", FloatArgumentType.floatArg())
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    float amount = FloatArgumentType.getFloat(context, "amount");
                                    player.hurt(player.damageSources().generic(),amount);
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
                                player.sendSystemMessage(Component.literal("Id: " + stack.getItem().getDescriptionId()));
                                player.sendSystemMessage(Component.literal("Enchantments list: " + stack.getEnchantmentTags()));
                                for(Enchantment enchantment: stack.getAllEnchantments().keySet()) {
                                    player.sendSystemMessage(Component.literal("Ench name: " + enchantment.getDescriptionId()));
                                    player.sendSystemMessage(Component.literal("Ench min lvl: " + enchantment.getMinLevel()));
                                    player.sendSystemMessage(Component.literal("Ench max lvl: " + enchantment.getMaxLevel()));
                                    player.sendSystemMessage(Component.literal("Ench lvl: " + stack.getEnchantmentLevel(enchantment)));
                                    if(enchantment instanceof ProtectionEnchantment)
                                        player.sendSystemMessage(Component.literal("Ench damage protection: " + enchantment.getDamageProtection(stack.getEnchantmentLevel(enchantment),player.damageSources().generic())));
                                }
                                player.sendSystemMessage(Component.literal("Tags: " + stack.getOrCreateTag()));
                                player.sendSystemMessage(Component.literal("Damage: " + stack.getDamageValue()));
                                player.sendSystemMessage(Component.literal("Side is client: " + player.getCommandSenderWorld().isClientSide()));
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
                                            VPUtil.addOverShield(player,overshields,false);
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
                                player.sendSystemMessage(Component.literal("Hardcore mode disabled."));
                            }
                            else {
                                ConfigHandler.COMMON.hardcore.set(true);
                                player.sendSystemMessage(Component.literal("Hardcore mode enabled, all bosses hp now is x" + ConfigHandler.COMMON.bossHP.get() + " and attack is x" + ConfigHandler.COMMON.bossHP.get() + " armor and armor toughness " + ConfigHandler.COMMON.bossHP.get() + " Shields from max hp percent " + ConfigHandler.COMMON.bossHP.get() + " Over Shields percent " + ConfigHandler.COMMON.bossHP.get() + " Healing per second percent from max hp " + ConfigHandler.COMMON.bossHP.get() + " damage absorption percent " + ConfigHandler.COMMON.absorbHardcore.get()));
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .then(Commands.literal("reduceChallenge").requires(sender -> sender.hasPermission(2))
                    .then(Commands.argument("challengeReduce", IntegerArgumentType.integer())
                        .then(Commands.argument("amount", IntegerArgumentType.integer())
                            .executes(context -> {
                                int challenge = IntegerArgumentType.getInteger(context, "challengeReduce");
                                int amount = IntegerArgumentType.getInteger(context, "amount");
                                if (challenge > PlayerCapabilityVP.totalVestiges) {
                                    context.getSource().sendFailure(Component.literal("There is no such challenge number! >:("));
                                    return 0;
                                }
                                ServerPlayer player = context.getSource().getPlayerOrException();
                                ConfigHandler.COMMON.getChallengeReduceByNumber(challenge).set(amount);
                                player.sendSystemMessage(Component.literal("Progress maximum for challenge " + challenge + " has been reduced for " + amount));
                                return Command.SINGLE_SUCCESS;
                            })
                        )
                    )
                )
                .then(Commands.literal("getType")
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            boolean found = false;
                            for(LivingEntity entity: VPUtil.ray(player,3,60,true)){
                                player.sendSystemMessage(Component.literal("descriptionId " + entity.getType().getDescriptionId() + " raw type: " + entity.getType().toString()));
                                found = true;
                            }
                            if(!found)
                                player.sendSystemMessage(Component.literal("No creatures found. You should look at creature."));
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .then(Commands.literal("fish")
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            player.sendSystemMessage(Component.literal(""));
                            player.sendSystemMessage(Component.literal("Fish drops in current biome:").withStyle(ChatFormatting.BLUE));
                            VPUtil.printFishDrop(player);
                            return Command.SINGLE_SUCCESS;
                        })
                )
        );
    }
}
