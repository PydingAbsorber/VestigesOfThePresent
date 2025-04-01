package com.pyding.vp.client.sounds;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class VPSoundUtil {

    public static double bufferVolume = -1;

    public static float getRecordsVolume(SoundSource source) {
        return Minecraft.getInstance().options.getSoundSourceVolume(source);
    }

    public static void reduceRecordsVolume(SoundSource source, double scalePercent, double scale) {
        float volume = getRecordsVolume(source);
        if(bufferVolume == -1){
            if(volume == 0)
                return;
            else bufferVolume = volume;
        } else if(volume == 0){
            Minecraft.getInstance().options.getSoundSourceOptionInstance(source).set(bufferVolume);
            bufferVolume = -1;
            return;
        }
        Minecraft.getInstance().options.getSoundSourceOptionInstance(source).set(Math.max(0,(volume*scalePercent)-scale));
    }

    public static void reduceRecordsVolume(SoundSource source, ResourceLocation sound, double scalePercent, double scale) {
        float volume = getRecordsVolume(source);
        if(bufferVolume == -1){
            if(volume == 0)
                return;
            else bufferVolume = volume;
        } else if(volume == 0){
            Minecraft.getInstance().options.getSoundSourceOptionInstance(source).set(bufferVolume);
            bufferVolume = -1;
            Minecraft.getInstance().getSoundManager().stop(sound, source);
            return;
        }
        Minecraft.getInstance().options.getSoundSourceOptionInstance(source).set(Math.max(0,(volume*scalePercent)-scale));
    }

    public static void increaseRecordsVolume(SoundSource source, double scalePercent, double scale) {
        float volume = getRecordsVolume(source);
        if(volume < bufferVolume) {
            Minecraft.getInstance().options.getSoundSourceOptionInstance(source).set(Math.min(bufferVolume, (volume + volume*scalePercent) + scale));
        } else bufferVolume = -1;
    }
}
