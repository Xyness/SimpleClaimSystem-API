package fr.xyness.SimpleClaimSystem.Events;

import org.bukkit.event.HandlerList;

import fr.xyness.SimpleClaimSystem.Types.Claim;

import java.util.UUID;

/**
 * Called when a claim flag is changed.
 */
public class ClaimFlagChangeEvent extends ClaimEvent {

    private static final HandlerList HANDLERS = new HandlerList();
    private final String flag;
    private final boolean value;
    private final UUID playerId;

    public ClaimFlagChangeEvent(Claim claim, String flag, boolean value, UUID playerId) {
        super(claim);
        this.flag = flag;
        this.value = value;
        this.playerId = playerId;
    }

    /** Gets the flag name. */
    public String getFlag() { return flag; }

    /** Gets the new value. */
    public boolean getValue() { return value; }

    /** Gets the UUID of the player who changed the flag. */
    public UUID getPlayerId() { return playerId; }

    @Override public HandlerList getHandlers() { return HANDLERS; }
    public static HandlerList getHandlerList() { return HANDLERS; }
}
