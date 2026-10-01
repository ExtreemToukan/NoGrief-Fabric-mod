package com.example.nongrief;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BucketItem;
import net.minecraft.item.FireChargeItem;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.ActionResult;
import org.lwjgl.glfw.GLFW;

/**
 * Client-side safety switch for combat.
 *
 * When enabled, NoGrief blocks accidental block breaking and the most common
 * block/place/fire actions while entity attacks remain untouched.
 */
public final class NoGriefClient implements ClientModInitializer {
    public static final String MOD_ID = "nongrief";

    private static boolean protectionEnabled = false;
    private static KeyBinding toggleKey;

    @Override
    public void onInitializeClient() {
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.nongrief.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F12,
                "category.nongrief"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.wasPressed()) {
                protectionEnabled = !protectionEnabled;

                if (client.player != null) {
                    client.player.sendMessage(
                            Text.translatable(protectionEnabled
                                    ? "message.nongrief.enabled"
                                    : "message.nongrief.disabled")
                                    .formatted(protectionEnabled
                                            ? Formatting.GREEN    // #55FF55 — салатовый
                                            : Formatting.RED),   // #FF5555 — светло-красный
                            true
                    );
                }
            }
        });

        // Left-clicking an entity is not affected. Only block attacks are cancelled.
        AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
            if (!world.isClient() || !protectionEnabled) {
                return ActionResult.PASS;
            }
            return ActionResult.FAIL;
        });

        // Prevent the most common accidental grief actions performed with right click.
        // Normal interaction with doors, chests, buttons, etc. remains available when
        // the player is not holding one of these place/destructive items.
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (!world.isClient() || !protectionEnabled) {
                return ActionResult.PASS;
            }

            Item item = player.getStackInHand(hand).getItem();
            if (item instanceof BlockItem
                    || item instanceof BucketItem
                    || item instanceof FlintAndSteelItem
                    || item instanceof FireChargeItem) {
                return ActionResult.FAIL;
            }

            return ActionResult.PASS;
        });
    }

    public static boolean isProtectionEnabled() {
        return protectionEnabled;
    }
}
