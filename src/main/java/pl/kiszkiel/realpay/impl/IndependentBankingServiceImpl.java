package pl.kiszkiel.realpay.impl;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import pl.kiszkiel.realpay.obj.Bank;
import pl.kiszkiel.realpay.service.IIndependentBankingService;
import pl.kiszkiel.realpay.utils.Responses;

import java.util.ArrayList;
import java.util.List;

public class IndependentBankingServiceImpl implements IIndependentBankingService {

    private List<Bank> banks;

    public IndependentBankingServiceImpl(@NotNull List<Bank> banks) {
        this.banks = banks;
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        // Name is already taken
        if (bankExists(name)) return Responses.quietFailure("Name is already taken");

        // Just create it
        this.banks.add(new Bank(name, player, 0.0, null));
        return Responses.QUIET_SUCCESS;
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        // Is bank exists
        if (!bankExists(name)) return Responses.quietFailure("Bank doesn't exists");

        // Remove the bank
        banks.remove(getBank(name));
        return Responses.QUIET_SUCCESS;
    }

    @Override
    public EconomyResponse getBalance(String name) {
        // Is bank exists
        if (!bankExists(name)) return Responses.quietFailure("Bank doesn't exists");

        // Return
        return new EconomyResponse(0.0,
                getBank(name).getBalance(),
                EconomyResponse.ResponseType.SUCCESS,
                null);
    }

    @Override
    public EconomyResponse withdraw(String name, double amount) {
        // Is bank exists
        if (!bankExists(name)) return Responses.quietFailure("Bank doesn't exists");

        Bank bank = getBank(name);

        // Check for money enough
        if (bank.getBalance() < amount)
            return new EconomyResponse(amount,
                    bank.getBalance(),
                    EconomyResponse.ResponseType.FAILURE,
                    "Not enough money");

        // Update balance
        bank.setBalance(bank.getBalance() - amount);

        // And return
        return new EconomyResponse(amount, bank.getBalance(),
                EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse deposit(String name, double amount) {
        // Check deposit amount
        if (amount < 0) return Responses.quietFailure("Cannot deposit negative amount");

        // Check bank existence
        if (!bankExists(name)) return Responses.quietFailure("Bank doesn't exist");

        // Update it
        Bank bank = getBank(name);
        bank.setBalance(bank.getBalance() + amount);

        // Return response
        return new EconomyResponse(amount, bank.getBalance(), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        // Check bank existence
        if (!bankExists(name)) return Responses.quietFailure("Bank doesn't exist");

        // Check owner
        Bank bank = getBank(name);
        return new EconomyResponse(0.0,
                bank.getBalance(),
                bank.getOwner().equals(player)
                        ? EconomyResponse.ResponseType.SUCCESS
                        : EconomyResponse.ResponseType.FAILURE,
                null);
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        // Check bank existence
        if (!bankExists(name)) return Responses.quietFailure("Bank doesn't exist");

        // Check owner
        Bank bank = getBank(name);
        return new EconomyResponse(0.0,
                bank.getBalance(),
                bank.getMembers().contains(player)
                        ? EconomyResponse.ResponseType.SUCCESS
                        : EconomyResponse.ResponseType.FAILURE,
                null);
    }

    @Override
    public List<String> getBanks() {
        List<String> result = new ArrayList<>();
        this.banks.forEach(bank -> result.add(bank.getName()));
        return result;
    }

    @Override
    public @Nullable Bank getBank(String name) {
        return banks.stream().filter(bank -> bank.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }
}
