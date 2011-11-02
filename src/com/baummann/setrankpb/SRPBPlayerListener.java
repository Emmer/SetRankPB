package com.baummann.setrankpb;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.iCo6.system.Account;
import com.iCo6.system.Holdings;

public class SRPBPlayerListener extends PlayerListener {
    private SetRankPB plugin;
    
    public SRPBPlayerListener(SetRankPB instance) {
    	plugin = instance;
    }
    
	public void onPlayerChat(PlayerChatEvent event) {
    	try {
    		Player player = event.getPlayer();
        	String message = event.getMessage();
        	if (plugin.confirming.containsKey(player)) {
        		if (message.equalsIgnoreCase("Yes")) {
        			Account account = new Account(player.getName());
        			Holdings holdings = account.getHoldings();
        			holdings.subtract(plugin.prices.get(plugin.confirming.get(player)));
        			if (account == null || holdings == null) {
        				player.sendMessage(ChatColor.RED + "Your account couldn't be found.");
        			}
        			RankHandler rankHandler = plugin.getHandler();
        			rankHandler.setRank(player, plugin.confirming.get(player));
        			rankHandler.triggerAchievement(player, plugin.confirming.get(player));
        			player.sendMessage(ChatColor.GREEN + "Rank bought.");
        			event.setCancelled(true);
        		} else if (message.equalsIgnoreCase("No")) {
        			player.sendMessage(ChatColor.RED + "Operation aborted.");
        			event.setCancelled(true);
        		} else {
        			player.sendMessage(ChatColor.RED + "Didn't receive Yes/No. Operation aborted.");
        			event.setCancelled(true);
        		}
        		plugin.confirming.remove(player);
        	}
    	} catch (Exception e) {e.printStackTrace();}
    }
    
    public void onPlayerQuit(PlayerQuitEvent event) {
    	plugin.confirming.remove(event.getPlayer());
    }
}
