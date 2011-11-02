package com.baummann.setrankpb;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.config.Configuration;
import org.getspout.spoutapi.player.SpoutPlayer;

public class RankHandler {
    public File permissionsFile = new File("plugins/PermissionsBukkit/config.yml");
    public static SetRankPB plugin;
    
    public RankHandler(SetRankPB instance) {
    	plugin = instance;
    }
    
    public Configuration config() {
    	if (!permissionsFile.exists()) {
    		try {
    			permissionsFile.getParentFile().createNewFile();
    			permissionsFile.createNewFile();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	}
    	Configuration config = new Configuration(permissionsFile);
    	config.load();
    	return config;
    }
    
    public void setRank(Player player, String rank) {
    	Configuration config = config();
    	config.load();
    	plugin.loadConfig();
    	List<String> group = new ArrayList<String>();
    	group.add(rank);
    	config.setProperty("users." + player.getName() + ".groups", group);
    	config.save();
    	plugin.reloadPermissions();
    }
    
    public void setOfflineRank(String player, String rank) {
    	Configuration config = config();
    	config.load();
    	plugin.loadConfig();
    	List<String> group = new ArrayList<String>();
    	group.add(rank);
    	config.setProperty("users." + player + ".groups", group);
    	config.save();
    	plugin.reloadPermissions();
    }
    
    @SuppressWarnings("unchecked")
    public String getRank(Player player) {
    	Configuration config = config();
    	config.load();
    	plugin.loadConfig();
    	ArrayList<String> groups = (ArrayList<String>) config.getProperty("users." + player.getName() + ".groups");
    	config.save();
    	String g = "";
    	for (String s : groups) {
    		g = s;
    	}
    	return g;
    }
    
    public void triggerAchievement(Player player, String rank) {
		try {
		    if (player instanceof SpoutPlayer && plugin.achievements && plugin.useSpout) {
			    try {((SpoutPlayer)player).sendNotification("SetRankPB", "You are now " + rank, Material.getMaterial(plugin.icon.toUpperCase().replace(" ", "_")));} catch (NullPointerException e) {System.out.println("[SetRankPB] WARNING: The material " + plugin.icon.toUpperCase().replace(" ", "_") + " does not exist. Using default.");((SpoutPlayer)player).sendNotification("SetRankPB", "You are now " + rank, Material.DIAMOND_PICKAXE);} catch (Exception e) {}
		    }
		} catch (NoClassDefFoundError e) {}
    }
}
