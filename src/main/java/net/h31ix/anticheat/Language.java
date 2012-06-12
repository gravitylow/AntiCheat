package net.h31ix.anticheat;

import org.bukkit.configuration.file.FileConfiguration;

public class Language
{
    private static String[] medAlert = null;
    private static String[] highAlert = null;
    private static String[] warning = null;
    private static String banReason = null;
    private static String banBroadcast = null;
    private static String kickReason = null;
    private static String kickBroadcast = null;

    public Language(FileConfiguration file)
    {
        medAlert = file.getList("alert.medium_alert").toArray(new String[file.getList("alert.medium_alert").size()]);
        highAlert = file.getList("alert.high_alert").toArray(new String[file.getList("alert.high_alert").size()]);
        warning = file.getList("warning.player_warning").toArray(new String[file.getList("warning.player_warning").size()]);
        banReason = file.getString("ban.ban_reason");
        banBroadcast = file.getString("ban.ban_broadcast");
        kickReason = file.getString("kick.kick_reason");
        kickBroadcast = file.getString("kick.kick_broadcast");
    }

    public String[] getMediumAlert()
    {
        return medAlert;
    }

    public String[] getHighAlert()
    {
        return highAlert;
    }

    public String[] getWarning()
    {
        return warning;
    }

    public String getBanReason()
    {
        return banReason;
    }

    public String getBanBroadcast()
    {
        return banBroadcast;
    }

    public String getKickReason()
    {
        return kickReason;
    }

    public String getKickBroadcast()
    {
        return kickBroadcast;
    }
}
