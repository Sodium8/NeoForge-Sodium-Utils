package com.example.examplemod.block.custom;

import com.example.examplemod.Config;
import com.example.examplemod.block.entity.ModBlockEntities;
import com.example.examplemod.block.entity.RedstoneComputerEntity;
import com.example.examplemod.screen.RedstoneComputerScreen;
import com.example.examplemod.screen.menu.RedstoneComputerMenu;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.content.equipment.wrench.WrenchItem;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class RedstoneComputerBlock extends BaseEntityBlock {

    public RedstoneComputerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RedstoneComputerEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide) return ItemInteractionResult.SUCCESS;
        if (stack.getItem() instanceof WrenchItem){
            if (level.getBlockEntity(pos) instanceof RedstoneComputerEntity be) {
                ((ServerPlayer) player).openMenu(new SimpleMenuProvider(
                        (containerId, inventory, p) -> new RedstoneComputerMenu(containerId, inventory, be),
                        Component.literal("Logic")
                ), pos);
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level,
                                                                  BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null :
                createTickerHelper(type, ModBlockEntities.REDSTONE_COMPUTER_BLOCK_ENTITY.get(),
                        (lvl, pos, st, be) -> ((RedstoneComputerEntity) be).tick());
    }
}
