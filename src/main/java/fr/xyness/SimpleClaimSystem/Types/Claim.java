package fr.xyness.SimpleClaimSystem.Types;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;

import fr.xyness.SimpleClaimSystem.Enums.ClaimRole;

/**
 * Claim object
 */
public class Claim {


    // ***************
    // *  Variables  *
    // ***************


	// Id of the claim.
    private int id;

    // UUID of the owner of the claim.
    private volatile UUID ownerUuid;

    // Name of the owner of the claim.
    private volatile String ownerName;

    // Name of the claim.
    private volatile String claimName;

    // Description of the claim.
    private volatile String description;

    // Chunks of the claim.
    private Set<ChunkKey> chunks = ConcurrentHashMap.newKeySet();

    // UUID of the world of the claim.
    private String worldUuid;

    // Spawn location of the claim.
    private volatile Location spawnLocation;

    // Members map (UUID -> role name).
    private Map<UUID, String> members = new ConcurrentHashMap<>();

    // Temporary-invite expirations (UUID -> expiration timestamp). Only entries for members with a
    // bounded membership (set via /claim invite <player> <duration>) are present; permanent members
    // are absent from the map. Maintained in parallel with {@link #members} — stays null-clean via
    // the add/remove helpers below.
    private Map<UUID, LocalDateTime> memberExpirations = new ConcurrentHashMap<>();

    // Banned players map (UUID -> Unban LocalDateTime).
    private Map<UUID, LocalDateTime> banned = new ConcurrentHashMap<>();

    // Permissions map (role name -> (permission, value)).
    private Map<String, Map<String, Boolean>> permissions = new ConcurrentHashMap<>();

    // Flags map (Flags -> Value).
    private Map<String, Boolean> flags = new ConcurrentHashMap<>();

    // Other things (Thing -> Value).
    private Map<String, Object> other_things = new ConcurrentHashMap<>();

    // Custom roles for this claim (ordered list of role names).
    private volatile List<String> customRoles = new ArrayList<>();


    // ******************
    // *  Constructors  *
    // ******************


    /**
     * Main constructor.
     *
     * @param id The id of the claim.
     * @param ownerUuid The UUID of the owner of the claim.
     * @param ownerName The name of the owner of the claim.
     * @param claimName The name of the claim.
     * @param description The description of the claim.
     * @param chunks The chunks of the claim.
     * @param worldUuid The UUID of the world of the claim.
     * @param spawnLocation The spawn location of the claim.
     * @param members The members of the claim.
     * @param banned The banned players from the claim.
     * @param permissions The settings of the claim.
     * @param flags The flags of the claim.
     * @param other_things The other things of the claim.
     */
    public Claim(int id, UUID ownerUuid, String ownerName, String claimName, String description, Set<ChunkKey> chunks, String worldUuid,
                 Location spawnLocation, Map<UUID, String> members, Map<UUID, LocalDateTime> banned, Map<String, Map<String, Boolean>> permissions,
                 Map<String, Boolean> flags, Map<String, Object> other_things) {
        this.id = id;
        this.ownerUuid = ownerUuid;
        this.claimName = claimName;
        this.description = description;
        this.chunks = chunks;
        this.worldUuid = worldUuid;
        this.spawnLocation = spawnLocation;
        this.members = members;
        this.banned = banned;
        this.permissions = permissions;
        this.flags = flags;
        this.other_things = other_things;
    }

    /**
     * Second constructor.
     *
     * @param id The id of the claim.
     * @param ownerUuid The UUID of the owner of the claim.
     * @param ownerName The name of the owner of the claim.
     * @param claimName The name of the claim.
     * @param worldUuid The UUID of the world of the claim.
     */
    public Claim(int id, UUID ownerUuid, String ownerName, String claimName, String worldUuid) {
        this.id = id;
        this.ownerUuid = ownerUuid;
        this.ownerName = ownerName;
        this.claimName = claimName;
        this.worldUuid = worldUuid;
    }

    /**
     * Third constructor.
     *
     * @param ownerUuid The UUID of the owner of the claim.
     * @param ownerName The name of the owner of the claim.
     * @param claimName The name of the claim.
     * @param description The description of the claim.
     * @param worldUuid The UUID of the world of the claim.
     * @param spawnLocation The spawn location of the claim.
     */
    public Claim(UUID ownerUuid, String ownerName, String claimName, String description, String worldUuid, Location spawnLocation) {
        this.ownerUuid = ownerUuid;
        this.ownerName = ownerName;
        this.claimName = claimName;
        this.description = description;
        this.worldUuid = worldUuid;
        this.spawnLocation = spawnLocation;
    }

    /**
     * Fourth constructor.
     *
     * @param id The id of the claim.
     * @param ownerName The name of the owner of the claim.
     * @param claimName The name of the claim.
     * @param description The description of the claim.
     * @param chunks The chunks of the claim.
     */
    public Claim(int id, String ownerName, String claimName, String description, Set<ChunkKey> chunks) {
        this.id = id;
        this.ownerName = ownerName;
        this.claimName = claimName;
        this.description = description;
        this.chunks = chunks;
    }


    // *************
    // *  Methods  *
    // *************


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Claim claim = (Claim) o;
        return id == claim.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public UUID getOwnerUuid() { return ownerUuid; }
    public void setOwnerUuid(UUID ownerUuid) { this.ownerUuid = ownerUuid; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public String getClaimName() { return claimName; }
    public void setClaimName(String claimName) { this.claimName = claimName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Set<ChunkKey> getChunks() { return chunks; }
    public void setChunks(Set<ChunkKey> chunks) { this.chunks = chunks; }
    public void addChunk(ChunkKey key) { chunks.add(key); }
    public void addChunks(Set<ChunkKey> chunks) { this.chunks.addAll(chunks); }
    public void removeChunk(ChunkKey key) { chunks.remove(key); }
    public boolean containsChunk(ChunkKey key) { return chunks.contains(key); }

    public String getWorldUuid() { return worldUuid; }
    public void setWorldName(String worldUuid) { this.worldUuid = worldUuid; }

    public Location getSpawnLocation() { return spawnLocation; }
    public void setSpawnLocation(Location spawnLocation) { this.spawnLocation = spawnLocation; }

    // --- Members (String-based roles) ---

    public Map<UUID, String> getMembers() { return members; }
    public void setMembers(Map<UUID, String> members) { this.members = members; }
    public void addMember(UUID uuid, String role) { members.put(uuid, role); }
    public void removeMember(UUID uuid) {
        members.remove(uuid);
        memberExpirations.remove(uuid); // keep the parallel map consistent
    }

    /** Backward-compatible method accepting ClaimRole enum. */
    public void addMember(UUID uuid, ClaimRole role) { members.put(uuid, role.name()); }

    /**
     * Adds a member with an optional expiration timestamp.
     * Pass {@code null} for a permanent membership (same as {@link #addMember(UUID, String)}).
     */
    public void addMember(UUID uuid, String role, LocalDateTime expiresAt) {
        members.put(uuid, role);
        if (expiresAt == null) memberExpirations.remove(uuid);
        else memberExpirations.put(uuid, expiresAt);
    }

    // --- Member expirations (for temporary invitations) ---

    /** @return map of member UUIDs to their membership expiration. Missing keys = permanent member. */
    public Map<UUID, LocalDateTime> getMemberExpirations() { return memberExpirations; }

    public void setMemberExpirations(Map<UUID, LocalDateTime> memberExpirations) {
        this.memberExpirations = memberExpirations == null ? new ConcurrentHashMap<>() : memberExpirations;
    }

    /** @return the expiration timestamp for this member, or {@code null} if permanent / not a member. */
    public LocalDateTime getMemberExpiresAt(UUID uuid) { return memberExpirations.get(uuid); }

    /** @return true if this member has a bounded (temporary) membership. */
    public boolean isTemporaryMember(UUID uuid) { return memberExpirations.containsKey(uuid); }

    public String getRole(UUID uuid) {
        return members.getOrDefault(uuid, ClaimRole.VISITOR.name());
    }

    /** Gets the role as ClaimRole enum, or VISITOR if it's a custom role. */
    public ClaimRole getRoleEnum(UUID uuid) {
        String role = getRole(uuid);
        try {
            return ClaimRole.valueOf(role);
        } catch (IllegalArgumentException e) {
            return ClaimRole.VISITOR;
        }
    }

    public boolean isMember(UUID uuid) { return members.containsKey(uuid); }

    // --- Banned ---

    public Map<UUID, LocalDateTime> getBanned() { return banned; }
    public LocalDateTime getBanTime(UUID banUuid) { return banned.get(banUuid); }
    public void setBanned(Map<UUID, LocalDateTime> banned) { this.banned = banned; }
    public void banPlayer(UUID uuid, LocalDateTime date) { banned.put(uuid, date); }
    public void unbanPlayer(UUID uuid) { banned.remove(uuid); }

    public boolean isBanned(UUID uuid) {
    	LocalDateTime date = banned.get(uuid);
        return date != null && LocalDateTime.now().isBefore(date);
    }

    // --- Permissions (String-based roles) ---

    public Map<String, Map<String, Boolean>> getPermissions() { return permissions; }
    public void setPermissions(Map<String, Map<String, Boolean>> permissions) {
        this.permissions = new ConcurrentHashMap<>();
        permissions.forEach((k, v) -> this.permissions.put(k, new ConcurrentHashMap<>(v)));
    }

    public void setPermission(String role, String key, Boolean value) {
        Map<String, Boolean> rolePerms = permissions.get(role);
        if (rolePerms != null) rolePerms.put(key, value);
    }

    public Boolean getPermission(String role, String key) {
    	if (ClaimRole.OWNER.name().equals(role)) return true;
        Map<String, Boolean> rolePerms = permissions.get(role);
        if (rolePerms == null) return false;
        return rolePerms.getOrDefault(key, false);
    }

    /** Backward-compatible method accepting ClaimRole enum. */
    public void setPermission(ClaimRole role, String key, Boolean value) {
        setPermission(role.name(), key, value);
    }

    /** Backward-compatible method accepting ClaimRole enum. */
    public Boolean getPermission(ClaimRole role, String key) {
        return getPermission(role.name(), key);
    }

    // --- Flags ---

    public Map<String,Boolean> getFlags() { return flags; }
    public void setFlags(Map<String,Boolean> flags) { this.flags = new ConcurrentHashMap<>(flags); }
    public void setFlag(String key, Boolean value) { flags.put(key, value); }
    public Boolean getFlag(String key) { return flags.get(key); }

    // --- Other things ---

    public Map<String,Object> getOtherThings() { return other_things; }
    public void setOtherThings(Map<String,Object> other_things) {
        this.other_things = new ConcurrentHashMap<>(other_things);
        syncCustomRolesFromOtherThings();
    }
    public Object getOtherThing(String key) { return other_things.get(key); }
    public void setOtherThing(String key, Object value) { other_things.put(key, value); }

    // --- Custom Roles ---

    public List<String> getCustomRoles() { return customRoles; }
    public void setCustomRoles(List<String> customRoles) {
        this.customRoles = customRoles;
        other_things.put("customRoles", customRoles);
    }

    public void addCustomRole(String roleName) {
        if (!customRoles.contains(roleName)) customRoles.add(roleName);
        other_things.put("customRoles", customRoles);
    }

    public void removeCustomRole(String roleName) {
        customRoles.remove(roleName);
        other_things.put("customRoles", customRoles);
    }

    /**
     * Restores customRoles from other_things map.
     * Called automatically by setOtherThings().
     */
    private void syncCustomRolesFromOtherThings() {
        Object raw = other_things.get("customRoles");
        if (raw instanceof List<?> list) {
            customRoles = new ArrayList<>();
            for (Object o : list) {
                customRoles.add(String.valueOf(o));
            }
        }
    }

    public boolean hasCustomRole(String roleName) {
        return customRoles.contains(roleName);
    }

    /**
     * Gets all role names for this claim (defaults + custom), excluding OWNER.
     * Ordered: VISITOR, custom roles..., MEMBER, MODERATOR.
     *
     * @return An ordered list of role names.
     */
    public List<String> getAllRoles() {
        List<String> roles = new ArrayList<>();
        roles.add(ClaimRole.VISITOR.name());
        roles.addAll(customRoles);
        roles.add(ClaimRole.MEMBER.name());
        roles.add(ClaimRole.MODERATOR.name());
        return roles;
    }

    /**
     * Gets the next role in the cycling order (for GUI).
     *
     * @param currentRole The current role name.
     * @return The next role name.
     */
    public String getNextRole(String currentRole) {
        List<String> roles = getAllRoles();
        int idx = roles.indexOf(currentRole);
        if (idx == -1) return ClaimRole.VISITOR.name();
        return roles.get((idx + 1) % roles.size());
    }

}
