package pl.kiszkiel.realpay.paper;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationStore;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.kiszkiel.realpay.paper.translations.ColorfulTranslator;
import pl.kiszkiel.realpay.paper.translations.TranslationsLoader;
import pl.kiszkiel.realpay.utils.Database;

import java.io.File;
import java.text.MessageFormat;

public final class RealPay extends JavaPlugin {

    public static final Logger LOGGER = LoggerFactory.getLogger(RealPay.class);
    private Database database;
    private String currencyNamePlural, currencyNameSingular;

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) getDataFolder().mkdir();
        saveResource("config.yml", false);
        if (!new File(getDataFolder(), "config.yml").exists()) saveDefaultConfig();
        // Englishes
        saveResource("lang_en_US.properties", false);
        //saveResource("lang_en_GB.properties", false);
        //saveResource("lang_en_AU.properties", false);

        // Slavics
        //saveResource("lang_pl_PL.properties", false);
        //saveResource("lang_ua_UA.properties", false);
        saveResource("lang_ru_RU.properties", false);
        //saveResource("lang_hr_HR.properties", false);

        // Baltics
        //saveResource("lang_lt_LT.properties", false);
        //saveResource("lang_lv_LV.properties", false);
        //saveResource("lang_ee_EE.properties", false);

        // Germans
        //saveResource("lang_de_DE.properties", false);
        //saveResource("lang_de_AT.properties", false);
        //saveResource("lang_de_CH.properties", false);

        loadTranslations();

        // Load database and register service
        //loadDatabase();

        try {
            //Bukkit.getServicesManager().register(Economy.class, new VaultEconomyImpl(), this, ServicePriority.Normal);
            LOGGER.info("Successfully registered Vault implementation");
        } catch (Exception ex) {
            throw new RuntimeException("Cannot Vault implementation:", ex);
        }
    }

    @Override
    public void onDisable() {
        this.database.close();
    }

    private void loadDatabase() {
        this.database = new Database(
                getConfig().getString("storage.mysql.address"),
                getConfig().getInt("storage.mysql.port"),
                getConfig().getString("storage.mysql.name"),
                getConfig().getString("storage.mysql.user"),
                getConfig().getString("storage.mysql.password")
        );
    }

    private void loadTranslations() {
        TranslationStore.StringBased<MessageFormat> store =
                TranslationStore.messageFormat(Key.key("realpay", "colorful_translator"));

        LOGGER.info("Storing translations...");
        TranslationsLoader translationsLoader = new TranslationsLoader(store, getDataFolder());
        translationsLoader.loadAll();
        LOGGER.info("Translations stored");

        GlobalTranslator.translator().addSource(new ColorfulTranslator(store));
    }

    public String currencyNamePlural() {
        if (currencyNamePlural == null) currencyNamePlural = getConfig().getString("currency_name_plural");
        return currencyNamePlural;
    }

    public String currencyNameSingular() {
        if (currencyNameSingular == null) currencyNameSingular = getConfig().getString("currency_name_singular");
        return currencyNameSingular;
    }
}
