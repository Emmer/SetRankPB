package com.baummann.setrankpb.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.player.SpoutPlayer;

import com.baummann.setrankpb.RankHandler;
import com.baummann.setrankpb.SetRankPB;

public class CommandRank implements CommandExecutor {
    public static SetRankPB plugin;
    private RankHandler handler;
    
    public CommandRank(SetRankPB instance) {
    	plugin = instance;
    	handler = plugin.getHandler();
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLine, String[] split) {
    	try {
        	if (sender instanceof Player) {
        		Player player = (Player)sender;
        		if (!plugin.canUse) {
        			player.sendMessage(ChatColor.RED + "---Warning---");
        			player.sendMessage(ChatColor.RED + "This server is not running PermissionsBukkit which makes the plugin unusable.");
        			player.sendMessage(ChatColor.RED + "Please download SuperpermsBridge and PermissionsBukkit to get the plugin to work.");
        			return true;
        		}
        		if (player.hasPermission("setrankpb.rankall") || player.hasPermission("setrankpb.rank." + split[1])) {
        			Player target = plugin.getServer().matchPlayer(split[0]).get(0);
        			handler.setRank(target, split[1]);
        			player.sendMessage("You changed " + target.getName() + "'s rank to " + split[1] + ".");
        			try {
        				if (target instanceof SpoutPlayer && plugin.achievements && plugin.useSpout) {
        				    try {((SpoutPlayer)target).sendNotification("SetRankPB", "You are now " + split[1], Material.getMaterial(plugin.icon.toUpperCase().replace(" ", "_")));} catch (NullPointerException e) {System.out.println("[SetRankPB] WARNING: The material " + plugin.icon.toUpperCase().replace(" ", "_") + " does not exist. Using default.");((SpoutPlayer)target).sendNotification("SetRankPB", "You are now " + split[1], Material.DIAMOND_PICKAXE);} catch (Exception e) {}
        			    }
        			} catch (NoClassDefFoundError e) {}
        			return true;
        		} else {
        			player.sendMessage(plugin.noPermission);
        			return true;
        		}
        	} else {
        		Player target = plugin.getServer().matchPlayer(split[0]).get(0);
        		handler.setRank(target, split[1]);
    			sender.sendMessage("You changed " + target.getName() + "'s rank to " + split[1] + ".");
    			try {
        		    if (target instanceof SpoutPlayer && plugin.achievements && plugin.useSpout) {
    				    try {((SpoutPlayer)target).sendNotification("SetRankPB", "You are now " + split[1], Material.getMaterial(plugin.icon.toUpperCase().replace(" ", "_")));} catch (NullPointerException e) {System.out.println("[SetRankPB] WARNING: The material " + plugin.icon.toUpperCase().replace(" ", "_") + " does not exist. Using default.");((SpoutPlayer)target).sendNotification("SetRankPB", "You are now " + split[1], Material.DIAMOND_PICKAXE);} catch (Exception e) {}
        		    }
    			} catch (NoClassDefFoundError e) {}
        		return true;
        	}
    	} catch (ArrayIndexOutOfBoundsException e) {
    		sender.sendMessage(ChatColor.RED + "Wrong syntax! Usage: /rank [Player] [Rank]");
    		return true;
    	} catch (IndexOutOfBoundsException e) {
    		sender.sendMessage(ChatColor.RED + "No such player!");
    		return true;
    	}
    }
}
