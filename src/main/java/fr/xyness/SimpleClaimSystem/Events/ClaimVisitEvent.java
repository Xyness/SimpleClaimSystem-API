package fr.xyness.SimpleClaimSystem.Events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import fr.xyness.SimpleClaimSystem.Types.Claim;

import java.util.UUID;

/**
 * Called when a player is about to be teleported by /claim visit (or by clicking a warp icon
 * in the warps GUI). Cancellable so anti-grief, region restrictions, or custom paywalls can
 * block the TP before it happens. Fired AFTER ban check and warp-open check, BEFORE payment
 * and teleport — listeners that cancel must own the messaging back to the player.
 */
public class ClaimVisitEvent extends ClaimEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private final UUID visitorId;
    private boolean cancelled;

    /**
     * @param claim     The target claim.
     * @param visitorId The visiting player's UUID.
     */
    public ClaimVisitEvent(Claim claim, UUID visitorId) {
        super(claim);
        this.visitorId = visitorId;
    }

    /** Returns the visiting player's UUID. */
    public UUID getVisitorId() { return visitorId; }

    @Override public boolean isCancelled() { return cancelled; }
    @Override public void setCancelled(boolean cancel) { this.cancelled = cancel; }
    @Override public HandlerList getHandlers() { return HANDLERS; }
    public static HandlerList getHandlerList() { return HANDLERS; }
}
