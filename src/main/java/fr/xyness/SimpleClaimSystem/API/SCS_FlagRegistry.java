package fr.xyness.SimpleClaimSystem.API;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Static registry of custom flags / permissions declared by external plugins. Available from
 * the moment the API JAR is on the classpath — no need to wait for SimpleClaimSystem's
 * {@code onEnable} to fire. This is the recommended entry point for {@code onLoad}-based
 * registration: SCS reads the registry during its own startup migration to seed missing
 * flag/permission values on existing claims.
 *
 * <p>Calling this in {@code onEnable} also works — the registry is mutable at runtime — but
 * any claims that loaded before the registration will be missing your flag's value until
 * the next plugin reload (their stored map simply lacks the key, and {@code claim.getFlag}
 * will return {@code false}).</p>
 *
 * <pre>{@code
 * public class MyPlugin extends JavaPlugin {
 *     @Override
 *     public void onLoad() {
 *         SCS_FlagRegistry.registerFlag(FlagDefinition.builder("my_flag")
 *             .defaultValue(true)
 *             .owningPluginName(getName())
 *             .build());
 *     }
 *
 *     @EventHandler
 *     public void onSomething(SomeEvent event) {
 *         SCS_API api = SCS_API_Provider.get();
 *         api.getClaim(event.getLocation().getChunk()).ifPresent(claim -> {
 *             if (!claim.getFlag("my_flag")) event.setCancelled(true);
 *         });
 *     }
 * }
 * }</pre>
 *
 * <p><strong>Threading:</strong> registration is thread-safe (backed by a
 * {@link ConcurrentHashMap}) but generally called from the main thread during plugin
 * lifecycle hooks.</p>
 */
public final class SCS_FlagRegistry {

    private static final Map<String, FlagDefinition> FLAGS = new ConcurrentHashMap<>();
    private static final Map<String, PermissionDefinition> PERMISSIONS = new ConcurrentHashMap<>();

    /**
     * Optional change listener invoked whenever a flag/permission is registered or
     * unregistered. SCS itself subscribes here to auto-register {@code scs.bypass.<key>}
     * permissions via Bukkit's PluginManager and to refresh claim caches.
     */
    private static volatile Consumer<RegistryChange> changeListener;

    private SCS_FlagRegistry() {}


    // *********
    // *  Flag  *
    // *********


    public static void registerFlag(FlagDefinition def) {
        if (def == null) throw new IllegalArgumentException("FlagDefinition cannot be null");
        FLAGS.put(normalize(def.getKey()), def);
        notifyListener(RegistryChange.Kind.FLAG_REGISTERED, def.getKey(), def, null);
    }

    public static void unregisterFlag(String key) {
        if (key == null) return;
        FlagDefinition removed = FLAGS.remove(normalize(key));
        if (removed != null) {
            notifyListener(RegistryChange.Kind.FLAG_UNREGISTERED, removed.getKey(), removed, null);
        }
    }

    public static boolean isFlagRegistered(String key) {
        return key != null && FLAGS.containsKey(normalize(key));
    }

    public static FlagDefinition getFlag(String key) {
        return key == null ? null : FLAGS.get(normalize(key));
    }

    /**
     * Snapshot of every registered flag, keyed by lowercase flag name. The returned map is
     * unmodifiable; new registrations after this call are not reflected.
     */
    public static Map<String, FlagDefinition> getAllFlags() {
        return Collections.unmodifiableMap(FLAGS);
    }


    // **************
    // *  Perm        *
    // **************


    public static void registerPermission(PermissionDefinition def) {
        if (def == null) throw new IllegalArgumentException("PermissionDefinition cannot be null");
        PERMISSIONS.put(normalize(def.getKey()), def);
        notifyListener(RegistryChange.Kind.PERMISSION_REGISTERED, def.getKey(), null, def);
    }

    public static void unregisterPermission(String key) {
        if (key == null) return;
        PermissionDefinition removed = PERMISSIONS.remove(normalize(key));
        if (removed != null) {
            notifyListener(RegistryChange.Kind.PERMISSION_UNREGISTERED, removed.getKey(), null, removed);
        }
    }

    public static boolean isPermissionRegistered(String key) {
        return key != null && PERMISSIONS.containsKey(normalize(key));
    }

    public static PermissionDefinition getPermission(String key) {
        return key == null ? null : PERMISSIONS.get(normalize(key));
    }

    public static Map<String, PermissionDefinition> getAllPermissions() {
        return Collections.unmodifiableMap(PERMISSIONS);
    }


    // ******************
    // *  Plugin scoping *
    // ******************


    /**
     * Unregisters every flag and permission owned by the given plugin. Useful in a plugin's
     * {@code onDisable} to clean up state when the plugin is reloaded.
     *
     * @param pluginName The owning plugin name (matched against
     *                   {@link FlagDefinition#getOwningPluginName()} / equivalent on permission).
     */
    public static void unregisterAllOwnedBy(String pluginName) {
        if (pluginName == null) return;
        FLAGS.entrySet().removeIf(e -> {
            if (pluginName.equals(e.getValue().getOwningPluginName())) {
                notifyListener(RegistryChange.Kind.FLAG_UNREGISTERED, e.getValue().getKey(), e.getValue(), null);
                return true;
            }
            return false;
        });
        PERMISSIONS.entrySet().removeIf(e -> {
            if (pluginName.equals(e.getValue().getOwningPluginName())) {
                notifyListener(RegistryChange.Kind.PERMISSION_UNREGISTERED, e.getValue().getKey(), null, e.getValue());
                return true;
            }
            return false;
        });
    }


    // ****************
    // *  Internal     *
    // ****************


    /**
     * Internal hook for SimpleClaimSystem to receive change notifications. Setting this is
     * NOT part of the public API — external plugins should not call it.
     */
    public static void __setInternalChangeListener(Consumer<RegistryChange> listener) {
        changeListener = listener;
    }

    private static String normalize(String key) {
        return key.toLowerCase(Locale.ROOT);
    }

    private static void notifyListener(RegistryChange.Kind kind, String key,
                                        FlagDefinition flag, PermissionDefinition permission) {
        Consumer<RegistryChange> l = changeListener;
        if (l == null) return;
        try {
            l.accept(new RegistryChange(kind, key, flag, permission));
        } catch (Throwable t) {
            // Listener errors must never break a plugin's onLoad — swallow but print.
            t.printStackTrace();
        }
    }

    /**
     * Event payload for the internal change listener.
     */
    public static final class RegistryChange {
        public enum Kind {
            FLAG_REGISTERED, FLAG_UNREGISTERED,
            PERMISSION_REGISTERED, PERMISSION_UNREGISTERED
        }

        private final Kind kind;
        private final String key;
        private final FlagDefinition flag;
        private final PermissionDefinition permission;

        public RegistryChange(Kind kind, String key, FlagDefinition flag, PermissionDefinition permission) {
            this.kind = kind;
            this.key = key;
            this.flag = flag;
            this.permission = permission;
        }

        public Kind getKind() { return kind; }
        public String getKey() { return key; }
        public FlagDefinition getFlag() { return flag; }
        public PermissionDefinition getPermission() { return permission; }
    }
}
