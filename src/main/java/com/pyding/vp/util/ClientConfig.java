package com.pyding.vp.util;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC = BUILDER.build();

    public static final ModConfigSpec.DoubleValue guiScaleChest = BUILDER.comment("Gui scale").defineInRange("guiScaleChest", 1, 0.01, 10);
    public static final ModConfigSpec.DoubleValue guiScaleLoot = BUILDER.comment("Gui scale").defineInRange("guiScaleLoot", 1, 0.01, 10);
    public static final ModConfigSpec.DoubleValue guiScaleGuide = BUILDER.comment("Gui scale").defineInRange("guiScaleGuide", 1, 0.01, 10);
    public static final ModConfigSpec.DoubleValue guiScaleVortex = BUILDER.comment("Gui scale").defineInRange("guiScaleVortex", 1, 0.01, 10);
    public static final ModConfigSpec.DoubleValue guiScaleChallenge = BUILDER.comment("Gui scale").defineInRange("guiScaleChallenge", 1, 0.01, 10);
    public static final ModConfigSpec.DoubleValue guiScaleLeaderboard = BUILDER.comment("Gui scale").defineInRange("guiScaleVestige", 1, 0.01, 10);
    public static final ModConfigSpec.DoubleValue guiScaleVestige = BUILDER.comment("Gui scale").defineInRange("guiScaleLeaderboard", 1, 0.01, 10);
    public static final ModConfigSpec.BooleanValue renderSoulIntegrity = BUILDER.comment("Should Soul Integrity be rendered?").define("renderSoulIntegrity", true);
    
}
