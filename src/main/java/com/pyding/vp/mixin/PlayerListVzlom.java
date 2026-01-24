package com.pyding.vp.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(value = PlayerList.class)
public interface PlayerListVzlom {

    @Accessor("players")
    @Mutable
    List<ServerPlayer> getPlayers();
}
