package fr.xyness.SimpleClaimSystem.API;

/**
 * Static accessor for the SimpleClaimSystem API.
 */
public final class SCS_API_Provider {


    // ***************
    // *  Variables  *
    // ***************


	// API. volatile because register() is called once on the main thread during plugin enable,
	// while get() can be called from any thread by other plugins — without volatile the JLS
	// gives no visibility guarantees across threads.
    private static volatile SCS_API api;


    // *****************
    // *  Constructor  *
    // *****************


    /**
     * Main constructor.
     */
    private SCS_API_Provider() {}


    // *************
    // *  Methods  *
    // *************


    /**
     * Gets the API.
     *
     * @return The API.
     */
    public static SCS_API get() {
        if (api == null) {
            throw new IllegalStateException("SimpleClaimSystem API is not loaded yet!");
        }
        return api;
    }

    /**
     * Checks if the api is registered.
     *
     * @return True if registered, false otherwise.
     */
    public static boolean isRegistered() {
    	return api != null;
    }

    /**
     * Registers the API.
     *
     * @param instance The API.
     */
    public static void register(SCS_API instance) {
        if (api != null) {
            throw new IllegalStateException("SimpleClaimSystem API is already registered!");
        }
        api = instance;
    }

    /**
     * Unregisters the API.
     */
    public static void unregister() {
        api = null;
    }

}
