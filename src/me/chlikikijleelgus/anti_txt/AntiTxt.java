package me.chlikikijleelgus.anti_txt;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class AntiTxt extends JavaPlugin implements Listener {
	FileConfiguration config;
	List<String> disallowed;
	
	public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        //Loads config, saves default if not present
        config = getConfig();
    	disallowed = config.getStringList("Disallowed Phrases");
    	config.options().copyDefaults(true);
        this.saveDefaultConfig();
        saveConfig();
    }
	
	public void onDisable() {}

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
    	if(cmd.getName().equalsIgnoreCase("atadd")){
    		if (args.length != 1) {
    	           return false;
    	        }
    		args [0] = (" " +args[0].toLowerCase() + " "); // Puts it into proper format for storage.
    		for(String s : disallowed){ // Checks for duplicates. 
    			if (args[0].equalsIgnoreCase(s)){
    				sender.sendMessage("Duplicate word found.");
    				return false;
    			}
    		}

    		disallowed.add(args[0]); // Add the word to the config file
    		config.set("Disallowed Phrases", disallowed);
    		saveConfig();
    		return true;
    	} //End command atadd
    	
    	if(cmd.getName().equalsIgnoreCase("atrem")){
    		if (args.length != 1) {
    	           return false;
    	        }
    		args[0] = (" " +args[0].toLowerCase() + " "); // Puts the word into the proper format

    		disallowed.remove(args[0]);
    		config.set("Disallowed Phrases", disallowed);
    		saveConfig();
    		return true;
    	} //End command atrem
    	
    	if(cmd.getName().equalsIgnoreCase("atlist")){
    		for(String s : disallowed){
    			sender.sendMessage(s);
    		}
    		return true;
    	} //End command atlist
    	return false; 
    }



    @EventHandler(ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
    	if(event.isCancelled()){return;}; //If the event is already cancelled, do nothing.
    	
    	String message = " "+ event.getMessage().toLowerCase() + " "; // adds in an extra spaces to detect disallowed phrases at the beginning/end of the line. Easier than checking for line begin.
    	
    	for(String s : disallowed){
    		if(message.indexOf(s) != -1){ // If message contains a forbidden word, cancel the chat event
    			event.setCancelled(true);
    			event.getPlayer().sendMessage(ChatColor.RED + "Text speak is not allowed here. Detected: " + s);
    			break;
    		}
    	}
    	
       
    }
}

