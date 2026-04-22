package com.example.examplemod.client;

import com.example.examplemod.SodiumUtilMod;
import com.example.examplemod.item.ModItems;
import com.example.examplemod.screen.RedstoneComputerScreen;
import com.example.examplemod.screen.menu.ModMenus;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.LodestoneTracker;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(
        modid = SodiumUtilMod.MODID,
        bus = EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT
)

public class ClientModEvents {
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(
                ModMenus.REDSTONE_COMPUTER_MENU.get(),
                RedstoneComputerScreen::new
        );
    }
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(
                    ModItems.GPS_COMPASS_ITEM.get(),
                    ResourceLocation.withDefaultNamespace("angle"),
                    (ItemStack stack, ClientLevel level, LivingEntity entity, int seed) -> {
                        if (level == null || entity == null) return 0.0F;
                        LodestoneTracker tracker = stack.get(DataComponents.LODESTONE_TRACKER);
                        if (tracker == null) {
                            return 0.0F;
                        }

                        if (!level.dimension().location().toString().equals(tracker.target().get().dimension().location().toString())) {
                            return 0.0F;
                        }
                        double targetX = tracker.target().get().pos().getX();
                        double targetZ = tracker.target().get().pos().getY();
                        double playerX = entity.getX();
                        double playerZ = entity.getZ();

                        double dx = targetX - playerX;
                        double dz = targetZ - playerZ;

                        double angleToTarget = Math.atan2(dz, dx);
                        double playerRot = entity.getYRot() * (Math.PI / 180.0);

                        double result = angleToTarget - playerRot - (Math.PI / 2.0);
                        result = (result % (Math.PI * 2) + Math.PI * 2) % (Math.PI * 2);

                        return (float) (result / (Math.PI * 2));
                    }
            );
        });
    }
}
