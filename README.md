# SimpleClaimSystem API

Public API for [SimpleClaimSystem2](https://builtbybit.com/resources/simpleclaimsystem.92437/) — a chunk-based land claiming plugin for Minecraft servers (Spigot/Paper/Folia).

This module allows developers to integrate with SimpleClaimSystem from their own plugins.

## Installation

### Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.Xyness</groupId>
        <artifactId>SimpleClaimSystem-API</artifactId>
        <version>v2.1.1</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

### Gradle (Groovy)

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    compileOnly 'com.github.Xyness:SimpleClaimSystem-API:v2.1.1'
}
```

### Gradle (Kotlin DSL)

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.github.Xyness:SimpleClaimSystem-API:v2.1.1")
}
```

## plugin.yml

Add SimpleClaimSystem as a dependency in your `plugin.yml`:

```yaml
depend: [SimpleClaimSystem]
# or
softdepend: [SimpleClaimSystem]
```

## Usage

### Getting the API instance

```java
import fr.xyness.SimpleClaimSystem.API.SCS_API;
import fr.xyness.SimpleClaimSystem.API.SCS_API_Provider;

// Check if the API is available
if (SCS_API_Provider.isRegistered()) {
    SCS_API api = SCS_API_Provider.get();
}
```

### Get a claim from a chunk

```java
import fr.xyness.SimpleClaimSystem.Types.Claim;

// Synchronous
Optional<Claim> claim = api.getClaim(player.getLocation().getChunk());
claim.ifPresent(c -> {
    String owner = c.getOwnerName();
    String name = c.getClaimName();
    boolean canBuild = c.getPermission(c.getRole(player.getUniqueId()), "build");
});

// Asynchronous
api.getClaimAsync(chunk).thenAccept(opt -> {
    opt.ifPresent(c -> {
        // Handle claim
    });
});
```

### Get player data

```java
import fr.xyness.SimpleClaimSystem.Types.PlayerData;

Optional<PlayerData> data = api.getPlayer(player.getUniqueId());
data.ifPresent(pd -> {
    String name = pd.getName();
    Map<String, Object> settings = pd.getSettings();
});
```

### Check world mode

```java
import fr.xyness.SimpleClaimSystem.Enums.WorldMode;

WorldMode mode = api.getWorldMode(world);
if (mode == WorldMode.PROTECTED) {
    // World is in protected mode
}
```

### Check claim membership and roles

```java
import fr.xyness.SimpleClaimSystem.Enums.ClaimRole;

Optional<Claim> opt = api.getClaim(chunk);
opt.ifPresent(claim -> {
    UUID playerId = player.getUniqueId();

    // Get role (returns VISITOR if not a member)
    ClaimRole role = claim.getRole(playerId);

    // Check membership
    boolean isMember = claim.isMember(playerId);

    // Check specific permission
    boolean canInteract = claim.getPermission(role, "interact_chest");

    // Check if banned
    boolean isBanned = claim.isBanned(playerId);

    // Get flags
    boolean explosions = claim.getFlag("creeper_explosions");
});
```

### Quick checks (no need to get the full Claim object)

```java
// Is this location claimed?
boolean claimed = api.isClaimed(player.getLocation());

// Can this player build here?
boolean canBuild = api.hasPermission(chunk, playerId, "build");

// Is this player banned from this chunk's claim?
boolean banned = api.isBanned(chunk, playerId);

// Get player's role in this chunk
ClaimRole role = api.getRole(chunk, playerId);

// Check a flag
Boolean pvp = api.getFlag(chunk, "pvp");
```

### Manage members (Factions/Kingdoms integration)

```java
// Add a faction member to all claims owned by the leader
api.addMemberToAllClaims(leaderUuid, newMemberUuid, ClaimRole.MEMBER);

// Remove a member who left the faction from all claims
api.removeMemberFromAllClaims(leaderUuid, leavingMemberUuid);

// Add a member to a specific claim
Optional<Claim> opt = api.getClaimByOwnerAndName(ownerUuid, "My Base");
opt.ifPresent(claim -> {
    api.addMember(claim, memberUuid, ClaimRole.MEMBER);
});

// Promote a member to moderator
api.setMemberRole(claim, memberUuid, ClaimRole.MODERATOR);
```

### Manage bans

```java
// Ban a player from a claim for 7 days
api.banPlayer(claim, enemyUuid, LocalDateTime.now().plusDays(7));

// Unban a player
api.unbanPlayer(claim, playerUuid);
```

### Modify permissions and flags

```java
// Allow members to build in a claim
api.setPermission(claim, ClaimRole.MEMBER, "build", true);

// Disable PvP in a claim
api.setFlag(claim, "pvp", false);
```

### Player limits and statistics

```java
// Limits
int maxClaims = api.getMaxClaims(player);
int maxChunks = api.getMaxChunks(player);
double cost = api.getChunkCost(player);

// Current counts
int claimCount = api.getClaimCount(player.getUniqueId());
int chunkCount = api.getChunkCount(player.getUniqueId());
```

### Query claims by owner

```java
// Get all claims for a player
List<Claim> claims = api.getClaimsByOwner(ownerUuid);

// Get claim names only
List<String> names = api.getClaimNamesByOwner(ownerUuid);

// Find a specific claim
Optional<Claim> claim = api.getClaimByOwnerAndName(ownerUuid, "My Base");

// Get members/banned of a claim
Map<UUID, ClaimRole> members = api.getClaimMembers(ownerUuid, "My Base");
Map<UUID, LocalDateTime> banned = api.getClaimBanned(ownerUuid, "My Base");
```

### Plugin settings

```java
boolean economy = api.getBooleanSetting("claims.economy.enabled", false);
String lang = api.getStringSetting("lang", "en_US");
```

## API Reference

### Claim Queries

| Method | Sync/Async | Description |
|--------|-----------|-------------|
| `getClaim(Chunk)` | Sync | Get a claim by chunk |
| `getClaim(ChunkKey)` | Sync | Get a claim by chunk key |
| `getClaimAsync(Chunk)` | Async | Get a claim by chunk |
| `getClaimAsync(ChunkKey)` | Async | Get a claim by chunk key |
| `getClaims(Set<ChunkKey>)` | Sync | Bulk get multiple claims |
| `getClaimByOwnerAndName(UUID, String)` | Sync | Find claim by owner + name |
| `getClaimsByOwner(UUID)` | Sync | Get all claims by owner |
| `getClaimsByOwnerAsync(UUID)` | Async | Get all claims by owner (full data from DB) |
| `getClaimNamesByOwner(UUID)` | Sync | Get claim names by owner |
| `getClaimMembers(UUID, String)` | Sync | Get members of a claim |
| `getClaimBanned(UUID, String)` | Sync | Get banned players of a claim |

### Claim Modification

| Method | Description |
|--------|-------------|
| `addMember(Claim, UUID, ClaimRole)` | Add a member to a claim |
| `addMemberToAllClaims(UUID, UUID, ClaimRole)` | Add a member to all claims of an owner |
| `removeMember(Claim, UUID)` | Remove a member from a claim |
| `removeMemberFromAllClaims(UUID, UUID)` | Remove a member from all claims of an owner |
| `setMemberRole(Claim, UUID, ClaimRole)` | Change a member's role |
| `banPlayer(Claim, UUID, LocalDateTime)` | Ban a player from a claim |
| `unbanPlayer(Claim, UUID)` | Unban a player from a claim |
| `setPermission(Claim, ClaimRole, String, boolean)` | Set a permission for a role |
| `setFlag(Claim, String, boolean)` | Set a flag value |

### Claim Checks

| Method | Description |
|--------|-------------|
| `isClaimed(ChunkKey)` | Check if chunk is claimed |
| `isClaimed(Chunk)` | Check if chunk is claimed |
| `isClaimed(Location)` | Check if location is in a claim |
| `hasPermission(Chunk, UUID, String)` | Check permission in claim |
| `isBanned(Chunk, UUID)` | Check if player is banned |
| `isMember(Chunk, UUID)` | Check if player is a member |
| `getRole(Chunk, UUID)` | Get player's role in claim |
| `getFlag(Chunk, String)` | Get flag value in claim |

### Player Queries

| Method | Sync/Async | Description |
|--------|-----------|-------------|
| `getPlayer(UUID)` | Sync | Get player data |
| `getPlayerAsync(UUID)` | Async | Get player data |
| `getPlayerByNameAsync(String)` | Async | Get player data by name |
| `getPlayersAsync(List<UUID>)` | Async | Bulk get player data |
| `getPlayerNamesAsync(List<UUID>)` | Async | Get names for UUIDs |

### Player Limits & Stats

| Method | Description |
|--------|-------------|
| `getMaxClaims(Player)` | Max claims allowed |
| `getMaxChunks(Player)` | Max chunks allowed |
| `getMaxRadius(Player)` | Max claim radius |
| `getMaxMembers(Player)` | Max members per claim |
| `getChunkCost(Player)` | Cost per chunk |
| `getCostMultiplier(Player)` | Cost multiplier |
| `getChunkCount(UUID)` | Current chunk count |
| `getClaimCount(UUID)` | Current claim count |

### World & Settings

| Method | Description |
|--------|-------------|
| `getWorldMode(World)` | Get world mode |
| `getSettingsForProtectedMode()` | Default permissions for Protected mode |
| `getSettingsForSurvivalRequiringClaimsMode()` | Default permissions for SRC mode |
| `getSetting(String)` | Get raw setting value |
| `getBooleanSetting(String, boolean)` | Get boolean setting |
| `getStringSetting(String, String)` | Get string setting |
| `getIntSetting(String, int)` | Get integer setting |
| `getDoubleSetting(String, double)` | Get double setting |

## Types

| Class | Description |
|-------|-------------|
| `Claim` | Represents a land claim with chunks, members, permissions, flags |
| `PlayerData` | Player profile with UUID, name, texture, settings |
| `ChunkKey` | Immutable chunk identifier (worldId, x, z) |
| `ClaimRole` | Enum: `VISITOR`, `MEMBER`, `MODERATOR`, `OWNER` |
| `WorldMode` | Enum: `SURVIVAL`, `SURVIVAL_REQUIRING_CLAIMS`, `PROTECTED`, `DISABLED` |

## Requirements

- Java 21+
- Spigot/Paper/Folia 1.21+
- SimpleClaimSystem plugin installed on the server

## Links

- [BuiltByBit](https://builtbybit.com/resources/simpleclaimsystem.92437/)
- [Javadoc](https://javadoc.jitpack.io/com/github/Xyness/SimpleClaimSystem-API/v2.1.1/javadoc/)

## License

This API is provided for integration purposes. SimpleClaimSystem is a premium plugin by Xyness.
