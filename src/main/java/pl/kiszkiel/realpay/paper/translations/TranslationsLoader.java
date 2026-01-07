package pl.kiszkiel.realpay.paper.translations;

import net.kyori.adventure.translation.TranslationStore;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import pl.kiszkiel.realpay.paper.RealPay;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class TranslationsLoader {

    private final TranslationStore.StringBased<MessageFormat> store;
    private final File dataFolder;

    public TranslationsLoader(TranslationStore.StringBased<MessageFormat> store, File dataFolder) {
        this.store = store;
        this.dataFolder = dataFolder;
    }

    public void loadAll() {
        for (File translationFile : Objects.requireNonNull(dataFolder.listFiles(
                f ->
                        f.getName().startsWith("lang_")
                                && f.getName().endsWith(".properties")))
        ) {
            loadFile(translationFile);
        }
    }

    public void loadFile(@NonNull File translationsFile) {
        try {
            Locale locale = getLocale(translationsFile.getName());
            RealPay.LOGGER.info("Loading translation file {} as {} locale", translationsFile.getName(), locale.getDisplayName());

            // Load properties
            Properties properties = new Properties();
            try (FileInputStream fis = new FileInputStream(translationsFile);
                 BufferedInputStream bis = new BufferedInputStream(fis);
                 InputStreamReader isr = new InputStreamReader(bis, StandardCharsets.UTF_8)
            ) {
                properties.load(isr);
            }

            // Read properties
            for (Map.Entry<Object, Object> translationEntry : properties.entrySet()) {
                String key = (String) translationEntry.getKey();
                String value = (String) translationEntry.getValue();

                // Register a translation
                store.register(
                        key,
                        locale,
                        new MessageFormat(value, locale)
                );
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot load translation file " + translationsFile.getName(), e);
        }
    }

    /**
     * Extracts locale from language translation file with format lang_ll_CC.properties
     * @param translationFileName Translation file name
     * @return Locale extracted from file
     */
    @Contract(pure = true)
    private Locale getLocale(@NonNull String translationFileName) {
        String[] split = translationFileName.split("_");
        String[] locale = new String[] {
                split[split.length - 2],
                split[split.length - 1].split("\\.")[0]
        };

        return Locale.of(locale[0], locale[1]);
    }
}
