package net.gravitydevelopment.anticheat.config.providers;

import java.util.List;

public interface Lang {

    /**
     * Get the alert to send when a player's hack group changes.
     *
     * @return Alert
     */
    public List<String> ALERT();

    /**
     * Get the warning to send players when they enter a group.
     *
     * @return Warning.
     */
    public List<String> WARNING();

    /**
     * Get the warning to send players for spamming.
     *
     * @return Spam warning.
     */
    public String SPAM_WARNING();

    /**
     * Get the reason for kicking a player for spam.
     *
     * @return Spam kicking reason.
     */
    public String SPAM_KICK_REASON();

    /**
     * Get the reason for banning a player for spam.
     *
     * @return Spam banning reason.
     */
    public String SPAM_BAN_REASON();

    /**
     * Get the broadcast for kicking a player for spam.
     *
     * @return Spam kicking broadcast.
     */
    public String SPAM_KICK_BROADCAST();

    /**
     * Get the broadcast for banning a player for spam.
     *
     * @return Spam banning broadcast.
     */
    public String SPAM_BAN_BROADCAST();

    /**
     * Get the reason for banning a player.
     *
     * @return Banning reason.
     */
    public String BAN_REASON();

    /**
     * Get the broadcast for banning a player.
     *
     * @return Banning broadcast.
     */
    public String BAN_BROADCAST();

    /**
     * Get the reason for kicking a player.
     *
     * @return Kicking reason.
     */
    public String KICK_REASON();

    /**
     * Get the broadcast for kicking a player.
     *
     * @return Kicking broadcast.
     */
    public String KICK_BROADCAST();
}
