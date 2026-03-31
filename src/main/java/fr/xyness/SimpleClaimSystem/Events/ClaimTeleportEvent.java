package fr.xyness.SimpleClaimSystem.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import fr.xyness.SimpleClaimSystem.Types.Claim;

/**
 * Called when a player teleports to a claim spawn.
 * This event is cancellable.
 */
public class ClaimTeleportEvent extends ClaimEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private boolean cancelled;

    public ClaimTeleportEvent(Claim claim, Player player) {
        super(claim);
        this.player = player;
    }

    /** Gets the player teleporting. */
    public Player getPlayer() { return player; }

    @Override public boolean isCancelled() { return cancelled; }
    @Override public void setCancelled(boolean cancel) { this.cancelled = cancel; }
    @Override public HandlerList getHandlers() { return HANDLERS; }
    public static HandlerList getHandlerList() { return HANDLERS; }
}
