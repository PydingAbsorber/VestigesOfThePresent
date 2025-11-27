package com.pyding.vp.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import com.pyding.vp.capability.PlayerCapabilityVP;
import com.pyding.vp.event.EventHandler;
import com.pyding.vp.item.ModItems;
import com.pyding.vp.item.MysteryChest;
import com.pyding.vp.item.vestiges.Vestige;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.network.packets.PlayerFlyPacket;
import com.pyding.vp.util.*;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementList;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VPCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("vestiges")
                .then(Commands.literal("leaderboard")
                        .then(Commands.literal("register")
                                .then(Commands.argument("password", StringArgumentType.string())
                                        .executes(context -> {
                                            String password = StringArgumentType.getString(context, "password");
                                            ServerPlayer player = context.getSource().getPlayerOrException();
                                            if(LeaderboardUtil.addNickname(player,player.getUUID(),password).equals("You have been registered")) {
                                                player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                                                    if(cap.getPassword().isEmpty()) {
                                                        cap.setPassword(password);
                                                        cap.sync(player);
                                                        player.sendSystemMessage(Component.literal("You also logged in, no need to type login.").withStyle(ChatFormatting.GRAY));
                                                        player.sendSystemMessage(Component.literal("Remember your password please! Write it down somewhere.").withStyle(ChatFormatting.RED));
                                                    } else {
                                                        player.sendSystemMessage(Component.literal("You already logged in.").withStyle(ChatFormatting.GRAY));
                                                    }
                                                });
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        })
                                )
                        )
                        .then(Commands.literal("login")
                                .then(Commands.argument("password", StringArgumentType.string())
                                        .executes(context -> {
                                            String password = StringArgumentType.getString(context, "password");
                                            ServerPlayer player = context.getSource().getPlayerOrException();
                                            player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                                                if(!cap.getPassword().isEmpty())
                                                    player.sendSystemMessage(Component.literal("You logged in already. No need to do it every time lol").withStyle(ChatFormatting.GREEN));
                                                else LeaderboardUtil.checkPassword(player,player.getUUID(),password);
                                            });
                                            return Command.SINGLE_SUCCESS;
                                        })
                                )
                        )
                        .then(Commands.literal("enable").requires(sender -> sender.hasPermission(2))
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    if (ConfigHandler.COMMON.leaderboard.get()) {
                                        ConfigHandler.COMMON.leaderboard.set(false);
                                        player.sendSystemMessage(Component.literal("Leaderboard disabled.").withStyle(ChatFormatting.DARK_RED));
                                    } else {
                                        ConfigHandler.COMMON.leaderboard.set(true);
                                        player.sendSystemMessage(Component.literal("Leaderboard enabled.").withStyle(ChatFormatting.DARK_GREEN));
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                        .then(Commands.literal("info")
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    player.sendSystemMessage(Component.translatable("vp.leaderboard1").append(GradientUtil.goldenGradient(Component.translatable("vp.leaderboard.gold").getString())));
                                    player.sendSystemMessage(Component.translatable("vp.leaderboard2"));
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                        .then(Commands.literal("showAll")
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    PacketHandler.sendToClient(new PlayerFlyPacket(14),player);
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                        .then(Commands.literal("showYourself")
                                .executes(context -> {
                                    if(!ConfigHandler.COMMON.leaderboard.get()){
                                        context.getSource().getPlayerOrException().sendSystemMessage(Component.literal("Leaderboard is disabled"));
                                        return Command.SINGLE_SUCCESS;
                                    } else LeaderboardUtil.printYourself(context.getSource().getPlayerOrException());
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                        .then(Commands.literal("checkConnection")
                                .executes(context -> {
                                    LeaderboardUtil.printCheck(context.getSource().getPlayerOrException());
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
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
                        .then(Commands.literal("entities")
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    VPUtil.clearEntities(player.getServer(),true);
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
                        .then(Commands.literal("removeAll")
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                                        cap.removeAllFriends(player);
                                    });
                                    player.sendSystemMessage(Component.literal("All friends removed"));
                                    return Command.SINGLE_SUCCESS;
                                })
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
                                    player.sendSystemMessage(Component.literal("You can add creature to friends by it's Id/Display Name/Nametag").withStyle(ChatFormatting.BLUE));
                                    player.sendSystemMessage(Component.literal("To know creature's Id type command /vestiges getType"));
                                    player.sendSystemMessage(Component.literal("If you type for example ,,cow,, it will count all mobs that has cow in it's id"));
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
                .then(Commands.literal("jail").requires(sender -> sender.hasPermission(2))
                    .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("time", LongArgumentType.longArg())
                            .executes(context -> {
                                ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                long time = LongArgumentType.getLong(context,"time");
                                VPUtil.bindEntity(player,time);
                                return Command.SINGLE_SUCCESS;
                            })
                        )
                    )
                )
                .then(Commands.literal("progress")
                        .then(Commands.literal("show")
                            .executes(context -> {
                                ServerPlayer player = context.getSource().getPlayerOrException();
                                player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                                    for(int i = 1; i < PlayerCapabilityVP.totalVestiges; i++){
                                        int progress = cap.getChallenge(i);
                                        if(i == 12)
                                            progress = VPUtil.getCurseAmount(player);
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
                                player.sendSystemMessage(Component.literal("Class: " + stack.getClass().getName()));
                                player.sendSystemMessage(Component.literal("Extends: " + stack.getClass().getSuperclass().getName()));
                                player.sendSystemMessage(Component.literal("Implements: "));
                                Class<?>[] interfaces = stack.getClass().getInterfaces();
                                for (Class<?> interfaceClass : interfaces) {
                                    player.sendSystemMessage(Component.literal(interfaceClass.getName()));
                                }
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
                .then(Commands.literal("enableCruel").requires(sender -> sender.hasPermission(2))
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            if(ConfigHandler.COMMON.cruelMode.get()) {
                                ConfigHandler.COMMON.cruelMode.set(false);
                                player.sendSystemMessage(Component.literal("Cruel mode §4disabled."));
                            }
                            else {
                                ConfigHandler.COMMON.cruelMode.set(true);
                                player.sendSystemMessage(Component.literal("§7Cruel mode §aenabled! \n§7All bosses max hp now is §cx" + ConfigHandler.COMMON.bossHP.get() + " §7and attack is §cx" + ConfigHandler.COMMON.bossHP.get() + " §7armor and armor toughness is §cx" + ConfigHandler.COMMON.bossHP.get() + " \n§7All bosses now have Shields from max hp percent §cx" + ConfigHandler.COMMON.shieldCruel.get() + " §7and Over Shields §cx" + ConfigHandler.COMMON.overShieldCruel.get() + " \n§7All bosses now are also Healing §c" + ConfigHandler.COMMON.bossHP.get() +"% §7from max hp per second.\nAll bosses also have DPS cap from max health §c" + ConfigHandler.COMMON.absorbCruel.get()*100 + "%" + " that can be exceeded by Vestige's Passive/Special/Ultimate damage by x2/x4/x6. \nAll monsters also have x" + ConfigHandler.COMMON.healthBoost.get() + " max health and chance to spawn with random armor."));
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
                                ConfigHandler.COMMON.reduceChallenges.get().set(challenge-1,amount);
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
                            player.sendSystemMessage(Component.literal(""));
                            player.sendSystemMessage(Component.literal("Fish drops in current biome:").withStyle(ChatFormatting.BLUE));
                            VPUtil.printFishDrop(player);
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .then(Commands.literal("getAllFishLoot")
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            VPUtil.initFishMap();
                            List<String> list = new ArrayList<>();
                            List<String> rares = new ArrayList<>();
                            for(Item item: VPUtil.fishList){
                                if(VPUtil.isRare(new ItemStack(item)))
                                    rares.add(item.getDescriptionId());
                                else list.add(item.getDescriptionId());
                            }
                            player.sendSystemMessage(Component.literal(""));
                            player.sendSystemMessage(Component.literal(""));
                            player.sendSystemMessage(Component.literal("All possible loot from fishing with Abyssal Pearl:").withStyle(ChatFormatting.GRAY));
                            player.sendSystemMessage(VPUtil.filterAndTranslate(list.toString(),ChatFormatting.BLUE));
                            player.sendSystemMessage(Component.literal("Rare loot:").withStyle(ChatFormatting.GRAY));
                            player.sendSystemMessage(VPUtil.filterAndTranslate(rares.toString(),ChatFormatting.RED));
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .then(Commands.literal("enableEvent").requires(sender -> sender.hasPermission(2))
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            if(ConfigHandler.COMMON.eventMode.get()) {
                                ConfigHandler.COMMON.eventMode.set(false);
                                player.sendSystemMessage(Component.literal("Event mode disabled."));
                            }
                            else {
                                ConfigHandler.COMMON.eventMode.set(true);
                                player.sendSystemMessage(Component.literal("Event mode enabled. All teleportations and fly disabled for players with no creative."));
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .then(Commands.literal("mysteryChest").requires(sender -> sender.hasPermission(2))
                        .then(Commands.literal("add")
                                .then(Commands.argument("common/rare/mythic/legendary", StringArgumentType.string())
                                        .then(Commands.argument("stackSize", IntegerArgumentType.integer())
                                                .executes(context -> {
                                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                                    String arg = StringArgumentType.getString(context,"common/rare/mythic/legendary");
                                                    int size = IntegerArgumentType.getInteger(context, "stackSize");
                                                    String element = player.getMainHandItem().getDescriptionId();
                                                    if(size > 1)
                                                        element += size;
                                                    ConfigHandler.COMMON.lootDrops.set(VPUtil.addMysteryLoot(ConfigHandler.COMMON.lootDrops.get().toString(),element,arg));
                                                    MysteryChest.init();
                                                    player.sendSystemMessage(Component.literal("Item in main hand added to " + arg));
                                                    return Command.SINGLE_SUCCESS;
                                                })
                                        )
                                )
                        )
                        .then(Commands.literal("remove")
                                .then(Commands.argument("common/rare/mythic/legendary", StringArgumentType.string())
                                        .then(Commands.argument("stackSize", IntegerArgumentType.integer())
                                                .executes(context -> {
                                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                                    String arg = StringArgumentType.getString(context,"common/rare/mythic/legendary");
                                                    int size = IntegerArgumentType.getInteger(context, "stackSize");
                                                    String element = player.getMainHandItem().getDescriptionId();
                                                    if(size > 1)
                                                        element += size;
                                                    ConfigHandler.COMMON.lootDrops.set(VPUtil.removeMysteryLoot(ConfigHandler.COMMON.lootDrops.get().toString(),element,arg));
                                                    MysteryChest.init();
                                                    player.sendSystemMessage(Component.literal("Item in main hand removed from " + arg));
                                                    return Command.SINGLE_SUCCESS;
                                                })
                                        )
                                )
                        )
                        .then(Commands.literal("syncAdvancements")
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                                        int advancements = 0;
                                        for(Advancement advancement: player.server.getAdvancements().getAllAdvancements()){
                                            if(!advancement.getId().getPath().startsWith("recipes/") && advancement.getDisplay() != null && player.getAdvancements().getOrStartProgress(advancement).isDone()){
                                                advancements++;
                                            }
                                        }
                                        cap.setAdvancement(player,advancements);
                                    });
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
                .then(Commands.literal("renderSoulIntegrity")
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            PacketHandler.sendToClient(new PlayerFlyPacket(13),player);
                            player.sendSystemMessage(Component.literal("Render of Soul Integrity is now" + ClientConfig.COMMON.renderSoulIntegrity.get()).withStyle(ChatFormatting.GRAY));
                            VPUtil.printFishDrop(player);
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .then(Commands.literal("statTrack")
                        .then(Commands.literal("data")
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    player.sendSystemMessage(Component.literal("Max Health: " + player.getMaxHealth()));
                                    player.sendSystemMessage(Component.literal("Health: " + player.getHealth()));
                                    player.sendSystemMessage(Component.literal("CanResurrect: " + VPUtil.canResurrect(player)));
                                    player.sendSystemMessage(Component.literal("Roflan: " + VPUtil.isRoflanEbalo(player)));
                                    player.sendSystemMessage(Component.literal("IsAlive: " + player.isAlive()));
                                    player.sendSystemMessage(Component.literal("Cheating: " + LeaderboardUtil.isCheating(player)));
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                        .then(Commands.literal("roflanList")
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    player.sendSystemMessage(Component.literal("UUID: " + player.getUUID()));
                                    player.sendSystemMessage(Component.literal("Roflan List: " + VPUtil.roflan));
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                        .then(Commands.literal("goldenNameList")
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    player.sendSystemMessage(Component.literal("Golden List: " + LeaderboardUtil.getTopPlayers()));
                                    player.sendSystemMessage(Component.literal("Has Golden: " + LeaderboardUtil.hasGoldenName(player.getUUID())));
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                        .then(Commands.literal("isHalloween")
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    player.sendSystemMessage(Component.literal("Is Halloween: " + VPUtil.isHalloween()));
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
        );
    }
}
