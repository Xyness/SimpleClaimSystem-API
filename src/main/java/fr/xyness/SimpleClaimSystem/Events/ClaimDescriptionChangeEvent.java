package fr.xyness.SimpleClaimSystem.Events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import fr.xyness.SimpleClaimSystem.Types.Claim;

import java.util.UUID;

/**
 * Called when a claim description is changed. Cancellable.
 */
public class ClaimDescriptionChangeEvent extends ClaimEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private final String oldDescription;
    private final String newDescription;
    private final UUID playerId;
    private boolean cancelled;

    public ClaimDescriptionChangeEvent(Claim claim, String oldDescription, String newDescription, UUID playerId) {
        super(claim);
        this.oldDescription = oldDescription;
        this.newDescription = newDescription;
        this.playerId = playerId;
    }

    /** Gets the old description. */
    public String getOldDescription() { return oldDescription; }

    /** Gets the new description. */
    public String getNewDescription() { return newDescription; }

    /** Gets the UUID of the player who changed the description. */
    public UUID getPlayerId() { return playerId; }

    @Override public boolean isCancelled() { return cancelled; }
    @Override public void setCancelled(boolean cancel) { this.cancelled = cancel; }
    @Override public HandlerList getHandlers() { return HANDLERS; }
    public static HandlerList getHandlerList() { return HANDLERS; }
}
