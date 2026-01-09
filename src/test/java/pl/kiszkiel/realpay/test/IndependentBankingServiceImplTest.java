package pl.kiszkiel.realpay.test;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.kiszkiel.realpay.impl.IndependentBankingServiceImpl;
import pl.kiszkiel.realpay.obj.Bank;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class IndependentBankingServiceImplTest {

    private IndependentBankingServiceImpl service;

    @BeforeEach
    void setUp() {
        List<Bank> banks = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < r.nextInt(100, 1500); i++) {
            // Random balance
            double balance = r.nextDouble(100, 150_000);

            // Random owner
            OfflinePlayer owner = TestUtils.getRandomOfflinePlayer();

            // Generate members
            List<OfflinePlayer> members = new ArrayList<>();
            for (int j = 0; j < r.nextInt(15); j++) {
                members.add(TestUtils.getRandomOfflinePlayer());
            }

            banks.add(new Bank("bank%d".formatted(i), owner, balance, members));
        }

        System.out.printf("Generated %d banks%n", banks.size());
        this.service = new IndependentBankingServiceImpl(banks);
    }

    @Test
    void createBank() {
        // Random owner
        OfflinePlayer owner = TestUtils.getRandomOfflinePlayer();

        // Bank name
        String name = "bank%d".formatted(service.getBanks().size());

        var response = service.createBank(name, owner);

        // Make an assertion
        assertEquals(EconomyResponse.ResponseType.SUCCESS, response.type);
    }

    @Test
    void deleteBank() {
        assertNotNull(service.getBank("bank0"));
        var response = service.deleteBank("bank0");
        assertEquals(EconomyResponse.ResponseType.SUCCESS, response.type);
        assertNull(service.getBank("bank0"));
    }

    @Test
    void getBalance() {
        // Get response and check it
        var response = service.getBalance("bank0");
        assertEquals(EconomyResponse.ResponseType.SUCCESS, response.type, () -> response.errorMessage);

        // Get balance
        double balance = response.balance;

        // Assert it
        assertEquals(
                /*
                bank0 is always exists
                take it balance and assert it with it balance got by another way
                 */
                service.getBank("bank0").getBalance(),

                // Compare it with balance got more official way
                balance
        );
    }

    @Test
    void withdraw() {
        double checkSum = 60.0;
        double balance = service.getBalance("bank0").balance;
        service.withdraw("bank0", checkSum);
        assertEquals(balance - checkSum, service.getBalance("bank0").balance);
    }

    @Test
    void deposit() {
        double checkSum = 60.0;
        double balance = service.getBalance("bank0").balance;
        service.deposit("bank0", checkSum);
        assertEquals(balance + checkSum, service.getBalance("bank0").balance);
    }

    @Test
    void getBanks() {
        List<String> banks = service.getBanks();
        assertTrue(banks.size() > 100);
        assertTrue(banks.size() < 1500);
    }
}