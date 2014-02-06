// Not production code yet. Still experimental and needs a lot of work.

package net.gravitydevelopment.anticheat.util;

import net.gravitydevelopment.anticheat.AntiCheat;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.HashMap;

public class Calibrator {

    private final AntiCheat plugin;
    private final Server server;
    private final Player player;

    private HashMap<String, Integer> ints = new HashMap<String, Integer>();
    private HashMap<String, Double> doubles = new HashMap<String, Double>();

    public Calibrator(AntiCheat plugin, Player player) {
        this.plugin = plugin;
        this.server = plugin.getServer();
        this.player = player;

        player.sendMessage("You have entered calibration mode.");
    }

    public Player getPlayer() {
        return player;
    }

    private void registerEvents(CalibrationStep step) {
        server.getPluginManager().registerEvents(step, plugin);
    }

    private void unregisterEvents(CalibrationStep step) {
        HandlerList.unregisterAll(step);
    }

    public class CalibrationStep implements Listener {

        private final String key;

        private int trials = 0;
        public static final int MAX_TRIALS = 10;

        public CalibrationStep(String key, String instruction) {
            this.key = key;
            registerEvents(this);
            getPlayer().sendMessage(instruction);
        }

        public void end() {
            unregisterEvents(this);
        }

        public long getTime() {
            return System.currentTimeMillis();
        }

        public String getKey() {
            return key;
        }

        public int addTrial() {
            return trials++;
        }
    }

    public class MinimumCalibrationStep extends CalibrationStep {

        public MinimumCalibrationStep(String key, String instruction) {
            super(key, instruction);
        }

        public void value() {

        }
    }

}
