package com.pyding.vp.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {
    public static String KEY_CATEGORY_VP = "vp.key";
    public static String KEY_FIRST_VP = "vp.key.first";
    public static String KEY_SECOND_VP = "vp.key.second";

    public static String KEY_FIRST_ULT_VP = "vp.key.first_ult";
    public static String KEY_SECOND_ULT_VP = "vp.key.second_ult";

    public static final KeyMapping FIRST_KEY = new KeyMapping(KEY_FIRST_VP, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_C,KEY_CATEGORY_VP);
    public static final KeyMapping SECOND_KEY = new KeyMapping(KEY_SECOND_VP, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V,KEY_CATEGORY_VP);

    public static final KeyMapping FIRST_KEY_ULT = new KeyMapping(KEY_FIRST_ULT_VP, KeyConflictContext.IN_GAME,  KeyModifier.SHIFT, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_C,KEY_CATEGORY_VP);
    public static final KeyMapping SECOND_KEY_ULT = new KeyMapping(KEY_SECOND_ULT_VP, KeyConflictContext.IN_GAME, KeyModifier.SHIFT, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V,KEY_CATEGORY_VP);

}
