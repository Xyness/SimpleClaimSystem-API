package fr.xyness.SimpleClaimSystem.Events;

import org.bukkit.event.HandlerList;

import fr.xyness.SimpleClaimSystem.Enums.ClaimRole;
import fr.xyness.SimpleClaimSystem.Types.Claim;

import java.util.UUID;

/**
 * Called when a member is added, removed, promoted, or demoted in a claim.
 */
public class ClaimMemberEvent extends ClaimEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    /** The type of member action. */
    public enum Action { ADD, REMOVE, PROMOTE, DEMOTE, KICK, BAN, UNBAN }

    private final UUID memberId;
    private final Action action;
    private final ClaimRole role;

    public ClaimMemberEvent(Claim claim, UUID memberId, Action action, ClaimRole role) {
        super(claim);
        this.memberId = memberId;
        this.action = action;
        this.role = role;
    }

    /** Gets the UUID of the member affected. */
    public UUID getMemberId() { return memberId; }

    /** Gets the action performed. */
    public Action getAction() { return action; }

    /** Gets the role of the member (may be null for REMOVE/KICK/BAN/UNBAN). */
    public ClaimRole getRole() { return role; }

    @Override public HandlerList getHandlers() { return HANDLERS; }
    public static HandlerList getHandlerList() { return HANDLERS; }
}
