package fr.xyness.SimpleClaimSystem.API;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.bukkit.Chunk;
import org.bukkit.World;

import fr.xyness.SimpleClaimSystem.Enums.WorldMode;
import fr.xyness.SimpleClaimSystem.Types.Claim;
import fr.xyness.SimpleClaimSystem.Types.PlayerData;

/**
 * SimpleClaimSystem public API.
 */
public interface SCS_API {

	/**
	 * Gets a claim, can be empty.
	 *
	 * @param chunk The chunk.
	 * @return An Optional&lt;Claim&gt;.
	 */
    Optional<Claim> getClaim(Chunk chunk);

	/**
	 * Gets a claim async, can be empty.
	 *
	 * @param chunk The chunk.
	 * @return A CompletableFuture wrapping an Optional&lt;Claim&gt;.
	 */
    CompletableFuture<Optional<Claim>> getClaimAsync(Chunk chunk);

    /**
     * Gets a player, can be empty.
     *
     * @param playerId The player's UUID.
     * @return An Optional&lt;PlayerData&gt;.
     */
    Optional<PlayerData> getPlayer(UUID playerId);

    /**
     * Gets a player async, can be empty.
     *
     * @param playerId The player's UUID.
     * @return A CompletableFuture wrapping an Optional&lt;PlayerData&gt;.
     */
    CompletableFuture<Optional<PlayerData>> getPlayerAsync(UUID playerId);

    /**
     * Gets the mode of a world.
     *
     * @param world The world.
     * @return The world mode.
     */
    WorldMode getWorldMode(World world);

    /**
     * Gets the permissions for the mode "Protected".
     *
     * @return The settings.
     */
    Map<String,Boolean> getSettingsForProtectedMode();

    /**
     * Gets the permissions for the mode "Survival Requiring Claims".
     *
     * @return The settings.
     */
    Map<String,Boolean> getSettingsForSurvivalRequiringClaimsMode();

}
