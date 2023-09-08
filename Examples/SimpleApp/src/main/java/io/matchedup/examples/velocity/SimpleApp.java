package io.matchedup.examples.velocity;

import io.matchedup.api.MatchedUpClient;
import io.matchedup.examples.velocity.commands.JoinMatchCommand;
import io.matchedup.examples.velocity.listeners.MatchedUpEventListener;
import io.matchedup.examples.velocity.utils.Reader;

public class SimpleApp {

    private static final String ACCESS_KEY = "kc9MWqwhfzIuXrjOPydE1A==";
    private static final String SECRET_KEY = "6z81MysUX48jNXf5tw9ZuInUG0xMIVBQzHKcdMlAyjw=";

    public static void main(String[] args) {
        // Create client
        MatchedUpClient matchedUpClient = new MatchedUpClient(ACCESS_KEY, SECRET_KEY);

        // Register MatchedUp event listener
        new MatchedUpEventListener(matchedUpClient);

        // Connect and block thread
        System.out.println("Connecting to MatchedUp servers");
        if (matchedUpClient.connect()) {
            // Take input
            runInputLoop(matchedUpClient);
        }
        // Close
        matchedUpClient.close();
    }

    public static void runInputLoop(MatchedUpClient matchedUpClient) {
        String cmd = "";
        while (!cmd.trim().equalsIgnoreCase("exit")) {
            cmd = Reader.readString("Enter a command (join, exit): ", false);

            switch (cmd) {
                case "join":
                    JoinMatchCommand.execute(matchedUpClient);
                    break;
            }
        }
        System.out.println("Bye!");
    }

}
