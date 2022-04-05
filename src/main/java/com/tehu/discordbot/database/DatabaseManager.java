package com.tehu.discordbot.database;

public interface DatabaseManager {
    DatabaseManager INSTANCE = new SQLiteDataSource();

    String getPrefix(long guildId);
    void setPrefix(long guildId, String newPrefix);
    boolean containsGuild(long guildId);
}
