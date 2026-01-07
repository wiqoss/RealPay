package pl.kiszkiel.realpay.service;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

import java.util.List;

public interface IIndependentBankingService {
    public EconomyResponse createBank(String name, OfflinePlayer player);

    public EconomyResponse deleteBank(String name);

    public EconomyResponse getBalance(String name);

    default EconomyResponse has(String name, double amount) {
        return new EconomyResponse(
                // Amount to check
                amount,

                // Current bank balance
                getBalance(name).balance,

                // Has or not
                getBalance(name).balance >= amount
                        // Has
                        ? EconomyResponse.ResponseType.SUCCESS
                        // Hasn't
                        : EconomyResponse.ResponseType.FAILURE,

                // Comment is always null
                null);
    }

    public EconomyResponse withdraw(String name, double amount);

    public EconomyResponse deposit(String name, double amount);

    public EconomyResponse isBankOwner(String name, OfflinePlayer player);

    public EconomyResponse isBankMember(String name, OfflinePlayer player);

    public List<String> getBanks();
}
