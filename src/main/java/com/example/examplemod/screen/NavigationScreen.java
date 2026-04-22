package com.example.examplemod.screen;

import com.example.examplemod.network.packets.ExamplePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;

public class NavigationScreen extends Screen {

    private EditBox field1;
    private EditBox field2;

    public NavigationScreen() {
        super(Component.literal("Setting Coordinates"));
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;

        field1 = new EditBox(this.font, centerX - 100, this.height / 2 - 30, 200, 20, Component.literal("Поле 1"));
        field2 = new EditBox(this.font, centerX - 100, this.height / 2, 200, 20, Component.literal("Поле 2"));

        field1.setMaxLength(50);
        field2.setMaxLength(50);

        this.addRenderableWidget(field1);
        this.addRenderableWidget(field2);

        this.addRenderableWidget(
                Button.builder(Component.literal("Set"), button -> {
                    try {
                        String text1 = field1.getValue();
                        String text2 = field2.getValue();
                        PacketDistributor.sendToServer(new ExamplePacket(new BlockPos(Integer.parseInt(text1), 0, Integer.parseInt(text2))));
                        onClose();
                    }catch (NumberFormatException e){

                    }
                        })
                        .bounds(centerX - 50, this.height / 2 + 40, 100, 20)
                        .build()
        );

        this.setInitialFocus(field1);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);

        super.render(guiGraphics, mouseX, mouseY, partialTick);

        guiGraphics.drawString(this.font, "Enter Coordinates:", this.width / 2 - 60, this.height / 2 - 50, 0xFFFFFF);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        return field1.charTyped(codePoint, modifiers) || field2.charTyped(codePoint, modifiers) || super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return field1.keyPressed(keyCode, scanCode, modifiers)
                || field2.keyPressed(keyCode, scanCode, modifiers)
                || super.keyPressed(keyCode, scanCode, modifiers);
    }
}