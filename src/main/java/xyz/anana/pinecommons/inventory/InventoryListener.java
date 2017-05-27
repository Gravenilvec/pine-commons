package xyz.anana.pinecommons.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * @author <a href="mailto:contact@anana.xyz">Anana</a>
 */
public class InventoryListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(click(e));
    }

    /**
     * On a player click on an item call InventoryBase.
     *
     * @param e The bukkit event.
     */
    private boolean click(InventoryClickEvent e) {
        for (InventoryBase inv : InventoryBase.getInventories()) {
            //Filter items
            if (!inv.getItems().containsKey(e.getSlot()) || !inv.getItems().containsValue(e.getCurrentItem()))
                continue;

            //Setup methods
            if (e.isLeftClick())
                inv.onLeftClick((Player) e.getWhoClicked(), e.getCurrentItem());
            else
                inv.onRightClick((Player) e.getWhoClicked(), e.getCurrentItem());

            return inv.isCancelled();
        }

        return false;
    }

}
