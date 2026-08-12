package fr.xyness.SimpleClaimSystem.Events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * Called once per owner and per tax run, before the money is taken.
 *
 * <p>The amount is mutable: a listener can apply its own discount (rank, event, temporary
 * exemption) by calling {@link #setAmount(double)}, or set it to {@code 0}. Cancelling the event
 * skips the charge entirely for that run and leaves the owner's tax meter untouched, so the same
 * periods are billed again on the next run.</p>
 *
 * <p>Fired off the main thread — the tax collector runs asynchronously.</p>
 */
public class ClaimTaxEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private final UUID ownerUuid;
    private final int chunks;
    private final long periods;
    private final double baseAmount;
    private double amount;
    private boolean cancelled;

    /**
     * @param ownerUuid  The claim owner being charged.
     * @param chunks     The number of chunks owned.
     * @param periods    The number of whole taxation periods being billed.
     * @param amount     The amount about to be charged.
     */
    public ClaimTaxEvent(UUID ownerUuid, int chunks, long periods, double amount) {
        super(true);
        this.ownerUuid = ownerUuid;
        this.chunks = chunks;
        this.periods = periods;
        this.baseAmount = amount;
        this.amount = amount;
    }

    /** Gets the owner being charged. */
    public UUID getOwnerUuid() { return ownerUuid; }

    /** Gets the number of chunks the owner holds. */
    public int getChunks() { return chunks; }

    /** Gets how many whole taxation periods are being billed at once. */
    public long getPeriods() { return periods; }

    /** Gets the amount computed by the plugin, before any listener changed it. */
    public double getBaseAmount() { return baseAmount; }

    /** Gets the amount that will be charged. */
    public double getAmount() { return amount; }

    /**
     * Overrides the amount to charge. Negative values are clamped to zero.
     *
     * @param amount The new amount.
     */
    public void setAmount(double amount) { this.amount = Math.max(0, amount); }

    @Override public boolean isCancelled() { return cancelled; }
    @Override public void setCancelled(boolean cancel) { this.cancelled = cancel; }
    @Override public HandlerList getHandlers() { return HANDLERS; }
    public static HandlerList getHandlerList() { return HANDLERS; }
}
