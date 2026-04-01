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
        <version>v2.1.4</version>
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
    compileOnly 'com.github.Xyness:SimpleClaimSystem-API:v2.1.4'
}
```

### Gradle (Kotlin DSL)

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.github.Xyness:SimpleClaimSystem-API:v2.1.4")
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

    // Get role (returns "VISITOR" if not a member)
    String role = claim.getRole(playerId);

    // Check membership
    boolean isMember = claim.isMember(playerId);

    // Check specific permission (works with default and custom roles)
    boolean canInteract = claim.getPermission(role, "interact_chest");

    // Check if banned
    boolean isBanned = claim.isBanned(playerId);

    // Get flags
    boolean explosions = claim.getFlag("creeper_explosions");

    // Get all available roles (defaults + custom)
    List<String> allRoles = claim.getAllRoles();

    // Get custom roles only
    List<String> customRoles = claim.getCustomRoles();

    // Check if a role is a default role
    boolean isDefault = ClaimRole.isDefault(role);
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

// Get player's role in this chunk (returns String)
String role = api.getRole(chunk, playerId);

// Check a flag
Boolean pvp = api.getFlag(chunk, "pvp");
```

### Manage members (Factions/Kingdoms integration)

```java
// Add a faction member to all claims owned by the leader
api.addMemberToAllClaims(leaderUuid, newMemberUuid, "MEMBER");

// Remove a member who left the faction from all claims
api.removeMemberFromAllClaims(leaderUuid, leavingMemberUuid);

// Add a member to a specific claim
Optional<Claim> opt = api.getClaimByOwnerAndName(ownerUuid, "My Base");
opt.ifPresent(claim -> {
    api.addMember(claim, memberUuid, "MEMBER");
});

// Set a member's role (works with default and custom roles)
api.setMemberRole(claim, memberUuid, "MODERATOR");

// Set a custom role
api.setMemberRole(claim, memberUuid, "BUILDER");
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
api.setPermission(claim, "MEMBER", "build", true);

// Set permission for a custom role
api.setPermission(claim, "BUILDER", "place_block", true);

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
Map<UUID, String> members = api.getClaimMembers(ownerUuid, "My Base");
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
| `addMember(Claim, UUID, String)` | Add a member to a claim with a role name |
| `addMemberToAllClaims(UUID, UUID, String)` | Add a member to all claims of an owner |
| `removeMember(Claim, UUID)` | Remove a member from a claim |
| `removeMemberFromAllClaims(UUID, UUID)` | Remove a member from all claims of an owner |
| `setMemberRole(Claim, UUID, String)` | Change a member's role |
| `banPlayer(Claim, UUID, LocalDateTime)` | Ban a player from a claim |
| `unbanPlayer(Claim, UUID)` | Unban a player from a claim |
| `setPermission(Claim, String, String, boolean)` | Set a permission for a role |
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
| `getRole(Chunk, UUID)` | Get player's role name in claim |
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

## Roles

SimpleClaimSystem supports **custom roles** in addition to the 4 default roles.

### Default Roles

| Role | Description |
|------|-------------|
| `VISITOR` | Non-member, minimal permissions |
| `MEMBER` | Standard member |
| `MODERATOR` | Advanced member with management permissions |
| `OWNER` | Claim owner, full permissions (always returns `true`) |

### Custom Roles

Claim owners can create custom roles per claim with their own set of permissions. Custom roles are placed between VISITOR and MEMBER in the hierarchy.

```java
// Roles are now String-based (not enum)
String role = claim.getRole(playerId);  // Returns "VISITOR", "MEMBER", "MODERATOR", "BUILDER", etc.

// Check if a role is a default role
boolean isDefault = ClaimRole.isDefault(role);  // true for VISITOR/MEMBER/MODERATOR/OWNER

// Get all roles for a claim (defaults + custom)
List<String> allRoles = claim.getAllRoles();  // ["VISITOR", "BUILDER", "GUARD", "MEMBER", "MODERATOR"]

// Get only custom roles
List<String> custom = claim.getCustomRoles();  // ["BUILDER", "GUARD"]

// Permissions work the same way for default and custom roles
boolean canBuild = claim.getPermission("BUILDER", "place_block");
```

### Migration Note

Since v2.1.4, roles use `String` instead of `ClaimRole` enum throughout the API. Backward-compatible methods accepting `ClaimRole` are still available on the `Claim` class (`addMember`, `setPermission`, `getPermission`).

## Events

SimpleClaimSystem fires custom Bukkit events for all claim actions. Listen to them in your plugin to react to claim changes.

### Available Events

| Event | Description | Cancellable |
|-------|-------------|:-----------:|
| `ClaimCreateEvent` | Fired when a claim is created | No |
| `ClaimDeleteEvent` | Fired when a claim is deleted | No |
| `ClaimExpireEvent` | Fired when a claim is auto-purged due to owner inactivity | No |
| `ClaimEnterEvent` | Fired when a player enters a claim | Yes |
| `ClaimLeaveEvent` | Fired when a player leaves a claim | No |
| `ClaimTeleportEvent` | Fired when a player teleports to a claim spawn | Yes |
| `ClaimMemberEvent` | Fired when a member is added, removed, kicked, banned, unbanned, promoted, demoted, or role changed | No |
| `ClaimOwnerTransferEvent` | Fired when claim ownership is transferred | No |
| `ClaimSaleEvent` | Fired when a claim is listed for sale, sale cancelled, or bought | No |
| `ClaimRenameEvent` | Fired when a claim is renamed | No |
| `ClaimDescriptionChangeEvent` | Fired when a claim description is changed | No |
| `ClaimSpawnChangeEvent` | Fired when a claim spawn location is changed | No |
| `ClaimFlagChangeEvent` | Fired when a claim flag is toggled | No |
| `ClaimPermissionChangeEvent` | Fired when a claim permission is changed | No |
| `ClaimChunkEvent` | Fired when a chunk is added to or removed from a claim | No |
| `ClaimMergeEvent` | Fired when multiple claims are merged | No |

### Listening to Events

```java
import fr.xyness.SimpleClaimSystem.Events.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MyListener implements Listener {

    @EventHandler
    public void onClaimCreate(ClaimCreateEvent event) {
        Claim claim = event.getClaim();
        UUID creator = event.getPlayerId();
        getLogger().info(claim.getOwnerName() + " created claim " + claim.getClaimName());
    }

    @EventHandler
    public void onClaimEnter(ClaimEnterEvent event) {
        Player player = event.getPlayer();
        Claim claim = event.getClaim();

        // Prevent entering if player is not allowed
        if (someCustomCheck(player, claim)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMemberAction(ClaimMemberEvent event) {
        // Use getRoleName() to get the String role (works for custom roles too)
        String role = event.getRoleName();

        switch (event.getAction()) {
            case ADD -> getLogger().info("Member added as " + role);
            case KICK -> getLogger().info("Member kicked from " + event.getClaim().getClaimName());
            case BAN -> getLogger().info("Player banned from " + event.getClaim().getClaimName());
            case PROMOTE -> getLogger().info("Member promoted to " + role);
            case DEMOTE -> getLogger().info("Member demoted to " + role);
            case ROLE_CHANGE -> getLogger().info("Member role changed to " + role);
        }
    }

    @EventHandler
    public void onPermissionChange(ClaimPermissionChangeEvent event) {
        // getRoleName() returns the String role (can be a custom role)
        String role = event.getRoleName();
        getLogger().info("Permission " + event.getPermission() + " set to " + event.getValue()
            + " for role " + role + " in " + event.getClaim().getClaimName());
    }

    @EventHandler
    public void onClaimSale(ClaimSaleEvent event) {
        switch (event.getAction()) {
            case LISTED -> getLogger().info(event.getClaim().getClaimName() + " listed for $" + event.getPrice());
            case BOUGHT -> getLogger().info(event.getPlayerId() + " bought " + event.getClaim().getClaimName());
            case CANCELLED -> getLogger().info("Sale cancelled for " + event.getClaim().getClaimName());
        }
    }
}
```

### ClaimMemberEvent Actions

| Action | Trigger |
|--------|---------|
| `ADD` | Member accepted an invitation |
| `REMOVE` | Member removed via API |
| `KICK` | Member kicked by owner/moderator or dashboard |
| `BAN` | Player banned from claim |
| `UNBAN` | Player unbanned from claim |
| `PROMOTE` | Member promoted |
| `DEMOTE` | Member demoted |
| `ROLE_CHANGE` | Member role changed via `/claim role set` |

### ClaimSaleEvent Actions

| Action | Trigger |
|--------|---------|
| `LISTED` | Claim put up for sale |
| `CANCELLED` | Sale cancelled |
| `BOUGHT` | Claim purchased by another player |

### ClaimChunkEvent Actions

| Action | Trigger |
|--------|---------|
| `ADD` | Chunk added to a claim |
| `REMOVE` | Chunk removed from a claim |

### Base Class

All events extend `ClaimEvent` which provides `getClaim()` to access the claim involved. Events are fired asynchronously (`async = true`).

## Types

| Class | Description |
|-------|-------------|
| `Claim` | Represents a land claim with chunks, members, permissions, flags, and custom roles |
| `PlayerData` | Player profile with UUID, name, texture, settings |
| `ChunkKey` | Immutable chunk identifier (worldId, x, z) |
| `ClaimRole` | Enum: `VISITOR`, `MEMBER`, `MODERATOR`, `OWNER` — default roles. Use `ClaimRole.isDefault(String)` to check |
| `WorldMode` | Enum: `SURVIVAL`, `SURVIVAL_REQUIRING_CLAIMS`, `PROTECTED`, `DISABLED` |

## Requirements

- Java 21+
- Spigot/Paper/Folia 1.21+
- SimpleClaimSystem plugin installed on the server

## Links

- [BuiltByBit](https://builtbybit.com/resources/simpleclaimsystem.92437/)
- [Javadoc](https://javadoc.jitpack.io/com/github/Xyness/SimpleClaimSystem-API/v2.1.4/javadoc/)

## License

This API is provided for integration purposes. SimpleClaimSystem is a premium plugin by Xyness.
