package net.h31ix.anticheat.manage;

import java.util.HashMap;
import java.util.Map;
import net.h31ix.anticheat.Anticheat;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

public class BlockManager
{
  public Map<Player, Boolean> place = new HashMap();
  public Map<Player, Boolean> broke = new HashMap();
  Anticheat plugin;

  public BlockManager(Anticheat plugin)
  {
    this.plugin = plugin;
  }

  public void logPlace(final Player player)
  {
      place.put(player, true);
      plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable()
      {
          @Override
          public void run()
          {
            place.put(player, Boolean.valueOf(false));
          }
      }, 1/2L);
  }

  public void logBreak(final Player player)
  {
      broke.put(player, true);
      plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable()
      {
          @Override
          public void run()
          {
            broke.put(player, Boolean.valueOf(false));
          }
      }, 2L);
  }

  public boolean justBroke(Player player)
  {
      if(broke.get(player) == null || broke.get(player) == false)
      {
          return false;
      }
      else
      {
          return true;
      }
  }

  public boolean justPlaced(Player player)
  {
      if(place.get(player) == null || place.get(player) == false)
      {
          return false;
      }
      else
      {
          return true;
      }
  }
}