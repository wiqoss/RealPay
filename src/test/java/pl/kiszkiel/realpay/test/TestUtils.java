package pl.kiszkiel.realpay.test;

import org.bukkit.OfflinePlayer;
import org.jspecify.annotations.NonNull;
import org.mockbukkit.mockbukkit.entity.OfflinePlayerMock;

import java.util.Random;

public class TestUtils {

    private static final Random RANDOM = new Random();

    public static @NonNull OfflinePlayer getRandomOfflinePlayer() {
        return new OfflinePlayerMock(Integer.toString(RANDOM.nextInt(10_000, 10_000_000)));
    }
}
