package com.sodium.sodiumutil.screen.menu;

import com.sodium.sodiumutil.block.ModBlocks;
import com.sodium.sodiumutil.block.entity.RedstoneComputerEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class RedstoneComputerMenu extends AbstractContainerMenu {
    public final RedstoneComputerEntity blockEntity;
    private final Level level;

    public RedstoneComputerMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public RedstoneComputerMenu(int containerId, Inventory inv, BlockEntity blockEntity) {
        super(ModMenus.REDSTONE_COMPUTER_MENU.get(), containerId);
        this.blockEntity = ((RedstoneComputerEntity) blockEntity);
        this.level = inv.player.level();
    }



    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, ModBlocks.REDSTONE_COMPUTER_BLOCK.get());
    }
}