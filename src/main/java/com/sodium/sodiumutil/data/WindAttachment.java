package com.sodium.sodiumutil.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

import static com.sodium.sodiumutil.SodiumUtilMod.MODID;

public class WindAttachment {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MODID);

    public static final Supplier<AttachmentType<WindAttachment>> WIND_ATTACHMENT =
            ATTACHMENT_TYPES.register("wind", () -> WindAttachment.TYPE);

    public static void register(IEventBus modEventBus) {
        ATTACHMENT_TYPES.register(modEventBus);
    }

    public float strength = 0.3f;
    public float direction = 0.0f;

    public static final Codec<WindAttachment> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("strength").forGetter(a -> a.strength),
                    Codec.FLOAT.fieldOf("direction").forGetter(a -> a.direction)
            ).apply(instance, WindAttachment::new)
    );

    public static final AttachmentType<WindAttachment> TYPE =
            AttachmentType.builder(WindAttachment::new)
                    .serialize(CODEC.fieldOf("wind").codec())
                    .build();

    public WindAttachment(float strength, float direction) {
        this.strength = strength;
        this.direction = direction;
    }

    public WindAttachment() {
        this(0.3f, 0.0f);
    }
}
