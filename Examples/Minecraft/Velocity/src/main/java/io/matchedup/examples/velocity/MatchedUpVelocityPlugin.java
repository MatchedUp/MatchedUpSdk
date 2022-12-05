package io.matchedup.examples.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import io.matchedup.api.MatchedUpClient;
import io.matchedup.examples.velocity.commands.JoinMatchCommand;
import io.matchedup.examples.velocity.listeners.MatchedUpEventListener;
import org.slf4j.Logger;

@Plugin(
        id = "matchedup",
        name = "MatchedUp",
        version = "1.0-SNAPSHOT",
        authors = { "Jon Rapp" }
)
public class MatchedUpVelocityPlugin {

    private static final String ACCESS_KEY = "zo24gHk7QEvL8JoKApIjNg==";
    private static final String SECRET_KEY = "OHpjNaYyGxkrIOsv+Jq4oHQIHbwcoJ4z8pu5jjEAsDo=";

    private final ProxyServer velocityServer;
    private final Logger log;

    @Inject
    public MatchedUpVelocityPlugin(ProxyServer velocityServer, Logger log) {
        this.velocityServer = velocityServer;
        this.log = log;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        // Create client
        MatchedUpClient matchedUpClient = new MatchedUpClient(ACCESS_KEY, SECRET_KEY, log);

        // Register MatchedUp event listener
        new MatchedUpEventListener(velocityServer, matchedUpClient, log);

        // Connect without blocking thread
        matchedUpClient.connectAsync();

        // Register join command to interact with MatchedUp
        velocityServer.getCommandManager().register(
                velocityServer.getCommandManager().metaBuilder("join").build(),
                new JoinMatchCommand(matchedUpClient)
        );
    }

}
