package fr.xyness.SimpleClaimSystem.API;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * Definition of a custom claim role-permission declared by an external plugin via
 * {@link SCS_FlagRegistry#registerPermission(PermissionDefinition)}.
 *
 * <p><strong>Data-only:</strong> SimpleClaimSystem only stores the permission's value per
 * claim+role and exposes it through {@code claim.getPermission(role, key)}. The plugin
 * author wires their own {@code @EventHandler}s that check the permission and cancel events.
 * SCS does NOT automatically invoke any handler tied to this definition.</p>
 *
 * <p><strong>Registration timing:</strong> register in your plugin's {@code onLoad()} so
 * SCS's startup migration (which runs in {@code onEnable()}) sees your declared defaults
 * when seeding missing keys on existing claims.</p>
 *
 * <pre>{@code
 * @Override
 * public void onLoad() {
 *     SCS_FlagRegistry.registerPermission(PermissionDefinition.builder("my_perm")
 *         .defaultPerRole("VISITOR", false)
 *         .defaultPerRole("MEMBER", true)
 *         .defaultPerRole("MODERATOR", true)
 *         .titleKey("my_perm-title")
 *         .loreKey("my_perm-lore")
 *         .iconMaterial("DIAMOND")
 *         .build());
 * }
 * }</pre>
 */
public final class PermissionDefinition {

    private final String key;
    /** Per-role defaults. Unspecified roles fall back to {@link #fallbackDefault}. */
    private final Map<String, Boolean> defaultsPerRole;
    private final boolean fallbackDefault;
    private final String titleKey;
    private final String loreKey;
    private final String iconMaterial;
    private final String owningPluginName;

    private PermissionDefinition(Builder b) {
        this.key = b.key;
        this.defaultsPerRole = Collections.unmodifiableMap(new HashMap<>(b.defaultsPerRole));
        this.fallbackDefault = b.fallbackDefault;
        this.titleKey = b.titleKey;
        this.loreKey = b.loreKey;
        this.iconMaterial = b.iconMaterial;
        this.owningPluginName = b.owningPluginName;
    }

    public String getKey() { return key; }

    /** Per-role defaults. Roles not in this map use {@link #getFallbackDefault()}. */
    public Map<String, Boolean> getDefaultsPerRole() { return defaultsPerRole; }

    /** Fallback value for roles not explicitly listed in {@link #getDefaultsPerRole()}. */
    public boolean getFallbackDefault() { return fallbackDefault; }

    public String getTitleKey() { return titleKey; }
    public String getLoreKey() { return loreKey; }
    public String getIconMaterial() { return iconMaterial; }
    public String getOwningPluginName() { return owningPluginName; }

    /**
     * Resolves the default for a given role: explicit per-role entry if present, else
     * {@link #getFallbackDefault()}. Role names are matched case-insensitively (uppercase
     * normalization).
     */
    public boolean resolveDefault(String roleName) {
        if (roleName == null) return fallbackDefault;
        Boolean v = defaultsPerRole.get(roleName.toUpperCase(Locale.ROOT));
        return v == null ? fallbackDefault : v;
    }

    public static Builder builder(String key) {
        return new Builder(key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PermissionDefinition that)) return false;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    public static final class Builder {
        private final String key;
        private final Map<String, Boolean> defaultsPerRole = new HashMap<>();
        private boolean fallbackDefault = false;
        private String titleKey;
        private String loreKey;
        private String iconMaterial;
        private String owningPluginName;

        private Builder(String key) {
            if (key == null || key.isBlank()) {
                throw new IllegalArgumentException("Permission key cannot be null/blank");
            }
            this.key = key.toLowerCase(Locale.ROOT);
        }

        public Builder defaultPerRole(String role, boolean value) {
            defaultsPerRole.put(role.toUpperCase(Locale.ROOT), value);
            return this;
        }

        public Builder fallbackDefault(boolean v) { this.fallbackDefault = v; return this; }
        public Builder titleKey(String v) { this.titleKey = v; return this; }
        public Builder loreKey(String v) { this.loreKey = v; return this; }
        public Builder iconMaterial(String v) { this.iconMaterial = v; return this; }
        public Builder owningPluginName(String v) { this.owningPluginName = v; return this; }

        public PermissionDefinition build() { return new PermissionDefinition(this); }
    }
}
