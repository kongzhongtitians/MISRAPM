package gd.rf.kongzhongtitian.MISRAPM.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import org.lwjgl.glfw.GLFW;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class MISRAPMClient implements ClientModInitializer {

    private KeyBinding openSearchKey;

    @Override
    public void onInitializeClient() {
        openSearchKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.misrapm.open_search",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_I,
                "category.misrapm"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.currentScreen != null) {
                return;
            }

            if (openSearchKey.wasPressed()) {
                ItemStack stack = client.player.getMainHandStack();
                if (stack.isEmpty()) {
                    return;
                }

                String itemName = stack.getName().getString();
                openSearchPage(itemName);
            }
        });
    }

    private void openSearchPage(String itemName) {
        try {
            String encoded = URLEncoder.encode(itemName, StandardCharsets.UTF_8)
                    .replace("+", "%20");
            String url = "https://search.mcmod.cn/s?key=" + encoded;

            Util.getOperatingSystem().open(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}