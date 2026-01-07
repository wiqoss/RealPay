package pl.kiszkiel.realpay.service;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

public interface IPlayerBankingService {
    public boolean hasAccount(OfflinePlayer offlinePlayer);

    public double getBalance(OfflinePlayer player);

    default boolean has(OfflinePlayer player, double amount) {
        return getBalance(player) >= amount;
    }

    public EconomyResponse withdraw(OfflinePlayer player, double amount);

    public EconomyResponse deposit(OfflinePlayer player, double amount);

    public boolean createPlayerAccount(OfflinePlayer player);
}
