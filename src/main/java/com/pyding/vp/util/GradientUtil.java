package com.pyding.vp.util;

import com.pyding.vp.VestigesOfThePresent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = VestigesOfThePresent.MODID)
public class GradientUtil {

    private static final int COLOR_START = 0xffd700; // золотой
    private static final int COLOR_END   = 0xffffff; // солнечный

    @SubscribeEvent
    public static void onServerChat(ServerChatEvent event) {
        if(event.getPlayer() != null && LeaderboardUtil.hasGoldenName(event.getPlayer().getUUID())) {
            String originalText = event.getMessage().getString();
            Component gradientMessage = buildGradientComponent(originalText);
            event.setMessage(gradientMessage);
        }
    }

    /**
     * Создаёт текстовый компонент, в котором каждый символ оригинального сообщения
     * плавно переходит по цвету от COLOR_START к COLOR_END.
     */
    public static Component buildGradientComponent(String message) {
        MutableComponent result = Component.empty();
        int length = message.length();
        if (length == 0) {
            return result;
        }

        for (int i = 0; i < length; i++) {
            char c = message.charAt(i);
            float t = (float) i / (length - 1);
            int color = interpolateColor(COLOR_START, COLOR_END, t);
            result.append(
                    Component.literal(String.valueOf(c))
                            .withStyle(style -> style.withColor(color))
            );
        }
        return result;
    }

    /**
     * Интерполирует (линейно) два цвета по коэффициенту t (0..1).
     */
    public static int interpolateColor(int start, int end, float t) {
        int r1 = (start >> 16) & 0xFF;
        int g1 = (start >> 8) & 0xFF;
        int b1 = start & 0xFF;
        int r2 = (end >> 16) & 0xFF;
        int g2 = (end >> 8) & 0xFF;
        int b2 = end & 0xFF;

        int r = (int) (r1 + (r2 - r1) * t);
        int g = (int) (g1 + (g2 - g1) * t);
        int b = (int) (b1 + (b2 - b1) * t);

        return (r << 16) | (g << 8) | b;
    }
}


