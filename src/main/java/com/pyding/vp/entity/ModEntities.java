package com.pyding.vp.entity;

import com.pyding.vp.VestigesOfThePresent;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, VestigesOfThePresent.MODID);

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }

    public static final DeferredHolder<EntityType<?>, EntityType<BlackHole>> BLACK_HOLE =
            ENTITIES.register("black_hole", () -> EntityType.Builder.<BlackHole>of(BlackHole::new, MobCategory.MISC)
                    .sized(11.0F, 11.0F)
                    .clientTrackingRange(64)
                    .build(ResourceLocation.fromNamespaceAndPath(VestigesOfThePresent.MODID, "black_hole").toString()));

    public static final DeferredHolder<EntityType<?>, EntityType<VortexEntity>> VORTEX =
            ENTITIES.register("vortex", () -> EntityType.Builder.<VortexEntity>of(VortexEntity::new, MobCategory.MISC)
                    .sized(11.0F, 11.0F)
                    .clientTrackingRange(64)
                    .build(ResourceLocation.fromNamespaceAndPath(VestigesOfThePresent.MODID, "vortex").toString()));

    public static final DeferredHolder<EntityType<?>, EntityType<HunterKiller>> KILLER =
            ENTITIES.register("killer", () -> EntityType.Builder.of(HunterKiller::new, MobCategory.MONSTER)
                    .sized(2.0F, 2.0F)
                    .clientTrackingRange(64)
                    .build(ResourceLocation.fromNamespaceAndPath(VestigesOfThePresent.MODID, "killer").toString()));

    public static final DeferredHolder<EntityType<?>, EntityType<EasterEggEntity>> EASTER_EGG_ENTITY =
            ENTITIES.register("easter_egg_entity", () -> EntityType.Builder.<EasterEggEntity>of(EasterEggEntity::new, MobCategory.MISC)
                    .sized(1.0F, 1.0F)
                    .clientTrackingRange(64)
                    .build(ResourceLocation.fromNamespaceAndPath(VestigesOfThePresent.MODID, "easter_egg_entity").toString()));

    public static final DeferredHolder<EntityType<?>, EntityType<CloudEntity>> CLOUD_ENTITY =
            ENTITIES.register("cloud_entity", () -> EntityType.Builder.<CloudEntity>of(CloudEntity::new, MobCategory.MISC)
                    .sized(3.0F, 3.0F)
                    .clientTrackingRange(64)
                    .build(ResourceLocation.fromNamespaceAndPath(VestigesOfThePresent.MODID, "cloud_entity").toString()));

    public static final DeferredHolder<EntityType<?>, EntityType<HungryOyster>> OYSTER =
            ENTITIES.register("oyster", () -> EntityType.Builder.of(HungryOyster::new, MobCategory.WATER_CREATURE)
                    .sized(3.0F, 3.0F)
                    .clientTrackingRange(64)
                    .build(ResourceLocation.fromNamespaceAndPath(VestigesOfThePresent.MODID, "oyster").toString()));

    public static final DeferredHolder<EntityType<?>, EntityType<SillySeashell>> SEASHELL =
            ENTITIES.register("seashell", () -> EntityType.Builder.of(SillySeashell::new, MobCategory.WATER_CREATURE)
                    .sized(3.0F, 3.0F)
                    .clientTrackingRange(64)
                    .build(ResourceLocation.fromNamespaceAndPath(VestigesOfThePresent.MODID, "seashell").toString()));

    public static final DeferredHolder<EntityType<?>, EntityType<ShellHealEntity>> SHELLHEAL =
            ENTITIES.register("shellheal", () -> EntityType.Builder.<ShellHealEntity>of(ShellHealEntity::new, MobCategory.MISC)
                    .sized(1.0F, 1.0F)
                    .clientTrackingRange(64)
                    .build(ResourceLocation.fromNamespaceAndPath(VestigesOfThePresent.MODID, "shellheal").toString()));
}
