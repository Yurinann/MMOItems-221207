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
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * mmoitems
 * 28/11/2022
 *
 * @author Roch Blondiaux (Kiwix).
 */
public class AdventureUtils {

    private static final MiniMessage MINI_MESSAGE;

    static {
        MINI_MESSAGE = MiniMessage.builder()
                .editTags(builder -> builder.resolver(TagResolver.resolver("math", AdventureUtils::createMathTag)))
                .build();
    }

    private static Tag createMathTag(final ArgumentQueue args, final Context ctx) {
        final String expression = args.popOr("The <math> tag requires exactly one argument, the formula to apply.").value();
        final String result = MythicLib.plugin.getMMOConfig().decimals.format(new DoubleEvaluator().evaluate(expression));

        return Tag.inserting(Component.text(result));
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
}
