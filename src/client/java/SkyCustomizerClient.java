
package dev.skycustomizer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class SkyCustomizerClient implements ClientModInitializer {
    private static final Path CONFIG = FabricLoader.getInstance().getConfigDir().resolve("skycustomizer.properties");

    private static final String[] MODES = {
        "Vanilla", "Sky Blue", "Rose", "Sunset", "Purple",
        "Emerald", "Midnight", "Space", "Galaxy Sky", "Custom RGB"
    };

    private static final int[] COLORS = {
        0, 0x62BFFF, 0xFF82CC, 0xFF8950, 0x9656D7,
        0x30CDA8, 0x0B173B, 0x100727, 0x00251F, 0x70B3FF
    };

    private static int mode = 0;
    private static int customColor = 0x70B3FF;
    private static boolean brightStars = true;
    private static KeyBinding openMenu;

    @Override
    public void onInitializeClient() {
        load();
        openMenu = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.skycustomizer.open",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            "category.skycustomizer"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenu.wasPressed()) {
                if (client.currentScreen instanceof SkyCustomizerScreen) {
                    client.setScreen(null);
                } else {
                    client.setScreen(new SkyCustomizerScreen());
                }
            }
        });
    }

    public static boolean isGalaxySky() {
        return mode == 8;
    }

    public static int getSkyColor() {
        if (mode == 0 || isGalaxySky()) {
            return -1;
        }
        return mode == MODES.length - 1 ? customColor : COLORS[mode];
    }

    public static float starBrightness(float original) {
        if (brightStars && (mode == 6 || mode == 7)) {
            return Math.max(original, mode == 7 ? 0.9f : 0.55f);
        }
        return original;
    }

    public static String modeName() { return MODES[mode]; }
    public static int color() { return customColor; }
    public static boolean stars() { return brightStars; }

    public static void cycleMode(int step) {
        mode = Math.floorMod(mode + step, MODES.length);
        save();
    }

    public static void adjustColor(int component, int step) {
        int shift = (2 - component) * 8;
        int current = (customColor >> shift) & 0xFF;
        int next = Math.max(0, Math.min(255, current + step));
        customColor = (customColor & ~(255 << shift)) | (next << shift);
        mode = MODES.length - 1;
        save();
    }

    public static void toggleStars() {
        brightStars = !brightStars;
        save();
    }

    private static void load() {
        if (!Files.isRegularFile(CONFIG)) return;

        Properties p = new Properties();
        try (InputStream in = Files.newInputStream(CONFIG)) {
            p.load(in);
            mode = Math.max(0, Math.min(MODES.length - 1,
                Integer.parseInt(p.getProperty("mode", "0"))));
            customColor = Integer.parseInt(
                p.getProperty("customColor", "70B3FF"), 16) & 0xFFFFFF;
            brightStars = Boolean.parseBoolean(
                p.getProperty("brightStars", "true"));
        } catch (Exception e) {
            System.err.println("[Sky Customizer] Could not read config: " + e.getMessage());
        }
    }

    private static void save() {
        Properties p = new Properties();
        p.setProperty("mode", Integer.toString(mode));
        p.setProperty("customColor", String.format("%06X", customColor));
        p.setProperty("brightStars", Boolean.toString(brightStars));

        try {
            Files.createDirectories(CONFIG.getParent());
            try (OutputStream out = Files.newOutputStream(CONFIG)) {
                p.store(out, "Sky Customizer settings");
            }
        } catch (Exception e) {
            System.err.println("[Sky Customizer] Could not save config: " + e.getMessage());
        }
    }
}
