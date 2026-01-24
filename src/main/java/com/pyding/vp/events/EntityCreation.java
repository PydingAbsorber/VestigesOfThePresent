package com.pyding.vp.events;

import com.pyding.vp.VestigesOfThePresent;
import com.pyding.vp.entity.HungryOyster;
import com.pyding.vp.entity.HunterKiller;
import com.pyding.vp.entity.ModEntities;
import com.pyding.vp.entity.SillySeashell;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(modid = VestigesOfThePresent.MODID)
public class EntityCreation {
    @SubscribeEvent
    public static void createAttributes(EntityAttributeCreationEvent event){
        event.put(ModEntities.KILLER.get(), HunterKiller.createAttributes().build());
        event.put(ModEntities.OYSTER.get(), HungryOyster.createAttributes().build());
        event.put(ModEntities.SEASHELL.get(), SillySeashell.createAttributes().build());
    }
}
