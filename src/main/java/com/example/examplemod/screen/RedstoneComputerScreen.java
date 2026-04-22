package com.example.examplemod.screen;

import com.example.examplemod.network.packets.SaveCodePacket;
import com.example.examplemod.screen.customWidgets.MultiLineTextBox;
import com.example.examplemod.screen.menu.RedstoneComputerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

public class RedstoneComputerScreen extends AbstractContainerScreen<RedstoneComputerMenu> {
    int _width = 300;
    int _height = 200;
    private MultiLineTextBox field1;

    public RedstoneComputerScreen(RedstoneComputerMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    protected void init() {
        int centerX = this.width/2;
        int centerY = this.height/2;
        field1 = new MultiLineTextBox(this.font, centerX - _width/2 + 20, centerY - _height / 2 + 20, _width-40, _height-50, Component.literal("Code"));
        field1.setValue(this.menu.blockEntity.getCode());
        System.out.println("SET "+field1.getValue());
        this.addRenderableWidget(field1);
        this.addRenderableWidget(
                Button.builder(Component.literal("Save"), button -> {
                            String text1 = field1.getValue();
                            this.menu.blockEntity.setCode(text1);
                            PacketDistributor.sendToServer(new SaveCodePacket(this.menu.blockEntity.getBlockPos(), text1));
                            onClose();
                        })
                        .bounds(centerX + _width/2 - 30, centerY+_height/2-30, 20, 20)
                        .build()
        );
        this.setInitialFocus(field1);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        int centerX = this.width/2;
        int centerY = this.height/2;
        guiGraphics.fill(centerX-_width/2, centerY-_height/2, centerX+_width/2, centerY+_height/2, 0xFF333333);
        for (Renderable i: this.renderables){
            if (i instanceof AbstractWidget){
                i.render(guiGraphics, mouseX, mouseY, partialTick);
            }
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        guiGraphics.fill(0, 0, this.width, this.height, 0x00000000);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        return field1.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return field1.keyPressed(keyCode, scanCode, modifiers);
    }
}