package net.h31ix.anticheat.command;

import java.util.ArrayList;
import java.util.List;
import net.h31ix.anticheat.Anticheat;
import net.h31ix.anticheat.PlayerTracker;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandManager implements CommandExecutor {
    Anticheat plugin;
    PlayerTracker tracker;
    ChatColor red = ChatColor.RED;
    ChatColor yellow = ChatColor.YELLOW;
    ChatColor green = ChatColor.GREEN;
    
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
                        plugin.config.setLog(true);
                        cs.sendMessage("Logging enabled. Check the console.");
                    }
                    else if(args[1].equalsIgnoreCase("disable"))
                    {
                        plugin.config.setLog(false);
                        cs.sendMessage("Logs will no longer be sent to the console.");
                    }   
                    else
                    {
                        cs.sendMessage("Usage: /anticheat log [enable/disable]");
                    }
                }
            }
            else
            {
                cs.sendMessage("Unrecognized command.");
            }            
        }
        else if (args.length == 1)
        {
            if(args[0].equalsIgnoreCase("report"))
            {
                if(hasPermission("admin",cs))
                {
                    List<Player> high = new ArrayList<Player>();
                    List<Player> med = new ArrayList<Player>();
                    List<Player> low = new ArrayList<Player>();
                    for(Player player : cs.getServer().getOnlinePlayers())
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
                cs.sendMessage("Unrecognized command.");
            }              
        }
        else
        {
            cs.sendMessage("Unrecognized command.");
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
