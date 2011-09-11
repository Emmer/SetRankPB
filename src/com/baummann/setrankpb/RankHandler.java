package com.baummann.setrankpb;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.util.config.Configuration;

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
    	List<String> group = new ArrayList<String>();
    	group.add(rank);
    	config.setProperty("users." + player.getName() + ".groups", group);
    	config.save();
    	plugin.reloadPermissions();
    }
}
