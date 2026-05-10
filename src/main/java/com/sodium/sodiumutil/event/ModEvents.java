package com.sodium.sodiumutil.event;

import com.sodium.sodiumutil.data.WindAttachment;
import com.sodium.sodiumutil.util.WindNoise;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class ModEvents {

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        if (player.isCreative() || player.isSpectator() || player.isSleeping() || player.isPassenger()) {
            return;
        }

        applyWindToPlayer(player);
    }

    private static void applyWindToPlayer(ServerPlayer player) {
        Vec3 wind = WindNoise.getWindAt(player.level(), player.position());

        double force = 0.1;

        if (player.onGround()) force *= 0.4;
        if (player.isFallFlying()) force *= 1.6;
        System.out.println(wind.scale(force).x+" "+wind.scale(force).z);
        player.addDeltaMovement(wind.scale(force));
    }

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        if (!(event.getLevel() instanceof Level level) || level.isClientSide) {
            return;
        }

        if (event.getChunk() instanceof LevelChunk chunk) {
            WindAttachment wind = chunk.getData(WindAttachment.WIND_ATTACHMENT.get());

            if (wind == null) {
                wind = new WindAttachment();
                chunk.setData(WindAttachment.WIND_ATTACHMENT.get(), wind);
            }

            updateWind(wind, level, chunk.getPos());
        }
    }

    private static void updateWind(WindAttachment wind, Level level, ChunkPos pos) {
        wind.strength = WindNoise.getStrength(level, pos);
        wind.direction = WindNoise.getDirection(level, pos);
    }
}
