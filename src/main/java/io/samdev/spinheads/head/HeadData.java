package io.samdev.spinheads.head;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import io.samdev.spinheads.SpinHeads;
import io.samdev.spinheads.data.TrackedHead;
import io.samdev.spinheads.util.UtilLocation;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HeadData
{
    private final String name;
    private final String head;
    private final List<String> hologramLines;
    private final List<String> actions;

    HeadData(String name, String head, List<String> hologramLines, List<String> actions)
    {
        this.name = name;
        this.head = head;
        this.hologramLines = hologramLines;
        this.actions = actions;
    }

    public String getName()
    {
        return name;
    }

    public TrackedHead spawnHead(Location location)
    {
        location = UtilLocation.fixLocation(location);
        location.getBlock().setType(Material.BARRIER);

        UUID uuid = UUID.randomUUID();

        ArmorStand headEntity = location.getWorld().spawn(location.clone().add(0, -1.2D, 0), ArmorStand.class);
        fixArmorStand(headEntity);
        headEntity.setHelmet(toStack());

        return new TrackedHead(uuid, this, location, headEntity.getUniqueId(), spawnHologram(location));
    }

    private List<UUID> spawnHologram(Location location)
    {
        List<UUID> entities = new ArrayList<>();
        location = location.clone().add(0, 0.6D, 0);

        for (String line : Lists.reverse(hologramLines))
        {
            location.add(0.0D, 0.25D, 0.0D);

            ArmorStand armorStand = location.getWorld().spawn(location, ArmorStand.class);

            fixArmorStand(armorStand);

            armorStand.setCustomName(line);
            armorStand.setCustomNameVisible(true);

            entities.add(armorStand.getUniqueId());
        }

        return entities;
    }

    private void fixArmorStand(ArmorStand stand)
    {
        stand.setMarker(true);
        stand.setVisible(false);
        stand.setGravity(false);
    }

    private ItemStack toStack()
    {
        ItemStack stack = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) stack.getItemMeta();

        if (head.length() <= 16)
        {
            meta.setOwner(head);
        }
        else
        {
            applyBase64(meta, head);
        }

        stack.setItemMeta(meta);
        return stack;
    }

    private Field profileField;

    private void applyBase64(SkullMeta meta, String base64)
    {
        if (profileField == null)
        {
            assignProfileField(meta);
        }

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("texture", base64));

        try
        {
            profileField.set(meta, profile);
        }
        catch (IllegalAccessException ignored) {}
    }

    private void assignProfileField(SkullMeta meta)
    {
        try
        {
            profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
        }
        catch (NoSuchFieldException ignored) {}
    }

    void executeCommands(Player player)
    {
        SpinHeads.getPlugin(SpinHeads.class).getActionUtil().executeActions(player, actions);
    }
}
