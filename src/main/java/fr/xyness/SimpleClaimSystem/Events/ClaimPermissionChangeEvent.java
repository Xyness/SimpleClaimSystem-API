package fr.xyness.SimpleClaimSystem.Events;

import org.bukkit.event.HandlerList;

import fr.xyness.SimpleClaimSystem.Enums.ClaimRole;
import fr.xyness.SimpleClaimSystem.Types.Claim;

import java.util.UUID;

/**
 * Called when a claim permission is changed.
 */
public class ClaimPermissionChangeEvent extends ClaimEvent {

    private static final HandlerList HANDLERS = new HandlerList();
    private final ClaimRole role;
    private final String permission;
    private final boolean value;
    private final UUID playerId;

    public ClaimPermissionChangeEvent(Claim claim, ClaimRole role, String permission, boolean value, UUID playerId) {
        super(claim);
        this.role = role;
        this.permission = permission;
        this.value = value;
        this.playerId = playerId;
    }

    /** Gets the role affected. */
    public ClaimRole getRole() { return role; }

    /** Gets the permission name. */
    public String getPermission() { return permission; }

    /** Gets the new value. */
    public boolean getValue() { return value; }

    /** Gets the UUID of the player who changed the permission. */
    public UUID getPlayerId() { return playerId; }

    @Override public HandlerList getHandlers() { return HANDLERS; }
    public static HandlerList getHandlerList() { return HANDLERS; }
}
