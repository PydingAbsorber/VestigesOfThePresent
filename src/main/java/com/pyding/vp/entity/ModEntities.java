package com.pyding.vp.entity;

import com.pyding.vp.VestigesOfPresent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, VestigesOfPresent.MODID);

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }

    public static final RegistryObject<EntityType<BlackHole>> BLACK_HOLE =
            ENTITIES.register("black_hole", () -> EntityType.Builder.<BlackHole>of(BlackHole::new, MobCategory.MISC)
                    .sized(11, 11)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(VestigesOfPresent.MODID, "black_hole").toString()));

    public static final RegistryObject<EntityType<VortexEntity>> VORTEX =
            ENTITIES.register("vortex", () -> EntityType.Builder.<VortexEntity>of(VortexEntity::new, MobCategory.MISC)
                    .sized(11, 11)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(VestigesOfPresent.MODID, "vortex").toString()));
    public static final RegistryObject<EntityType<HunterKiller>> KILLER =
            ENTITIES.register("killer", () -> EntityType.Builder.of(HunterKiller::new, MobCategory.MONSTER)
                    .sized(2, 2)
                    .clientTrackingRange(64)
                    .build("killer"));
    public static final RegistryObject<EntityType<EasterEggEntity>> EASTER_EGG_ENTITY =
            ENTITIES.register("easter_egg_entity", () -> EntityType.Builder.<EasterEggEntity>of(EasterEggEntity::new, MobCategory.MISC)
                    .sized(4, 4)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(VestigesOfPresent.MODID, "easter_egg_entity").toString()));
    public static final RegistryObject<EntityType<CloudEntity>> CLOUD_ENTITY =
            ENTITIES.register("cloud_entity", () -> EntityType.Builder.<CloudEntity>of(CloudEntity::new, MobCategory.MISC)
                    .sized(11, 11)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(VestigesOfPresent.MODID, "cloud_entity").toString()));
    public static final RegistryObject<EntityType<HungryOyster>> OYSTER =
            ENTITIES.register("oyster", () -> EntityType.Builder.of(HungryOyster::new, MobCategory.WATER_CREATURE)
                    .sized(5, 5)
                    .clientTrackingRange(64)
                    .build("oyster"));
}
