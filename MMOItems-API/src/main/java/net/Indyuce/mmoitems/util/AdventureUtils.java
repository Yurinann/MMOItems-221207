package net.Indyuce.mmoitems.util;

import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.adventure.text.Component;
import io.lumine.mythic.lib.adventure.text.minimessage.Context;
import io.lumine.mythic.lib.adventure.text.minimessage.MiniMessage;
import io.lumine.mythic.lib.adventure.text.minimessage.tag.Tag;
import io.lumine.mythic.lib.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import io.lumine.mythic.lib.adventure.text.minimessage.tag.resolver.TagResolver;
import io.lumine.mythic.lib.adventure.text.serializer.legacy.LegacyComponentSerializer;
import io.lumine.mythic.lib.parser.client.eval.DoubleEvaluator;
import net.Indyuce.mmoitems.MMOItems;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

/**
 * mmoitems
 * 28/11/2022
 *
 * @author Roch Blondiaux (Kiwix).
 */
public class AdventureUtils {

    private static final MiniMessage MINI_MESSAGE;

    static {
        Map<Character, String> LEGACY_COLORS = new HashMap<>(Map.of('0', "black", '1', "dark_blue", '2', "dark_green", '3', "dark_aqua", '4', "dark_red", '5', "dark_purple", '6', "gold", '7', "gray", '8', "dark_gray", '9', "blue"));
        LEGACY_COLORS.putAll(Map.of('a', "green", 'b', "aqua", 'c', "red", 'd', "light_purple", 'e', "yellow", 'f', "white"));
        LEGACY_COLORS.putAll(Map.of('k', "obfuscated", 'l', "bold", 'm', "strikethrough", 'n', "underline", 'o', "italic", 'r', "reset"));
        final UnaryOperator<String> LEGACY_TRANSLATOR = s -> {
            for (Map.Entry<Character, String> entry : LEGACY_COLORS.entrySet())
                s = s.replace("&%c".formatted(entry.getKey()), "<%s>".formatted(entry.getValue()))
                        .replace("%c%c".formatted(ChatColor.COLOR_CHAR, entry.getKey()), "<%s>".formatted(entry.getValue()));
            return s;
        };

        final BiFunction<ArgumentQueue, Context, Tag> MATH_TAG = (argumentQueue, context) -> {
            final String expression = argumentQueue.popOr("The <math> tag requires exactly one argument, the formula to apply.").value();
            final String result = MythicLib.plugin.getMMOConfig().decimals.format(new DoubleEvaluator().evaluate(expression));

            return Tag.inserting(Component.text(result));
        };

        MINI_MESSAGE = MiniMessage.builder()
                .editTags(builder -> builder.resolver(TagResolver.resolver("math", MATH_TAG)))
                .preProcessor(LEGACY_TRANSLATOR)
                .build();
    }


    /**
     * Convert a component to a legacy string
     * translated by the {@link org.bukkit.ChatColor#translateAlternateColorCodes(char, String)} function.
     *
     * @param component The component to convert.
     * @return The converted component.
     */
    public static @NotNull String legacyComponent(@NotNull Component component) {
        return LegacyComponentSerializer.legacyAmpersand().serialize(component);
    }


    /**
     * Convert plain text to a component using the {@link MiniMessage} library.
     *
     * @param string The string to convert.
     * @return The converted component.
     */
    public static @NotNull Component miniMessage(@NotNull String string) {
        return MINI_MESSAGE.deserialize(string);
    }

    /**
     * Convert plain text to a component using the {@link MiniMessage} library.
     * The component is then resolved using the given tag resolver.
     *
     * @param string    The string to convert.
     * @param resolvers The resolvers to use.
     * @return The converted component.
     */
    public static @NotNull Component miniMessage(@NotNull String string, TagResolver... resolvers) {
        return MINI_MESSAGE.deserialize(string, resolvers);
    }

    public static @NotNull Collection<Component> miniMessage(@NotNull Collection<String> strings) {
        return strings.stream().map(AdventureUtils::miniMessage).toList();
    }

    public static @NotNull Collection<Component> miniMessage(@NotNull Collection<String> strings, TagResolver... resolvers) {
        return strings.stream().map(string -> miniMessage(string, resolvers)).toList();
    }

    public static void message(@NotNull Player player, @NotNull Component component) {
        MMOItems.ADVENTURE.player(player).sendMessage(component);
    }

    public static @NotNull ItemMeta lore(@NotNull ItemMeta meta, @NotNull Collection<Component> lore) {
        meta.setLore(lore.stream().map(AdventureUtils::legacyComponent).toList());
        return meta;
    }

    public static @NotNull ItemMeta displayName(@NotNull ItemMeta meta, @NotNull Component displayName) {
        meta.setDisplayName(legacyComponent(displayName));
        return meta;
    }
}
