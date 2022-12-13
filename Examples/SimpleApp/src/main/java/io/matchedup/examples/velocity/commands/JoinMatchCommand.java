package io.matchedup.examples.velocity.commands;

import io.matchedup.api.MatchedUpClient;
import io.matchedup.api.actions.RequestMatch;
import io.matchedup.api.types.PlayerAttributeType;
import io.matchedup.examples.velocity.utils.Reader;

import java.util.HashMap;
import java.util.Map;

public class JoinMatchCommand {

    public static void execute(MatchedUpClient matchedUpClient) {
        // Get match info
        String matchName = Reader.readString("Enter the match name: ");
        String uuid = Reader.readString("Enter the player uuid: ");
        int attribCount = Reader.readInt("Enter the number of player attributes you want to add: ");

        // Build player attributes
        Map<String, PlayerAttributeType> playerAttributes = new HashMap<>();
        for (int i = 0; i < attribCount; i++) {
            String property = Reader.readString(String.format("(#%s) Enter the player attribute property", attribCount));
            PlayerAttributeType value = Reader.readPlayerAttributeType(
                    String.format("(#%s) Enter the player attribute value", attribCount)
            );
            playerAttributes.put(property, value);
        }

        // Request match
        matchedUpClient.submitAction(new RequestMatch(uuid, matchName, playerAttributes));
        System.out.printf("  > Requested match '%s' successfully %n", matchName);
    }
}
