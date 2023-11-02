package com.agaloth.spawnershutdown.commands;

import com.agaloth.spawnershutdown.SpawnerShutdown;
import com.agaloth.spawnershutdown.listener.ChunkListener;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReloadCommand implements CommandExecutor, TabCompleter {
    private SpawnerShutdown plugin;
    ChunkListener chunkListener;
    MiniMessage minimessage;

    public ReloadCommand(SpawnerShutdown plugin) {
        this.plugin = plugin;
        this.chunkListener = new ChunkListener(this.plugin);
        initMiniMessage();
    }

    private void initMiniMessage() {
        minimessage = MiniMessage.builder()
                .tags(TagResolver.builder()
                        .resolver(StandardTags.color())
                        .resolver(StandardTags.decorations())
                        .resolver(StandardTags.gradient())
                        .resolver(StandardTags.rainbow())
                        .build()
                )
                .build();
    }

    private void sendPermissionMessage(CommandSender sender) {
        var parsed = minimessage.deserialize(Objects.requireNonNull(this.plugin.getConfig().getString("prefix")) + this.plugin.getConfig().getString("permission-message"));
        sender.sendMessage(parsed);
    }

    private void sendReloadMessage(CommandSender sender) {
        var parsed = minimessage.deserialize(Objects.requireNonNull(this.plugin.getConfig().getString("prefix")) + this.plugin.getConfig().getString("plugin-reload-message"));
        sender.sendMessage(parsed);
    }

    private void reloadBlacklist() {
        this.plugin.reloadConfig();
        FileConfiguration config = this.plugin.getConfig();
        List<String> spawnerBlacklist = config.getStringList("spawner-blacklist");
        this.plugin.updateBlacklist(spawnerBlacklist);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        switch (cmd.getName().toLowerCase()) {
            case "spawnershutdown":
            case "ssr":
            case "ss":
                if (!sender.hasPermission("spawnershutdown.reload")) {
                    sendPermissionMessage(sender);
                    return true;
                } else if(sender.hasPermission("spawnershutdown.reload")) {
                    if (args.length == 1) {
                        if(args[0].equalsIgnoreCase("reload")) {
                            reloadBlacklist();
                            sendReloadMessage(sender);
                        }

                    }
            }
                return true;
            default:
                return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("reload");
        }

        return completions;
    }
}
