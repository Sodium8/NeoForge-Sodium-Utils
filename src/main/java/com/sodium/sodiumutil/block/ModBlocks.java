package com.sodium.sodiumutil.block;

import com.sodium.sodiumutil.block.custom.GPSStation;
import com.sodium.sodiumutil.block.custom.RedstoneComputerBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.sodium.sodiumutil.SodiumUtilMod.MODID;
import static com.sodium.sodiumutil.item.ModItems.ITEMS;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredBlock<Block> GPS_STATION_BLOCK = BLOCKS.register("gps_station_block",
            ()->new GPSStation(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));
    public static final DeferredItem<BlockItem> GPS_STATION_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("gps_station_block", GPS_STATION_BLOCK);
    public static final DeferredBlock<Block> ANTENNA_BLOCK = BLOCKS.register("antenna_block",
            ()->new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));
    public static final DeferredItem<BlockItem> ANTENNA_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("antenna_block", ANTENNA_BLOCK);
    public static final DeferredBlock<Block> REDSTONE_COMPUTER_BLOCK = BLOCKS.register("redstone_computer_block",
            ()->new RedstoneComputerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));
    public static final DeferredItem<BlockItem> REDSTONE_COMPUTER_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("redstone_computer_block", REDSTONE_COMPUTER_BLOCK);

}
