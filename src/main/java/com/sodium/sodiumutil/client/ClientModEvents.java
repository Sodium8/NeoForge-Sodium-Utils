package com.sodium.sodiumutil.client;

import com.sodium.sodiumutil.SodiumUtilMod;
import com.sodium.sodiumutil.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.LodestoneTracker;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(
        modid = SodiumUtilMod.MODID,
        bus = EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT
)

public class ClientModEvents {
    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiEvent.Post event){
        Minecraft mc = Minecraft.getInstance();
    }
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {

    }
}
