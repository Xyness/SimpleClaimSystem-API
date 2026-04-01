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
    private final String roleName;
    private final String permission;
    private final boolean value;
    private final UUID playerId;

    public ClaimPermissionChangeEvent(Claim claim, String roleName, String permission, boolean value, UUID playerId) {
        super(claim);
        this.roleName = roleName;
        this.permission = permission;
        this.value = value;
        this.playerId = playerId;
    }

    /** Backward-compatible constructor accepting ClaimRole enum. */
    public ClaimPermissionChangeEvent(Claim claim, ClaimRole role, String permission, boolean value, UUID playerId) {
        this(claim, role.name(), permission, value, playerId);
    }

    /** Gets the role name affected. */
    public String getRoleName() { return roleName; }

    /** Gets the role as ClaimRole enum, or null if custom. */
    public ClaimRole getRole() {
        try {
            return ClaimRole.valueOf(roleName);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /** Gets the permission name. */
    public String getPermission() { return permission; }

    /** Gets the new value. */
    public boolean getValue() { return value; }

    /** Gets the UUID of the player who changed the permission. */
    public UUID getPlayerId() { return playerId; }

    @Override public HandlerList getHandlers() { return HANDLERS; }
    public static HandlerList getHandlerList() { return HANDLERS; }
}
