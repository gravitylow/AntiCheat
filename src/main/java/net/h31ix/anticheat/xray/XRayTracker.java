/*
 * AntiCheat for Bukkit.
 * Copyright (C) 2012 AntiCheat Team | http://gravitydevelopment.net
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class XRayTracker {
	private Map<String, Integer> diamond = new HashMap<String, Integer>();
	private Map<String, Integer> gold = new HashMap<String, Integer>();
	private Map<String, Integer> iron = new HashMap<String, Integer>();
	private Map<String, Integer> lapis = new HashMap<String, Integer>();
	private Map<String, Integer> redstone = new HashMap<String, Integer>();
	private Map<String, Integer> block = new HashMap<String, Integer>();
	private Map<String, Integer> totalblock = new HashMap<String, Integer>();
	private List<String> alerted = new ArrayList<String>();
	private static final ChatColor GREEN = ChatColor.GREEN;
	private static final ChatColor WHITE = ChatColor.WHITE;
	private static final ChatColor RED = ChatColor.RED;
	private static final ChatColor GRAY = ChatColor.GRAY;
	private static final int RATIO_DIVISOR = 3;
	private static final int POWER = 10;
	private static final int MIN_BLOCK_COUNT = 100;
	
	public XRayTracker() {
	}
	
	public boolean sufficientData(String player) {
		return totalblock.get(player) != null && totalblock.get(player) >= MIN_BLOCK_COUNT;
	}
	
	public void calculate(CommandSender cs, String player, double x, double b, String type) {
		ChatColor color = WHITE;
		if (x >= b / RATIO_DIVISOR) {
			color = RED;
		}
		cs.sendMessage(GRAY + "Percent " + type + " ore: " + color + round(x) + "%");
	}
	
	public boolean calculate(String player, double x, double b) {
		return x >= b / RATIO_DIVISOR;
	}
	
	public boolean hasAbnormal(String player) {
		XRayStats stats = new XRayStats(player, diamond, gold, iron, lapis, redstone, block, totalblock);
		double total = stats.getOther();
		return calculate(player, stats.getDiamond(), total) || calculate(player, stats.getGold(), total) || calculate(player, stats.getIron(), total) || calculate(player, stats.getLapis(), total) || calculate(player, stats.getRedstone(), total);
	}
	
	public boolean hasAlerted(String player) {
		return alerted.contains(player);
	}
	
	public void logAlert(String player) {
		alerted.add(player);
	}
	
	public void sendStats(CommandSender cs, String player) {
		getStats(cs, player);
	}
	
	public void getStats(CommandSender cs, String player) {
		XRayStats stats = new XRayStats(player, diamond, gold, iron, lapis, redstone, block, totalblock);
		double total = stats.getOther();
		cs.sendMessage("--------------------[" + GREEN + "X-Ray Stats" + WHITE + "]---------------------");
		cs.sendMessage(GRAY + "Player: " + WHITE + player);
		cs.sendMessage(GRAY + "Total blocks broken: " + WHITE + stats.getTotal());
		calculate(cs, player, stats.getDiamond(), total, "diamond");
		calculate(cs, player, stats.getGold(), total, "gold");
		calculate(cs, player, stats.getIron(), total, "iron");
		calculate(cs, player, stats.getLapis(), total, "lapis");
		calculate(cs, player, stats.getRedstone(), total, "redstone");
		cs.sendMessage(GRAY + "Percent all other blocks: " + WHITE + round(stats.getOther()) + "%");
		cs.sendMessage("-----------------------------------------------------");
	}
	
	public void addDiamond(String player) {
		addOre(player, diamond);
	}
	
	public void addGold(String player) {
		addOre(player, gold);
	}
	
	public void addIron(String player) {
		addOre(player, iron);
	}
	
	public void addLapis(String player) {
		addOre(player, lapis);
	}
	
	public void addRedstone(String player) {
		addOre(player, redstone);
	}
	
	public void addBlock(String player) {
		addOre(player, block);
	}
	
	public void addTotal(String player) {
		addOre(player, totalblock);
	}
	
	private void addOre(String player, Map<String, Integer> map) {
		if (map.get(player) == null || map.get(player) == 0) {
			map.put(player, 1);
		} else {
			int playerLevel = map.get(player);
			map.put(player, playerLevel + 1);
		}
	}
	
	private float round(double num) {
		double number = num;
		float p = (float) Math.pow(POWER, 1);
		number *= p;
		float tmp = Math.round(number);
		return (float) tmp / p;
	}
	
	public void reset(String player) {
		totalblock.put(player, 1);
		diamond.put(player, 0);
		iron.put(player, 0);
		gold.put(player, 0);
		redstone.put(player, 0);
		lapis.put(player, 0);
		totalblock.put(player, 0);
	}
}
