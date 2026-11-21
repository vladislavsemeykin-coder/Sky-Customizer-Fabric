package dev.skycustomizer;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public final class SkyCustomizerScreen extends Screen {
    public SkyCustomizerScreen() { super(Text.literal("Sky Customizer")); }

    @Override
    protected void init() {
        int x = width / 2 - 120;
        int y = Math.max(30, height / 2 - 102);
        addDrawableChild(ButtonWidget.builder(Text.literal("< Previous"), b -> {
            SkyCustomizerClient.cycleMode(-1);
            refresh();
        }).dimensions(x, y + 25, 114, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Next >"), b -> {
            SkyCustomizerClient.cycleMode(1);
            refresh();
        }).dimensions(x + 126, y + 25, 114, 20).build());

        int[] minusX = {x, x + 82, x + 164};
        for (int component = 0; component < 3; component++) {
            final int which = component;
            addDrawableChild(ButtonWidget.builder(Text.literal("-"), b -> {
                SkyCustomizerClient.adjustColor(which, -16); refresh();
            }).dimensions(minusX[component], y + 93, 34, 20).build());
            addDrawableChild(ButtonWidget.builder(Text.literal("+"), b -> {
                SkyCustomizerClient.adjustColor(which, 16); refresh();
            }).dimensions(minusX[component] + 43, y + 93, 34, 20).build());
        }

        addDrawableChild(ButtonWidget.builder(Text.literal("Stars: " + (SkyCustomizerClient.stars() ? "ON" : "OFF")), b -> {
            SkyCustomizerClient.toggleStars(); refresh();
        }).dimensions(x, y + 125, 240, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Done"), b -> close())
            .dimensions(x, y + 156, 240, 20).build());
    }

    private void refresh() {
        if (client != null) client.setScreen(new SkyCustomizerScreen());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        int x = width / 2 - 120;
        int y = Math.max(30, height / 2 - 102);
        context.drawCenteredTextWithShadow(textRenderer, "SKY CUSTOMIZER", width / 2, y + 2, 0xFFFFFF);
        context.drawCenteredTextWithShadow(textRenderer, "Preset: " + SkyCustomizerClient.modeName(), width / 2, y + 52, 0xFFFFFF);
        context.drawCenteredTextWithShadow(textRenderer, "Custom RGB color (adjust with +/-)", width / 2, y + 69, 0xCCCCCC);
        int c = SkyCustomizerClient.color();
        String[] labels = {"R: " + ((c >> 16) & 255), "G: " + ((c >> 8) & 255), "B: " + (c & 255)};
        for (int i = 0; i < 3; i++) {
            context.drawCenteredTextWithShadow(textRenderer, labels[i], x + 38 + i * 82, y + 82, 0xFFFFFF);
        }
        context.fill(x, y + 60, x + 240, y + 65, 0xFF000000 | (SkyCustomizerClient.getSkyColor() == -1 ? 0x7AAEFF : SkyCustomizerClient.getSkyColor()));
        context.drawCenteredTextWithShadow(textRenderer, "Client-only | Overworld sky", width / 2, y + 186, 0x999999);
    }

    @Override
    public void close() { if (client != null) client.setScreen(null); }
}
