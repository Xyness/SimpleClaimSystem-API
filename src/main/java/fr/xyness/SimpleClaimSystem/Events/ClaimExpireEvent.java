package fr.xyness.SimpleClaimSystem.Events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import fr.xyness.SimpleClaimSystem.Types.Claim;

import java.util.UUID;

/**
 * Called when a claim is about to be automatically deleted by the auto-purge system due to owner
 * inactivity. Cancellable: a listener can keep a claim alive (server spawn area, faction base,
 * paid protection…) without disabling auto-purge globally.
 *
 * <p>Fired off the main thread — the purge task runs asynchronously.</p>
 */
public class ClaimExpireEvent extends ClaimEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private final UUID ownerUuid;
    private boolean cancelled;

    /**
     * @param claim     The claim about to be purged.
     * @param ownerUuid The UUID of the inactive owner.
     */
    public ClaimExpireEvent(Claim claim, UUID ownerUuid) {
        super(claim);
        this.ownerUuid = ownerUuid;
    }

    /** Gets the UUID of the inactive owner. */
    public UUID getOwnerUuid() { return ownerUuid; }

    @Override public boolean isCancelled() { return cancelled; }
    @Override public void setCancelled(boolean cancel) { this.cancelled = cancel; }
    @Override public HandlerList getHandlers() { return HANDLERS; }
    public static HandlerList getHandlerList() { return HANDLERS; }
}
