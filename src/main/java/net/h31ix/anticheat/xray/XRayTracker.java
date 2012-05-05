package net.h31ix.anticheat.xray;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class XRayTracker {
    private Map<Player,Integer> diamond = new HashMap<Player,Integer>();
    private Map<Player,Integer> gold = new HashMap<Player,Integer>();
    private Map<Player,Integer> iron = new HashMap<Player,Integer>();
    private Map<Player,Integer> coal = new HashMap<Player,Integer>();
    private Map<Player,Integer> lapis = new HashMap<Player,Integer>();
    private Map<Player,Integer> redstone = new HashMap<Player,Integer>();
    private Map<Player,Integer> block = new HashMap<Player,Integer>();
    private Map<Player,Integer> totalblock = new HashMap<Player,Integer>();
    private static final ChatColor green = ChatColor.GREEN;
    private static final ChatColor white = ChatColor.WHITE;
    private static final ChatColor red = ChatColor.RED;
    private static final ChatColor gray = ChatColor.GRAY;
    private static final int DIVISOR = 100;
    private static final int RATIO_DIVISOR = 3;
    private static final int MIN_BLOCK_COUNT = 150;
    
    public XRayTracker()
    {
        
    }
    
    public void sendStats(CommandSender cs, Player player)
    {
        int t = 1;
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
            d = (diamond.get(player)/t)*DIVISOR;
        }  
        if(gold.get(player) != null)
        {
            g = (gold.get(player)/t)*DIVISOR;
        } 
        if(iron.get(player) != null)
        {
            i = (iron.get(player)/t)*DIVISOR;
        } 
        if(coal.get(player) != null)
        {
            c = (coal.get(player)/t)*DIVISOR;
        } 
        if(lapis.get(player) != null)
        {
            la = (lapis.get(player)/t)*DIVISOR;
        } 
        if(redstone.get(player) != null)
        {
            r = (redstone.get(player)/t)*DIVISOR;
        } 
        if(block.get(player) != null)
        {
            b = (block.get(player)/t)*DIVISOR;
        }  
        cs.sendMessage("--------------------["+green+"X-Ray Stats"+white+"]---------------------");
        ChatColor color = white;
        if(d >= b/RATIO_DIVISOR && t > MIN_BLOCK_COUNT)
        {
            color = red;
        }
        cs.sendMessage(gray+"Player: "+white+player.getName());
        cs.sendMessage(gray+"Total blocks broken: "+white+t);
        cs.sendMessage(gray+"Percent diamond ore: "+color+round(d)+"%");
        color = white;
        if(g >= b/RATIO_DIVISOR && t > MIN_BLOCK_COUNT)
        {
            color = red;
        }        
        cs.sendMessage(gray+"Percent gold ore: "+color+round(g)+"%");
        color = white;
        if(i >= b/RATIO_DIVISOR && t > MIN_BLOCK_COUNT)
        {
            color = red;
        }        
        cs.sendMessage(gray+"Percent iron ore: "+color+round(i)+"%");
        color = white;
        if(c >= b/RATIO_DIVISOR && t > MIN_BLOCK_COUNT)
        {
            color = red;
        }            
        cs.sendMessage(gray+"Percent coal ore: "+color+round(c)+"%");
        color = white;
        if(la >= b/RATIO_DIVISOR && t > MIN_BLOCK_COUNT)
        {
            color = red;
        }            
        cs.sendMessage(gray+"Percent lapis ore: "+color+round(la)+"%");
        color = white;
        if(r >= b/RATIO_DIVISOR && t > MIN_BLOCK_COUNT)
        {
            color = red;
        }            
        cs.sendMessage(gray+"Percent redstone ore: "+color+round(r)+"%");
        cs.sendMessage(gray+"Percent all other blocks: "+white+round(b)+"%");
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
    
    private float round(double num) 
    {
        float p = (float)Math.pow(10,1);
        num*=p;
        float tmp = Math.round(num);
        return (float)tmp/p;
    } 
  
    public void reset(Player player)
    {
        totalblock.put(player,1);
        diamond.put(player,0);
        iron.put(player,0);
        gold.put(player,0);
        coal.put(player,0);
        redstone.put(player,0);
        lapis.put(player,0);
        totalblock.put(player,0);
    }
}
