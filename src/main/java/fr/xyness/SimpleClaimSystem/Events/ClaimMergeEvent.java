package fr.xyness.SimpleClaimSystem.Events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import fr.xyness.SimpleClaimSystem.Types.Claim;

import java.util.List;
import java.util.UUID;

/**
 * Called when multiple claims are merged into one. Cancellable.
 */
public class ClaimMergeEvent extends ClaimEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private final List<Claim> mergedClaims;
    private final UUID playerId;
    private boolean cancelled;

    public ClaimMergeEvent(Claim targetClaim, List<Claim> mergedClaims, UUID playerId) {
        super(targetClaim);
        this.mergedClaims = mergedClaims;
        this.playerId = playerId;
    }

    /** Gets the list of claims that were merged into the target. */
    public List<Claim> getMergedClaims() { return mergedClaims; }

    /** Gets the UUID of the player who initiated the merge. */
    public UUID getPlayerId() { return playerId; }

    @Override public boolean isCancelled() { return cancelled; }
    @Override public void setCancelled(boolean cancel) { this.cancelled = cancel; }
    @Override public HandlerList getHandlers() { return HANDLERS; }
    public static HandlerList getHandlerList() { return HANDLERS; }
}
