package com.example.examplemod.block.entity;

import com.example.examplemod.SodiumUtilMod;
import com.example.examplemod.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, SodiumUtilMod.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<RedstoneComputerEntity>> REDSTONE_COMPUTER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("my_block_entity",
                    () -> BlockEntityType.Builder.of(
                            RedstoneComputerEntity::new,
                            ModBlocks.REDSTONE_COMPUTER_BLOCK.get()
                    ).build(null));
}
