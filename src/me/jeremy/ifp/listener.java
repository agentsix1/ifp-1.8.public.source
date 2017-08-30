package me.jeremy.ifp;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class listener implements Listener{
	static main plugin;
	public listener(main main) {
		plugin = main;
	}
	
	@SuppressWarnings("unused")
	@EventHandler
	public static void onJoin(PlayerJoinEvent e) {
		if (false) { 
			//database.attemptAddorUpdate(e.getPlayer());	
		} else {
			plugin.getPlayers().set("Players." + e.getPlayer().getUniqueId().toString() + ".Name", e.getPlayer().getName());
			plugin.saveConfig();
			plugin.getPlayers().set("Players." + e.getPlayer().getUniqueId().toString() + ".Enabled", true);
			plugin.saveConfig();
			plugin.getPlayers().set("Players." + e.getPlayer().getUniqueId().toString() + ".Items", new ArrayList<String>());
			plugin.saveConfig();
			plugin.reloadPlayers();
			plugin.mnuVal.put(e.getPlayer(), -1);
			
		}
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		if (plugin.mnuVal.containsKey(Bukkit.getPlayer(e.getPlayer().getName()))) {
			Player p = Bukkit.getPlayer(e.getPlayer().getName());
			if (plugin.mnuVal.get(p) > -1) {
				plugin.mnuVal.put(p, -1);
			}
		}
		
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player p = (Player) event.getWhoClicked();
		ItemStack clicked = event.getCurrentItem();
		Inventory inventory = event.getInventory();
		if (plugin.enableAdd) {
			Boolean cont = false;
			for (String item : plugin.getBlocks().getStringList("Blocks")) {
				if (item.equals(clicked.getType().toString() + ":" + clicked.getData().getData())){
					cont = true;
					plugin.pSend(p, "Block Found");
				}
			}
			if (!cont) {
				List<String> a = plugin.getBlocks().getStringList("Blocks");
				a.add(clicked.getType().toString() + ":" + clicked.getData().getData());
				plugin.getBlocks().set("Blocks", a);
				plugin.saveBlocks();
				plugin.pSend(p, "Block Added - " + clicked.getType().toString() + ":" + clicked.getData().getData());
			}
			plugin.pSend(p, "Canceled");
			event.setCancelled(true);
			
			
		}
		
			/* - Menu Values
			 *  - Admin Main Menu (0)
			 *  - Admin View List (1)
			 *  - Admin Add/Remove (2)
			 *  - Player Main Menu (3)
			 *  - Player View List (4)
			 *  - Player Add/Remove (5)
			 */
		try {
			switch (plugin.mnuVal.get(p)) {
			case 0: // Admin Main Menu
				plugin.mnuVal.put(p, 0);
				event.setCancelled(true);
				if (clicked.getType().toString().equals("LAVA_BUCKET")) {
					 plugin.clearList(p, true);
					 p.closeInventory();
				 }
				if (clicked.getType().toString().equals("WATER_BUCKET")) {
					plugin.srlVerVal.put(p, 0);
					plugin.srlHozVal.put(p, 0);
					 plugin.addremGUIMenu(p, true);
				 }
				if (clicked.getType().toString().equals("BUCKET") & event.getSlot() == 6) {
					plugin.viewGUIMenu(p, 1, true, false);
				 }
				break;
			case 1: // Admin View List
				plugin.mnuVal.put(p, 1);
				event.setCancelled(true);
				if (clicked.getType().toString().equals("STAINED_GLASS")) {
					if (clicked.getData().getData() == (byte) 14) {
						int pg = Integer.parseInt(inventory.getName().split("\\(")[1].split("\\/")[0]);
						if (event.getInventory().getName().contains(plugin.ct("&9Public &7List"))) {
							plugin.viewGUIMenu(p, pg - 1, true, true);
						} else {
							plugin.viewGUIMenu(p, pg - 1, true, false);
						}
						
					}
					if (clicked.getData().getData() == (byte) 5) {
						int pg = Integer.parseInt(inventory.getName().split("\\(")[1].split("\\/")[0]);
						if (event.getInventory().getName().contains(plugin.ct("&9Public &7List"))) {
							plugin.viewGUIMenu(p, pg + 1, true, true);
						} else {
							plugin.viewGUIMenu(p, pg + 1, true, false);
						}
						//plugin.viewGUIMenu(p, pg + 1, true, false);
					}
				} else if (clicked.getType().toString().equals("CHEST") && clicked.getItemMeta().getDisplayName().equals(plugin.ct("&9M&8ain &9M&8enu"))) {
					if (event.getInventory().getName().contains(plugin.ct("&9Public &7List"))) {
						plugin.mainGUIMenu(p, false);
					} else {
						plugin.mainGUIMenu(p, true);
					}
					
				}
				break;
			case 2: // Admin Add/Remove
				plugin.mnuVal.put(p, 2);
				if (clicked.getType().toString().equals("CHEST") && clicked.getItemMeta().getDisplayName().equals(plugin.ct("&9M&8ain &9M&8enu"))) { 
					plugin.mainGUIMenu(p, true);
				}
				if (event.getSlot() > 0 & event.getSlot() < 8) { //Category Change
					plugin.selectCat(p, event.getSlot(), event.getCurrentItem(), true);
				} else 
					if (event.getSlot() > 8 & event.getSlot() < 17 || event.getSlot() > 17 & event.getSlot() < 26 || event.getSlot() > 26 & event.getSlot() < 35 || 
							event.getSlot() > 35 & event.getSlot() < 44 || event.getSlot() > 44 & event.getSlot() < 53 ) { // Selected Item
						if (event.getCurrentItem().getItemMeta().hasEnchants()) {
							plugin.removeItem(p, event.getCurrentItem(), true);
							plugin.scrollDown(p, plugin.catItems, plugin.srlVerVal.get(p) - 1, true);
						} else {
							plugin.addItem(p, event.getCurrentItem(), true);
							plugin.scrollDown(p, plugin.catItems, plugin.srlVerVal.get(p)-1, true);
						}
						
					} else 
				if (event.getSlot() == 0 & clicked.getData().getData() == (byte) 5){ 
						plugin.scrollLeft(p, plugin.catItems, plugin.srlHozVal.get(p), true);
				} else
				if (event.getSlot() == 8 & clicked.getData().getData() == (byte) 5) {
						plugin.scrollRight(p, plugin.catItems, plugin.srlHozVal.get(p), true);
				} else 
				if (event.getSlot() == 17 & clicked.getData().getData() == (byte) 5){
					
					plugin.scrollUp(p, plugin.catItems, plugin.srlVerVal.get(p), true);
				} else 
				if (event.getSlot() == 53 & clicked.getData().getData() == (byte) 5) {
					plugin.scrollDown(p, plugin.catItems, plugin.srlVerVal.get(p), true);
				}
				event.setCancelled(true);
				break;
			case 3: // Player Main Menu
				plugin.mnuVal.put(p, 3);
				event.setCancelled(true);
				if (clicked.getType().toString().equals("LAVA_BUCKET")) {
					plugin.clearList(p, false);
					p.closeInventory();
				 }
				if (clicked.getType().toString().equals("WATER_BUCKET")) {
					plugin.srlVerVal.put(p, 0);
					plugin.srlHozVal.put(p, 0);
					 plugin.addremGUIMenu(p, false);
					 
				 }
				if (clicked.getType().toString().equals("BUCKET") & event.getSlot() == 6) {
					plugin.viewGUIMenu(p, 1, false, false);
				 } 
				if (clicked.getType().toString().equals("BUCKET") & event.getSlot() == 8) {
					plugin.viewGUIMenu(p, 1, true, true);
				 } 
				break;
			case 4: // Player View List
				plugin.mnuVal.put(p, 4);
				event.setCancelled(true);
				if (clicked.getType().toString().equals("STAINED_GLASS")) {
					if (clicked.getData().getData() == (byte) 14) {
						int pg = Integer.parseInt(inventory.getName().split("\\(")[1].split("\\/")[0]);
						plugin.viewGUIMenu(p, pg - 1, false, false);
					}
					if (clicked.getData().getData() == (byte) 5) {
						int pg = Integer.parseInt(inventory.getName().split("\\(")[1].split("\\/")[0]);
						plugin.viewGUIMenu(p, pg + 1, false, false);
					}
				} else if (clicked.getType().toString().equals("CHEST") && clicked.getItemMeta().getDisplayName().equals(plugin.ct("&9M&8ain &9M&8enu"))) { 
					plugin.mainGUIMenu(p, false);
				}
				break;
			case 5: // Player Add/Remove
				plugin.mnuVal.put(p, 5);
				if (clicked.getType().toString().equals("CHEST") && clicked.getItemMeta().getDisplayName().equals(plugin.ct("&9M&8ain &9M&8enu"))) { 
					plugin.mainGUIMenu(p, false);
				}
				if (event.getSlot() > 0 & event.getSlot() < 8) { //Category Change
					plugin.selectCat(p, event.getSlot(), event.getCurrentItem(), false);
				} else 
				if (event.getSlot() > 8 & event.getSlot() < 17 || event.getSlot() > 17 & event.getSlot() < 26 || event.getSlot() > 26 & event.getSlot() < 35 || 
						event.getSlot() > 35 & event.getSlot() < 44 || event.getSlot() > 44 & event.getSlot() < 53 ) { // Selected Item
					if (event.getCurrentItem().getItemMeta().hasEnchants()) {
						plugin.removeItem(p, event.getCurrentItem(), false);
						plugin.scrollDown(p, plugin.catItems, plugin.srlVerVal.get(p) - 1, false);
					} else {
						plugin.addItem(p, event.getCurrentItem(), false);
						plugin.scrollDown(p, plugin.catItems, plugin.srlVerVal.get(p)-1, false);
					}
					
				} else 
				if (event.getSlot() == 0 & clicked.getData().getData() == (byte) 5){ 
						plugin.scrollLeft(p, plugin.catItems, plugin.srlHozVal.get(p), false);
				} else
				if (event.getSlot() == 8 & clicked.getData().getData() == (byte) 5) {
						plugin.scrollRight(p, plugin.catItems, plugin.srlHozVal.get(p), false);
				} else 
				if (event.getSlot() == 17 & clicked.getData().getData() == (byte) 5){
					
					plugin.scrollUp(p, plugin.catItems, plugin.srlVerVal.get(p), false);
				} else 
				if (event.getSlot() == 53 & clicked.getData().getData() == (byte) 5) {
					plugin.scrollDown(p, plugin.catItems, plugin.srlVerVal.get(p), false);
				}
		
				
				event.setCancelled(true);
				break;
				
			}
			
			
			
		} catch (NullPointerException ex) {
			
		}
		
	}
	
	@EventHandler
    public static void PickupItem(PlayerPickupItemEvent e) {
        Player p = e.getPlayer();
        	if (checkPublicFilter(p, e.getItem().getItemStack())) {
        		e.setCancelled(true);
        		return;
        	}
        	if (checkPrivateFiler(p, e.getItem().getItemStack())) {
        		e.setCancelled(true);
        		return;
        	} 
    }

	public static boolean checkPrivateFiler(Player p, ItemStack i) {
		//if (!false) { //Database: Enabled: false
			if (p.hasPermission("itemfilterpickup.user") || p.hasPermission("itemfilterpickup.user.canfilter")) {
				if (plugin.getPlayers().getBoolean("Players." + p.getUniqueId().toString() + ".Enabled")) {
					int b = 1;
					int max = getMaxFilter(p);
					for (String filter : plugin.getPlayers().getStringList("Players." + p.getUniqueId().toString() + ".Items")) {
						if (max == -1) {
							ItemStack itemA = new ItemStack(Material.matchMaterial(filter.split(":")[0]), i.getAmount(), (byte) Integer.parseInt(filter.split(":")[1]));
							if (i.equals(itemA)) {
								return true;
							}
							b++;
						} else if (b <= max && max != -1) { 
							ItemStack itemA = new ItemStack(Material.matchMaterial(filter.split(":")[0]), i.getAmount(), (byte) Integer.parseInt(filter.split(":")[1]));
							if (i.equals(itemA)) {
								return true;
							}
							b++;
						} else {
							break;
						}
					}
					return false;
				} else {
					return false;
				}
			} else{
				return false;
			}
		/*} else {
			return false;
			//return database.checkDatabase(p, i);
		}*/
	}
	
	public static boolean checkBeyondMaxC(Player p, ItemStack i) {
		int b = 1;
		int max = getMaxFilter(p);
		for (String filter : plugin.getPlayers().getStringList("Players." + p.getUniqueId().toString() + ".Items")) {
			if (max == -1) {
				ItemStack itemA = new ItemStack(Material.matchMaterial(filter.split(":")[0]), i.getAmount(), (byte) Integer.parseInt(filter.split(":")[1]));
				if (i.equals(itemA)) {
					return true;
				}
				b++;
			} else if (b <= max && max != -1) { 
				ItemStack itemA = new ItemStack(Material.matchMaterial(filter.split(":")[0]), i.getAmount(), (byte) Integer.parseInt(filter.split(":")[1]));
				if (i.equals(itemA)) {
					return true;
				}
				b++;
			} else {
				break;
			}
		}
		return false;
	}

	public static boolean checkPrivateFilerC(Player p, ItemStack i) {
		//if (!false) { //Database: Enabled: false
			if (p.hasPermission("itemfilterpickup.user") || p.hasPermission("itemfilterpickup.user.canfilter")) {
				for (String filter : plugin.getPlayers().getStringList("Players." + p.getUniqueId().toString() + ".Items")) {
						ItemStack itemA = new ItemStack(Material.matchMaterial(filter.split(":")[0]), i.getAmount(), (byte) Integer.parseInt(filter.split(":")[1]));
						if (i.equals(itemA)) {
							return true;
						}
				}
				return false;
			} else{
				return false;
			}
		/*} else {
			return false;
			//return database.checkDatabase(p, i);
		}*/
	}

	private static boolean checkPublicFilter(Player p, ItemStack i) {
		if (p.hasPermission("itemfilterpickup.public.bypass")) {
			return false;
		} else {
		if (!p.hasPermission("itemfilterpickup.admin")) {
			if (plugin.getConfig().getBoolean("Public Pickup Filter.Enabled")) {
				for (String filter : plugin.getConfig().getStringList("Public Pickup Filter.Items")) {
					ItemStack itemA = new ItemStack(Material.matchMaterial(filter.split(":")[0]), i.getAmount(), (byte) Integer.parseInt(filter.split(":")[1]));
					if (i.equals(itemA)) {
						return true;
					}
				}
				return false;
			} else {
				return false;
			}
		} else{
			return false;
		}
		}
	}
	
	public static boolean checkPublicFilterC(Player p, ItemStack i) {
		if (p.hasPermission("itemfilterpickup.public.bypass")) {
			return false;
		} else {
		if (!p.hasPermission("itemfilterpickup.admin")) {
				for (String filter : plugin.getConfig().getStringList("Public Pickup Filter.Items")) {
					ItemStack itemA = new ItemStack(Material.matchMaterial(filter.split(":")[0]), i.getAmount(), (byte) Integer.parseInt(filter.split(":")[1]));
					if (i.equals(itemA)) {
						return true;
					}
				}
				return false;
		} else{
			return false;
		}
		}
	}
	
	public static boolean checkPublicFilterGlow(Player p, ItemStack i) {
		if (p.hasPermission("itemfilterpickup.admin.edit") || p.hasPermission("itemfilterpickup.admin")) {
				for (String filter : plugin.getConfig().getStringList("Public Pickup Filter.Items")) {
					ItemStack itemA = new ItemStack(Material.matchMaterial(filter.split(":")[0]), i.getAmount(), (byte) Integer.parseInt(filter.split(":")[1]));
					if (i.equals(itemA)) {
						return true;
					}
				}
				return false;
		} else{
			return false;
		}
	}
	
	public static int getMaxFilter(Player p) {
		int curMax = 0;
		for (String perm : plugin.getConfig().getStringList("Permission/Filter Amount")) {
			if (p.hasPermission("itemfilterpickup.user.max." + perm.split(":")[0])) {
				if (curMax < Integer.parseInt(perm.split(":")[1])) {
					curMax = Integer.parseInt(perm.split(":")[1]);
				} else if (Integer.parseInt(perm.split(":")[1]) == -1) {
					return -1;
				}
			}
		}
		return curMax;
	}
}
