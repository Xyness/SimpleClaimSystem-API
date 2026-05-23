package fr.xyness.SimpleClaimSystem.API;

import java.util.Locale;
import java.util.Objects;

/**
 * Definition of a custom claim flag declared by an external plugin via
 * {@link SCS_FlagRegistry#registerFlag(FlagDefinition)}.
 *
 * <p><strong>Data-only:</strong> SimpleClaimSystem only stores the flag's value per claim and
 * exposes it through {@code claim.getFlag(key)}. The plugin author must wire their own
 * {@code @EventHandler}s that check the flag and cancel the relevant events. SCS does NOT
 * automatically invoke any handler tied to this definition.</p>
 *
 * <p><strong>Registration timing:</strong> register your flag in your plugin's
 * {@code onLoad()} method. SCS's startup migration runs in {@code onEnable()} and uses the
 * declared default values to seed missing keys on existing claims; if you register later,
 * existing claims won't have the flag until the next startup.</p>
 *
 * <pre>{@code
 * @Override
 * public void onLoad() {
 *     SCS_FlagRegistry.registerFlag(FlagDefinition.builder("my_flag")
 *         .defaultValue(true)
 *         .titleKey("my_flag-title")
 *         .loreKey("my_flag-lore")
 *         .iconMaterial("DIAMOND")
 *         .build());
 * }
 * }</pre>
 */
public final class FlagDefinition {

    private final String key;
    private final boolean defaultValue;
    private final Boolean protectedModeDefault;
    private final Boolean survivalRequiringClaimsModeDefault;
    private final String titleKey;
    private final String loreKey;
    private final String iconMaterial;
    private final String owningPluginName;

    private FlagDefinition(Builder b) {
        this.key = b.key;
        this.defaultValue = b.defaultValue;
        this.protectedModeDefault = b.protectedModeDefault;
        this.survivalRequiringClaimsModeDefault = b.survivalRequiringClaimsModeDefault;
        this.titleKey = b.titleKey;
        this.loreKey = b.loreKey;
        this.iconMaterial = b.iconMaterial;
        this.owningPluginName = b.owningPluginName;
    }

    /** The flag's storage key, lowercase (matches what {@code claim.getFlag(key)} expects). */
    public String getKey() { return key; }

    /** Default value used to seed claims that don't yet have this flag. */
    public boolean getDefaultValue() { return defaultValue; }

    /** Default in PROTECTED world mode. May be {@code null} ({@code defaultValue} is used). */
    public Boolean getProtectedModeDefault() { return protectedModeDefault; }

    /** Default in SURVIVAL_REQUIRING_CLAIMS mode. May be {@code null}. */
    public Boolean getSurvivalRequiringClaimsModeDefault() { return survivalRequiringClaimsModeDefault; }

    /** Language key for the GUI display title (e.g. {@code "my_flag-title"}). May be {@code null}. */
    public String getTitleKey() { return titleKey; }

    /** Language key for the GUI display lore. May be {@code null}. */
    public String getLoreKey() { return loreKey; }

    /** Bukkit {@code Material.name()} used for the GUI item. May be {@code null}. */
    public String getIconMaterial() { return iconMaterial; }

    /** Plugin that registered this flag, for diagnostics / cleanup on plugin disable. */
    public String getOwningPluginName() { return owningPluginName; }

    public static Builder builder(String key) {
        return new Builder(key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FlagDefinition that)) return false;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    public static final class Builder {
        private final String key;
        private boolean defaultValue = true;
        private Boolean protectedModeDefault;
        private Boolean survivalRequiringClaimsModeDefault;
        private String titleKey;
        private String loreKey;
        private String iconMaterial;
        private String owningPluginName;

        private Builder(String key) {
            if (key == null || key.isBlank()) {
                throw new IllegalArgumentException("Flag key cannot be null/blank");
            }
            this.key = key.toLowerCase(Locale.ROOT);
        }

        public Builder defaultValue(boolean v) { this.defaultValue = v; return this; }
        public Builder protectedModeDefault(Boolean v) { this.protectedModeDefault = v; return this; }
        public Builder survivalRequiringClaimsModeDefault(Boolean v) { this.survivalRequiringClaimsModeDefault = v; return this; }
        public Builder titleKey(String v) { this.titleKey = v; return this; }
        public Builder loreKey(String v) { this.loreKey = v; return this; }
        public Builder iconMaterial(String v) { this.iconMaterial = v; return this; }
        public Builder owningPluginName(String v) { this.owningPluginName = v; return this; }

        public FlagDefinition build() { return new FlagDefinition(this); }
    }
}
