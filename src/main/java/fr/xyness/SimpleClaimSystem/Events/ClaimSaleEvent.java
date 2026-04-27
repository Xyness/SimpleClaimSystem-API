package fr.xyness.SimpleClaimSystem.Events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import fr.xyness.SimpleClaimSystem.Types.Claim;

import java.util.UUID;

/**
 * Called when a claim sale status changes (listed, cancelled, bought). Cancellable.
 */
public class ClaimSaleEvent extends ClaimEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    /** The type of sale action. */
    public enum Action { LISTED, CANCELLED, BOUGHT }

    private final Action action;
    private final UUID playerId;
    private final double price;
    private boolean cancelled;

    public ClaimSaleEvent(Claim claim, Action action, UUID playerId, double price) {
        super(claim);
        this.action = action;
        this.playerId = playerId;
        this.price = price;
    }

    /** Gets the sale action type. */
    public Action getAction() { return action; }

    /** Gets the UUID of the player involved. */
    public UUID getPlayerId() { return playerId; }

    /** Gets the sale price. */
    public double getPrice() { return price; }

    @Override public boolean isCancelled() { return cancelled; }
    @Override public void setCancelled(boolean cancel) { this.cancelled = cancel; }
    @Override public HandlerList getHandlers() { return HANDLERS; }
    public static HandlerList getHandlerList() { return HANDLERS; }
}
