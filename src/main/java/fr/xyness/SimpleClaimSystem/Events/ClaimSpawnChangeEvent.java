package fr.xyness.SimpleClaimSystem.Events;

import org.bukkit.Location;
import org.bukkit.event.HandlerList;

import fr.xyness.SimpleClaimSystem.Types.Claim;

import java.util.UUID;

/**
 * Called when a claim spawn location is changed.
 */
public class ClaimSpawnChangeEvent extends ClaimEvent {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Location newSpawn;
    private final UUID playerId;

    public ClaimSpawnChangeEvent(Claim claim, Location newSpawn, UUID playerId) {
        super(claim);
        this.newSpawn = newSpawn;
        this.playerId = playerId;
    }

    /** Gets the new spawn location. */
    public Location getNewSpawn() { return newSpawn; }

    /** Gets the UUID of the player who changed the spawn. */
    public UUID getPlayerId() { return playerId; }

    @Override public HandlerList getHandlers() { return HANDLERS; }
    public static HandlerList getHandlerList() { return HANDLERS; }
}
