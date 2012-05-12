/*
 * AntiCheat for Bukkit.
 * Copyright (C) 2012 H31IX http://h31ix.net
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.h31ix.anticheat;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import net.h31ix.anticheat.event.*;
import net.h31ix.anticheat.manage.AnticheatManager;
import net.h31ix.anticheat.xray.XRayListener;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Anticheat extends JavaPlugin 
{
    private static Anticheat plugin;
    private final List<Listener> eventList = new ArrayList<Listener>();
    private static boolean update = false;
    private static final int BYTE_SIZE = 1024;
    
    @Override
    public void onDisable() 
    {
        if(update && AnticheatManager.CONFIGURATION.autoUpdate())
        {
            saveFile("plugins\\AntiCheat.jar", "http://dl.dropbox.com/u/38228324/AntiCheat.jar");
        }
    }

    @Override
    public void onEnable() 
    {
        plugin = this;
        checkForUpdate();
        if(!new File("plugins/AntiCheat/config.yml").exists())
        {
            saveDefaultConfig();
        }
        eventList.add(new PlayerListener());
        eventList.add(new BlockListener());
        eventList.add(new EntityListener());
        eventList.add(new VehicleListener());
        if(AnticheatManager.CONFIGURATION.logXRay())
        {
            eventList.add(new XRayListener());
        }
        for(Listener listener : eventList)
        {
            getServer().getPluginManager().registerEvents(listener, this);
        }
        getCommand("anticheat").setExecutor(new CommandHandler());
    } 
    
  private void saveFile(String file, String url) 
  {
    BufferedInputStream in = null;
    FileOutputStream fout = null;
    try
    {
      in = new BufferedInputStream(new URL(url).openStream());
      fout = new FileOutputStream(file);

      byte[] data = new byte[BYTE_SIZE];
      int count;
      while ((count = in.read(data, 0, BYTE_SIZE)) != -1)
      {
        fout.write(data, 0, count);
      }
    }
    catch(Exception ex)
    {
        
    }
    finally
    {
        try
        {
          if (in != null)
          {
            in.close();
          }
          if (fout != null)
          {
            fout.close();
          }
        }
        catch(Exception ex)
        {
        }
    }
  }
    
    private void checkForUpdate()
    {
        URL url = null;
        URLConnection urlConn = null;
        InputStreamReader  inStream = null;
        BufferedReader buff = null;  
        String v = "";
        try 
        {
            url  = new URL("http://dl.dropbox.com/u/38228324/anticheatVersion.txt");
            urlConn = url.openConnection();
            inStream = new InputStreamReader(urlConn.getInputStream());            
        } 
        catch(Exception ex)
        {
        }
        buff= new BufferedReader(inStream);
        try 
        {
          v =buff.readLine(); 
          urlConn = null;
          inStream = null;            
          buff.close(); 
          buff = null;
        } 
        catch (Exception ex) 
        {
        }
        if (!this.getDescription().getVersion().equalsIgnoreCase(v))
        {
            update = true;
        }
        else
        {
            update = false;
        }
    } 
    
    public static Anticheat getPlugin()
    {
        return plugin;
    }
    
    public static boolean isUpdated()
    {
        return !update;
    }
    
    public static String getVersion()
    {
        return plugin.getDescription().getVersion();
    }
}

