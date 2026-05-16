package fr.xyness.SimpleClaimSystem.Events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import fr.xyness.SimpleClaimSystem.Types.Claim;

import java.util.UUID;

/**
 * Called when the public-warp boolean of a claim is about to flip (via /claim settings toggle
 * or /scs forceWarp). Cancellable so third-party plugins can veto opening/closing a warp.
 */
public class ClaimWarpToggleEvent extends ClaimEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private final boolean newState;
    private final UUID playerId;
    private boolean cancelled;

    /**
     * @param claim    The claim being toggled.
     * @param newState The state the warp will move to (true = open).
     * @param playerId The actor's UUID (claim owner for /claim settings, admin for /scs forceWarp).
     */
    public ClaimWarpToggleEvent(Claim claim, boolean newState, UUID playerId) {
        super(claim);
        this.newState = newState;
        this.playerId = playerId;
    }

    /** Returns the new warp state (true = open). */
    public boolean getNewState() { return newState; }

    /** Returns the UUID of the actor (owner or admin) triggering the toggle. */
    public UUID getPlayerId() { return playerId; }

    @Override public boolean isCancelled() { return cancelled; }
    @Override public void setCancelled(boolean cancel) { this.cancelled = cancel; }
    @Override public HandlerList getHandlers() { return HANDLERS; }
    public static HandlerList getHandlerList() { return HANDLERS; }
}
