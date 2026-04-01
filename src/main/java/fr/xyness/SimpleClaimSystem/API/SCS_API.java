package fr.xyness.SimpleClaimSystem.API;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import fr.xyness.SimpleClaimSystem.Enums.WorldMode;
import fr.xyness.SimpleClaimSystem.Types.ChunkKey;
import fr.xyness.SimpleClaimSystem.Types.Claim;
import fr.xyness.SimpleClaimSystem.Types.PlayerData;

/**
 * SimpleClaimSystem public API.
 *
 * <p>Access this API via {@link SCS_API_Provider#get()}.</p>
 */
public interface SCS_API {


    // **********************
    // *  Claim - Queries  *
    // **********************


    /**
     * Gets a claim at the given chunk (synchronous).
     *
     * @param chunk The chunk.
     * @return An Optional containing the Claim, or empty if unclaimed.
     */
    Optional<Claim> getClaim(Chunk chunk);

    /**
     * Gets a claim at the given chunk (asynchronous).
     *
     * @param chunk The chunk.
     * @return A CompletableFuture wrapping an Optional Claim.
     */
    CompletableFuture<Optional<Claim>> getClaimAsync(Chunk chunk);

    /**
     * Gets a claim at the given chunk key (synchronous).
     *
     * @param key The chunk key.
     * @return An Optional containing the Claim, or empty if unclaimed.
     */
    Optional<Claim> getClaim(ChunkKey key);

    /**
     * Gets a claim at the given chunk key (asynchronous).
     *
     * @param key The chunk key.
     * @return A CompletableFuture wrapping an Optional Claim.
     */
    CompletableFuture<Optional<Claim>> getClaimAsync(ChunkKey key);

    /**
     * Gets multiple claims at once (synchronous).
     *
     * @param keys The set of chunk keys to look up.
     * @return A map of chunk key to Optional Claim.
     */
    Map<ChunkKey, Optional<Claim>> getClaims(Set<ChunkKey> keys);

    /**
     * Gets a claim by its owner's UUID and claim name (synchronous).
     *
     * @param ownerUuid The owner's UUID.
     * @param claimName The claim name (case-insensitive).
     * @return An Optional containing the Claim, or empty if not found.
     */
    Optional<Claim> getClaimByOwnerAndName(UUID ownerUuid, String claimName);

    /**
     * Gets all claims owned by a player (synchronous, from cache).
     *
     * @param ownerUuid The owner's UUID.
     * @return A list of claims. May be empty.
     */
    List<Claim> getClaimsByOwner(UUID ownerUuid);

    /**
     * Gets all claims owned by a player from database (asynchronous).
     * Includes full data: members, permissions, flags, banned.
     *
     * @param ownerUuid The owner's UUID.
     * @return A CompletableFuture wrapping a list of claims.
     */
    CompletableFuture<List<Claim>> getClaimsByOwnerAsync(UUID ownerUuid);

    /**
     * Gets all claim names owned by a player (synchronous).
     *
     * @param ownerUuid The owner's UUID.
     * @return A list of claim names. May be empty.
     */
    List<String> getClaimNamesByOwner(UUID ownerUuid);

    /**
     * Gets the members of a claim by owner and claim name (synchronous).
     *
     * @param ownerUuid The owner's UUID.
     * @param claimName The claim name.
     * @return A map of member UUID to role name.
     */
    Map<UUID, String> getClaimMembers(UUID ownerUuid, String claimName);

    /**
     * Gets the banned players of a claim by owner and claim name (synchronous).
     *
     * @param ownerUuid The owner's UUID.
     * @param claimName The claim name.
     * @return A map of banned UUID to unban date.
     */
    Map<UUID, LocalDateTime> getClaimBanned(UUID ownerUuid, String claimName);


    // **************************
    // *  Claim - Modification *
    // **************************


    /**
     * Adds a member to a claim (asynchronous).
     * Persists to database and updates cache.
     *
     * @param claim The claim.
     * @param memberUuid The UUID of the member to add.
     * @param role The role to assign (MEMBER, MODERATOR).
     * @return A CompletableFuture that completes when the operation is done.
     */
    CompletableFuture<Void> addMember(Claim claim, UUID memberUuid, String role);

    /**
     * Adds a member to all claims owned by a player (asynchronous).
     *
     * @param ownerUuid The owner's UUID.
     * @param memberUuid The UUID of the member to add.
     * @param role The role to assign.
     * @return A CompletableFuture that completes when the operation is done.
     */
    CompletableFuture<Void> addMemberToAllClaims(UUID ownerUuid, UUID memberUuid, String role);

    /**
     * Removes a member from a claim (asynchronous).
     * Persists to database and updates cache.
     *
     * @param claim The claim.
     * @param memberUuid The UUID of the member to remove.
     * @return A CompletableFuture that completes when the operation is done.
     */
    CompletableFuture<Void> removeMember(Claim claim, UUID memberUuid);

    /**
     * Removes a member from all claims owned by a player (asynchronous).
     *
     * @param ownerUuid The owner's UUID.
     * @param memberUuid The UUID of the member to remove.
     * @return A CompletableFuture that completes when the operation is done.
     */
    CompletableFuture<Void> removeMemberFromAllClaims(UUID ownerUuid, UUID memberUuid);

    /**
     * Changes the role of a member in a claim (asynchronous).
     *
     * @param claim The claim.
     * @param memberUuid The UUID of the member.
     * @param newRole The new role.
     * @return A CompletableFuture that completes when the operation is done.
     */
    CompletableFuture<Void> setMemberRole(Claim claim, UUID memberUuid, String newRole);

    /**
     * Bans a player from a claim (asynchronous).
     *
     * @param claim The claim.
     * @param playerUuid The UUID of the player to ban.
     * @param expiration The expiration date of the ban.
     * @return A CompletableFuture that completes when the operation is done.
     */
    CompletableFuture<Void> banPlayer(Claim claim, UUID playerUuid, LocalDateTime expiration);

    /**
     * Unbans a player from a claim (asynchronous).
     *
     * @param claim The claim.
     * @param playerUuid The UUID of the player to unban.
     * @return A CompletableFuture that completes when the operation is done.
     */
    CompletableFuture<Void> unbanPlayer(Claim claim, UUID playerUuid);

    /**
     * Sets a permission value for a role in a claim (asynchronous).
     *
     * @param claim The claim.
     * @param role The role.
     * @param permission The permission key.
     * @param value The permission value.
     * @return A CompletableFuture that completes when the operation is done.
     */
    CompletableFuture<Void> setPermission(Claim claim, String role, String permission, boolean value);

    /**
     * Sets a flag value for a claim (asynchronous).
     *
     * @param claim The claim.
     * @param flag The flag key.
     * @param value The flag value.
     * @return A CompletableFuture that completes when the operation is done.
     */
    CompletableFuture<Void> setFlag(Claim claim, String flag, boolean value);


    // *********************
    // *  Claim - Checks  *
    // *********************


    /**
     * Checks whether a chunk is claimed.
     *
     * @param key The chunk key.
     * @return True if the chunk is part of a claim.
     */
    boolean isClaimed(ChunkKey key);

    /**
     * Checks whether a chunk is claimed.
     *
     * @param chunk The chunk.
     * @return True if the chunk is part of a claim.
     */
    boolean isClaimed(Chunk chunk);

    /**
     * Checks whether a location is inside a claim.
     *
     * @param location The location.
     * @return True if the location is inside a claim.
     */
    boolean isClaimed(Location location);

    /**
     * Checks whether a player has a specific permission in a claim at the given chunk.
     *
     * @param chunk The chunk.
     * @param playerId The player's UUID.
     * @param permission The permission key (e.g. "build", "interact_chest").
     * @return True if permitted, false if denied or chunk is unclaimed.
     */
    boolean hasPermission(Chunk chunk, UUID playerId, String permission);

    /**
     * Checks whether a player is banned from a claim at the given chunk.
     *
     * @param chunk The chunk.
     * @param playerId The player's UUID.
     * @return True if the player is currently banned.
     */
    boolean isBanned(Chunk chunk, UUID playerId);

    /**
     * Checks whether a player is a member of the claim at the given chunk.
     *
     * @param chunk The chunk.
     * @param playerId The player's UUID.
     * @return True if the player is a member (any role except VISITOR).
     */
    boolean isMember(Chunk chunk, UUID playerId);

    /**
     * Gets the role of a player in the claim at the given chunk.
     *
     * @param chunk The chunk.
     * @param playerId The player's UUID.
     * @return The player's role, or VISITOR if not a member or unclaimed.
     */
    String getRole(Chunk chunk, UUID playerId);

    /**
     * Gets a flag value for the claim at the given chunk.
     *
     * @param chunk The chunk.
     * @param flag The flag key (e.g. "creeper_explosions", "pvp").
     * @return The flag value, or null if unclaimed or flag not set.
     */
    Boolean getFlag(Chunk chunk, String flag);


    // **********************
    // *  Player - Queries *
    // **********************


    /**
     * Gets player data (synchronous).
     *
     * @param playerId The player's UUID.
     * @return An Optional containing PlayerData, or empty.
     */
    Optional<PlayerData> getPlayer(UUID playerId);

    /**
     * Gets player data (asynchronous).
     *
     * @param playerId The player's UUID.
     * @return A CompletableFuture wrapping an Optional PlayerData.
     */
    CompletableFuture<Optional<PlayerData>> getPlayerAsync(UUID playerId);

    /**
     * Gets player data by name (asynchronous). Works for offline players.
     *
     * @param playerName The player's name.
     * @return A CompletableFuture wrapping an Optional PlayerData.
     */
    CompletableFuture<Optional<PlayerData>> getPlayerByNameAsync(String playerName);

    /**
     * Gets multiple players' data at once (asynchronous).
     *
     * @param playerIds The list of player UUIDs.
     * @return A CompletableFuture wrapping a map of UUID to Optional PlayerData.
     */
    CompletableFuture<Map<UUID, Optional<PlayerData>>> getPlayersAsync(List<UUID> playerIds);

    /**
     * Gets player names for a list of UUIDs (asynchronous).
     *
     * @param uuids The list of UUIDs.
     * @return A CompletableFuture wrapping a set of player names.
     */
    CompletableFuture<Set<String>> getPlayerNamesAsync(List<UUID> uuids);


    // **********************
    // *  Player - Limits  *
    // **********************


    /**
     * Gets the maximum number of claims a player can create.
     *
     * @param player The player.
     * @return The maximum claims allowed.
     */
    int getMaxClaims(Player player);

    /**
     * Gets the maximum number of chunks a player can claim.
     *
     * @param player The player.
     * @return The maximum chunks allowed.
     */
    int getMaxChunks(Player player);

    /**
     * Gets the maximum claim radius for a player.
     *
     * @param player The player.
     * @return The maximum radius.
     */
    int getMaxRadius(Player player);

    /**
     * Gets the maximum number of members a player can add to a claim.
     *
     * @param player The player.
     * @return The maximum members allowed.
     */
    int getMaxMembers(Player player);

    /**
     * Gets the cost per chunk for a player.
     *
     * @param player The player.
     * @return The cost per chunk.
     */
    double getChunkCost(Player player);

    /**
     * Gets the cost multiplier for a player.
     *
     * @param player The player.
     * @return The cost multiplier.
     */
    double getCostMultiplier(Player player);

    /**
     * Gets the number of chunks currently owned by a player.
     *
     * @param playerId The player's UUID.
     * @return The number of chunks owned.
     */
    int getChunkCount(UUID playerId);

    /**
     * Gets the number of claims currently owned by a player.
     *
     * @param playerId The player's UUID.
     * @return The number of claims owned.
     */
    int getClaimCount(UUID playerId);


    // ***********************
    // *  World - Settings  *
    // ***********************


    /**
     * Gets the mode of a world.
     *
     * @param world The world.
     * @return The world mode.
     */
    WorldMode getWorldMode(World world);

    /**
     * Gets the default permissions for Protected mode.
     *
     * @return A map of permission key to boolean value.
     */
    Map<String, Boolean> getSettingsForProtectedMode();

    /**
     * Gets the default permissions for Survival Requiring Claims mode.
     *
     * @return A map of permission key to boolean value.
     */
    Map<String, Boolean> getSettingsForSurvivalRequiringClaimsMode();

    /**
     * Gets a plugin setting value.
     *
     * @param key The setting key (dot-separated path).
     * @return The setting value, or null if not found.
     */
    Object getSetting(String key);

    /**
     * Gets a boolean plugin setting.
     *
     * @param key The setting key.
     * @param defaultValue The default value if not found.
     * @return The boolean value.
     */
    boolean getBooleanSetting(String key, boolean defaultValue);

    /**
     * Gets a string plugin setting.
     *
     * @param key The setting key.
     * @param defaultValue The default value if not found.
     * @return The string value.
     */
    String getStringSetting(String key, String defaultValue);

    /**
     * Gets an integer plugin setting.
     *
     * @param key The setting key.
     * @param defaultValue The default value if not found.
     * @return The integer value.
     */
    int getIntSetting(String key, int defaultValue);

    /**
     * Gets a double plugin setting.
     *
     * @param key The setting key.
     * @param defaultValue The default value if not found.
     * @return The double value.
     */
    double getDoubleSetting(String key, double defaultValue);

}
