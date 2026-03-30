package fr.xyness.SimpleClaimSystem.Types;

import java.time.LocalDateTime;
import java.util.*;

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
    private UUID ownerUuid;

    // Name of the owner of the claim.
    private String ownerName;

    // Name of the claim.
    private String claimName;

    // Description of the claim.
    private String description;

    // Chunks of the claim.
    private Set<ChunkKey> chunks = new HashSet<>();

    // UUID of the world of the claim.
    private String worldUuid;

    // Spawn location of the claim.
    private Location spawnLocation;

    // Members map (UUID -> role).
    private Map<UUID, ClaimRole> members = new HashMap<>();

    // Banned players map (UUID -> Unban LocalDateTime).
    private Map<UUID, LocalDateTime> banned = new HashMap<>();

    // Settings map (ClaimRole -> (Setting,Value)).
    private Map<ClaimRole,Map<String, Boolean>> permissions = new HashMap<>();

    // Flags map (Flags -> Value).
    private Map<String, Boolean> flags = new HashMap<>();

    // Other things (Thing -> Value).
    private Map<String, Object> other_things = new HashMap<>();


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
                 Location spawnLocation, Map<UUID, ClaimRole> members, Map<UUID, LocalDateTime> banned, Map<ClaimRole,Map<String, Boolean>> permissions,
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

    public Map<UUID, ClaimRole> getMembers() { return members; }
    public void setMembers(Map<UUID, ClaimRole> members) { this.members = members; }
    public void addMember(UUID uuid, ClaimRole role) { members.put(uuid, role); }
    public void removeMember(UUID uuid) { members.remove(uuid); }

    public ClaimRole getRole(UUID uuid) {
        return members.getOrDefault(uuid, ClaimRole.VISITOR);
    }

    public boolean isMember(UUID uuid) { return members.containsKey(uuid); }

    public Map<UUID, LocalDateTime> getBanned() { return banned; }
    public LocalDateTime getBanTime(UUID banUuid) { return banned.get(banUuid); }
    public void setBanned(Map<UUID, LocalDateTime> banned) { this.banned = banned; }
    public void banPlayer(UUID uuid, LocalDateTime date) { banned.put(uuid, date); }
    public void unbanPlayer(UUID uuid) { banned.remove(uuid); }

    public boolean isBanned(UUID uuid) {
    	LocalDateTime date = banned.get(uuid);
        return date != null && LocalDateTime.now().isBefore(date);
    }

    public Map<ClaimRole,Map<String, Boolean>> getPermissions() { return permissions; }
    public void setPermissions(Map<ClaimRole,Map<String, Boolean>> permissions) { this.permissions = permissions; }

    public void setPermission(ClaimRole role, String key, Boolean value) {
        permissions.get(role).put(key, value);
    }

    public Boolean getPermission(ClaimRole role, String key) {
    	if(role == ClaimRole.OWNER) return true;
        return permissions.get(role).getOrDefault(key,false);
    }

    public Map<String,Boolean> getFlags() { return flags; }
    public void setFlags(Map<String,Boolean> flags) { this.flags = flags; }
    public void setFlag(String key, Boolean value) { flags.put(key, value); }
    public Boolean getFlag(String key) { return flags.get(key); }

    public Map<String,Object> getOtherThings() { return other_things; }
    public void setOtherThings(Map<String,Object> other_things) { this.other_things = other_things; }
    public Object getOtherThing(String key) { return other_things.get(key); }
    public void setOtherThing(String key, Object value) { other_things.put(key, value); }

}
