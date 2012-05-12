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

package net.h31ix.anticheat.xray;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class XRayTracker {
    private Map<String,Integer> diamond = new HashMap<String,Integer>();
    private Map<String,Integer> gold = new HashMap<String,Integer>();
    private Map<String,Integer> iron = new HashMap<String,Integer>();
    private Map<String,Integer> coal = new HashMap<String,Integer>();
    private Map<String,Integer> lapis = new HashMap<String,Integer>();
    private Map<String,Integer> redstone = new HashMap<String,Integer>();
    private Map<String,Integer> block = new HashMap<String,Integer>();
    private Map<String,Integer> totalblock = new HashMap<String,Integer>();
    private static final ChatColor GREEN = ChatColor.GREEN;
    private static final ChatColor WHITE = ChatColor.WHITE;
    private static final ChatColor RED = ChatColor.RED;
    private static final ChatColor GRAY = ChatColor.GRAY;
    private static final int DIVISOR = 100;
    private static final int RATIO_DIVISOR = 3;
    private static final int POWER = 10;
    private static final int MIN_BLOCK_COUNT = 100;
    
    public XRayTracker()
    {
        
    }
    
    public boolean sufficientData(String player)
    {
        return totalblock.get(player) != null && totalblock.get(player) >= MIN_BLOCK_COUNT;             
    }
    
    public void calculate(CommandSender cs, String player, double x, double b, String type)
    {
        ChatColor color = WHITE;
        if(x >= b/RATIO_DIVISOR)
        {
            color = RED;
        }    
        cs.sendMessage(GRAY+"Percent "+type+" ore: "+color+round(x)+"%");
    }
    public void sendMessage(CommandSender cs, String player, double t, double d, double g, double i, double c, double la, double r, double b)
    {
        cs.sendMessage("--------------------["+GREEN+"X-Ray Stats"+WHITE+"]---------------------");
        cs.sendMessage(GRAY+"Player: "+WHITE+player);
        cs.sendMessage(GRAY+"Total blocks broken: "+WHITE+t);
        calculate(cs,player,d,b,"diamond");
        calculate(cs,player,g,b,"gold");
        calculate(cs,player,i,b,"iron");
        calculate(cs,player,c,b,"coal");
        calculate(cs,player,la,b,"lapis");
        calculate(cs,player,r,b,"redstone");
        cs.sendMessage(GRAY+"Percent all other blocks: "+WHITE+round(b)+"%");
        cs.sendMessage("-----------------------------------------------------");        
    }
    
    public void sendStats(CommandSender cs, String player)
    {
        getStats(cs,player);
    }
    
    public void getStats(CommandSender cs, String player)
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
        sendMessage(cs,player,t,d,g,i,c,la,r,b);
    }
    
    public void addDiamond(String player)
    {
        addOre(player,diamond);     
    }   
    
    public void addGold(String player)
    {
        addOre(player,gold);  
    }   
    
    public void addCoal(String player)
    {
        addOre(player,coal);  
    }  
    
    public void addIron(String player)
    {
        addOre(player,iron); 
    }  
    
    public void addLapis(String player)
    {
        addOre(player,lapis); 
    } 
    
    public void addRedstone(String player)
    {
        addOre(player,redstone);
    }   
    
    public void addBlock(String player)
    {
        addOre(player,block); 
    } 
    
    public void addTotal(String player)
    {
        addOre(player,totalblock); 
    }    
    
    private void addOre(String player, Map<String,Integer> map)
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
        double number = num;
        float p = (float)Math.pow(POWER,1);
        number*=p;
        float tmp = Math.round(number);
        return (float)tmp/p;
    }
  
    public void reset(String player)
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
