package fr.xyness.SimpleClaimSystem.Types;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

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
    private volatile List<String> customRoles = new CopyOnWriteArrayList<>();


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

    /** @return The DB id of the claim. */
    public int getId() { return id; }

    /** @param id The DB id to set. */
    public void setId(int id) { this.id = id; }

    /** @return The owner's UUID. */
    public UUID getOwnerUuid() { return ownerUuid; }

    /** @param ownerUuid The new owner's UUID. */
    public void setOwnerUuid(UUID ownerUuid) { this.ownerUuid = ownerUuid; }

    /** @return The owner's last known name. */
    public String getOwnerName() { return ownerName; }

    /** @param ownerName The new owner name. */
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    /** @return The claim name. */
    public String getClaimName() { return claimName; }

    /** @param claimName The new claim name. */
    public void setClaimName(String claimName) { this.claimName = claimName; }

    /** @return The claim description, possibly empty. */
    public String getDescription() { return description; }

    /** @param description The new description. */
    public void setDescription(String description) { this.description = description; }

    /**
     * @return An unmodifiable view of the chunk keys that make up the claim. Mutations must go
     *         through {@link #addChunk}, {@link #addChunks}, or {@link #removeChunk}; calling
     *         remove/add on the returned set throws {@link UnsupportedOperationException}.
     */
    public Set<ChunkKey> getChunks() { return Collections.unmodifiableSet(chunks); }

    /** @return The internal mutable chunk set. INTERNAL — do not expose to API consumers. */
    public Set<ChunkKey> getChunksMutable() { return chunks; }

    /** @param chunks The new chunk set. */
    public void setChunks(Set<ChunkKey> chunks) { this.chunks = chunks; }

    /** @param key The chunk to add. */
    public void addChunk(ChunkKey key) { chunks.add(key); }

    /** @param chunks The chunks to add in bulk. */
    public void addChunks(Set<ChunkKey> chunks) { this.chunks.addAll(chunks); }

    /** @param key The chunk to remove. */
    public void removeChunk(ChunkKey key) { chunks.remove(key); }

    /**
     * Tells whether the claim contains the given chunk.
     *
     * @param key The chunk key.
     * @return true if the chunk is part of this claim.
     */
    public boolean containsChunk(ChunkKey key) { return chunks.contains(key); }

    /** @return The world UUID (as a string). */
    public String getWorldUuid() { return worldUuid; }

    /** @param worldUuid The new world UUID. */
    public void setWorldName(String worldUuid) { this.worldUuid = worldUuid; }

    /** @return The claim's teleport spawn location. */
    public Location getSpawnLocation() { return spawnLocation; }

    /** @param spawnLocation The new teleport spawn location. */
    public void setSpawnLocation(Location spawnLocation) { this.spawnLocation = spawnLocation; }


    // *************
    // *  Members  *
    // *************


    /**
     * @return An unmodifiable view of the members map (UUID -> role name). Mutations must go
     *         through {@link #addMember}, {@link #removeMember}, or related helpers.
     */
    public Map<UUID, String> getMembers() { return Collections.unmodifiableMap(members); }

    /** @return The internal mutable members map. INTERNAL — do not expose to API consumers. */
    public Map<UUID, String> getMembersMutable() { return members; }

    /** @param members The new members map. */
    public void setMembers(Map<UUID, String> members) { this.members = members; }

    /**
     * Adds a permanent member to the claim.
     *
     * @param uuid The member's UUID.
     * @param role The role name.
     */
    public void addMember(UUID uuid, String role) { members.put(uuid, role); }

    /**
     * Removes a member. Clears the parallel expiration entry to avoid stale data.
     *
     * @param uuid The member's UUID.
     */
    public void removeMember(UUID uuid) {
        members.remove(uuid);
        memberExpirations.remove(uuid);
    }

    /**
     * Adds a permanent member using the ClaimRole enum (convenience).
     *
     * @param uuid The member's UUID.
     * @param role The role enum.
     */
    public void addMember(UUID uuid, ClaimRole role) { members.put(uuid, role.name()); }

    /**
     * Adds a member with an optional expiration timestamp. Null expiresAt is equivalent to
     * the permanent {@link #addMember(UUID, String)} variant.
     *
     * @param uuid The member's UUID.
     * @param role The role name.
     * @param expiresAt The expiry timestamp, or null for permanent.
     */
    public void addMember(UUID uuid, String role, LocalDateTime expiresAt) {
        members.put(uuid, role);
        if (expiresAt == null) memberExpirations.remove(uuid);
        else memberExpirations.put(uuid, expiresAt);
    }

    /**
     * @return An unmodifiable view of the member expiration map. Missing keys = permanent
     *         membership. Mutations must go through {@link #addMember(UUID, String, LocalDateTime)}
     *         or {@link #removeMember}.
     */
    public Map<UUID, LocalDateTime> getMemberExpirations() { return Collections.unmodifiableMap(memberExpirations); }

    /** @return The internal mutable expirations map. INTERNAL — do not expose to API consumers. */
    public Map<UUID, LocalDateTime> getMemberExpirationsMutable() { return memberExpirations; }

    /** @param memberExpirations The new expiration map; null resets to an empty map. */
    public void setMemberExpirations(Map<UUID, LocalDateTime> memberExpirations) {
        this.memberExpirations = memberExpirations == null ? new ConcurrentHashMap<>() : memberExpirations;
    }

    /**
     * Returns the expiration timestamp for a member.
     *
     * @param uuid The member's UUID.
     * @return The expiry timestamp, or null if the membership is permanent or the player is not a member.
     */
    public LocalDateTime getMemberExpiresAt(UUID uuid) { return memberExpirations.get(uuid); }

    /**
     * Tells whether a member has a bounded (temporary) membership.
     *
     * @param uuid The member's UUID.
     * @return true if the member has an expiry set.
     */
    public boolean isTemporaryMember(UUID uuid) { return memberExpirations.containsKey(uuid); }

    /**
     * Returns the role name for the given player, or VISITOR if they are not a member.
     *
     * @param uuid The player's UUID.
     * @return The role name (default, custom, or VISITOR fallback).
     */
    public String getRole(UUID uuid) {
        return members.getOrDefault(uuid, ClaimRole.VISITOR.name());
    }

    /**
     * Returns the role as a ClaimRole enum, falling back to VISITOR for custom roles.
     *
     * @param uuid The player's UUID.
     * @return The enum value.
     */
    public ClaimRole getRoleEnum(UUID uuid) {
        String role = getRole(uuid);
        try {
            return ClaimRole.valueOf(role);
        } catch (IllegalArgumentException e) {
            return ClaimRole.VISITOR;
        }
    }

    /**
     * Tells whether the given player is a member of the claim.
     *
     * @param uuid The player's UUID.
     * @return true if the player is a member.
     */
    public boolean isMember(UUID uuid) { return members.containsKey(uuid); }


    // ************
    // *  Banned  *
    // ************


    /**
     * @return An unmodifiable view of the banned map. Mutations must go through
     *         {@link #banPlayer} or {@link #unbanPlayer}.
     */
    public Map<UUID, LocalDateTime> getBanned() { return Collections.unmodifiableMap(banned); }

    /** @return The internal mutable banned map. INTERNAL — do not expose to API consumers. */
    public Map<UUID, LocalDateTime> getBannedMutable() { return banned; }

    /**
     * Returns the ban expiry for a given UUID.
     *
     * @param banUuid The banned player's UUID.
     * @return The expiry timestamp, or null if not banned.
     */
    public LocalDateTime getBanTime(UUID banUuid) { return banned.get(banUuid); }

    /** @param banned The new banned map. */
    public void setBanned(Map<UUID, LocalDateTime> banned) { this.banned = banned; }

    /**
     * Bans a player until the given date.
     *
     * @param uuid The banned player's UUID.
     * @param date The ban expiry timestamp.
     */
    public void banPlayer(UUID uuid, LocalDateTime date) { banned.put(uuid, date); }

    /**
     * Unbans a player.
     *
     * @param uuid The banned player's UUID.
     */
    public void unbanPlayer(UUID uuid) { banned.remove(uuid); }

    /**
     * Tells whether a player is currently banned (expiry in the future).
     *
     * @param uuid The player's UUID.
     * @return true if banned.
     */
    public boolean isBanned(UUID uuid) {
    	LocalDateTime date = banned.get(uuid);
        return date != null && LocalDateTime.now().isBefore(date);
    }


    // *****************
    // *  Permissions  *
    // *****************


    /**
     * @return An unmodifiable view of the permissions map (role name -> permission map). The
     *         inner role maps are also unmodifiable. Mutations must go through
     *         {@link #setPermission}.
     */
    public Map<String, Map<String, Boolean>> getPermissions() {
        Map<String, Map<String, Boolean>> view = new java.util.HashMap<>();
        permissions.forEach((role, perms) -> view.put(role, Collections.unmodifiableMap(perms)));
        return Collections.unmodifiableMap(view);
    }

    /** @return The internal mutable permissions map. INTERNAL — do not expose to API consumers. */
    public Map<String, Map<String, Boolean>> getPermissionsMutable() { return permissions; }

    /**
     * Replaces the permission map. The input is defensively copied into thread-safe maps.
     *
     * @param permissions The new permissions map.
     */
    public void setPermissions(Map<String, Map<String, Boolean>> permissions) {
        this.permissions = new ConcurrentHashMap<>();
        permissions.forEach((k, v) -> this.permissions.put(k, new ConcurrentHashMap<>(v)));
    }

    /**
     * Sets a single permission value on a role.
     *
     * @param role The role name.
     * @param key The permission key.
     * @param value The permission value.
     */
    public void setPermission(String role, String key, Boolean value) {
        Map<String, Boolean> rolePerms = permissions.get(role);
        if (rolePerms != null) rolePerms.put(key, value);
    }

    /**
     * Returns the permission value for a role. OWNER is always allowed.
     *
     * @param role The role name.
     * @param key The permission key.
     * @return true if allowed, false otherwise.
     */
    public Boolean getPermission(String role, String key) {
    	if (ClaimRole.OWNER.name().equals(role)) return true;
        Map<String, Boolean> rolePerms = permissions.get(role);
        if (rolePerms == null) return false;
        return rolePerms.getOrDefault(key, false);
    }

    /**
     * Sets a permission value using the ClaimRole enum (convenience).
     *
     * @param role The role enum.
     * @param key The permission key.
     * @param value The permission value.
     */
    public void setPermission(ClaimRole role, String key, Boolean value) {
        setPermission(role.name(), key, value);
    }

    /**
     * Returns the permission value using the ClaimRole enum (convenience).
     *
     * @param role The role enum.
     * @param key The permission key.
     * @return true if allowed.
     */
    public Boolean getPermission(ClaimRole role, String key) {
        return getPermission(role.name(), key);
    }


    // ***********
    // *  Flags  *
    // ***********


    /**
     * @return An unmodifiable view of the flags map. Mutations must go through {@link #setFlag}.
     */
    public Map<String,Boolean> getFlags() { return Collections.unmodifiableMap(flags); }

    /** @return The internal mutable flags map. INTERNAL — do not expose to API consumers. */
    public Map<String,Boolean> getFlagsMutable() { return flags; }

    /**
     * Replaces the flags map with a defensive thread-safe copy.
     *
     * @param flags The new flags map.
     */
    public void setFlags(Map<String,Boolean> flags) { this.flags = new ConcurrentHashMap<>(flags); }

    /**
     * Sets a single flag value.
     *
     * @param key The flag key.
     * @param value The flag value.
     */
    public void setFlag(String key, Boolean value) { flags.put(key, value); }

    /**
     * Returns a flag value, or null if unset.
     *
     * @param key The flag key.
     * @return The flag value.
     */
    public Boolean getFlag(String key) { return flags.get(key); }


    // ******************
    // *  Other things  *
    // ******************


    /**
     * @return An unmodifiable view of the miscellaneous claim-level metadata map. Mutations must
     *         go through {@link #setOtherThing}.
     */
    public Map<String,Object> getOtherThings() { return Collections.unmodifiableMap(other_things); }

    /** @return The internal mutable metadata map. INTERNAL — do not expose to API consumers. */
    public Map<String,Object> getOtherThingsMutable() { return other_things; }

    /**
     * Replaces the metadata map. Custom roles are re-read from the map to stay in sync.
     *
     * @param other_things The new metadata map.
     */
    public void setOtherThings(Map<String,Object> other_things) {
        this.other_things = new ConcurrentHashMap<>(other_things);
        syncCustomRolesFromOtherThings();
    }

    /**
     * Looks up a metadata entry by key.
     *
     * @param key The key.
     * @return The value, or null if unset.
     */
    public Object getOtherThing(String key) { return other_things.get(key); }

    /**
     * Sets or replaces a metadata entry.
     *
     * @param key The key.
     * @param value The value.
     */
    public void setOtherThing(String key, Object value) { other_things.put(key, value); }


    // ******************
    // *  Custom roles  *
    // ******************


    /**
     * @return An unmodifiable view of the custom role names list. Mutations must go through
     *         {@link #addCustomRole} / {@link #removeCustomRole} / {@link #setCustomRoles}.
     */
    public List<String> getCustomRoles() { return Collections.unmodifiableList(customRoles); }

    /** @return The internal mutable custom roles list. INTERNAL — do not expose to API consumers. */
    public List<String> getCustomRolesMutable() { return customRoles; }

    /**
     * Replaces the custom role list. Always wraps into a thread-safe list because callers
     * may pass an ArrayList from JSON deserialisation, which would expose us to CME on
     * concurrent reads from the cache.
     *
     * @param customRoles The new list.
     */
    public void setCustomRoles(List<String> customRoles) {
        this.customRoles = new CopyOnWriteArrayList<>(customRoles);
        other_things.put("customRoles", this.customRoles);
    }

    /**
     * Adds a custom role (no-op if already present).
     *
     * @param roleName The role name.
     */
    public void addCustomRole(String roleName) {
        if (!customRoles.contains(roleName)) customRoles.add(roleName);
        other_things.put("customRoles", customRoles);
    }

    /**
     * Removes a custom role.
     *
     * @param roleName The role name.
     */
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
            CopyOnWriteArrayList<String> rebuilt = new CopyOnWriteArrayList<>();
            for (Object o : list) {
                rebuilt.add(String.valueOf(o));
            }
            customRoles = rebuilt;
        }
    }

    /**
     * Tells whether a custom role with the given name exists on the claim.
     *
     * @param roleName The role name.
     * @return true if the role is a custom role of this claim.
     */
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
