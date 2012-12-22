package net.h31ix.anticheat.util;

import org.bukkit.Material;

public enum ToolHardness {
	
	// NOTHING(null, 1.5)
	WOOD(0.75D),
	STONE(0.40D),
	IRON(0.25D),
	DIAMOND(0.20D),
	SHEARS(0.55D),
	GOLD(0.15D);
	
	double hardness;
	
	ToolHardness(double hard) {
		hardness = hard;
	}
	
	public static double getToolHardness(Material tool) {
		for (ToolHardness e : ToolHardness.values()) {
			if (tool.name().contains(e.name())) { return e.hardness; }
		}
		
		return 1.50D;
	}
}
