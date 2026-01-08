package pl.kiszkiel.realpay.utils;

import net.milkbowl.vault.economy.EconomyResponse;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public class Responses {
    /**
     * Success without balances and message
     */
    public static EconomyResponse QUIET_SUCCESS = new EconomyResponse(
            0.0, 0.0, EconomyResponse.ResponseType.SUCCESS, null);

    /**
     * Failure without balances and message
     */
    public static EconomyResponse QUIET_FAILURE = new EconomyResponse(
            0.0, 0.0, EconomyResponse.ResponseType.FAILURE, null);

    /**
     * Failure without balances and with a message
     * @param message Message to insert
     * @return Failure response without balances and with your message inserted into
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NonNull EconomyResponse quietFailure(String message) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, message);
    }
}
