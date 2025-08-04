package com.pyding.vp.util;

import com.pyding.vp.VestigesOfThePresent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = VestigesOfThePresent.MODID)
public class GradientUtil {

    @SubscribeEvent
    public static void onServerChat(ServerChatEvent event) {
        if(event.getPlayer() != null) {
            Player player = event.getPlayer();
            /*if(LeaderboardUtil.hasSpecialName(player.getName().getString())){
                String originalText = event.getMessage().getString();
                Component gradientMessage = customGradient(originalText,PURPLE_DARK_PURPLE);
                event.setMessage(gradientMessage);
            }
w            else */if(LeaderboardUtil.hasGoldenName(player.getUUID())) {
                String originalText = event.getMessage().getString();
                Component gradientMessage = goldenGradient(originalText);
                event.setMessage(gradientMessage);
            }
        }
    }

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

    public static Component customGradient(String message, int[] colors) {
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
            int color = getRainbowColor(animatedT,colors);
            result.append(
                    Component.literal(String.valueOf(c))
                            .withStyle(Style.EMPTY.withColor(color))
            );
        }

        return result;
    }

    public static final int[] RAINBOW_COLORS = {
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

    public static final int[] GOLD_TO_WHITE = {
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

    public static final int[] PURPLE_DARK_PURPLE = {
            0x4B1B6D, // Интенсивный фиолетовый
            0x5A267A, // Яркий королевский фиолетовый
            0x6A3588, // Сочно-фиолетовый
            0x7B4495, // Малиново-фиолетовый
            0x8D55A3, // Нежно-сиреневый с фиолетовым оттенком
            0x9F67B0, // Пастельно-фиолетовый
            0xB27ABD, // Лавандово-молочный
            0xC58ECA, // Светло-лавандовый
            0xD8A3D6, // Очень светлый перламутровый фиолетовый
            0xC58ECA, // Возврат к светло-лавандовому
            0xB27ABD, // Лавандово-молочный
            0x9F67B0, // Пастельно-фиолетовый
            0x8D55A3, // Нежно-сиреневый
            0x7B4495, // Малиново-фиолетовый
            0x6A3588, // Сочно-фиолетовый
            0x5A267A, // Яркий королевский фиолетовый
            0x4B1B6D, // Интенсивный фиолетовый
    };

    public static final int[] BLUE_LIGHT_BLUE = {
            0x00008B, // Тёмно-синий
            0x0000CD, // Средне-синий
            0x0000FF, // Синий
            0x1E90FF, // Ярко-голубой
            0x00BFFF, // Насыщенный небесный
            0x87CEEB, // Небесно-голубой
            0x87CEFA, // Светло-небесный
            0xADD8E6, // Светло-голубой
            0xE0FFFF, // Очень светлый голубой (бирюзовый оттенок)
            0xADD8E6, // Светло-голубой
            0x87CEFA, // Светло-небесный
            0x87CEEB, // Небесно-голубой
            0x00BFFF, // Насыщенный небесный
            0x1E90FF, // Ярко-голубой
            0x0000FF, // Синий
            0x0000CD, // Средне-синий
            0x00008B, // Тёмно-синий
    };


    public static final int[] MYSTERY = {
            // Фиолетовая гамма (15 шагов: светлый → тёмный)
            0xE0B0FF, 0xD19BEE, 0xC287DD, 0xB373CC, 0xA45FBB,
            0x954BAA, 0x863799, 0x772388, 0x680F77, 0x590066,
            0x4A0055, 0x3B0044, 0x2C0033, 0x1D0022, 0x1A0030,

            // Зелёная гамма (15 шагов: тёмный → обычный)
            0x002000, 0x003000, 0x004000, 0x005000, 0x006000,
            0x007000, 0x008000, 0x009000, 0x00A000, 0x00B000,
            0x00C000, 0x00D000, 0x00E000, 0x00F000, 0x00FF00,

            // Обратный путь для циклического эффекта
            0x00F000, 0x00E000, 0x00D000, 0x00C000, 0x00B000,
            0x00A000, 0x009000, 0x008000, 0x007000, 0x006000,
            0x005000, 0x004000, 0x003000, 0x002000, 0x1A0030,
            0x2C0033, 0x3B0044, 0x4A0055, 0x590066, 0x680F77,
            0x772388, 0x863799, 0x954BAA, 0xA45FBB, 0xB373CC,
            0xC287DD, 0xD19BEE, 0xE0B0FF
    };

    private static int getRainbowColor(float t, int[] colors) {
        int colorCount = colors.length;
        float segment = 1.0f / (colorCount - 1);

        int index1 = (int) (t / segment);
        int index2 = (index1 + 1) % colorCount;

        float localT = (t % segment) / segment;

        return interpolateColor(colors[index1], colors[index2], localT);
    }


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


