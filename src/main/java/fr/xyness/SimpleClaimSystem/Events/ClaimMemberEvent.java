package fr.xyness.SimpleClaimSystem.Events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import fr.xyness.SimpleClaimSystem.Enums.ClaimRole;
import fr.xyness.SimpleClaimSystem.Types.Claim;

import java.util.UUID;

/**
 * Called when a member is added, removed, promoted, or demoted in a claim. Cancellable.
 */
public class ClaimMemberEvent extends ClaimEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    /** The type of member action. */
    public enum Action { ADD, REMOVE, PROMOTE, DEMOTE, KICK, BAN, UNBAN, ROLE_CHANGE }

    private final UUID memberId;
    private final Action action;
    private final String roleName;
    private boolean cancelled;

    public ClaimMemberEvent(Claim claim, UUID memberId, Action action, String roleName) {
        super(claim);
        this.memberId = memberId;
        this.action = action;
        this.roleName = roleName;
    }


    /** Gets the UUID of the member affected. */
    public UUID getMemberId() { return memberId; }

    /** Gets the action performed. */
    public Action getAction() { return action; }

    /** Gets the role name of the member (may be null for REMOVE/KICK/BAN/UNBAN). */
    public String getRoleName() { return roleName; }

    /** Gets the role as ClaimRole enum, or null if custom/null. */
    public ClaimRole getRole() {
        if (roleName == null) return null;
        try {
            return ClaimRole.valueOf(roleName);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override public boolean isCancelled() { return cancelled; }
    @Override public void setCancelled(boolean cancel) { this.cancelled = cancel; }
    @Override public HandlerList getHandlers() { return HANDLERS; }
    public static HandlerList getHandlerList() { return HANDLERS; }
}
