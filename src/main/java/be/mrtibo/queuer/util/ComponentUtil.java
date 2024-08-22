package be.mrtibo.queuer.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public class ComponentUtil {

    public static Component fromString(String miniMessageFormatted, TagResolver... resolver) {
        return MiniMessage.miniMessage().deserialize(miniMessageFormatted, resolver);
    }

}
