package com.sodium.sodiumutil.item.custom;

import com.sodium.sodiumutil.block.ModBlocks;
import com.sodium.sodiumutil.block.custom.GPSStation;
import com.sodium.sodiumutil.screen.NavigationScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.LodestoneTracker;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class GpsCompassItem extends Item {
    public GpsCompassItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemBySlot(EquipmentSlot.MAINHAND);
        if (gps_nearby(level, player)) {
            if (level.isClientSide) {
                System.out.println("screen");
                Minecraft.getInstance().setScreen(new NavigationScreen());
            }
        } else if (stack.get(DataComponents.LODESTONE_TRACKER) != null) {
            ItemStack lodestoneCompass = new ItemStack(Items.COMPASS);
            setTarget(lodestoneCompass, stack.get(DataComponents.LODESTONE_TRACKER).target().get().pos(), stack.get(DataComponents.LODESTONE_TRACKER).target().get().dimension());
            player.setItemSlot(EquipmentSlot.MAINHAND, lodestoneCompass);

            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.containerMenu.broadcastChanges();
                serverPlayer.inventoryMenu.sendAllDataToRemote();
            }
        }
        return super.use(level, player, usedHand);
    }

    public static void setTarget(ItemStack stack, BlockPos pos, ResourceKey<Level> dimension) {
        LodestoneTracker tracker = new LodestoneTracker(
                Optional.of(new GlobalPos(dimension, pos)),
                false
        );
        stack.set(DataComponents.LODESTONE_TRACKER, tracker);
    }
    private boolean gps_nearby(Level level, LivingEntity plr) {
        BlockPos pos = plr.blockPosition().offset(new BlockPos(0, -1, 0));
        BlockState bs = level.getBlockState(pos);
        if (bs.getBlock() == ModBlocks.GPS_STATION_BLOCK.get()){
            if (bs.getValue(GPSStation.ANTENNA_COUNT) >= 3){
                return true;
            }
        }
        return false;
    }
}
