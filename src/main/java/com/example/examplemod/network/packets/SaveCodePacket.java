package com.example.examplemod.network.packets;

import com.example.examplemod.SodiumUtilMod;
import com.example.examplemod.block.entity.RedstoneComputerEntity;
import com.example.examplemod.item.custom.GpsCompassItem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SaveCodePacket(BlockPos pos, String code) implements CustomPacketPayload {

    public static final Type<SaveCodePacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(SodiumUtilMod.MODID, "save_code_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SaveCodePacket> STREAM_CODEC =
            StreamCodec.of(
                    (buf, packet) -> {buf.writeBlockPos(packet.pos); buf.writeUtf(packet.code);},
                    buf -> new SaveCodePacket(buf.readBlockPos(), buf.readUtf())
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SaveCodePacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player player = ctx.player();

            BlockEntity be = player.level().getBlockEntity(packet.pos);
            if (be instanceof RedstoneComputerEntity) {
                System.out.println("SAVED "+packet.code);
                ((RedstoneComputerEntity) be).setCode(packet.code);
            }
        });
    }
}
