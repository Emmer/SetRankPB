package com.baummann.setrankpb;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Properties;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import com.baummann.setrankpb.commands.CommandPromotion;
import com.baummann.setrankpb.commands.CommandRank;
import com.iCo6.iConomy;
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
	public static String version = "1.2.1";
	final Plugin setRankPB = this;
    public String noPermission = ChatColor.RED + "You don't have Permission to use this!";
    public iConomy iConomy;
    public boolean useiConomy = false;
    public boolean useEconomy = false;
    public LinkedHashMap<String, Double> prices = new LinkedHashMap<String, Double>(); 
    public HashMap<Player, String> confirming = new HashMap<Player, String>();
    private final SRPBPlayerListener playerListener = new SRPBPlayerListener(this);
    public void onEnable() {
    	System.out.println("[SetRankPB] Enabling...");
    	if (!new File("plugins/SetRankPB").exists()) new File("plugins/SetRankPB").mkdir();
    	registerEvents();
    	loadConfig();
    	Plugin permBukkit = getServer().getPluginManager().getPlugin("PermissionsBukkit");
    	Plugin iconomy = getServer().getPluginManager().getPlugin("iConomy");
    	if (iconomy != null) {iConomy = (iConomy)iconomy; System.out.println("[SetRankPB] Found iConomy."); useiConomy = true;}
    	else {System.out.println("Couldn't find iConomy");}
    	if (permBukkit != null) {canUse = true; plugin = (PermissionsPlugin)permBukkit;}
    	else canUse = false;
    	Plugin spout = getServer().getPluginManager().getPlugin("Spout");
    	if (spout != null) {
    		System.out.println("[SetRankPB] Found Spout.");
    		useSpout = true;
    	} else {
    		System.out.println("[SetRankPB] Couldn't find Spout, not using achievements.");
    	    achievements = false;
    	}
    	getCommand("promotion").setExecutor(new CommandPromotion(this));
    	getCommand("rank").setExecutor(new CommandRank(this));
    	System.out.println("[SetRankPB] Enabled. Version " + version);
    }
    
    public void registerEvents() {
    	PluginManager pm = getServer().getPluginManager();
    	pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Event.Priority.Normal, this);
    	pm.registerEvent(Event.Type.PLAYER_CHAT, playerListener, Event.Priority.Normal, this);
    }
    
    public void onDisable() {
    	System.out.println("[SetRankPB] Disabling...");
    	getServer().getScheduler().cancelTasks(this);
    	System.out.println("[SetRankPB] Disabled.");
    }
    
    public RankHandler getHandler() {
    	return new RankHandler(this);
    }
    
    @SuppressWarnings("unchecked")
    public void loadConfig() {
    	Configuration config = getConfiguration();
    	config.load();
    	config.setHeader("#SetRankPB config file", 
    			"#What are prices?:",
    			"#Prices are prices for ranks. Using prices players can buy ranks.",
    			"#Prices only work with the iConomy plugin.",
    			"#If you don't want prices, just set \"use\" to false.",
    			"#An example for the prices config has been generated.");
    	prices = (LinkedHashMap<String, Double>)config.getProperty("SetRankPB.iConomy.prices");
    	if (prices == null) {
    		prices = new LinkedHashMap<String, Double>();
    		prices.put("exampleRank", 3.0);
    		config.setProperty("SetRankPB.iConomy.prices", prices);
    	}
    	useEconomy = config.getBoolean("SetRankPB.iConomy.use", true);
    	achievements = config.getBoolean("SetRankPB.Spout.use-achievements", true);
    	icon = config.getString("SetRankPB.Spout.achievement-icon", "diamond pickaxe");
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
