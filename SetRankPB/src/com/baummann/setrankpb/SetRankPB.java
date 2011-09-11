package com.baummann.setrankpb;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Properties;

import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.baummann.setrankpb.commands.CommandRank;
import com.platymuus.bukkit.permissions.PermissionsPlugin;

public class SetRankPB extends JavaPlugin {
	protected PermissionsPlugin plugin;
	public Properties props = new Properties();
	public String md = "plugins/SetRankPB";
	public File config = new File(md + "/config.yml");
	public boolean canUse = false;
	public static String version = "1.0";
	final Plugin setRankPB = this;
    public String noPermission = ChatColor.RED + "You don't have Permission to use this!";
    public void onEnable() {
    	System.out.println("[SetRankPB] Enabling...");
    	Plugin permBukkit = getServer().getPluginManager().getPlugin("PermissionsBukkit");
    	if (permBukkit != null) {canUse = true; plugin = (PermissionsPlugin)permBukkit;}
    	else canUse = false;
    	System.out.println("[SetRankPB] Restoring RAM...");
    	getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
    		public void run() {
    			
    		}
    	}, 20 * 2);
    	getCommand("rank").setExecutor(new CommandRank(this));
    	System.out.println("[SetRankPB] Starting schedulers...");
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
