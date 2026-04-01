package fr.xyness.SimpleClaimSystem.Enums;

/**
 * Claim roles.
 */
public enum ClaimRole {
	VISITOR,
    MEMBER,
    MODERATOR,
    OWNER;

    /**
     * Checks if a role name corresponds to a default role.
     *
     * @param roleName The role name.
     * @return True if the role is a default role.
     */
    public static boolean isDefault(String roleName) {
        try {
            valueOf(roleName);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
