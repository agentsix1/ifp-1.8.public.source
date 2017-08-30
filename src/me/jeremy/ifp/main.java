package me.jeremy.ifp;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;



public class main extends JavaPlugin {
	public Boolean enableAdd = false;
	public HashMap<Player, Integer> srlVerVal = new HashMap<Player, Integer>();
	public List<ItemStack> catItems = new ArrayList<ItemStack>();
	public HashMap<Player, Integer> srlHozVal = new HashMap<Player, Integer>();
	public HashMap<Player, Integer> mnuVal = new HashMap<Player, Integer>();
	public HashMap<Player, Integer> selCat = new HashMap<Player, Integer>();
	public HashMap<Player, Inventory> curInv = new HashMap<Player, Inventory>();
	public int addremitem = 0;
	
	@SuppressWarnings("unused")
	@Override
	public void onEnable() {
		Bukkit.getServer().getPluginManager().registerEvents(new listener(this), this);
		//Bukkit.getServer().getPluginManager().registerEvents(new database(this), this);
		loadConfiguration();
		if (false) {
	    	//database.connect();
	    }
		System.out.println("[ItemFilterPickup] Plugin is fully loaded and ready to go! Good luck!");
		
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public void loadConfiguration(){
		getConfig().options().copyDefaults(true);
	    saveConfig();
	    reloadConfig();
	    getPlayers().options().copyDefaults(true);
	    savePlayers();
	    reloadPlayers();
	    getBlocks().options().copyDefaults(true);
	    saveBlocks();
	    reloadBlocks();
	    List<String> preMenus = getBlocks().getStringList("Menus");
		for (String menu : preMenus) {
			catItems.add(getItem(getBlocks().getString(menu + ".Block")));
		}
					
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			
			if (cmdLabel.equalsIgnoreCase("ifp") || cmdLabel.equalsIgnoreCase("itemfilter")) {
				if (args.length == 0) {
					help(p); 
					/*if (enableAdd) {
						enableAdd = false;
						pSend(p, "Block Add Disabled");
					} else {
						enableAdd = true;
						pSend(p, "Block Add Enabled");
						Inventory myInv = Bukkit.createInventory(null, 54, ct("&cTest"));
						myInv.setItem(0, getItem("STONE:0:1:none:none:none"));
						myInv.setItem(8, getItem("STAINED_GLASS:14:1:none:&aU&8p:none"));
						myInv.setItem(17, getItem("STAINED_GLASS:0:1:none: :none"));
						myInv.setItem(26, getItem("STAINED_GLASS:0:1:none: :none"));
						myInv.setItem(35, getItem("STAINED_GLASS:0:1:none: :none"));
						myInv.setItem(44, getItem("STAINED_GLASS:5:1:none:&aD&8own:none"));
						myInv.setItem(45, getItem("STAINED_GLASS:14:1:none:&aD&8own:none"));
						myInv.setItem(46, getItem("BRICK:0:1:none:&aB&8uilding &aB&8locks:none"));
						myInv.setItem(53, getItem("STAINED_GLASS:5:1:none:&aD&8own:none"));
						p.openInventory(myInv);
						
					}*/
					return true;
				} else if (args.length == 1) {
					if (args[0].equalsIgnoreCase("toggle")) {if (checkPermsMsg(p, "itemfilterpickup.user")) { toggleStatus(p, false); return true;}}
					if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("+")) {if (checkPermsMsg(p, "itemfilterpickup.user")) { addItem(p, p.getItemInHand(), false); return true;}}
					if (args[0].equalsIgnoreCase("gui")) {if (checkPermsMsg(p, "itemfilterpickup.user")) { guiOpen(p, "main", false); return true;}}
					if (args[0].equalsIgnoreCase("rem") || args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("-")) {if (checkPermsMsg(p, "itemfilterpickup.user")) { removeItem(p, p.getItemInHand(), false); return true;}}
					if (args[0].equalsIgnoreCase("clear") || args[0].equalsIgnoreCase("c")) {if (checkPermsMsg(p, "itemfilterpickup.user")){ clearList(p, false); return true;}}
					if (args[0].equalsIgnoreCase("list")) {if (checkPermsMsg(p, "itemfilterpickup.user")) { viewList(p, 1, false); return true;}}
					if (args[0].equalsIgnoreCase("help")) { help(p); return true;}
				} else if (args.length == 2) { 
					try {
						if (args[0].equalsIgnoreCase("list")) {if (p.hasPermission("itemfilterpickup.user") & checkPermsMsg(p, "itemfilterpickup.user")) { viewList(p, Integer.parseInt(args[1]), false); return true; }}
					} catch (NumberFormatException e) {
						pSend(p, getConfig().getString("Messages.failed-command").replace("%SYNTAX%", "/ifp list {#}").replace("%NL%", "\n"));
						return true;
					}
					
				}
			}
			if (cmdLabel.equalsIgnoreCase("ifpa") || cmdLabel.equalsIgnoreCase("itemfilteradmin")) {
				if (args.length == 0) {
					help(p); 
					return true;
				} else if (args.length == 1) {
					if (args[0].equalsIgnoreCase("reload")) {if (checkPermsMsg(p, "itemfilterpickup.admin") || checkPermsMsg(p, "itemfilterpickup.admin.reload")) { reloadConfig(); reloadPlayers(); pSend(p, getConfig().getString("Messages.reload")); return true;}}
					if (args[0].equalsIgnoreCase("toggle")) {if (checkPermsMsg(p, "itemfilterpickup.admin") || checkPermsMsg(p, "itemfilterpickup.admin.toggle")) { toggleStatus(p, true); return true;}}
					if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("+")) {if (checkPermsMsg(p, "itemfilterpickup.admin") || checkPermsMsg(p, "itemfilterpickup.edit")) { addItem(p, p.getItemInHand(), true); return true;}}
					if (args[0].equalsIgnoreCase("gui")) {if (checkPermsMsg(p, "itemfilterpickup.user")) { guiOpen(p, "main", true); return true;}}
					if (args[0].equalsIgnoreCase("rem") || args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("-")) {if (checkPermsMsg(p, "itemfilterpickup.admin") || checkPermsMsg(p, "itemfilterpickup.admin.edit")) { removeItem(p, p.getItemInHand(), true); return true;}}
					if (args[0].equalsIgnoreCase("clear") || args[0].equalsIgnoreCase("c")) {if (checkPermsMsg(p, "itemfilterpickup.admin") || checkPermsMsg(p, "itemfilterpickup.admin.edit")){ clearList(p, true); return true;}}
					if (args[0].equalsIgnoreCase("list")) {if (checkPermsMsg(p, "itemfilterpickup.admin") || checkPermsMsg(p, "itemfilterpickup.admin.edit") || checkPermsMsg(p, "itemfilterpickup.public.view")) { viewList(p, 1, true); return true;}}
					if (args[0].equalsIgnoreCase("help")) { help(p); return true;}
				} else if (args.length == 2) { 
					try {
						if (args[0].equalsIgnoreCase("list")) {if (p.hasPermission("itemfilterpickup.admin") || checkPermsMsg(p, "itemfilterpickup.public.view") || checkPermsMsg(p, "itemfilterpickup.admin.edit")) { viewList(p, Integer.parseInt(args[1]), true); return true; }}
					} catch (NumberFormatException e) {
						pSend(p, getConfig().getString("Messages.failed-command").replace("%SYNTAX%", "/ifpa list {#}").replace("%NL%", "\n"));
						return true;
					}
					
				}
			}
		}
		return false;
	}
	
	public String ct(String text) {
		String finalText = text.replace("&0", ChatColor.BLACK + "").replace("&1", ChatColor.DARK_BLUE + "").replace("&2", ChatColor.DARK_GREEN + "").replace("&3", ChatColor.DARK_AQUA + "").replace("&4", ChatColor.DARK_RED + "").replace("&5", ChatColor.DARK_PURPLE + "").replace("&6", ChatColor.GOLD + "").replace("&7", ChatColor.GRAY + "").replace("&8", ChatColor.DARK_GRAY + "").replace("&9", ChatColor.BLUE + "").replace("&a", ChatColor.GREEN + "").replace("&b", ChatColor.AQUA + "").replace("&c", ChatColor.RED + "").replace("&d", ChatColor.LIGHT_PURPLE + "").replace("&e", ChatColor.YELLOW + "").replace("&f", ChatColor.WHITE + "").replace("&l", ChatColor.BOLD + "").replace("&m", ChatColor.STRIKETHROUGH + "").replace("&n", ChatColor.UNDERLINE + "").replace("&o", ChatColor.ITALIC + "").replace("&r", ChatColor.RESET + "");
		return finalText;
	}
	
	public  ItemStack createItem(String it, int amount, int data) {
		ItemStack item = new ItemStack(Material.matchMaterial(it), amount, (byte) data);
		return item;
	}
	public  ItemStack addName(ItemStack it, String name) {
		if (!name.equalsIgnoreCase("none")) {
			ItemMeta m = it.getItemMeta();
	        m.setDisplayName(ct(name));
	        it.setItemMeta(m);
			return it;
		} else {
			return it;
		}
		
	}
	public  ItemStack addEnchant(ItemStack it, String enchantList) {
		if (!enchantList.equalsIgnoreCase("none")) {
			String[] e = enchantList.split(",");
			for (String preEnchant : e) {
				String[] enchant = preEnchant.split("@");
				it.addUnsafeEnchantment(Enchantment.getByName(enchant[0]), Integer.parseInt(enchant[1]));
			}
			return it;
		} else {
			return it;
		}
		
	}
	public  ItemStack addLore(ItemStack it, String loreList) {
		if (!loreList.equalsIgnoreCase("none")) {
			ItemMeta m = it.getItemMeta();
			List<String> lores = new ArrayList<String>();
			String[] preLores = loreList.split(",");
			for (String lore : preLores) { lores.add(ct(lore)); }
	        m.setLore(lores);
	        it.setItemMeta(m);
			return it;
		} else {
			return it;
		}
	}
	
	public  ItemStack getItem(String item) {
		String[] i = item.split(":"); // added
	    Integer amount = Integer.parseInt(i[2]); //added
	    String enchant = i[3];
	    String name = i[4];
	    String lore = i[5];
	    ItemStack newItem = createItem(i[0], amount, Integer.parseInt(i[1]));
		if (!enchant.equalsIgnoreCase("none")) {
			newItem = addEnchant(newItem, enchant);
		}
		if (!name.equalsIgnoreCase("none")) {
			newItem = addName(newItem, name);
		}
		if (!lore.equalsIgnoreCase("none")) {
			newItem = addLore(newItem, lore);
		}
		
		return newItem;
		
	}
	
	public void guiOpen(Player p, String menu, Boolean admin) {
		switch (menu) {
		case "main":
			if (admin) {
				mnuVal.put(p, 0);
			} else {
				mnuVal.put(p, 3);
			}
			mainGUIMenu(p, admin);
			
			
			break;
		}
			
		
	}
	
	public ItemStack glowItem(ItemStack i) {
		i.addUnsafeEnchantment(Enchantment.WATER_WORKER, 1);
		ItemMeta a = i.getItemMeta();
		a.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		i.setItemMeta(a);
		return i;
	}
	
	@SuppressWarnings("deprecation")
	public void fixCat(Player p, int slot, Boolean admin) {
		int s = 0;
		Inventory myInv = curInv.get(p);
		while (s < 8) {
			s++;
			if (myInv.getItem(s).getItemMeta().hasEnchants() & s != slot) {
				ItemStack i = myInv.getItem(s);
				ItemStack newI = getItem(i.getType().toString() + ":" + i.getData().getData() + ":1:none:none:none");
				ItemMeta newIM = newI.getItemMeta();
				newIM.setDisplayName(i.getItemMeta().getDisplayName());
				newI.setItemMeta(newIM);
				myInv.setItem(s, newI);
			}
		}
		curInv.put(p, myInv);
	}
	
	public void selectCat(Player p, int itemSlot, ItemStack item, Boolean admin) {
		
		Inventory myInv = curInv.get(p);
		List<String> menus = getBlocks().getStringList("Menus");
		
		if ((srlHozVal.get(p)+1)*8-1 >= selCat.get(p) & srlHozVal.get(p)*8-9 < selCat.get(p)) {
			myInv.setItem(selCat.get(p)+1-srlHozVal.get(p), getItem(getBlocks().getString(menus.get(selCat.get(p)) + ".Block")));
		}
		
		String menuName = "";
		int selectedId = 0;
		int i = -1;
		for (String n : menus) {
			i++;
			if (getItem(getBlocks().getString(n + ".Block")).equals(item)) {
				menuName = n;
				selectedId = i;
				break;
			}
		}
		if (menuName.equalsIgnoreCase("")) {
			myInv.setItem(itemSlot, glowItem(item));
			return;
		}
		selCat.put(p, selectedId);
		
		srlVerVal.put(p, 1);
		
		List<String> blocks = getBlocks().getStringList(menuName + ".Blocks");
		int val = 0;
		if (blocks.size()-1 > 40) {
			myInv.setItem(53, getItem("STAINED_GLASS:5:1:none:&aS&8croll &aD&8own:none"));
		} else {
			myInv.setItem(53, getItem("STAINED_GLASS:14:1:none:&aS&8croll &aD&8own:none"));
		}
		myInv.setItem(17, getItem("STAINED_GLASS:14:1:none:&aS&8croll &aU&8p:none"));
		val = 8;
		int start = val;
		int slot = 9;
		for (String Items : blocks) {
			if (!(val < start)) {
				if (slot > 52) {
					break;
				}
				if (slot != 17 & slot != 26 & slot != 35 & slot != 44 & slot != 53) {
					if (admin) {
						myInv.setItem(slot, addRemGlow(p, getItem(Items + ":1:none:none:none"), true));
					} else {
						myInv.setItem(slot, addRemGlow(p, getItem(Items + ":1:none:none:none"), false));
					}
					slot++;
				} else {
					if (admin) {
						myInv.setItem(slot+1, addRemGlow(p, getItem(Items + ":1:none:none:none"), true));
					} else {
						myInv.setItem(slot+1, addRemGlow(p, getItem(Items + ":1:none:none:none"), false));
					}
					slot+=2;
				}
			} else {
				val++;
			}
			
		}
		if (slot < 53) {
			while (slot < 53) {
				if (slot != 17 & slot != 26 & slot != 35 & slot != 44 & slot != 53) {
					myInv.setItem(slot, getItem("AIR:0:1:none:none:none"));
				}
				slot++;
			}
		}
		myInv.setItem(itemSlot, glowItem(item));
		curInv.put(p, myInv);
		fixCat(p, selCat.get(p)-(srlHozVal.get(p)-1), admin);
		p.openInventory(myInv);
		if (!admin) {
			mnuVal.put(p, 5);
		} else {
			mnuVal.put(p, 2);
		}
	}
		
	public void scrollUp(Player p, List<ItemStack> items, int scrollCount, Boolean admin) {
		srlVerVal.put(p, scrollCount - 1);
		mnuVal.put(p, 2);
		Inventory myInv = curInv.get(p);
		if (!admin) {
			myInv = curInv.get(p);
			mnuVal.put(p, 5);
		}
		List<String> menus = getBlocks().getStringList("Menus");
		List<String> blocks = getBlocks().getStringList(menus.get(selCat.get(p)) + ".Blocks");
		if ((scrollCount*8) + (8*5) >= blocks.size()+27-1) {
			myInv.setItem(53, getItem("STAINED_GLASS:14:1:none:&aS&8croll &aD&8own:none"));
		} else {
			myInv.setItem(53, getItem("STAINED_GLASS:5:1:none:&aS&8croll &aD&8own:none"));
		}
		
		if (scrollCount-1 > 1) {
			myInv.setItem(17, getItem("STAINED_GLASS:5:1:none:&aS&8croll &aU&8p:none"));
		} else {
			myInv.setItem(17, getItem("STAINED_GLASS:14:1:none:&aS&8croll &aU&8p:none"));
		}
		
		int val = 0;
		
		val = 8;
		int start = srlVerVal.get(p)*(val);
		int slot = 9;
		for (String Items : blocks) {
			if (!(val < start)) {
				if (slot > 52) {
					break;
				}
				if (slot != 17 & slot != 26 & slot != 35 & slot != 44 & slot != 53) {
					if (admin) {
						myInv.setItem(slot, addRemGlow(p, getItem(Items + ":1:none:none:none"), true));
					} else {
						myInv.setItem(slot, addRemGlow(p, getItem(Items + ":1:none:none:none"), false));
					}
					slot++;
				} else {
					if (admin) {
						myInv.setItem(slot+1, addRemGlow(p, getItem(Items + ":1:none:none:none"), true));
					} else {
						myInv.setItem(slot+1, addRemGlow(p, getItem(Items + ":1:none:none:none"), false));
					}
					slot+=2;
				}
			} else {
				val++;
			}
			
		}
		if (slot < 53) {
			while (slot < 53) {
				if (slot != 17 & slot != 26 & slot != 35 & slot != 44 & slot != 53) {
					myInv.setItem(slot, getItem("AIR:0:1:none:none:none"));
				}
				slot++;
			}
		}
		curInv.put(p, myInv);
		p.openInventory(myInv);
		if (!admin) {
			mnuVal.put(p, 5);
		} else {
			mnuVal.put(p, 2);
		}
	}
	
	public void scrollDown(Player p, List<ItemStack> items, int scrollCount, Boolean admin) {
		srlVerVal.put(p, scrollCount + 1);
		mnuVal.put(p, 2);
		Inventory myInv = curInv.get(p);
		if (!admin) {
			myInv = curInv.get(p);
			mnuVal.put(p, 5);
		}
		List<String> menus = getBlocks().getStringList("Menus");
		List<String> blocks = getBlocks().getStringList(menus.get(selCat.get(p)) + ".Blocks");
		if ((scrollCount*8) + (8*5) >= blocks.size()+9-1) {
			myInv.setItem(53, getItem("STAINED_GLASS:14:1:none:&aS&8croll &aD&8own:none"));
		} else {
			myInv.setItem(53, getItem("STAINED_GLASS:5:1:none:&aS&8croll &aD&8own:none"));
		}
		
		if (scrollCount+1 > 1) {
			myInv.setItem(17, getItem("STAINED_GLASS:5:1:none:&aS&8croll &aU&8p:none"));
		} else {
			myInv.setItem(17, getItem("STAINED_GLASS:14:1:none:&aS&8croll &aU&8p:none"));
		}
		
		int val = 0;
		
		
		val = 8;
		int start = srlVerVal.get(p)*(val);
		int slot = 9;
		for (String Items : blocks) {
			if (!(val < start)) {
				if (slot > 52) {
					break;
				}
				
				if (slot != 17 & slot != 26 & slot != 35 & slot != 44 & slot != 53) {
					if (admin) {
						myInv.setItem(slot, addRemGlow(p, getItem(Items + ":1:none:none:none"), true));
					} else {
						myInv.setItem(slot, addRemGlow(p, getItem(Items + ":1:none:none:none"), false));
					}
					slot++;
				} else {
					if (admin) {
						myInv.setItem(slot+1, addRemGlow(p, getItem(Items + ":1:none:none:none"), true));
					} else {
						myInv.setItem(slot+1, addRemGlow(p, getItem(Items + ":1:none:none:none"), false));
					}
					slot+=2;
				}
			}
			val++;
		}
		if (slot < 53) {
			while (slot < 53) {
				if (slot != 17 & slot != 26 & slot != 35 & slot != 44 & slot != 53) {
					myInv.setItem(slot, getItem("AIR:0:1:none:none:none"));
				}
				slot++;
			}
		}
		curInv.put(p, myInv);
		p.openInventory(myInv);
		if (!admin) {
			mnuVal.put(p, 5);
		} else {
			mnuVal.put(p, 2);
		}
	}
	
	public void scrollRight(Player p, List<ItemStack> items, int scrollCount, Boolean admin) {
		srlHozVal.put(p, scrollCount + 1);
		scrollCount = scrollCount + 1;
		int i = 0;
		mnuVal.put(p, 2);
		Inventory myInv = curInv.get(p);
		if (!admin) {
			myInv = curInv.get(p);
			mnuVal.put(p, 5);
		}
		int distMenu = 6;
		int val = 0;
		if (scrollCount + 6 >= items.size()-1) {
			myInv.setItem(8, getItem("STAINED_GLASS:14:1:none:&aS&8croll &aR&8ight:none"));
		}
		if (scrollCount > 0) {
			myInv.setItem(0, getItem("STAINED_GLASS:5:1:none:&aS&8croll &aL&8eft:none"));
		}
		for (ItemStack item : items) {
				if (scrollCount <= i) {
					if (!(val > distMenu)) {
						if (selCat.get(p) == i) {
							myInv.setItem(val + 1, glowItem(item));
							val++;
						} else {
							myInv.setItem(val + 1, item);
							val++;
						}
					}
					
			}
			i++;
		}
		curInv.put(p, myInv);
		fixCat(p, selCat.get(p)-(srlHozVal.get(p)-1), admin);
		p.openInventory(myInv);
		if (!admin) {
			mnuVal.put(p, 5);
		} else {
			mnuVal.put(p, 2);
		}
	}
	
	public void scrollLeft(Player p, List<ItemStack> items, int scrollCount, Boolean admin) {
		srlHozVal.put(p, scrollCount - 1);
		scrollCount = scrollCount - 1;
		int i = 0; 
		mnuVal.put(p, 2);
		Inventory myInv = curInv.get(p);
		if (!admin) {
			myInv = curInv.get(p);
			mnuVal.put(p, 5);
		}
		if (scrollCount + 6 <= items.size()-1) {
			myInv.setItem(8, getItem("STAINED_GLASS:5:1:none:&aS&8croll &aR&8ight:none"));
		}
		if (scrollCount <= 0) {
			myInv.setItem(0, getItem("STAINED_GLASS:14:1:none:&aS&8croll &aL&8eft:none"));
		}
		int distMenu = 6;
		int val = 0;
		for (ItemStack item : items) {
				if (scrollCount <= i) {
					if (!(val > distMenu)) {
						if (selCat.get(p) == i) {
							myInv.setItem(val + 1, glowItem(item));
							val++;
							
						} else {
							myInv.setItem(val + 1, item);
							val++;
						}
					}
					
			}
			i++;
		}
		curInv.put(p, myInv);
		fixCat(p, selCat.get(p)-(srlHozVal.get(p)-1), admin);
		p.openInventory(myInv);
		if (!admin) {
			mnuVal.put(p, 5);
		} else {
			mnuVal.put(p, 2);
		}
	}
	
	
	/* - Menu Values
	 *  - Admin Main Menu (0)
	 *  - Admin View List (1)
	 *  - Admin Add/Remove (2)
	 *  - Player Main Menu (3)
	 *  - Player View List (4)
	 *  - Player Add/Remove (5)
	 */
	
	public void mainGUIMenu(Player p, Boolean admin) {
		p.closeInventory();
		if (admin) {
			if (p.hasPermission("itemfilterpickup.admin") || p.hasPermission("itemfilterpickup.admin.edit")) {
				mnuVal.put(p, 0);
				srlVerVal.put(p, 0);
				Inventory myInv = Bukkit.createInventory(null, 9, ct("&4Admin &7Main Menu"));
				myInv.setItem(4, getItem("LAVA_BUCKET:0:1:none:&6C&8lear &6L&8ist:none"));
				myInv.setItem(2, getItem("WATER_BUCKET:0:1:none:&aA&8dd &aM&8enu:none"));
				myInv.setItem(6, getItem("BUCKET:0:1:none:&9V&8iew &9L&8ist:none"));
				p.openInventory(myInv);
			} else {
				pSend(p, getConfig().getString("Messages.no-permission"));
			}
		} else {
			mnuVal.put(p, 3);
			srlHozVal.put(p, 0);
			Inventory myInv = Bukkit.createInventory(null, 9, ct("&9Player &7Main Menu"));
			myInv.setItem(4, getItem("LAVA_BUCKET:0:1:none:&6C&8lear &6L&8ist:none"));
			myInv.setItem(2, getItem("WATER_BUCKET:0:1:none:&aA&8dd &aM&8enu:none"));
			myInv.setItem(6, getItem("BUCKET:0:1:none:&aV&8iew &aL&8ist:none"));
			if (p.hasPermission("itemfilterpickup.public.view")) {
				myInv.setItem(8, getItem("BUCKET:0:1:none:&aV&8iew &aP&8ublic &aL&8ist:none"));	
			}
			
			p.openInventory(myInv);
		}
		
	}
	
	public List<ItemStack> getMenus() {
		List<ItemStack> list = new ArrayList<ItemStack>();
		for (String menu : getBlocks().getStringList("Menus")) {
			list.add(getItem(getBlocks().getString(menu + ".Block")));
		}
		return list;
	}
	
	public List<ItemStack> mnuItems(String mnu) {
		List<ItemStack> list = new ArrayList<ItemStack>();
		for (String item : getBlocks().getStringList(mnu + ".Blocks")) {
			list.add(getItem(item));
		}
		return list;
	}
	
	public ItemStack addRemGlow(Player p, ItemStack i, Boolean admin) {
		if (!admin) {
			for (String item : getPlayers().getStringList("Players." + p.getUniqueId().toString() + ".Items")) {
				ItemStack it = getItem(item + ":1:none:none:none");
				if (it.equals(i)) {
					if (listener.checkPrivateFilerC(p, it)) {
						if (listener.checkBeyondMaxC(p, it)) {
							it = glowItem(it);
							it = addLore(it, "&a&lIs No Longer Being Picked Up,&7Click To Remove Me");
							return it;
						} else {
							it = glowItem(it);
							it = addLore(it, "&c&lIs Still Being Picked Up,&7Click To Remove Me,&6You have reached the maximum,&6items you can pick to filter out");
							return it;
						}
						
						
					}
				}
				
			}
		} else {
			for (String item : getConfig().getStringList("Public Pickup Filter.Items")) {
				ItemStack it = getItem(item + ":1:none:none:none");
				if (it.equals(i)) {
					if (listener.checkPublicFilterGlow(p, it)) {
						it = glowItem(it);
						it = addLore(it, "&a&lIs No Longer Being Picked Up,&7Click To Remove Me");
						return it;					
					}
				}
				
			}
		}
		return i;
			
	}
	
	public void addremGUIMenu(Player p, Boolean admin) {
		mnuVal.put(p, 2);
		Inventory myInv = Bukkit.createInventory(null, 54, ct("&4Admin &7Add/Remove"));
		if (!admin) {
			myInv = Bukkit.createInventory(null, 54, ct("&9Player &7Add/Remove"));
			mnuVal.put(p, 5);
		}
		int maxItems = 0;
		int maxMenus = 0;
		int distMenu = 6;
		int val = 0;
		selCat.put(p, 0);
		srlVerVal.put(p, 1);
		srlHozVal.put(p, 0);
		List<String> menus = getBlocks().getStringList("Menus");
		List<String> blocks = getBlocks().getStringList(menus.get(selCat.get(p)) + ".Blocks");
		maxMenus = menus.size() - 1;
		maxItems = blocks.size() - 1;
		
		// Category Menu
		for (String menu : menus) {
				if (val <= distMenu) {
					if (selCat.get(p) == val) {
					try {
						ItemStack item = getItem(getBlocks().getString(menu + ".Block"));
						myInv.setItem(val + 1, glowItem(item));
					} catch (Exception ex) {
						addremGUIMenu(p, admin);
						return;
					}
					} else {
						myInv.setItem(val + 1, getItem(getBlocks().getString(menu + ".Block")));
					}
						
				}
				val++;
		}
		val = 8;
		int start = srlVerVal.get(p)*(val);
		int slot = 9;
		for (String Items : blocks) {
			if (!(val < start)) {
				if (slot > 52) {
					break;
				}
				
					if (slot != 17 & slot != 26 & slot != 35 & slot != 44 & slot != 53) {
						if (admin) {
							myInv.setItem(slot, addRemGlow(p, getItem(Items + ":1:none:none:none"), true));
						} else {
							myInv.setItem(slot, addRemGlow(p, getItem(Items + ":1:none:none:none"), false));
						}
						slot++;
					} else {
						if (admin) {
							myInv.setItem(slot+1, addRemGlow(p, getItem(Items + ":1:none:none:none"), true));
						} else {
							myInv.setItem(slot+1, addRemGlow(p, getItem(Items + ":1:none:none:none"), false));
						}
						slot+=2;
					}
			}
			val++;
		}	
		
		if (srlVerVal.get(p) > 1) {
			myInv.setItem(17, getItem("STAINED_GLASS:5:1:none:&aS&8croll &aU&8p:none"));
		} else {
			myInv.setItem(17, getItem("STAINED_GLASS:14:1:none:&aS&8croll &aU&8p:none"));
		}
		if (srlVerVal.get(p) < maxItems) {
			myInv.setItem(53, getItem("STAINED_GLASS:5:1:none:&aS&8croll &aD&8own:none"));
		} else {
			myInv.setItem(53, getItem("STAINED_GLASS:14:1:none:&aS&8croll &aD&8own:none"));
		}
		if (srlHozVal.get(p) > 0) {
			myInv.setItem(0, getItem("STAINED_GLASS:5:1:none:&aS&8croll &aL&8eft:none"));
		} else {
			myInv.setItem(0, getItem("STAINED_GLASS:14:1:none:&aS&8croll &aL&8eft:none"));
		}
		if (srlHozVal.get(p) < maxMenus) {
			myInv.setItem(8, getItem("STAINED_GLASS:5:1:none:&aS&8croll &aR&8ight:none"));
		} else {
			myInv.setItem(8, getItem("STAINED_GLASS:14:1:none:&aS&8croll &aR&8ight:none"));
		}
		myInv.setItem(44, getItem("CHEST:0:1:none:&9M&8ain &9M&8enu:none"));
		myInv.setItem(35, getItem("STAINED_GLASS:0:1:none: :none"));
		myInv.setItem(26, getItem("STAINED_GLASS:0:1:none: :none"));
		curInv.put(p, myInv);
		p.openInventory(myInv);
		if (!admin) {
			mnuVal.put(p, 5);
		} else {
			mnuVal.put(p, 2);
		}
		
	}
	
	public void viewGUIMenu(Player p, int page, Boolean admin, Boolean pubView) {
		int invI = 0;
		List<String> items = new ArrayList<String>();
		if (admin) {
			mnuVal.put(p, 1);
			srlVerVal.put(p, 0);
			srlHozVal.put(p, 0);
			items = getConfig().getStringList("Public Pickup Filter.Items");
		} else {
			mnuVal.put(p, 4);
			srlVerVal.put(p, 0);
			srlHozVal.put(p, 0);
			items = getPlayers().getStringList("Players."+p.getUniqueId().toString()+".Items");
		}
		double listLength = items.size();
		double length = 32;
		double pages = Math.ceil((double)listLength/(double)length);
		int i = 0;
		Inventory myInv = Bukkit.createInventory(null, 36, ct("&9Public &7List &r(" + page + "/" + (pages + "").replace(".0", "") + ")"));
		if (!pubView) {
			myInv = Bukkit.createInventory(null, 36, ct("&4Admin &7List &r(" + page + "/" + (pages + "").replace(".0", "") + ")"));
		}
		
		if (!admin) {
			myInv = Bukkit.createInventory(null, 36, ct("&9Player &7List &r(" + page + "/" + (pages + "").replace(".0", "") + ")"));
		}
		for (String item : items) {
			if (i < (length * page)) {
				if (i >= length * (page - 1)) {
					int max = listener.getMaxFilter(p);
					if (max >= i+1 || max == -1) {
						try {
							ItemStack aItem = getItem(item + ":1:none:none:none");
							myInv.setItem(invI, glowItem(aItem));
							
						} catch (Exception ex) {
							viewGUIMenu(p, page, admin, pubView);
							return;
						}
						
					} else {
						myInv.setItem(invI, getItem(item + ":1:none:none:none"));
					}
					
					invI++;
					i++;
					continue;
				}
			}
			i++;
		}

		if (page > 1) {
			myInv.setItem(33, getItem("STAINED_GLASS:14:1:none:&cP&8age &cB&8ack:none"));
		}
		if (listLength >= 32 && !(invI < 32)) {
			myInv.setItem(34, getItem("STAINED_GLASS:5:1:none:&aP&8age &aF&8orward:none"));
		}
		
		myInv.setItem(35, getItem("CHEST:0:1:none:&9M&8ain &9M&8enu:none"));
		p.openInventory(myInv);
		if (admin) {
			mnuVal.put(p, 1);
		} else {
			mnuVal.put(p, 4);
		}
	}
	
	private void help(Player p) {
		String sendMessage = "&7----- &6Item Filter Help &7-----\n";
		
		if (p.hasPermission("itemfilterpickup.user")) {
			sendMessage = 
							"&6/ifp {add/+} &7- Adds the item you are holding to your filter list.\n" + 
							"&6/ifp {rem/remove/del/delete/-}&7 - Removes the item you are holding from your filter list.\n" + 
							"&6/ifp clear&7 - Removes all items from your filter list\n" + 
							"&6/ifp toggle&7 - Enable/Disables the ability to pickup items in your filter list.\n" + 
							"&6/ifp list &7- Allows you to view the first page of your pickup filter list\n" + 
							"&6/ifp list {page#} &7- Allows you to chose a page of the pickup filter list to view\n" + 
							"&6/ifp help &7- This gives you more information about commands you can use\n";
			
		}
		if (p.hasPermission("itemfilterpickup.admin")){
			sendMessage = 
							"&6/ifpa {rem/remove/del/delete/-} &7- Removes the item you are holding from the public filter list.\n" + 
							"&6/ifpa clear &7- Removes all items from the public filter list\n" + 
							"&6/ifpa toggle &7- Enable/Disables the ability to pickup items in the public filter list.\n" +
							"&6/ifpa bypass &7- Enable/Disables the ability to pickup items in the public filter list for your self only.\n" +
							"&6/ifpa list &7- Allows you to view the first page of the public pickup filter list\n" + 
							"&6/ifpa list {page#} &7- Allows you to chose a page of the public pickup filter list to view\n" + 
							"&6/ifp reload &7- This will reload the Config and Player files\n";
		}
		if (!p.hasPermission("itemfilterpickup.admin") && p.hasPermission("itemfilterpickup.admin.edit")){
			sendMessage = 
							"&6/ifpa {rem/remove/del/delete/-} &7- Removes the item you are holding from the public filter list.\n" + 
							"&6/ifpa clear &7- Removes all items from the public filter list\n" + 
							"&6/ifpa list &7- Allows you to view the first page of the public pickup filter list\n" + 
							"&6/ifpa list {page#} &7- Allows you to chose a page of the public pickup filter list to view\n";
		}
		if (!p.hasPermission("itemfilterpickup.admin") && p.hasPermission("itemfilterpickup.public.bypass")){
			sendMessage = 
							"&6/ifpa bypass &7- Enable/Disables the ability to pickup items in the public filter list for your self only.\n";
		}
		if (!p.hasPermission("itemfilterpickup.admin") && p.hasPermission("itemfilterpickup.public.view")){
			sendMessage = 
							"&6/ifpa list &7- Allows you to view the first page of the public pickup filter list\n" + 
							"&6/ifpa list {page#} &7- Allows you to chose a page of the public pickup filter list to view\n";
		}
		if (!p.hasPermission("itemfilterpickup.admin") && p.hasPermission("itemfilterpickup.admin.reload")){
			sendMessage = 
							"&6/ifp reload &7- This will reload the Config and Player files\n";
		}
		if (!p.hasPermission("itemfilterpickup.admin") && p.hasPermission("itemfilterpickup.admin.toggle")){
			sendMessage = 
							"&6/ifp toggle&7 - Enable/Disables the ability to pickup items in your filter list.\n"; 
		}
		
		pSend(p, sendMessage);
	}

	private boolean checkPermsMsg(Player p, String string) {
		boolean state = false;
		for (String perms : string.split(",")) {
			if (p.hasPermission(perms)) {
				state = true;
			}
		}
		if (!state) {
			if (!string.equalsIgnoreCase("itemfilterpickup.admin")) {
				pSend(p, getConfig().getString("Messages.no-permission"));
			}
			
			return false;
		}
		return true;
	}

	public void clearList(Player p, Boolean admin) {
		if (admin) {
			getConfig().set("Public Pickup Filter.Items", new ArrayList<String>());
			saveConfig();
			pSend(p, getConfig().getString("Messages.public-clear-filter"));
		} else {
			getPlayers().set("Players." + p.getUniqueId().toString() + ".Items", new ArrayList<String>());
			savePlayers();
			pSend(p, getConfig().getString("Messages.clear-filter"));
		}		
	}

	@SuppressWarnings("unused")
	private void viewList(Player p, int page, Boolean admin) {
		int max = listener.getMaxFilter(p);
		List<String> filterList = new ArrayList<String>();
		if (false) {
			//filterList = database.getFilterList(p);
		} else {
			if (!admin) {
				filterList = getPlayers().getStringList("Players." + p.getUniqueId().toString() + ".Items");
			} else {
				filterList = getConfig().getStringList("Public Pickup Filter.Items");
			}
		}
		
		String filter = "";
		double listLength = filterList.size();
		double length = getConfig().getDouble("Settings.Page Length") + 1;
		double pages = Math.ceil((double)listLength/(double)length);
		int i = 1;
		for (String item : filterList) {
			item = item.split(":")[0];
			if (i < (length * page)) {
				if (i >= length * (page - 1)) {
					if (filter.equalsIgnoreCase("")) {
						if (!admin) {
							if (max >= i || max == -1) {
								filter = getConfig().getString("Messages.filter-item-layout").replace("%#%", i + "").replace("%ITEM%", "&a" + item);	
							} else {
								filter = getConfig().getString("Messages.filter-item-layout").replace("%#%", i + "").replace("%ITEM%", "&c" + item);
							}
						} else {
							filter = getConfig().getString("Messages.public-filter-item-layout").replace("%#%", i + "").replace("%ITEM%", "&a" + item);	
						}
						
					} else {
						if (!item.equalsIgnoreCase("")) {
							if (!admin) {
								if (max >= i || max == -1) {
									filter = filter + "\n" + getConfig().getString("Messages.filter-item-layout").replace("%#%", i + "").replace("%ITEM%", "&a" + item);
								} else {
									filter = filter + "\n" + getConfig().getString("Messages.filter-item-layout").replace("%#%", i + "").replace("%ITEM%", "&c" + item);
								}
							} else {
								filter = filter + "\n" + getConfig().getString("Messages.public-filter-item-layout").replace("%#%", i + "").replace("%ITEM%", "&a" + item);
							}
							
							
						}
					}
				}
			} else { break; }
			i++;
		}	
		if (!admin) {
			pSend(p, getConfig().getString("Messages.filter-layout").replace("%ITEMS%", filter).replace("%CUR_PAGE%", (page + "").replace(".0", "")).replace("%ALL_PAGE%", (pages + "").replace(".0", "")).replace("%LIST_SIZE%", (length + "").replace(".0", "")).replace("%ALL_ITEM_COUNT%", (listLength + "").replace(".0", "")).replace("%NL%", "\n"));
		} else {
			pSend(p, getConfig().getString("Messages.public-filter-layout").replace("%ITEMS%", filter).replace("%CUR_PAGE%", (page + "").replace(".0", "")).replace("%ALL_PAGE%", (pages + "").replace(".0", "")).replace("%LIST_SIZE%", (length + "").replace(".0", "")).replace("%ALL_ITEM_COUNT%", (listLength + "").replace(".0", "")).replace("%NL%", "\n"));
		}
		
	}

	@SuppressWarnings({ "unused", "deprecation" })
	public void removeItem(Player p, ItemStack i, Boolean admin) {
		if (false) {
			//if (database.removeItem(p, p.getItemInHand().getType().toString() + ":" + p.getItemInHand().getData().toString().split("\\(")[1].split("\\)")[0])) {
			//	pSend(p, getConfig().getString("Messages.remove-success-filter").replace("%ITEM%", p.getItemInHand().getType().toString()));
			//} else {
			//	pSend(p, getConfig().getString("Messages.remove-fail-filter").replace("%ITEM%", p.getItemInHand().getType().toString()));
			//}
		} else {
			List<String> removeItems = new ArrayList<String>();
			if (admin) {
				removeItems = getConfig().getStringList("Public Pickup Filter.Items");
			} else {
				removeItems = getPlayers().getStringList("Players." + p.getUniqueId().toString() + ".Items");
			}
				
			
					if (removeItems.remove(i.getType().toString() + ":" + i.getData().getData())) {
						if (admin) {
							getConfig().set("Public Pickup Filter.Items", removeItems);
							saveConfig();
							reloadConfig();
							pSend(p, getConfig().getString("Messages.public-remove-success-filter").replace("%ITEM%", i.getType().toString()));
						} else {
							getPlayers().set("Players." + p.getUniqueId().toString() + ".Items", removeItems);
							savePlayers();
							reloadPlayers();
							pSend(p, getConfig().getString("Messages.remove-success-filter").replace("%ITEM%", i.getType().toString()));
						}
						
						
					} else {
						if (admin) {
							pSend(p, getConfig().getString("Messages.public-remove-fail-filter").replace("%ITEM%", i.getType().toString()));
						} else {
							pSend(p, getConfig().getString("Messages.remove-fail-filter").replace("%ITEM%", i.getType().toString()));
						}
						
					}
			
		}
		
	}
	
	@SuppressWarnings({ "unused", "deprecation" })
	public void addItem(Player p, ItemStack i, Boolean admin) {
			if (false) {
				String item = i.getData().getData() + "";
				/*if (database.addToFilter(p, p.getItemInHand().getType().toString() + ":" + item)) {
					pSend(p, getConfig().getString("Messages.add-to-filter").replace("%ITEM%", p.getItemInHand().getType().toString()));
				} else {
					pSend(p, getConfig().getString("Messages.already-added-filter").replace("%ITEM%", p.getItemInHand().getType().toString()));
				}*/
				
			} else { 
				List<String> items = new ArrayList<String>();
				String item = i.getData().getData() + "";
				if (admin) {
					items = getConfig().getStringList("Public Pickup Filter.Items");
				} else {
					items = getPlayers().getStringList("Players." + p.getUniqueId().toString() + ".Items");
				}
				 
				
				if (items.contains(i.getType().toString() + ":" + item)) {
					if (admin) {
						pSend(p, getConfig().getString("Messages.public-already-added-filter").replace("%ITEM%", i.getType().toString()));
						return;
					} else {
						pSend(p, getConfig().getString("Messages.already-added-filter").replace("%ITEM%", i.getType().toString()));
						return;
					}
					
				}
				if (admin) {
					items.add(i.getType().toString() + ":" + item);
					getConfig().set("Public Pickup Filter.Items", items);
					saveConfig();
					reloadConfig();
					pSend(p, getConfig().getString("Messages.public-add-to-filter").replace("%ITEM%", i.getType().toString()));
				} else {
					items.add(i.getType().toString() + ":" + item);
					getPlayers().set("Players." + p.getUniqueId().toString() + ".Items", items);
					savePlayers();
					reloadPlayers();
					pSend(p, getConfig().getString("Messages.add-to-filter").replace("%ITEM%", i.getType().toString()));
				}
				
			}
	}

	@SuppressWarnings("unused")
	public void toggleStatus(Player p, Boolean admin) {
		if (false) {
			/*if (!database.getState(p)) {
				database.setState(p, 1);
				pSend(p, getConfig().getString("Messages.toggle-filter").replace("%STATE%", "&aON&r").replace("%STATE_REVERSE%", "&cOFF&r"));
			} else {
				database.setState(p, 0);
				pSend(p, getConfig().getString("Messages.toggle-filter").replace("%STATE%", "&cOFF&r").replace("%STATE_REVERSE%", "&aON&r"));
			}*/
			
		} else {
			if (admin) {
				if (!getConfig().getBoolean("Public Pickup Filter.Enabled")) {
					getConfig().set("Public Pickup Filter.Enabled", true);
					saveConfig();
					reloadConfig();
					pSend(p, getConfig().getString("Messages.public-toggle-filter").replace("%STATE%", "&aON&r").replace("%STATE_REVERSE%", "&cOFF&r"));
				} else {
					getConfig().set("Public Pickup Filter.Enabled", false);
					saveConfig();
					reloadConfig();
					pSend(p, getConfig().getString("Messages.public-toggle-filter").replace("%STATE%", "&cOFF&r").replace("%STATE_REVERSE%", "&aON&r"));
				}
			} else {
				if (!getPlayers().getBoolean("Players." + p.getUniqueId().toString() + ".Enabled")) {
					getPlayers().set("Players." + p.getUniqueId().toString() + ".Enabled", true);
					savePlayers();
					reloadPlayers();
					pSend(p, getConfig().getString("Messages.toggle-filter").replace("%STATE%", "&aON&r").replace("%STATE_REVERSE%", "&cOFF&r"));
				} else {
					getPlayers().set("Players." + p.getUniqueId().toString() + ".Enabled", false);
					savePlayers();
					reloadPlayers();
					pSend(p, getConfig().getString("Messages.toggle-filter").replace("%STATE%", "&cOFF&r").replace("%STATE_REVERSE%", "&aON&r"));
				}
			}
			
			
		}
	}
	
	public void pSend(Player p, String msg) {
		 p.sendMessage(colorChatb(msg));
	 }
	
	public String colorChatb(String message) {
		 return message.replace("&0", ChatColor.BLACK + "").replace("&1", ChatColor.DARK_BLUE + "").replace("&2", ChatColor.DARK_GREEN + "").replace("&3", ChatColor.DARK_AQUA + "").replace("&4", ChatColor.DARK_RED + "").replace("&5", ChatColor.DARK_PURPLE + "").replace("&6", ChatColor.GOLD + "").replace("&7", ChatColor.GRAY + "").replace("&8", ChatColor.DARK_GRAY + "").replace("&9", ChatColor.BLUE + "").replace("&a", ChatColor.GREEN + "").replace("&b", ChatColor.AQUA + "").replace("&c", ChatColor.RED + "").replace("&d", ChatColor.LIGHT_PURPLE + "").replace("&e", ChatColor.YELLOW + "").replace("&f", ChatColor.WHITE + "").replace("&l", ChatColor.BOLD + "").replace("&m", ChatColor.STRIKETHROUGH + "").replace("&n", ChatColor.UNDERLINE + "").replace("&o", ChatColor.ITALIC + "").replace("&r", ChatColor.RESET + "");
	 }
	
	 // Added the ability to have a custom chats config! - v0.8.2 - 10/3/2016
    private FileConfiguration playersConfig = null; //customConfig 
    private File players = null; //customConfigFile
    
    
    public void reloadPlayers() {
        if (players == null) {
        	players = new File(getDataFolder(), "players.yml");
        }
        playersConfig = YamlConfiguration.loadConfiguration(players);
        // Look for defaults in the jar
        Reader defConfigStream;
		try {
			defConfigStream = new InputStreamReader(this.getResource("players.yml"), "UTF8");
		
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            playersConfig.setDefaults(defConfig);
        }
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public FileConfiguration getPlayers() {
        if (playersConfig == null) {
            reloadPlayers();
        }
        return playersConfig;
    }
    
    public void savePlayers() {
        if (playersConfig == null || playersConfig == null) {
            return;
        }
        try {
        	getPlayers().save(players);
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, "Could not save config to " + players, ex);
        }
    }
    
    public void saveDefaultPlayers() {
        if (players == null) {
        	players = new File(getDataFolder(), "players.yml");
        }
        if (!players.exists()) {            
             this.saveResource("players.yml", false);
         }
    }
    
    
    private FileConfiguration blocksConfig = null; //customConfig 
    private File blocks = null; //customConfigFile
    
    
    public void reloadBlocks() {
        if (blocks == null) {
        	blocks = new File(getDataFolder(), "blocks.yml");
        }
        blocksConfig = YamlConfiguration.loadConfiguration(blocks);
        // Look for defaults in the jar
        Reader defConfigStream;
		try {
			defConfigStream = new InputStreamReader(this.getResource("blocks.yml"), "UTF8");
		
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            blocksConfig.setDefaults(defConfig);
        }
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public FileConfiguration getBlocks() {
        if (blocksConfig == null) {
            reloadBlocks();
        }
        return blocksConfig;
    }
    
    public void saveBlocks() {
        if (blocksConfig == null || blocksConfig == null) {
            return;
        }
        try {
        	getBlocks().save(blocks);
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, "Could not save config to " + blocks, ex);
        }
    }
    
    public void saveDefaultBlocks() {
        if (blocks == null) {
        	blocks = new File(getDataFolder(), "blocks.yml");
        }
        if (!blocks.exists()) {            
             this.saveResource("blocks.yml", false);
         }
    }
    //--- End of chats.yml writing tools
	
}
