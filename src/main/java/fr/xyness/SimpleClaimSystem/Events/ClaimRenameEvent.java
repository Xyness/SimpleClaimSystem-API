package fr.xyness.SimpleClaimSystem.Events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import fr.xyness.SimpleClaimSystem.Types.Claim;

import java.util.UUID;

/**
 * Called when a claim is renamed. Cancellable.
 */
public class ClaimRenameEvent extends ClaimEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private final String oldName;
    private final String newName;
    private final UUID playerId;
    private boolean cancelled;

    public ClaimRenameEvent(Claim claim, String oldName, String newName, UUID playerId) {
        super(claim);
        this.oldName = oldName;
        this.newName = newName;
        this.playerId = playerId;
    }

    /** Gets the old claim name. */
    public String getOldName() { return oldName; }

    /** Gets the new claim name. */
    public String getNewName() { return newName; }

    /** Gets the UUID of the player who renamed the claim. */
    public UUID getPlayerId() { return playerId; }

    @Override public boolean isCancelled() { return cancelled; }
    @Override public void setCancelled(boolean cancel) { this.cancelled = cancel; }
    @Override public HandlerList getHandlers() { return HANDLERS; }
    public static HandlerList getHandlerList() { return HANDLERS; }
}
