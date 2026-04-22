package com.example.examplemod.network.packets;

import com.example.examplemod.SodiumUtilMod;
import com.example.examplemod.item.custom.GpsCompassItem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ExamplePacket(BlockPos pos) implements CustomPacketPayload {

    public static final Type<ExamplePacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(SodiumUtilMod.MODID, "example_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ExamplePacket> STREAM_CODEC =
            StreamCodec.of(
                    (buf, packet) -> buf.writeBlockPos(packet.pos),
                    buf -> new ExamplePacket(buf.readBlockPos())
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ExamplePacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player player = ctx.player();

            ItemStack stack = player.getMainHandItem();
            if (stack.getItem() instanceof GpsCompassItem) {
                GpsCompassItem.setTarget(stack, packet.pos, player.level().dimension());
            }
        });
    }
}
