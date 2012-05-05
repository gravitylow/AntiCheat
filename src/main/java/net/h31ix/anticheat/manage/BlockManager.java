package net.h31ix.anticheat.manage;

import java.util.HashMap;
import java.util.Map;
import net.h31ix.anticheat.Anticheat;
import org.bukkit.entity.Player;

public class BlockManager
{
  private Map<Player, Boolean> place = new HashMap();
  private Map<Player, Boolean> broke = new HashMap();
  private Anticheat plugin;

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
            place.put(player, false);
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
            broke.put(player, false);
          }
      }, 2L);
  }

  public boolean justBroke(Player player)
  {
      if(broke.get(player) == null)
      {
          return false;
      }
      else
      {
          return broke.get(player);
      }
  }

  public boolean justPlaced(Player player)
  {
      if(place.get(player) == null)
      {
          return false;
      }
      else
      {
          return place.get(player);
      }
  }
}