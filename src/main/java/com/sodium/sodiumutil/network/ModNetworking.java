package com.sodium.sodiumutil.network;

import com.sodium.sodiumutil.network.packets.ExamplePacket;
import com.sodium.sodiumutil.network.packets.SaveCodePacket;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class ModNetworking {
    public static final String PROTOCOL_VERSION = "1";

    public static void register(final RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);

        registrar.playToServer(
                ExamplePacket.TYPE,
                ExamplePacket.STREAM_CODEC,
                ExamplePacket::handle
        );
        registrar.playToServer(
                SaveCodePacket.TYPE,
                SaveCodePacket.STREAM_CODEC,
                SaveCodePacket::handle
        );
    }
}
