package com.example.examplemod.block.custom;

import com.example.examplemod.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class GPSStation extends Block {
    public GPSStation(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(ANTENNA_COUNT, 0));
    }
    public static final IntegerProperty ANTENNA_COUNT = IntegerProperty.create("antenna_count", 0, 4);
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ANTENNA_COUNT);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!level.isClientSide) {
            int newCount = countNearbyAntennas(level, pos);
            int currentCount = state.getValue(ANTENNA_COUNT);

            if (newCount != currentCount) {
                level.setBlock(pos, state.setValue(ANTENNA_COUNT, Math.min(newCount, 4)), 3);
            }
        }
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
    }

    private int countNearbyAntennas(Level level, BlockPos center) {
        int count = 0;
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) continue; // Пропускаем саму станцию

                    BlockPos checkPos = center.offset(x, y, z);
                    if (level.getBlockState(checkPos).is(ModBlocks.ANTENNA_BLOCK.get())) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
}
