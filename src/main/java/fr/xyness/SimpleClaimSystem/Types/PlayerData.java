package fr.xyness.SimpleClaimSystem.Types;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * PlayerData object.
 */
public class PlayerData {


    // ***************
    // *  Variables  *
    // ***************


	// UUID of player
    private final UUID uuid;

    // Name of player
    private final String name;

    // Texture of player
    private final String texture;

    // Mojang UUID of player
    private final String mojang_uuid;

    // Settings
    private Map<String,Object> settings;


    // ******************
    // *  Constructors  *
    // ******************


    /**
     * Main constructor.
     *
     * @param uuid The UUID of player.
     * @param name The name of player.
     * @param texture The texture of player.
     * @param mojang_uuid The Mojang UUID of player.
     * @param settings The settings of player.
     */
    public PlayerData(UUID uuid, String name, String texture, String mojang_uuid, Map<String,Object> settings) {
        this.uuid = uuid;
        this.name = name;
        this.texture = texture;
        this.mojang_uuid = mojang_uuid;
        this.settings = new ConcurrentHashMap<>(settings);
    }


    // *************
    // *  Methods  *
    // *************


    @Override
    public String toString() {
        return "PlayerData{" + "uuid=" + uuid.toString() + ", name=" + name + ", texture=" + texture + ", mojangUuid=" + mojang_uuid + '}';
    }

    public UUID getUuid() { return uuid; }
    public String getName() { return name; }
    public String getTexture() { return texture; }
    public String getMojangUUID() { return mojang_uuid; }

    public Map<String,Object> getSettings() { return settings; }

    public Object getSetting(String setting) {
    	return settings.get(setting);
    }

    public void setSetting(String setting, Object value) {
    	settings.put(setting, value);
    }

    public void setSettings(Map<String,Object> settings) {
    	this.settings = new ConcurrentHashMap<>(settings);
    }

    public void removeSetting(String setting) {
    	settings.remove(setting);
    }

}
