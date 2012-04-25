package net.h31ix.anticheat.manage;

import net.h31ix.anticheat.Anticheat;

public class LoginManager {
    Anticheat plugin;
    boolean join;
    
    public LoginManager(Anticheat plugin)
    {
        this.plugin = plugin;
    }
    
    public boolean log()
    {
        //Log players entering the server. Don't allow them to join way too fast.
        if(join == true)
        {
            return false;
        }  
        else
        {
            join = true;
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() 
            {
                @Override
                public void run() 
                {
                    join = false;
                }
            },      5L);   
            return true;
        }         
    }   
}
