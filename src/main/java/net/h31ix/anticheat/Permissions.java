package net.h31ix.anticheat;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum Permissions {
	
	/* Check Nodes */
	ZOMBE_FLY("zombe.fly", true),
	ZOMBE_NOCLIP("zombe.noclip", true),
	ZOMBE_CHEAT("zombe.chest", true),
	FLY("", true),
	WATER_WALK("", true),
	NO_SWING("", true),
	FAST_BREAK("", true),
	FAST_PLACE("", true),
	SPAM("", true),
	SPRINT("", true),
	SNEAK("", true),
	SPEED("", true),
	SPIDER("", true),
	NOFALL("", true),
	FAST_BOW("", true),
	FAST_EAT("", true),
	FAST_HEAL("", true),
	FORCEFIELD("", true),
	XRAY("", true),
	LONG_REACH("", true),
	FAST_PROJECTILE("", true),
	ITEM_SPAM("", true),
	
	/* System Nodes */
	SYSTEM_LOG("system.log", false),
	SYSTEM_XRAY("system.xray", false),
	SYSTEM_RESET("system.reset", false),
	SYSTEM_SPY("system.spy", false),
	SYSTEM_HELP("system.help", false),
	SYSTEM_UPDATE("system.update", false),
	SYSTEM_REPORT("system.report", false),
	SYSTEM_RELOAD("system.reload", false);
	
	
	String permnode = "";
	boolean isCheckType;
	Permissions(String node, boolean isCheckTypeNode)
	{
		permnode = node;
		isCheckType = isCheckTypeNode;
	}
	
	public boolean get(CommandSender player) 
	{
		return getPermission(player);
	}
	
	public boolean getPermission(CommandSender player) 
	{
		return player.hasPermission(parsePermissionNode());
	}
	
	private String parsePermissionNode() 
	{
		if(!isCheckType) 
		{
			return "anticheat." + permnode;
		}
		else
		{
			return "anticheat.checks." + parseEnum();
		}
	}
	
	private String parseEnum() 
	{
		return this.name().toLowerCase().replace("_", "");
	}
	

}
