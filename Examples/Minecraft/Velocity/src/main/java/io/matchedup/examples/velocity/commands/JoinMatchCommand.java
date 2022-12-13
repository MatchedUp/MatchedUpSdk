package io.matchedup.examples.velocity.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import io.matchedup.api.MatchedUpClient;
import io.matchedup.api.actions.RequestMatch;
import io.matchedup.api.types.PlayerAttributeType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.HashMap;
import java.util.Map;

public class JoinMatchCommand implements SimpleCommand {

    private MatchedUpClient matchedUpClient;

    public JoinMatchCommand(MatchedUpClient matchedUpClient) {
        this.matchedUpClient = matchedUpClient;
    }

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player player)) {
            return;
        }

        // Validate the match name was provided
        if (invocation.arguments().length == 0) {
            player.sendMessage(Component.text("Please provide the match you'd like to join", NamedTextColor.RED));
            return;
        }

        // Vars for requesting a match
        String matchName = invocation.arguments()[0];
        Map<String, PlayerAttributeType> playerAttributes = new HashMap<>();

        // Add any given player attributes from args in format "<key>=<value>"
        for (int i = 1; i < invocation.arguments().length; i++) {
            String key = invocation.arguments()[i].split("=")[0];
            String value = invocation.arguments()[i].split("=")[1];
            playerAttributes.put(key, new PlayerAttributeType(value));
        }

        // Request match
        matchedUpClient.submitAction(new RequestMatch(player.getUniqueId().toString(), matchName, playerAttributes));
    }
}
