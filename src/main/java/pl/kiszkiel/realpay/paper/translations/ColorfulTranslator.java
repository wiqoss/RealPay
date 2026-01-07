package pl.kiszkiel.realpay.paper.translations;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.minimessage.translation.MiniMessageTranslator;
import net.kyori.adventure.translation.Translator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.Locale;

public class ColorfulTranslator extends MiniMessageTranslator {
    private final Translator delegate;

    public ColorfulTranslator(Translator delegate) {
        this.delegate = delegate;
    }

    @Override
    protected @Nullable String getMiniMessageString(@NotNull String key, @NotNull Locale locale) {
        // Default translation
        final MessageFormat format = delegate.translate(key, locale);
        if (format == null) {
            return null;
        }

        return format.toPattern();
    }

    @Override
    public @NotNull Key name() {
        return Key.key("realpay", "colorful_translator");
    }
}
