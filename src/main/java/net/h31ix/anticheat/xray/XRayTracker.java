package net.h31ix.anticheat.xray;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class XRayTracker {
    public Map<Player,Integer> diamond = new HashMap<Player,Integer>();
    public Map<Player,Integer> gold = new HashMap<Player,Integer>();
    public Map<Player,Integer> iron = new HashMap<Player,Integer>();
    public Map<Player,Integer> coal = new HashMap<Player,Integer>();
    public Map<Player,Integer> lapis = new HashMap<Player,Integer>();
    public Map<Player,Integer> redstone = new HashMap<Player,Integer>();
    public Map<Player,Integer> block = new HashMap<Player,Integer>();
    public Map<Player,Integer> totalblock = new HashMap<Player,Integer>();
    ChatColor green = ChatColor.GREEN;
    ChatColor white = ChatColor.WHITE;
    ChatColor red = ChatColor.RED;
    ChatColor yellow = ChatColor.YELLOW;
    ChatColor gray = ChatColor.GRAY;
    
    public XRayTracker()
    {
        
    }
    
    public void sendStats(CommandSender cs, Player player)
    {
        double t = 1;
        double d = 0;
        double g = 0;
        double i = 0;
        double c = 0;
        double la = 0;
        double r = 0; 
        double b = 0;
        if(totalblock.get(player) != null)
        {
            t = totalblock.get(player);
        }        
        if(diamond.get(player) != null)
        {
            d = (diamond.get(player)/t)*100;
        }  
        if(gold.get(player) != null)
        {
            g = (gold.get(player)/t)*100;
        } 
        if(iron.get(player) != null)
        {
            i = (iron.get(player)/t)*100;
        } 
        if(coal.get(player) != null)
        {
            c = (coal.get(player)/t)*100;
        } 
        if(lapis.get(player) != null)
        {
            la = (lapis.get(player)/t)*100;
        } 
        if(redstone.get(player) != null)
        {
            r = (redstone.get(player)/t)*100;
        } 
        if(block.get(player) != null)
        {
            b = (block.get(player)/t)*100;
        }  
        cs.sendMessage("--------------------["+green+"X-Ray Stats"+white+"]---------------------");
        ChatColor color = ChatColor.WHITE;
        if(d >= b/3 && t > 50)
        {
            color = ChatColor.RED;
        }
        cs.sendMessage(gray+"Total blocks broken: "+white+t);
        cs.sendMessage(gray+"Percent diamond ore: "+color+round(d,1)+"%");
        color = ChatColor.WHITE;
        if(g >= b/3 && t > 50)
        {
            color = ChatColor.RED;
        }        
        cs.sendMessage(gray+"Percent gold ore: "+color+round(g,1)+"%");
        color = ChatColor.WHITE;
        if(i >= b/3 && t > 50)
        {
            color = ChatColor.RED;
        }        
        cs.sendMessage(gray+"Percent iron ore: "+color+round(i,1)+"%");
        color = ChatColor.WHITE;
        if(c >= b/3 && t > 50)
        {
            color = ChatColor.RED;
        }            
        cs.sendMessage(gray+"Percent coal ore: "+color+round(c,1)+"%");
        color = ChatColor.WHITE;
        if(la >= b/3 && t > 50)
        {
            color = ChatColor.RED;
        }            
        cs.sendMessage(gray+"Percent lapis ore: "+color+round(la,1)+"%");
        color = ChatColor.WHITE;
        if(r >= b/3 && t > 50)
        {
            color = ChatColor.RED;
        }            
        cs.sendMessage(gray+"Percent redstone ore: "+color+round(r,1)+"%");
        cs.sendMessage(gray+"Percent all other blocks: "+white+round(b,1)+"%");
        cs.sendMessage("-----------------------------------------------------");
    }
    
    public void addDiamond(Player player)
    {
        addOre(player,diamond);     
    }   
    
    public void addGold(Player player)
    {
        addOre(player,gold);  
    }   
    
    public void addCoal(Player player)
    {
        addOre(player,coal);  
    }  
    
    public void addIron(Player player)
    {
        addOre(player,iron); 
    }  
    
    public void addLapis(Player player)
    {
        addOre(player,lapis); 
    } 
    
    public void addRedstone(Player player)
    {
        addOre(player,redstone);
    }   
    
    public void addBlock(Player player)
    {
        addOre(player,block); 
    } 
    
    public void addTotal(Player player)
    {
        addOre(player,totalblock); 
    }    
    
    private void addOre(Player player, Map<Player,Integer> map)
    {
        if(map.get(player) == null || map.get(player) == 0)
        {
            map.put(player,1);
        }
        else
        {
            int playerLevel = map.get(player);
            map.put(player, playerLevel+1);
        }          
    } 
    
  private float round(double Rval, int Rpl) 
  {
      float p = (float)Math.pow(10,Rpl);
      Rval = Rval * p;
      float tmp = Math.round(Rval);
      return (float)tmp/p;
  }    
}
