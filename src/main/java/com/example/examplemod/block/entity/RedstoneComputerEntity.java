package com.example.examplemod.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class RedstoneComputerEntity extends BlockEntity {
    public RedstoneComputerEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.REDSTONE_COMPUTER_BLOCK_ENTITY.get(), pos, blockState);
    }

    private String compilingCode = "";

    public void tick() {
        System.out.println(compilingCode);
    }
    public String getCode(){
        return compilingCode;
    }
    public void setCode(String value) {
        this.compilingCode = value;
        setChanged();
    }
    private void compile(String code){

    }
}
