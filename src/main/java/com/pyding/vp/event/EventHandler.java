package com.pyding.vp.event;

import com.pyding.vp.VestigesOfPresent;
import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import com.pyding.vp.capability.PlayerCapabilityVP;
import com.pyding.vp.item.ModItems;
import com.pyding.vp.item.artifacts.Vestige;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.network.packets.ClientToServerPacket;
import com.pyding.vp.util.VPUtil;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EggItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MinecartItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.AnimalTameEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.util.ICuriosHelper;

import java.util.Calendar;
import java.util.TimerTask;

@Mod.EventBusSubscriber(modid = VestigesOfPresent.MODID)
public class EventHandler {
    @SubscribeEvent(priority = EventPriority.LOWEST,receiveCanceled = true)
    public void deathEventLowest(LivingDeathEvent event){
        if(!event.isCanceled()) {
            if (event.getSource().getEntity() instanceof Player) {
                Player player = (Player) event.getSource().getEntity();
                if (player.getCommandSenderWorld().dimension() == Level.NETHER && player.getHealth() <= 1) {
                    player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(challange -> {
                        challange.setChallenge(5, challange.getChallenge(5) + 1, player);
                    });
                }
                player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(challange -> {
                    if (event.getEntity().getType().getCategory() == MobCategory.MONSTER)
                        challange.addMonsterKill(event.getEntity().getType().toString(), player);
                    if (event.getEntity().getType().getCategory() == MobCategory.CREATURE)
                        challange.addMobTame(event.getEntity().getType().toString(), player);
                });
            }
        } else if (event.getEntity() instanceof Player){
            if(event.isCanceled())
                System.out.println("canceled");
        }
    }
    @SubscribeEvent
    public static void tameEvent(AnimalTameEvent event){
        Animal animal = event.getAnimal();
        Player player = event.getTamer();
        player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(challange -> {
            challange.addMobTame(event.getEntity().getType().toString(),player);
        });
    }
    @SubscribeEvent
    public static void loginIn(PlayerEvent.PlayerLoggedInEvent event){
        Player player = event.getEntity();
        player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
            cap.sync(player);
        });
    }

    @SubscribeEvent
    public static void loginOut(PlayerEvent.PlayerLoggedOutEvent event){
        Player player = event.getEntity();
        player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
            cap.sync(player);
        });
    }
    @SubscribeEvent
    public static void onHeal(LivingHealEvent event){
        LivingEntity entity = event.getEntity();
        CompoundTag tag = entity.getPersistentData();
        if (tag != null) {
            float healingBonus = Math.max(-99,tag.getFloat("HealBonus"));
            event.setAmount(event.getAmount()+(event.getAmount()*(healingBonus/100)));
            float healDebt = tag.getFloat("HealDebt");
            if(healDebt > 0) {
                float lastHeal = event.getAmount();
                event.setAmount(Math.max(0, lastHeal - healDebt));
                if(healDebt-lastHeal > 0)
                    tag.putFloat("HealDebt", healDebt - lastHeal);
                else tag.putFloat("HealDebt", 0);
            }
        }
    }
    public static int firstSlotNumber = 0;
    public static Vestige firstVestige;
    @SubscribeEvent
    public static void tick(LivingEvent.LivingTickEvent event){
        LivingEntity entity = event.getEntity();
        CompoundTag tag = entity.getPersistentData();
        if (entity.tickCount % 300 != 0 && tag != null && tag.getFloat("HealBonus") != 0) {
            tag.putFloat("HealBonus",0);
            entity.getPersistentData().merge(tag);
        }
        if(event.getEntity() instanceof Player player){
            CompoundTag playerTag = player.getPersistentData();
            if(playerTag == null)
                playerTag = new CompoundTag();
            ICuriosHelper api = CuriosApi.getCuriosHelper();
            if(playerTag.getBoolean("VPButton1")){
                playerTag.putBoolean("VPButton1",false);
                api.findFirstCurio(player, (stackInSlot) -> {
                    if(stackInSlot.getItem() instanceof Vestige) {
                        System.out.println(stackInSlot);
                        return true;
                    }
                    return false;
                });
                api.getEquippedCurios(player).ifPresent(curio -> {
                    for(int i = 0; i < curio.getSlots(); i++){
                        ItemStack stack = curio.getStackInSlot(i);
                        if(stack.getItem() instanceof Vestige vestige){
                            if (!player.isShiftKeyDown())
                                vestige.setSpecialActive(vestige.getSpecialMaxTime(),player);
                            else vestige.setUltimateActive(vestige.getUltimateMaxTime(),player);
                            firstVestige = vestige;
                        }
                    }
                });
            } else if(playerTag.getBoolean("VPButton2")) {
                playerTag.putBoolean("VPButton2", false);
                api.getEquippedCurios(player).ifPresent(curio -> {
                    boolean aboba = false;
                    boolean aboba2 = false;
                    for(int i = 0; i < curio.getSlots(); i++){
                        ItemStack stack = curio.getStackInSlot(i);
                        if(stack.getItem() instanceof Vestige vestige){
                            if(aboba == false){
                                aboba = true;
                                firstSlotNumber = i;
                                continue;
                            }
                            aboba2 = true;
                            if(firstVestige != null && firstVestige == vestige)
                                return;
                            if (!player.isShiftKeyDown())
                                vestige.setSpecialActive(vestige.getSpecialMaxTime(),player);
                            else vestige.setUltimateActive(vestige.getUltimateMaxTime(),player);
                        }
                    }
                    if(aboba2 = false){
                        ItemStack stack = curio.getStackInSlot(firstSlotNumber);
                        if(stack.getItem() instanceof Vestige vestige){
                            if(firstVestige != null && firstVestige == vestige)
                                return;
                            if (!player.isShiftKeyDown())
                                vestige.setSpecialActive(vestige.getSpecialMaxTime(),player);
                            else vestige.setUltimateActive(vestige.getUltimateMaxTime(),player);
                        }
                    }
                });
            }
            player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                cap.addBiome(player.level.getBiome(player.blockPosition()).toString(), player);
                //System.out.println(player.level.getBiome(player.blockPosition()));
                for(int i = 0; i < PlayerCapabilityVP.totalVestiges; i++){
                    if(cap.getChallenge(i) >= cap.getMaximum(i) && !cap.hasCoolDown(i)){
                        cap.giveVestige(player,i);
                    }
                }
                if(System.currentTimeMillis() - cap.getTimeCd() >= 4*24*60*60*1000 || player.getMainHandItem().getItem() == Items.EGG) {
                    cap.clearCoolDown(player);
                    cap.addTimeCd(System.currentTimeMillis());
                }
                if(player.tickCount % 24000 == 0 && !cap.getLore(player,2))
                    player.sendSystemMessage(Component.translatable("vp.sleep"));
                if(player.tickCount % 100 == 0 && !cap.getLore(player,1))
                    cap.addLore(player,1);
            });
        }
    }

    @SubscribeEvent
    public static void capabilityAttach(AttachCapabilitiesEvent<Entity> event){
        if(event.getObject() instanceof Player && !(event.getObject() instanceof FakePlayer)){
            event.addCapability(new ResourceLocation(VestigesOfPresent.MODID, "properties"), new PlayerCapabilityProviderVP());
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event){
        event.getOriginal().reviveCaps();
        event.getOriginal().getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(oldStore -> {
            event.getEntity().getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(newStore -> {
                newStore.copyNBT(oldStore);
                newStore.sync(event.getEntity());
            });
        });
        event.getOriginal().invalidateCaps();
    }

    @SubscribeEvent
    public static void onPlayerClonedClient(ClientPlayerNetworkEvent.Clone event){
        event.getOldPlayer().reviveCaps();
        event.getOldPlayer().getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(oldStore -> {
            event.getNewPlayer().getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(newStore -> {
                newStore.copyNBT(oldStore);
            });
        });
        event.getOldPlayer().invalidateCaps();
    }


    @SubscribeEvent
    public static void sleep(PlayerSleepInBedEvent event){
        Player player = event.getEntity();
        /*player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap ->{
            cap.addLore(player,2);
        });*/
        PacketHandler.sendToServer(new ClientToServerPacket());
    }
}
