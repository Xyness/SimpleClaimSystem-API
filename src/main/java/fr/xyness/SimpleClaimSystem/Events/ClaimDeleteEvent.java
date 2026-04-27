package fr.xyness.SimpleClaimSystem.Events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import fr.xyness.SimpleClaimSystem.Types.Claim;

import java.util.UUID;

/**
 * Called when a claim is deleted. Cancellable — cancelling aborts the deletion
 * before any DB write or cache invalidation.
 */
public class ClaimDeleteEvent extends ClaimEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private final UUID playerId;
    private boolean cancelled;

    public ClaimDeleteEvent(Claim claim, UUID playerId) {
        super(claim);
        this.playerId = playerId;
    }

    /** Gets the UUID of the player who deleted the claim. */
    public UUID getPlayerId() { return playerId; }

    @Override public boolean isCancelled() { return cancelled; }
    @Override public void setCancelled(boolean cancel) { this.cancelled = cancel; }
    @Override public HandlerList getHandlers() { return HANDLERS; }
    public static HandlerList getHandlerList() { return HANDLERS; }
}
