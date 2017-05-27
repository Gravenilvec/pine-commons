package xyz.anana.pinecommons.inventory;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:contact@anana.xyz">Anana</a>
 */
@Getter
public abstract class InventoryBase {

    @Getter
    private static List<InventoryBase> inventories = new ArrayList<>();

    @Setter
    protected String displayName;
    @Setter
    protected boolean cancelled;
    protected final int slots;
    protected final Map<Integer, ItemStack> items;

    /**
     * Simplify creation of bukkit inventories.
     *
     * @param displayName The inventory name.
     * @param slots       The inventory slots.
     */
    public InventoryBase(String displayName, int slots) {
        Validate.notEmpty(displayName);
        Validate.isTrue(slots % 9 == 0 && slots > 0);

        this.displayName = displayName;
        this.slots = slots;
        this.items = new HashMap<>();

        inventories.add(this);
    }

    /**
     * Add an item to the inventory.
     *
     * @param slot The slot.
     * @param item The item.
     */
    public void addItem(int slot, ItemStack item) {
        Validate.isTrue(slot >= 0 && slot <= slots);
        Validate.notNull(item);

        items.put(slot, item);
    }

    /**
     * Add a collection of items to the inventory.
     *
     * @param items The collection of items.
     */
    public void addItems(Map<Integer, ItemStack> items) {
        for (Map.Entry<Integer, ItemStack> entry : items.entrySet())
            addItem(entry.getKey(), entry.getValue());
    }

    /**
     * Open the inventory to a player.
     *
     * @param p The player.
     */
    public Inventory open(Player p) {
        Inventory inventory = Bukkit.createInventory(null, slots, displayName);

        for (Map.Entry<Integer, ItemStack> items : getItems().entrySet())
            inventory.setItem(items.getKey(), items.getValue());

        p.openInventory(inventory);
        return inventory;
    }

    /**
     * On a player right click on an item.
     *
     * @param p    The player.
     * @param item The clicked item.
     */
    public abstract void onRightClick(Player p, ItemStack item);

    /**
     * On a player left click on an item.
     *
     * @param p    The player.
     * @param item The clicked item.
     */
    public abstract void onLeftClick(Player p, ItemStack item);

    /**
     * Returns slot find by ItemStack.
     *
     * @param item The item.
     * @return The slot find.
     */
    public Integer findByItem(ItemStack item) {
        Map<ItemStack, Integer> itemSlots = new HashMap<>();
        for (Map.Entry<Integer, ItemStack> items : getItems().entrySet())
            itemSlots.put(items.getValue(), items.getKey());

        return itemSlots.get(item);
    }

}
