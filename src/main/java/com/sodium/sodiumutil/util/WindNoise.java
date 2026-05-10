package com.sodium.sodiumutil.util;

import com.sodium.sodiumutil.data.WindAttachment;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;

public class WindNoise {

    public static float getStrength(Level level, ChunkPos pos) {
        if (!(level instanceof ServerLevel serverLevel)) return 0.3f;

        long day = level.getDayTime() / 24000L;
        long seed = serverLevel.getSeed() ^ (day * 0x5DEECE66DL) ^ ((long)pos.x * 31213L) ^ ((long)pos.z * 131231L);

        RandomSource random = RandomSource.create(seed);

        float noise = 0f;
        float amp = 1.0f;
        for (int i = 0; i < 5; i++) {
            noise += (random.nextFloat() * 2 - 1) * amp;
            amp *= 0.5f;
        }

        return Math.clamp((noise * 0.5f + 0.5f) * 0.75f + 0.15f, 0.05f, 1.0f);
    }

    public static float getDirection(Level level, ChunkPos pos) {
        if (!(level instanceof ServerLevel serverLevel)) return 0f;

        long day = level.getDayTime() / 24000L;
        // Большой масштаб для плавного изменения направления
        long seed = serverLevel.getSeed() ^ (day * 0x5DEECE66DL + 987654321L)
                ^ ((long)pos.x / 8 * 54321L) ^ ((long)pos.z / 8 * 98765L);

        RandomSource random = RandomSource.create(seed);
        return random.nextFloat() * (float) (Math.PI * 2);
    }

    public static Vec3 getWindAt(Level level, Vec3 pos) {
        int cx = (int) pos.x >> 4;
        int cz = (int) pos.z >> 4;

        double totalDx = 0, totalDz = 0;
        double weightSum = 0;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                ChunkPos cp = new ChunkPos(cx + dx, cz + dz);
                LevelChunk chunk = level.getChunk(cp.x, cp.z);

                WindAttachment wind = chunk.getData(WindAttachment.WIND_ATTACHMENT.get()); // важно!
                if (wind == null) continue;

                double chunkCenterX = cp.getMiddleBlockX();
                double chunkCenterZ = cp.getMiddleBlockZ();
                double distX = (pos.x - chunkCenterX) / 16.0;
                double distZ = (pos.z - chunkCenterZ) / 16.0;
                double weight = Math.max(0.0, 1.0 - Math.sqrt(distX * distX + distZ * distZ));

                double rad = wind.direction;
                totalDx += Math.cos(rad) * wind.strength * weight;
                totalDz += Math.sin(rad) * wind.strength * weight;
                weightSum += weight;
            }
        }

        if (weightSum < 0.01) return Vec3.ZERO;

        return new Vec3(totalDx / weightSum, 0, totalDz / weightSum);
    }
}