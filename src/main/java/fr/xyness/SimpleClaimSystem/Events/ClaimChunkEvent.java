package fr.xyness.SimpleClaimSystem.Events;

import org.bukkit.event.HandlerList;

import fr.xyness.SimpleClaimSystem.Types.ChunkKey;
import fr.xyness.SimpleClaimSystem.Types.Claim;

import java.util.UUID;

/**
 * Called when a chunk is added to or removed from a claim.
 */
public class ClaimChunkEvent extends ClaimEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    /** The type of chunk action. */
    public enum Action { ADD, REMOVE }

    private final ChunkKey chunkKey;
    private final Action action;
    private final UUID playerId;

    public ClaimChunkEvent(Claim claim, ChunkKey chunkKey, Action action, UUID playerId) {
        super(claim);
        this.chunkKey = chunkKey;
        this.action = action;
        this.playerId = playerId;
    }

    /** Gets the chunk key involved. */
    public ChunkKey getChunkKey() { return chunkKey; }

    /** Gets the action type. */
    public Action getAction() { return action; }

    /** Gets the UUID of the player who performed the action. */
    public UUID getPlayerId() { return playerId; }

    @Override public HandlerList getHandlers() { return HANDLERS; }
    public static HandlerList getHandlerList() { return HANDLERS; }
}
