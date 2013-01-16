package net.h31ix.anticheat.test;

import net.h31ix.anticheat.util.BlockHardness;
import org.bukkit.Material;
import org.junit.Assert;
import org.junit.Test;

public class BlockHardnessTest {

    @Test
    public void verifyExistence() {
        for(Material m : Material.values()) {
            if(!BlockHardness.hasBlockHardness(m) && m.isBlock() && m != Material.AIR && m != Material.BEDROCK) {
                Assert.fail("Material " + m + " is not accounted for by BlockHardness");

            }
        }
    }

    @Test
    public void verifyName() {
        for(Material m : Material.values()) {
            BlockHardness hardness = BlockHardness.getHardness(m);
            if(hardness != null && !hardness.name().equals(m.name())) {
                Assert.fail("BlockHardness " + hardness + " and its material " + m + " have mismatched names");
            }
        }
    }
}