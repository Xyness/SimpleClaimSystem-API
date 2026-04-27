package fr.xyness.SimpleClaimSystem.Events;

import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import fr.xyness.SimpleClaimSystem.Types.Claim;

import java.util.UUID;

/**
 * Called when a claim spawn location is changed. Cancellable.
 */
public class ClaimSpawnChangeEvent extends ClaimEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Location newSpawn;
    private final UUID playerId;
    private boolean cancelled;

    public ClaimSpawnChangeEvent(Claim claim, Location newSpawn, UUID playerId) {
        super(claim);
        this.newSpawn = newSpawn;
        this.playerId = playerId;
    }

    /** Gets the new spawn location. */
    public Location getNewSpawn() { return newSpawn; }

    /** Gets the UUID of the player who changed the spawn. */
    public UUID getPlayerId() { return playerId; }

    @Override public boolean isCancelled() { return cancelled; }
    @Override public void setCancelled(boolean cancel) { this.cancelled = cancel; }
    @Override public HandlerList getHandlers() { return HANDLERS; }
    public static HandlerList getHandlerList() { return HANDLERS; }
}
