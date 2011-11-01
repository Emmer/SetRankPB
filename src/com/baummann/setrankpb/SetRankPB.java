package com.baummann.setrankpb;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Properties;

import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import com.baummann.setrankpb.commands.CommandRank;
import com.platymuus.bukkit.permissions.PermissionsPlugin;

public class SetRankPB extends JavaPlugin {
	protected PermissionsPlugin plugin;
	public Properties props = new Properties();
	public String md = "plugins/SetRankPB";
	public File config = new File(md + "/config.yml");
	public boolean canUse = false;
	public boolean useSpout = false;
	public boolean achievements;
	public String icon;
	public static String version = "1.2";
	final Plugin setRankPB = this;
    public String noPermission = ChatColor.RED + "You don't have Permission to use this!";
    public void onEnable() {
    	System.out.println("[SetRankPB] Enabling...");
    	if (!new File("plugins/SetRankPB").exists()) new File("plugins/SetRankPB").mkdir();
    	loadConfig();
    	Plugin permBukkit = getServer().getPluginManager().getPlugin("PermissionsBukkit");
    	if (permBukkit != null) {canUse = true; plugin = (PermissionsPlugin)permBukkit;}
    	else canUse = false;
    	Plugin spout = getServer().getPluginManager().getPlugin("Spout");
    	if (spout != null) {
    		System.out.println("[SetRankPB] Successfully detected Spout!");
    		useSpout = true;
    	} else {
    		System.out.println("[SetRankPB] Couldn't find Spout, not using achievements.");
    	}
    	getCommand("rank").setExecutor(new CommandRank(this));
    	System.out.println("[SetRankPB] Enabled. Version " + version);
    }
    
    public void onDisable() {
    	System.out.println("[SetRankPB] Disabling...");
    	getServer().getScheduler().cancelTasks(this);
    	System.out.println("[SetRankPB] Disabled.");
    }
    
    public RankHandler getHandler() {
    	return new RankHandler(this);
    }
    
    public void loadConfig() {
    	Configuration config = getConfiguration();
    	config.load();
    	achievements = config.getBoolean("setrankpb.use-achievements", true);
    	icon = config.getString("setrankpb.achievement-icon", "diamond pickaxe");
    	config.save();
    }
    
    @SuppressWarnings("rawtypes")
    public void reloadPermissions() {
    	if (!canUse)return;
    	plugin.getConfiguration().load();
    	Class pluginClass = plugin.getClass();
    	Method[] methods = pluginClass.getDeclaredMethods();
    	for (Method method : methods) {
    		if (method.getName().equals("refreshPermissions")) {
    			method.setAccessible(true);
    			try {
    				method.invoke(plugin);
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
    		}
    	}
    }
}
