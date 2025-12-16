package com.pyding.vp.util;

import com.pyding.vp.capability.PlayerCapabilityVP;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class ClientConfig {
    public static final ClientConfig.Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    public ClientConfig() {
    }

    static {
        Pair<ClientConfig.Common, ForgeConfigSpec> specPair = (new ForgeConfigSpec.Builder()).configure(ClientConfig.Common::new);
        COMMON_SPEC = (ForgeConfigSpec)specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static class Common {
        public final ForgeConfigSpec.DoubleValue guiScaleChest;
        public final ForgeConfigSpec.DoubleValue guiScaleLoot;
        public final ForgeConfigSpec.DoubleValue guiScaleGuide;
        public final ForgeConfigSpec.DoubleValue guiScaleVortex;
        public final ForgeConfigSpec.DoubleValue guiScaleChallenge;
        public final ForgeConfigSpec.DoubleValue guiScaleLeaderboard;
        public final ForgeConfigSpec.DoubleValue guiScaleVestige;
        public final ForgeConfigSpec.BooleanValue renderSoulIntegrity;

        public Common(ForgeConfigSpec.Builder builder) {
            guiScaleChest = builder.comment("Gui scale").defineInRange("guiScaleChest", 1, 0.01, 10);
            guiScaleLoot = builder.comment("Gui scale").defineInRange("guiScaleLoot", 1, 0.01, 10);
            guiScaleGuide = builder.comment("Gui scale").defineInRange("guiScaleGuide", 1, 0.01, 10);
            guiScaleVortex = builder.comment("Gui scale").defineInRange("guiScaleVortex", 1, 0.01, 10);
            guiScaleChallenge = builder.comment("Gui scale").defineInRange("guiScaleChallenge", 1, 0.01, 10);
            guiScaleVestige = builder.comment("Gui scale").defineInRange("guiScaleVestige", 1, 0.01, 10);
            guiScaleLeaderboard = builder.comment("Gui scale").defineInRange("guiScaleLeaderboard", 1, 0.01, 10);
            renderSoulIntegrity = builder.comment("Should Soul Integrity be rendered?").define("renderSoulIntegrity", true);
        }
    }
}
