package com.pyding.vp.event;

import com.pyding.vp.VestigesOfPresent;
import com.pyding.vp.entity.HunterKiller;
import com.pyding.vp.entity.ModEntities;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = VestigesOfPresent.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityCreation {
    @SubscribeEvent
    public static void createAttributes(EntityAttributeCreationEvent event){
        event.put(ModEntities.KILLER.get(), HunterKiller.createAttributes().build());
    }
}
