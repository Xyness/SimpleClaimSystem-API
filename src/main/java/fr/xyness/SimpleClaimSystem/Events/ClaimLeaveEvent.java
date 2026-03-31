package fr.xyness.SimpleClaimSystem.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import fr.xyness.SimpleClaimSystem.Types.Claim;

/**
 * Called when a player leaves a claim.
 */
public class ClaimLeaveEvent extends ClaimEvent {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;

    public ClaimLeaveEvent(Claim claim, Player player) {
        super(claim);
        this.player = player;
    }

    /** Gets the player leaving the claim. */
    public Player getPlayer() { return player; }

    @Override public HandlerList getHandlers() { return HANDLERS; }
    public static HandlerList getHandlerList() { return HANDLERS; }
}
