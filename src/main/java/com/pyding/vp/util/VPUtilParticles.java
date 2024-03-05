package com.pyding.vp.util;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;

import java.util.HashMap;
import java.util.Map;

public class VPUtilParticles {
    private static final Map<Integer, ParticleOptions> particleIndexMap = new HashMap<>();
    private static final Map<ParticleOptions, Integer> particleOptionsMap = new HashMap<>();


    public static void fillParticleMaps() {
        particleOptionsMap.put(ParticleTypes.AMBIENT_ENTITY_EFFECT, 0);
        particleOptionsMap.put(ParticleTypes.ANGRY_VILLAGER, 1);
        //particleOptionsMap.put(ParticleTypes.BLOCK, 2);
        //particleOptionsMap.put(ParticleTypes.BLOCK_MARKER, 3);
        particleOptionsMap.put(ParticleTypes.BUBBLE, 4);
        particleOptionsMap.put(ParticleTypes.CLOUD, 5);
        particleOptionsMap.put(ParticleTypes.CRIT, 6);
        particleOptionsMap.put(ParticleTypes.DAMAGE_INDICATOR, 7);
        particleOptionsMap.put(ParticleTypes.DRAGON_BREATH, 8);
        particleOptionsMap.put(ParticleTypes.DRIPPING_LAVA, 9);
        particleOptionsMap.put(ParticleTypes.FALLING_LAVA, 10);
        particleOptionsMap.put(ParticleTypes.LANDING_LAVA, 11);
        particleOptionsMap.put(ParticleTypes.DRIPPING_WATER, 12);
        particleOptionsMap.put(ParticleTypes.FALLING_WATER, 13);
        //particleOptionsMap.put(ParticleTypes.DUST, 14);
        //particleOptionsMap.put(ParticleTypes.DUST_COLOR_TRANSITION, 15);
        particleOptionsMap.put(ParticleTypes.EFFECT, 16);
        particleOptionsMap.put(ParticleTypes.ELDER_GUARDIAN, 17);
        particleOptionsMap.put(ParticleTypes.ENCHANTED_HIT, 18);
        particleOptionsMap.put(ParticleTypes.ENCHANT, 19);
        particleOptionsMap.put(ParticleTypes.END_ROD, 20);
        particleOptionsMap.put(ParticleTypes.ENTITY_EFFECT, 21);
        particleOptionsMap.put(ParticleTypes.EXPLOSION_EMITTER, 22);
        particleOptionsMap.put(ParticleTypes.EXPLOSION, 23);
        particleOptionsMap.put(ParticleTypes.SONIC_BOOM, 24);
        //particleOptionsMap.put(ParticleTypes.FALLING_DUST, 25);
        particleOptionsMap.put(ParticleTypes.FIREWORK, 26);
        particleOptionsMap.put(ParticleTypes.FISHING, 27);
        particleOptionsMap.put(ParticleTypes.FLAME, 28);
        particleOptionsMap.put(ParticleTypes.CHERRY_LEAVES, 29);
        particleOptionsMap.put(ParticleTypes.SCULK_SOUL, 30);
        //particleOptionsMap.put(ParticleTypes.SCULK_CHARGE, 31);
        particleOptionsMap.put(ParticleTypes.SCULK_CHARGE_POP, 32);
        particleOptionsMap.put(ParticleTypes.SOUL_FIRE_FLAME, 33);
        particleOptionsMap.put(ParticleTypes.SOUL, 34);
        particleOptionsMap.put(ParticleTypes.FLASH, 35);
        particleOptionsMap.put(ParticleTypes.HAPPY_VILLAGER, 36);
        particleOptionsMap.put(ParticleTypes.COMPOSTER, 37);
        particleOptionsMap.put(ParticleTypes.HEART, 38);
        particleOptionsMap.put(ParticleTypes.INSTANT_EFFECT, 39);
        //particleOptionsMap.put(ParticleTypes.ITEM, 40);
        //particleOptionsMap.put(ParticleTypes.VIBRATION, 41);
        particleOptionsMap.put(ParticleTypes.ITEM_SLIME, 42);
        particleOptionsMap.put(ParticleTypes.ITEM_SNOWBALL, 43);
        particleOptionsMap.put(ParticleTypes.LARGE_SMOKE, 44);
        particleOptionsMap.put(ParticleTypes.LAVA, 45);
        particleOptionsMap.put(ParticleTypes.MYCELIUM, 46);
        particleOptionsMap.put(ParticleTypes.NOTE, 47);
        particleOptionsMap.put(ParticleTypes.POOF, 48);
        particleOptionsMap.put(ParticleTypes.PORTAL, 49);
        particleOptionsMap.put(ParticleTypes.RAIN, 50);
        particleOptionsMap.put(ParticleTypes.SMOKE, 51);
        particleOptionsMap.put(ParticleTypes.SNEEZE, 52);
        particleOptionsMap.put(ParticleTypes.SPIT, 53);
        particleOptionsMap.put(ParticleTypes.SQUID_INK, 54);
        particleOptionsMap.put(ParticleTypes.SWEEP_ATTACK, 55);
        particleOptionsMap.put(ParticleTypes.TOTEM_OF_UNDYING, 56);
        particleOptionsMap.put(ParticleTypes.UNDERWATER, 57);
        particleOptionsMap.put(ParticleTypes.SPLASH, 58);
        particleOptionsMap.put(ParticleTypes.WITCH, 59);
        particleOptionsMap.put(ParticleTypes.BUBBLE_POP, 60);
        particleOptionsMap.put(ParticleTypes.CURRENT_DOWN, 61);
        particleOptionsMap.put(ParticleTypes.BUBBLE_COLUMN_UP, 62);
        particleOptionsMap.put(ParticleTypes.NAUTILUS, 63);
        particleOptionsMap.put(ParticleTypes.DOLPHIN, 64);
        particleOptionsMap.put(ParticleTypes.CAMPFIRE_COSY_SMOKE, 65);
        particleOptionsMap.put(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, 66);
        particleOptionsMap.put(ParticleTypes.DRIPPING_HONEY, 67);
        particleOptionsMap.put(ParticleTypes.FALLING_HONEY, 68);
        particleOptionsMap.put(ParticleTypes.LANDING_HONEY, 69);
        particleOptionsMap.put(ParticleTypes.FALLING_NECTAR, 70);
        particleOptionsMap.put(ParticleTypes.FALLING_SPORE_BLOSSOM, 71);
        particleOptionsMap.put(ParticleTypes.ASH, 72);
        particleOptionsMap.put(ParticleTypes.CRIMSON_SPORE, 73);
        particleOptionsMap.put(ParticleTypes.WARPED_SPORE, 74);
        particleOptionsMap.put(ParticleTypes.SPORE_BLOSSOM_AIR, 75);
        particleOptionsMap.put(ParticleTypes.DRIPPING_OBSIDIAN_TEAR, 76);
        particleOptionsMap.put(ParticleTypes.FALLING_OBSIDIAN_TEAR, 77);
        particleOptionsMap.put(ParticleTypes.LANDING_OBSIDIAN_TEAR, 78);
        particleOptionsMap.put(ParticleTypes.REVERSE_PORTAL, 79);
        particleOptionsMap.put(ParticleTypes.WHITE_ASH, 80);
        particleOptionsMap.put(ParticleTypes.SMALL_FLAME, 81);
        particleOptionsMap.put(ParticleTypes.SNOWFLAKE, 82);
        particleOptionsMap.put(ParticleTypes.DRIPPING_DRIPSTONE_LAVA, 83);
        particleOptionsMap.put(ParticleTypes.FALLING_DRIPSTONE_LAVA, 84);
        particleOptionsMap.put(ParticleTypes.DRIPPING_DRIPSTONE_WATER, 85);
        particleOptionsMap.put(ParticleTypes.FALLING_DRIPSTONE_WATER, 86);
        particleOptionsMap.put(ParticleTypes.GLOW_SQUID_INK, 87);
        particleOptionsMap.put(ParticleTypes.GLOW, 88);
        particleOptionsMap.put(ParticleTypes.WAX_ON, 89);
        particleOptionsMap.put(ParticleTypes.WAX_OFF, 90);
        particleOptionsMap.put(ParticleTypes.ELECTRIC_SPARK, 91);
        particleOptionsMap.put(ParticleTypes.SCRAPE, 92);
        for (Map.Entry<ParticleOptions, Integer> entry : particleOptionsMap.entrySet()) {
            particleIndexMap.put(entry.getValue(), entry.getKey());
        }
    }

    public static int getParticleId(ParticleOptions particle) {
        return particleOptionsMap.getOrDefault(particle, -1);
    }

    public static ParticleOptions getParticleById(int id) {
        return particleIndexMap.getOrDefault(id, null);
    }
}
