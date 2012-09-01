package net.h31ix.anticheat.manage;

import java.util.ArrayList;
import java.util.List;
import net.h31ix.anticheat.Anticheat;
import net.h31ix.anticheat.util.Configuration;
import net.h31ix.anticheat.util.Language;
import net.h31ix.anticheat.util.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class UserManager 
{
    private List<User> users = new ArrayList<User>();
    private static Configuration config;
    private static Language lang;
    private static final ChatColor GRAY = ChatColor.GRAY;
    private static final ChatColor GOLD = ChatColor.GOLD;
    private static final ChatColor YELLOW = ChatColor.YELLOW;
    private static final ChatColor RED = ChatColor.RED;
    
    public UserManager(Configuration config)
    {
        this.config = config;
        lang = config.getLang();
    }
    
    public User getUser(String name)
    {
        for(User user : users)
        {
            if(user.getName().equalsIgnoreCase(name))
            {
                return user;
            }
        }
        // Otherwise try to load them
        return loadUserFromFile(name);
    }
    
    public List<User> getUsers()
    {
        return users;
    }
    
    public void addUser(User user)
    {
        users.add(user);
    }
    
    public void remove(User user)
    {
        config.saveLevel(user.getName(), user.getLevel());
        users.remove(user);
    }
    
    public int safeGetLevel(String name)
    {
        User user = getUser(name);
        if(user == null)
        {
            return 0;
        }
        else
        {
            return user.getLevel();
        }
    }
    
    public void safeSetLevel(String name, int level)
    {
        User user = getUser(name);
        if(user != null)
        {
            user.setLevel(level);
        }
    }    
    
    public void safeReset(String name)
    {
        User user = getUser(name);
        if(user != null)
        {
            user.resetLevel();
        }
    }   
    
    public boolean addUserFromFile(String name)
    {
        User user = loadUserFromFile(name);
        if(user != null)
        {
            users.add(user);
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public User loadUserFromFile(String name)
    {
        int level = config.getLevel(name);
        if(level == -1)
        {
            return null;
        }
        else
        {
            return new User(name, level);
        }
    }
    
    public void alertMed(User user, CheckType type)
    {
        String [] message = lang.getMediumAlert();
        for(int i=0;i<message.length;i++)
        {
            message[i] = message[i].replaceAll("&player", GOLD + user.getName() + GRAY);
            message[i] = message[i].replaceAll("&check", GOLD + CheckType.getName(type) + GRAY);
            message[i] = message[i].replaceAll("medium", YELLOW + "medium" + GRAY);
        }
        Utilities.alert(message);
        execute(user, "Medium");
    }
    
    public void alertHigh(User user, CheckType type)
    {
        String [] message = lang.getHighAlert();
        for(int i=0;i<message.length;i++)
        {
            message[i] = message[i].replaceAll("&player", user.getName());
            message[i] = message[i].replaceAll("&check", CheckType.getName(type));
            message[i] = message[i].replaceAll("high", RED + "high" + GRAY);
        }
        Utilities.alert(message); 
        execute(user, "High");
    }
    
    public void execute(User user, String event)
    {
        final String result = config.getResult(event);
        final String name = user.getName();
        if (result.startsWith("COMMAND["))
        {
            String command = result.replaceAll("COMMAND\\[", "").replaceAll("]", "").replaceAll("&player", name).replaceAll("&world", user.getPlayer().getWorld().getName());
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
        }
        else if (result.equalsIgnoreCase("KICK"))
        {          
            user.getPlayer().kickPlayer(RED + lang.getKickReason());
            Bukkit.broadcastMessage(RED + lang.getKickBroadcast().replaceAll("&player", name));
        }
        else if (result.equalsIgnoreCase("WARN"))
        {
            String[] message = lang.getWarning();
            for (String string : message)
            {
                user.getPlayer().sendMessage(RED + string);
            }
        }
        else if (result.equalsIgnoreCase("BAN"))
        {
            user.getPlayer().setBanned(true);
            user.getPlayer().kickPlayer(RED + lang.getBanReason());
            user.getPlayer().getServer().broadcastMessage(RED + lang.getBanBroadcast().replaceAll("&player", name));           
        }
    }
    
}
