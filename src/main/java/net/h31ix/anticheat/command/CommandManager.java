package net.h31ix.anticheat.command;

import java.util.ArrayList;
import java.util.List;
import net.h31ix.anticheat.Anticheat;
import net.h31ix.anticheat.PlayerTracker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

public class CommandManager implements CommandExecutor {
    Anticheat plugin;
    PlayerTracker tracker;
    ChatColor red = ChatColor.RED;
    ChatColor yellow = ChatColor.YELLOW;
    ChatColor green = ChatColor.GREEN;
    ChatColor white = ChatColor.WHITE;
    List<Player> high = new ArrayList<Player>();
    List<Player> med = new ArrayList<Player>();
    List<Player> low = new ArrayList<Player>();  
    Server server = Bukkit.getServer();
    
    public CommandManager(Anticheat plugin)
    {
        this.plugin = plugin;
        this.tracker = plugin.tracker;
    }
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String alias, String[] args) {
        if(args.length == 2)
        {
            if(args[0].equalsIgnoreCase("log"))
            {
                if(hasPermission("admin",cs))
                {
                    if(args[1].equalsIgnoreCase("enable"))
                    {
                        if(!plugin.config.logConsole)
                        {
                            plugin.config.setLog(true);
                            cs.sendMessage(green+"Console logging enabled.");                            
                        }
                        else
                        {
                            cs.sendMessage(green+"Console logging is already enabled!");
                        }
                    }
                    else if(args[1].equalsIgnoreCase("disable"))
                    {
                        if(plugin.config.logConsole)
                        {
                            plugin.config.setLog(false);
                            cs.sendMessage(green+"Console logging disabled.");                            
                        }
                        else
                        {
                            cs.sendMessage(green+"Console logging is already disabled!");
                        }
                    }   
                    else
                    {
                        cs.sendMessage(red+"Usage: /anticheat log [enable/disable]");
                    }
                }
            }
            else if(args[0].equalsIgnoreCase("pardon"))
            {   
                if(hasPermission("admin",cs))
                {
                    List<Player> list = server.matchPlayer(args[1]);
                    if(list.size() == 1)
                    {
                        Player player = list.get(0);
                        getPlayers();
                        if(low.contains(player))
                        {
                            cs.sendMessage(player.getName()+red+" is already in Low Level!");
                        }
                        else if (med.contains(player) || high.contains(player))
                        {
                            tracker.reset(player);
                            cs.sendMessage(player.getName()+green+" has been reset to Low Level.");
                        }
                    }
                    else if(list.size() > 1)
                    {
                        cs.sendMessage(red+"Multiple players found by name: "+white+args[1]+red+".");
                    }
                    else
                    {
                        cs.sendMessage(red+"Player: "+white+args[1]+red+" not found.");
                    }
                }                
            }                 
            else
            {
                cs.sendMessage(red+"Unrecognized command.");
            }            
        }
        else if (args.length == 1)
        {
            if(args[0].equalsIgnoreCase("help"))
            {   
                if(hasPermission("admin",cs))
                {
                    cs.sendMessage("----------------------["+green+"AntiCheat"+white+"]----------------------");
                    cs.sendMessage("/AntiCheat "+green+"log [Enable/Disable]"+white+" - toggle logging");
                    cs.sendMessage("/AntiCheat "+green+"report"+white+" - get a detailed cheat report");
                    cs.sendMessage("/AntiCheat "+green+"reload"+white+" - reload AntiCheat configuration");
                    cs.sendMessage("/AntiCheat "+green+"help"+white+" - access this page");
                    cs.sendMessage("/AntiCheat "+green+"pardon [user]"+white+" - reset user's level");
                    cs.sendMessage("-----------------------------------------------------");
                }                
            }           
            else if(args[0].equalsIgnoreCase("report"))
            {
                if(hasPermission("admin",cs))
                {
                    getPlayers();
                    if(!low.isEmpty())
                    {
                        cs.sendMessage(green+"----Level: Low (Not likely hacking)----");
                        for(Player player : low)
                        {     
                            cs.sendMessage(green+player.getName());
                        } 
                    }
                    if(!med.isEmpty())
                    {                    
                        cs.sendMessage(yellow+"----Level: Medium (Possibly hacking/lagging)----");
                        for(Player player : med)
                        {     
                            cs.sendMessage(yellow+player.getName());
                        }  
                    }
                    if(!high.isEmpty())
                    {                    
                        cs.sendMessage(red+"----Level: High (Probably hacking or bad connection)----");
                        for(Player player : high)
                        {     
                            cs.sendMessage(red+player.getName());
                        }  
                    }
                }
            }
            else if(args[0].equalsIgnoreCase("reload"))
            {
                if(hasPermission("admin",cs))
                {
                    plugin.config.load();
                    cs.sendMessage(green+"AntiCheat configuration reloaded.");
                }
            }            
            else
            {
                cs.sendMessage(red+"Unrecognized command.");
            }              
        }
        else
        {
            cs.sendMessage(red+"Unrecognized command.");
        }          
        return true;
    }
    
    public boolean hasPermission(String permission, CommandSender cs)
    {
        if(cs instanceof Player)
        {
            if(((Player)cs).hasPermission("anticheat."+permission))
            {
                return true;
            }
            else
            {
                cs.sendMessage("Insufficient permissions.");
                return false;
            }
        }
        else
        {
            return true;
        }
    }
    public void getPlayers()
    {
        high.clear();
        med.clear();
        low.clear();
        for(Player player : server.getOnlinePlayers())
        {
            int level = tracker.getLevel(player);
            if(level <= 10)
            {
                low.add(player);
            }
            else if(level <= 40)
            {
                med.add(player);
            }
            else
            {
                high.add(player);
            }
        }        
    }
    public static void sort(int a[],int n)
    {
        int i, j,t=0;
        for(i = 0; i < n; i++)
        {
            for(j = 1; j < (n-i); j++)
            {
                if(a[j-1] > a[j])
                {
                    t = a[j-1];
                    a[j-1]=a[j];
                    a[j]=t;
                }
            }
        }
    }    
}
