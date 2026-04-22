package com.example.examplemod.network;

import com.example.examplemod.network.packets.ExamplePacket;
import com.example.examplemod.network.packets.SaveCodePacket;
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
