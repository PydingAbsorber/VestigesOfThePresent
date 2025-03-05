package com.pyding.vp.util;

import com.pyding.vp.VestigesOfThePresent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = VestigesOfThePresent.MODID)
public class GradientUtil {

    private static final int GOLDEN = 0xffd700;
    private static final int WHITE = 0xffffff;

    @SubscribeEvent
    public static void onServerChat(ServerChatEvent event) {
        if(event.getPlayer() != null && LeaderboardUtil.hasGoldenName(event.getPlayer().getUUID())) {
            String originalText = event.getMessage().getString();
            Component gradientMessage = goldenGradient(originalText);
            event.setMessage(gradientMessage);
        }
    }

    /**
     * Создаёт текстовый компонент, в котором каждый символ оригинального сообщения
     * плавно переходит по цвету от COLOR_START к COLOR_END.
     */
    public static Component goldenGradient(String message) {
        MutableComponent result = Component.empty();
        int length = message.length();
        if (length == 0) {
            return result;
        }

        long time = System.currentTimeMillis();
        float progress = (float) (time % 1000L) / 1000L;

        for (int i = 0; i < length; i++) {
            char c = message.charAt(i);
            float t = (float) i / (length - 1);
            float animatedT = (t + progress) % 1.0f;
            int color = getRainbowColor(animatedT, GOLD_TO_WHITE);
            result.append(
                    Component.literal(String.valueOf(c))
                            .withStyle(Style.EMPTY.withColor(color))
            );
        }

        return result;
    }

    public static Component stellarGradient(String message) {
        MutableComponent result = Component.empty();
        int length = message.length();
        if (length == 0) {
            return result;
        }

        long time = System.currentTimeMillis();
        float progress = (float) (time % 1000L) / 1000L;

        for (int i = 0; i < length; i++) {
            char c = message.charAt(i);
            float t = (float) i / (length - 1);
            float animatedT = (t + progress) % 1.0f;
            int color = getRainbowColor(animatedT,RAINBOW_COLORS);
            result.append(
                    Component.literal(String.valueOf(c))
                            .withStyle(Style.EMPTY.withColor(color))
            );
        }

        return result;
    }

    private static final int[] RAINBOW_COLORS = {
            0xf5a478,  // Оранжевый
            0xf5bd93,  // Светло-оранжевый
            0xffe4c4,  // Персиковый (переход к холодным тонам)
            0x7b68ee,  // Сине-фиолетовый (лавандовый)
            0x9370db,  // Средний фиолетовый
            0x8b00ff,  // Яркий фиолетовый
            0x6a0dad,  // Насыщенный фиолетовый
            0x450e4f,  // Бардовый (пиковая точка)
            0x6a0dad,  // Насыщенный фиолетовый
            0x8b00ff,  // Яркий фиолетовый
            0x9370db,  // Средний фиолетовый
            0x7b68ee,  // Сине-фиолетовый
            0xffe4c4,  // Персиковый (переход к теплым тонам)
            0xf5bd93,  // Светло-оранжевый
            0xf5a478,  // Оранжевый
    };

    private static final int[] GOLD_TO_WHITE = {
            0xFFFAE6,  // Почти белый с золотистым оттенком
            0xFFF8D6,  // Очень светлый золотой
            0xFFF3B0,  // Кремово-золотой
            0xFFED8A,  // Мягкий золотистый
            0xFFE55C,  // Светло-золотой
            0xFFD700,  // Яркий золотой (Gold)
            0xFFE55C,  // Светло-золотой
            0xFFED8A,  // Мягкий золотистый
            0xFFF3B0,  // Кремово-золотой
            0xFFF8D6,  // Очень светлый золотой
            0xFFFAE6,  // Почти белый с золотистым оттенком
            0xFFFFFF   // Чистый белый (White)
    };

    private static int getRainbowColor(float t, int[] colors) {
        int colorCount = colors.length;
        float segment = 1.0f / (colorCount - 1);

        int index1 = (int) (t / segment);
        int index2 = (index1 + 1) % colorCount;

        float localT = (t % segment) / segment;

        return interpolateColor(colors[index1], colors[index2], localT);
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


