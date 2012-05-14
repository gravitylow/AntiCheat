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
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.h31ix.anticheat.event.*;
import net.h31ix.anticheat.manage.AnticheatManager;
import net.h31ix.anticheat.metrics.Metrics;
import net.h31ix.anticheat.xray.XRayListener;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Anticheat extends JavaPlugin 
{
    private static Anticheat plugin;
    private final List<Listener> eventList = new ArrayList<Listener>();
    private static boolean update = false;
    private static final int BYTE_SIZE = 1024;
    private static final Logger logger = Logger.getLogger("Minecraft");
    private static Configuration config;
    private static boolean verbose;
    private static String updateFolder;
    private static Metrics metrics;
    
    @Override
    public void onDisable() 
    {
        //Nothing to do.
    }

    @Override
    public void onEnable() 
    {
        plugin = this;
        if(!new File(this.getDataFolder()+"\\config.yml").exists())
        {
            saveDefaultConfig();
            if(verbose)
            {
                logger.log(Level.INFO,"[AC] Config file created");
            }            
        }        
        config = AnticheatManager.CONFIGURATION;
        verbose = config.verboseStartup();
        updateFolder = config.updateFolder();
        checkForUpdate();
        
        eventList.add(new PlayerListener());
        eventList.add(new BlockListener());
        eventList.add(new EntityListener());
        eventList.add(new VehicleListener());
        if(config.logXRay())
        {
            eventList.add(new XRayListener());
        }
        for(Listener listener : eventList)
        {
            getServer().getPluginManager().registerEvents(listener, this);
            if(verbose)
            {
                logger.log(Level.INFO,"[AC] Registered events for ".concat(listener.toString()));
            }            
        }
        getCommand("anticheat").setExecutor(new CommandHandler());
        if(verbose)
        {
            logger.log(Level.INFO,"[AC] Registered commands");
            logger.log(Level.INFO,"[AC] Finished loading.");
        } 
        if(update && config.autoUpdate())
        {
            if(verbose)
            {
                logger.log(Level.INFO,"[AC] Downloading the new update...");
            }  
            File file = new File("plugins\\"+updateFolder);
            if(!file.exists())
            {
                try 
                {
                    file.mkdir();
                } 
                catch (Exception ex) 
                {
                }
            }  
            try 
            {
                saveFile(file.getCanonicalPath()+"\\AntiCheat.jar", "http://dl.dropbox.com/u/38228324/AntiCheat.jar");
            } 
            catch (IOException ex) 
            {
            }             
        } 
        try 
        {
            metrics = new Metrics(this);
            metrics.start();
        }
        catch (IOException ex) 
        {
        }        
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
        if(verbose)
        {
            logger.log(Level.INFO,"[AC] AntiCheat update has been downloaded and will be installed on next launch.");
        }         
    }
  }
    
    private void checkForUpdate()
    {
        if(verbose)
        {
            logger.log(Level.INFO,"[AC] Checking for updates...");
        }    
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
            if(verbose)
            {
                logger.log(Level.INFO,"[AC] An update was found.");
            }            
        }
        else
        {
            if(verbose)
            {
                logger.log(Level.INFO,"[AC] No update found.");
            }            
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

