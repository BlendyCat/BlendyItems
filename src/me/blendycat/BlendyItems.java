package me.blendycat;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class BlendyItems extends JavaPlugin implements Listener {

	public Permission renamePermission = new Permission("blendyitems.item.rename");
	public Permission lorePermission = new Permission("blendyitems.item.lore");

	@Override
	public void onEnable() {
		Bukkit.getServer().getPluginManager().registerEvents((Listener) this, (Plugin) this);
		getLogger().info("BlendyItems has been enabled");
	}

	@Override
	public void onDisable() {
		getLogger().info("BlendyItems has been disabled");
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// Renames item in player's hand
		if (cmd.getName().equalsIgnoreCase("rename") && sender instanceof Player) {
			Player player = (Player) sender;
			// Checks if player has permission to name items
			if (player.hasPermission("blendyitems.item.rename")) {
				// Checks if command has enough arguments
				if (args.length >= 1) {
					int f = player.getInventory().getHeldItemSlot();
					if (player.getInventory().getItem(f) != null) {
						// Gets item in hand
						ItemStack item = player.getInventory().getItemInMainHand();
						// Gets meta from item
						ItemMeta meta = item.getItemMeta();
						// Edits custom name in meta
						StringBuilder sb = new StringBuilder();
						for(String s : args){
							sb.append(s+" ");
						}
						meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', sb.toString().trim()));
						// Updates item meta
						item.setItemMeta(meta);
					} else {
						player.sendMessage(ChatColor.RED + "You must be holding an item in your hand to rename!");
					}
				} else {
					player.sendMessage(ChatColor.RED + "You need to specify what to name the item!");
				}
			} else {
				player.sendMessage(ChatColor.RED + "You don't have permission to rename items!");
			}
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("lore") && sender instanceof Player) {
			Player player = (Player) sender;
			// Checks if player has permission to add lore to items
			if (player.hasPermission("blendyitems.item.lore")) {
				// Checks if command has enough arguments
				if (args.length != 0) {
					int f = player.getInventory().getHeldItemSlot();
					if (player.getInventory().getItem(f) != null) {
						// Gets item in hand
						ItemStack item = player.getInventory().getItemInMainHand();
						// Gets meta from item
						ItemMeta meta = item.getItemMeta();
						// Edits custom name in meta
						StringBuilder sb = new StringBuilder();
						for (int x = 0; x < args.length; x++)
							sb.append(args[x]).append(" ");
						meta.setLore(getLoreFromString(sb.toString().trim()));
						// Updates item meta
						item.setItemMeta(meta);
					} else {
						player.sendMessage(ChatColor.RED + "You must be holding an item in your hand to add lore");
					}
				} else {
					player.sendMessage(ChatColor.RED + "You need to specify what lore to add");
				}
			} else {
				player.sendMessage(ChatColor.RED + "You don't have permission to add lore to items");
			}
			return true;
		}
		
		if(cmd.getName().equalsIgnoreCase("iron")&& sender instanceof Player){
			Player player = (Player) sender;
			if(player.hasPermission("blendyitems.iron")){
				if(player.getInventory().getItemInMainHand().equals(new ItemStack(Material.COBBLESTONE, 64))){
					player.getInventory().remove(new ItemStack(Material.COBBLESTONE,64));
					player.getInventory().addItem(new ItemStack(Material.IRON_INGOT,3));
				}else{
					player.sendMessage(ChatColor.RED+"You must be holding a stack of cobblestone for this to work!");
				}
				return true;
			}else player.sendMessage(ChatColor.RED+"You do not have permission!");
		}
		// End of commands
		return false;
	}

	public List<String> getLoreFromString(String input) {
		List<String> lore = new ArrayList<String>();
		String[] parts = input.split("//");
		for(String part : parts){
			lore.add(ChatColor.translateAlternateColorCodes('&', part));
		}
		return lore;
	}

	@EventHandler
	public void onShearSheep(PlayerShearEntityEvent event) {
		Entity sheep = event.getEntity();
		Player player = event.getPlayer();
		sheep.setCustomName(ChatColor.GREEN + player.getName() + "'s " + ChatColor.AQUA + "Sheep");
		sheep.setCustomNameVisible(true);
	}

	@EventHandler
	public void onDropItem(PlayerDropItemEvent e) {
		Entity item = e.getItemDrop();
		Item itemS = e.getItemDrop();
		Player player = e.getPlayer();
		String itemName = (itemS.getItemStack().getType().toString().toLowerCase());
		itemName = (itemName.substring(0, 1).toUpperCase() + (itemName).substring(1).replace('_', ' '));
		if (itemS.getItemStack().getItemMeta().getDisplayName() == null) {
			item.setCustomName(ChatColor.RED + player.getName() + "'s " + ChatColor.GOLD + itemName);
		} else {
			item.setCustomName(ChatColor.RED + player.getName() + "'s "
					+ itemS.getItemStack().getItemMeta().getDisplayName().toString());
		}
		item.setCustomNameVisible(true);
	}
	
}
