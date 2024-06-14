package dk.wavebleak.wavespluginlib.itemhelpers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import dk.wavebleak.wavespluginlib.WavesPluginLib;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class ItemBuilder {

    private Material material = Material.STONE;
    private int amount = 1;
    private byte data = -1;
    private List<String> lore = new ArrayList<>();
    private final HashMap<Enchantment, Integer> enchants = new HashMap<>();
    private ItemMeta meta = null;
    private String name = null;

    public ItemStack build() {
        ItemStack itemStack;
        if(data != -1) {
            itemStack = new ItemStack(material, amount, data);
        } else {
            itemStack = new ItemStack(material, amount);
        }

        ItemMeta itemMeta;
        if(meta == null) {
            itemMeta = itemStack.getItemMeta();
        } else {
            itemMeta = meta;
        }

        if (name != null) {
            itemMeta.setDisplayName(name);
        }

        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);


        for(Map.Entry<Enchantment, Integer> enchant : enchants.entrySet()) {
            itemStack.addEnchantment(enchant.getKey(), enchant.getValue());
        }

        return itemStack;
    }

    public ItemBuilder fromTexture(String texture) {
        ItemStack skullItem = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta meta = (SkullMeta) skullItem.getItemMeta();

        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "Ingen...");
        gameProfile.getProperties().put("textures", new Property("textures", texture));

        try {
            Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, gameProfile);
        } catch (Exception e) {
            WavesPluginLib.pluginInstance.getLogger().severe("Couldn't create skull item: " + e.getMessage());
        }

        skullItem.setItemMeta(meta);

        this.meta = skullItem.getItemMeta();
        this.data = (byte) SkullType.PLAYER.ordinal();
        this.material = skullItem.getType();
        return this;
    }

    public ItemBuilder fromUUID(UUID targetUUID) {
        ItemStack skullItem = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta meta = (SkullMeta) skullItem.getItemMeta();

        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "Ingen...");
        gameProfile.getProperties().put("textures", new Property("textures", getTextureValue(targetUUID.toString())));

        try {
            Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, gameProfile);
        } catch (Exception e) {
            WavesPluginLib.pluginInstance.getLogger().severe("Couldn't create skull item: " + e.getMessage());
        }

        skullItem.setItemMeta(meta);

        this.meta = skullItem.getItemMeta();
        this.data = (byte) SkullType.PLAYER.ordinal();
        this.material = skullItem.getType();
        return this;
    }

    private String getTextureValue(String uuid) {
        String url = "https://sessionserver.mojang.com/session/minecraft/profile/" + uuid;


        try {
            URL requestUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();
                String jsonResponse = response.toString();

                Gson gson = new Gson();
                JsonObject json = gson.fromJson(jsonResponse, JsonObject.class);

                JsonArray properties = json.getAsJsonArray("properties");
                JsonObject texture = properties.get(0).getAsJsonObject();

                return texture.get("value").getAsString();
            }
        } catch (Exception e) {
            WavesPluginLib.pluginInstance.getLogger().severe("Couldn't create texture: " + e.getMessage());
        }
        return null;
    }

    public ItemBuilder addEnchant(Enchantment enchantment, int level) {
        enchants.put(enchantment, level);
        return this;
    }

    public ItemBuilder setName(String name) {
        this.name = ChatColor.translateAlternateColorCodes('&', name);
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        this.lore = Arrays.stream(lore).map(string -> ChatColor.translateAlternateColorCodes('&', string)).collect(Collectors.toList());
        return this;
    }

    public ItemBuilder addLore(String line) {
        this.lore.add(ChatColor.translateAlternateColorCodes('&', line));
        return this;
    }


    public <T extends ItemMeta> ItemBuilder setMeta(Class<T> metaClass, Consumer<T> metaConsumer) {
        T meta = metaClass.cast(Bukkit.getItemFactory().getItemMeta(material));
        if(meta != null) {
            metaConsumer.accept(meta);
            this.meta = meta;
        }
        return this;
    }


    public ItemBuilder setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemBuilder setData(byte data) {
        this.data = data;
        return this;
    }



}
