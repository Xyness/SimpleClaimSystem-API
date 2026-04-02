package fr.xyness.SimpleClaimSystem.Events;

import org.bukkit.event.Event;

import fr.xyness.SimpleClaimSystem.Types.Claim;

/**
 * Base class for all claim-related events.
 */
public abstract class ClaimEvent extends Event {


    // ***************
    // *  Variables  *
    // ***************


    /** The claim involved in this event. */
    private final Claim claim;


    // ******************
    // *  Constructors  *
    // ******************


    /**
     * Main constructor.
     *
     * @param claim The claim involved.
     */
    public ClaimEvent(Claim claim) {
        super(false);
        this.claim = claim;
    }


    // *************
    // *  Methods  *
    // *************


    /**
     * Gets the claim involved in this event.
     *
     * @return The claim.
     */
    public Claim getClaim() {
        return claim;
    }
}
