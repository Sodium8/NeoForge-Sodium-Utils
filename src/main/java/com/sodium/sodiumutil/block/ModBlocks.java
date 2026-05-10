package com.sodium.sodiumutil.block;

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
    /*public static final DeferredBlock<Block> GPS_STATION_BLOCK = BLOCKS.register("gps_station_block",
            ()->new GPSStation(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));*/

}
