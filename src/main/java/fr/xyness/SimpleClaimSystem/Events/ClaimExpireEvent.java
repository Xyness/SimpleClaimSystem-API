package fr.xyness.SimpleClaimSystem.Events;

import org.bukkit.event.HandlerList;

import fr.xyness.SimpleClaimSystem.Types.Claim;

import java.util.UUID;

/**
 * Called when a claim is automatically deleted by the auto-purge system
 * due to owner inactivity.
 */
public class ClaimExpireEvent extends ClaimEvent {

    private static final HandlerList HANDLERS = new HandlerList();
    private final UUID ownerUuid;

    public ClaimExpireEvent(Claim claim, UUID ownerUuid) {
        super(claim);
        this.ownerUuid = ownerUuid;
    }

    /** Gets the UUID of the inactive owner. */
    public UUID getOwnerUuid() { return ownerUuid; }

    @Override public HandlerList getHandlers() { return HANDLERS; }
    public static HandlerList getHandlerList() { return HANDLERS; }
}
