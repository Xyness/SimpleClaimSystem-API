package fr.xyness.SimpleClaimSystem.Events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import fr.xyness.SimpleClaimSystem.Types.Claim;

import java.util.UUID;

/**
 * Called when a player favourites or unfavourites a claim. Cancellable.
 */
public class ClaimFavoriteEvent extends ClaimEvent implements Cancellable {


    // **********
    // *  Type  *
    // **********


    /** What changed. */
    public enum Action {
        /** The claim was added to the player's favourites. */
        FAVORITE,
        /** The claim was removed from the player's favourites. */
        UNFAVORITE
    }


    // ***************
    // *  Variables  *
    // ***************


    /** Bukkit handler list. */
    private static final HandlerList HANDLERS = new HandlerList();

    /** UUID of the player toggling the favourite. */
    private final UUID playerId;

    /** Whether the claim was favourited or unfavourited. */
    private final Action action;

    /** Cancellation flag. */
    private boolean cancelled;


    // ******************
    // *  Constructors  *
    // ******************


    /**
     * Main constructor.
     *
     * @param claim The claim involved.
     * @param playerId The player toggling the favourite.
     * @param action FAVORITE or UNFAVORITE.
     */
    public ClaimFavoriteEvent(Claim claim, UUID playerId, Action action) {
        super(claim);
        this.playerId = playerId;
        this.action = action;
    }


    // *************
    // *  Methods  *
    // *************


    /**
     * Returns the UUID of the player toggling the favourite.
     *
     * @return The player UUID.
     */
    public UUID getPlayerId() { return playerId; }

    /**
     * Returns the action (FAVORITE or UNFAVORITE).
     *
     * @return The action.
     */
    public Action getAction() { return action; }

    @Override public boolean isCancelled() { return cancelled; }
    @Override public void setCancelled(boolean cancel) { this.cancelled = cancel; }
    @Override public HandlerList getHandlers() { return HANDLERS; }

    /**
     * Returns the static handler list (Bukkit convention).
     *
     * @return The handler list.
     */
    public static HandlerList getHandlerList() { return HANDLERS; }
}
