package fr.xyness.SimpleClaimSystem.Types;

import org.bukkit.Chunk;
import org.bukkit.World;

import java.util.Objects;
import java.util.UUID;

/**
 * ChunkKey object.
 */
public class ChunkKey {


    // ***************
    // *  Variables  *
    // ***************


	// UUID of the world of the chunk.
    private final UUID worldId;

    // X of the chunk.
    private final int x;

    // Z of the chunk.
    private final int z;


    // ******************
    // *  Constructors  *
    // ******************


    /**
     * Main constructor.
     *
     * @param worldId The UUID of the world of the chunk.
     * @param x The X of the chunk.
     * @param z The Z of the chunk.
     */
    public ChunkKey(UUID worldId, int x, int z) {
        this.worldId = worldId;
        this.x = x;
        this.z = z;
    }

    /**
     * Second constructor.
     *
     * @param world The world of the chunk.
     * @param x The X of the chunk.
     * @param z The Z of the chunk.
     */
    public ChunkKey(World world, int x, int z) {
        this(world.getUID(), x, z);
    }

    /**
     * Third constructor.
     *
     * @param chunk The chunk.
     */
    public ChunkKey(Chunk chunk) {
        this(chunk.getWorld().getUID(), chunk.getX(), chunk.getZ());
    }


    // *************
    // *  Methods  *
    // *************


    /**
     * Gets the world UUID of the chunk.
     *
     * @return The world UUID of the chunk.
     */
    public UUID getWorldId() {
        return worldId;
    }

    /**
     * Gets the X of the chunk.
     *
     * @return The X of the chunk.
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the Z of the chunk.
     *
     * @return The Z of the chunk.
     */
    public int getZ() {
        return z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChunkKey)) return false;
        ChunkKey key = (ChunkKey) o;
        return x == key.x && z == key.z && worldId.equals(key.worldId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(worldId, x, z);
    }

    @Override
    public String toString() {
        return "ChunkKey{" + "worldId=" + worldId + ", x=" + x + ", z=" + z + '}';
    }
}
