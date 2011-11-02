package com.baummann.setrankpb.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.baummann.setrankpb.RankHandler;
import com.baummann.setrankpb.SetRankPB;
import com.iCo6.system.Account;
import com.iCo6.system.Holdings;

public class CommandPromotion implements CommandExecutor {
    private SetRankPB plugin;
    
    public CommandPromotion(SetRankPB instance) {
    	plugin = instance;
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLine, String[] split) {
    	try {
    		plugin.loadConfig();
    		if (!(sender instanceof Player)) {
    			sender.sendMessage(ChatColor.RED + "That command can only be used in-game.");
    			return true;
    		}
    		Player player = (Player)sender;
    		if (split[0].equalsIgnoreCase("price")) {
    			if (player.hasPermission("setrankpb.iconomy.price")) {
    				if (plugin.prices.containsKey(split[1]) && plugin.useiConomy && plugin.useEconomy) {
    					player.sendMessage(ChatColor.GREEN + "The rank " + split[1] + ChatColor.GREEN + " costs " + plugin.prices.get(split[1]) + ".");
    				    Account account = new Account(player.getName());
    				    Holdings holdings = account.getHoldings();
    				    if (holdings.hasEnough(plugin.prices.get(split[1]))) {
    				    	player.sendMessage(ChatColor.GREEN + "Do you want to buy it? (Say \"Yes\" or \"No\")");
    				    	plugin.confirming.put(player, split[1]);
    				    	return true;
    				    } else {
    				    	player.sendMessage(ChatColor.RED + "You don't have enough money to buy that rank.");
    				    	return true;
    				    }
    				} else if (!plugin.useiConomy) {
    					player.sendMessage(ChatColor.RED + "The iConomy plugin hasn't been set up. Please contact the server admin.");
    					return true;
    				} else if (!plugin.useEconomy) {
    					player.sendMessage(ChatColor.RED + "This server doesn't use economy.");
    					return true;
    				} else if (!plugin.prices.containsKey(split[1])) {
    					player.sendMessage(ChatColor.RED + "That rank doesn't have a price or doesn't exist.");
    					return true;
    				} else {
    					player.sendMessage(ChatColor.RED + "Unknown error!");
    					return true;
    				}
    			} else {
    				player.sendMessage(plugin.noPermission);
    				return true;
    			}
    		}
    		if (split[0].equalsIgnoreCase("buy")) {
    			if (player.hasPermission("setrankpb.iconomy.buy")) {
    				Account account = new Account(player.getName());
    				Holdings holdings = account.getHoldings();
    				if (plugin.prices.containsKey(split[1])) {
    					if (holdings.hasEnough(plugin.prices.get(split[1]))) {
    						holdings.subtract(plugin.prices.get(split[1]));
    						RankHandler rankHandler = plugin.getHandler();
    						rankHandler.setRank(player, split[1]);
    						player.sendMessage(ChatColor.GREEN + "Rank bought.");
    						return true;
    					} else {
    						player.sendMessage(ChatColor.RED + "You don't have enough money.");
    						return true;
    					}
    				}
    			} else {
    				player.sendMessage(plugin.noPermission);
    				return true;
    			}
    		}
    		if (split[0].equalsIgnoreCase("whatami")) {
    			if (player.hasPermission("setrankpb.whatami")) {
    				player.sendMessage(ChatColor.GREEN + "You are " + plugin.getHandler().getRank(player));
    				return true;
    			}
    		}
    		player.sendMessage(ChatColor.RED + "Wrong Syntax! Commands:");
    		player.sendMessage(ChatColor.RED + "/promo price <rank> - View the price of a rank");
    		player.sendMessage(ChatColor.RED + "/promo buy <rank> - Buy a rank");
    		player.sendMessage(ChatColor.RED + "/promo whatami - Shows you what rank you are");
    		return true;
    	} catch (ArrayIndexOutOfBoundsException e) {
    		sender.sendMessage(ChatColor.RED + "Wrong Syntax! Commands:");
    		sender.sendMessage(ChatColor.RED + "/promo price <rank> - View the price of a rank");
    		sender.sendMessage(ChatColor.RED + "/promo buy <rank> - Buy a rank");
    		sender.sendMessage(ChatColor.RED + "/promo whatami - Shows you what rank you are");
    		return true;
    	}
    }
}
