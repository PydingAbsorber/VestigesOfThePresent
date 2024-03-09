package com.pyding.vp.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;

public class KeyBinding {
    public static String KEY_CATEGORY_VP = "vp.key";
    public static String KEY_FIRST_VP = "vp.key.first";
    public static String KEY_SECOND_VP = "vp.key.second";

    public static String KEY_FIRST_ULT_VP = "vp.key.first_ult";
    public static String KEY_SECOND_ULT_VP = "vp.key.second_ult";

    public static final KeyMapping FIRST_KEY = new KeyMapping(
            KEY_FIRST_VP,
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_C, -1),
            KEY_CATEGORY_VP);
    public static final KeyMapping SECOND_KEY = new KeyMapping(
            KEY_SECOND_VP,
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_V, -1),
            KEY_CATEGORY_VP);

    public static final KeyMapping FIRST_KEY_ULT = new KeyMapping(
            KEY_FIRST_ULT_VP,
            KeyConflictContext.IN_GAME,
            KeyModifier.SHIFT,
            InputConstants.getKey(InputConstants.KEY_C, -1),
            KEY_CATEGORY_VP);
    public static final KeyMapping SECOND_KEY_ULT = new KeyMapping(
            KEY_SECOND_ULT_VP,
            KeyConflictContext.IN_GAME,
            KeyModifier.SHIFT,
            InputConstants.getKey(InputConstants.KEY_V, -1),
            KEY_CATEGORY_VP);

}
