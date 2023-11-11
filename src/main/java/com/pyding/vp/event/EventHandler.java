package com.pyding.vp.event;

import com.pyding.vp.VestigesOfPresent;
import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import com.pyding.vp.capability.PlayerCapabilityVP;
import com.pyding.vp.item.artifacts.Artifact;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.util.ICuriosHelper;

import java.awt.*;

@Mod.EventBusSubscriber(modid = VestigesOfPresent.MODID)
public class EventHandler {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void deathEventLowest(LivingDeathEvent event){
        if (event.getSource().getEntity() instanceof Player){
            Player player = (Player) event.getSource().getEntity();
            if(player.getCommandSenderWorld().dimension() == Level.NETHER && player.getHealth() <= 1){
                player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(challange -> {
                    challange.setChallenge(5,challange.getChallenge(5) + 1);
                });
            }
            player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(challange -> {
                challange.setChallenge(5,challange.getChallenge(5) + 1);
                System.out.println(challange.getChallenge(5));
            });
            ICuriosHelper api = CuriosApi.getCuriosHelper();
            api.getEquippedCurios(player).ifPresent(curio -> {
                for(int i = 0; i < curio.getSlots(); i++){
                    ItemStack stack = curio.getStackInSlot(i);
                    if(stack.getItem() instanceof Artifact artifact){
                        artifact.setSpecialActive(666,player);
                    }
                }
            });
        }
    }

    @SubscribeEvent
    public static void capabilityAttach(AttachCapabilitiesEvent event){
        if(event.getObject() instanceof Player){
            if(!((Player) event.getObject()).getCapability(PlayerCapabilityProviderVP.playerCap).isPresent()){
                event.addCapability(new ResourceLocation(VestigesOfPresent.MODID, "properties"), new PlayerCapabilityProviderVP());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event){
        if(event.isWasDeath()) {
            event.getOriginal().reviveCaps();
            event.getOriginal().getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(oldStore -> {
                event.getEntity().getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(newStore -> {
                    newStore.copyNBT(oldStore);
                });
            });
            event.getOriginal().invalidateCaps();
        }
    }
}
