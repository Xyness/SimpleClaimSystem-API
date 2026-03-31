package fr.xyness.SimpleClaimSystem.Events;

import org.bukkit.event.HandlerList;

import fr.xyness.SimpleClaimSystem.Types.Claim;

import java.util.UUID;

/**
 * Called when ownership of a claim is transferred.
 */
public class ClaimOwnerTransferEvent extends ClaimEvent {

    private static final HandlerList HANDLERS = new HandlerList();
    private final UUID oldOwner;
    private final UUID newOwner;

    public ClaimOwnerTransferEvent(Claim claim, UUID oldOwner, UUID newOwner) {
        super(claim);
        this.oldOwner = oldOwner;
        this.newOwner = newOwner;
    }

    /** Gets the UUID of the previous owner. */
    public UUID getOldOwner() { return oldOwner; }

    /** Gets the UUID of the new owner. */
    public UUID getNewOwner() { return newOwner; }

    @Override public HandlerList getHandlers() { return HANDLERS; }
    public static HandlerList getHandlerList() { return HANDLERS; }
}
