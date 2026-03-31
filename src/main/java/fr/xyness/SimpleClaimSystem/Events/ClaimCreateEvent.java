package fr.xyness.SimpleClaimSystem.Events;

import org.bukkit.event.HandlerList;

import fr.xyness.SimpleClaimSystem.Types.Claim;

import java.util.UUID;

/**
 * Called when a claim is created.
 */
public class ClaimCreateEvent extends ClaimEvent {

    private static final HandlerList HANDLERS = new HandlerList();
    private final UUID playerId;

    public ClaimCreateEvent(Claim claim, UUID playerId) {
        super(claim);
        this.playerId = playerId;
    }

    /** Gets the UUID of the player who created the claim. */
    public UUID getPlayerId() { return playerId; }

    @Override public HandlerList getHandlers() { return HANDLERS; }
    public static HandlerList getHandlerList() { return HANDLERS; }
}
