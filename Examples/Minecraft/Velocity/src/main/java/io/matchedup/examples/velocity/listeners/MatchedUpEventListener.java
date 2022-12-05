package io.matchedup.examples.velocity.listeners;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import io.matchedup.api.MatchedUpClient;
import io.matchedup.api.events.error.ClientErrorEvent;
import io.matchedup.api.events.error.ServerErrorEvent;
import io.matchedup.api.events.error.UserErrorEvent;
import io.matchedup.api.events.match.MatchRequestCancelledEvent;
import io.matchedup.api.events.match.MatchCreatedEvent;
import io.matchedup.api.events.match.MatchRequestedEvent;
import io.matchedup.api.events.match.MatchRequestTimedOutEvent;
import io.matchedup.api.events.state.ConnectedEvent;
import io.matchedup.api.resources.MatchPlayer;
import io.matchedup.api.resources.MatchTicket;
import kotlin.Unit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MatchedUpEventListener {

    private final ProxyServer velocityServer;
    private final MatchedUpClient matchedUpClient;
    private final Logger log;

    public MatchedUpEventListener(ProxyServer velocityServer, MatchedUpClient matchedUpClient, Logger log) {
        this.velocityServer = velocityServer;
        this.matchedUpClient = matchedUpClient;
        this.log = log;

        registerListeners();
    }

    private void registerListeners() {
        matchedUpClient.getEventBus().registerListener(ConnectedEvent.class, this::onConnect);
        matchedUpClient.getEventBus().registerListener(MatchRequestedEvent.class, this::onMatchRequested);
        matchedUpClient.getEventBus().registerListener(MatchRequestTimedOutEvent.class, this::onMatchTimedOut);
        matchedUpClient.getEventBus().registerListener(MatchRequestCancelledEvent.class, this::onMatchCancelled);
        matchedUpClient.getEventBus().registerListener(MatchCreatedEvent.class, this::onMatchCreated);

        matchedUpClient.getEventBus().registerListener(UserErrorEvent.class, this::onUserError);
        matchedUpClient.getEventBus().registerListener(ClientErrorEvent.class, this::onClientError);
        matchedUpClient.getEventBus().registerListener(ServerErrorEvent.class, this::onServerError);
    }

    public Unit onConnect(ConnectedEvent event) {
        log.info("Connected to MatchedUp");
        return null;
    }

    private Unit onMatchRequested(MatchRequestedEvent event) {
        for (MatchPlayer player : event.players) {
            sendMessage(event.players, String.format("Match requested, ticket ID is %s!", event.ticketId));
        }
        return null;
    }

    private Unit onMatchTimedOut(MatchRequestTimedOutEvent event) {
        for (MatchTicket ticket : event.tickets) {
            sendMessage(ticket.players, "Match request timed out!");
        }
        return null;
    }

    private Unit onMatchCancelled(MatchRequestCancelledEvent event) {
        for (MatchTicket ticket : event.tickets) {
            sendMessage(ticket.players, "Match request was cancelled!");
        }
        return null;
    }

    private Unit onMatchCreated(MatchCreatedEvent event) {
        for (MatchTicket ticket : event.tickets) {
            sendMessage(ticket.players, String.format("Match created with ID %s", event.getMatchId()));
        }
        return null;
    }

    private Unit onUserError(UserErrorEvent event) {
        sendMessage(event.uuid, event.error, NamedTextColor.RED);
        return null;
    }

    private Unit onClientError(ClientErrorEvent event) {
        log.error(String.format("We did something wrong when making a request to MatchedUp: %s", event.error));
        return null;
    }

    private Unit onServerError(ServerErrorEvent event) {
        log.warn(String.format("Something went wrong on MatchedUp's server: %s", event.error));
        return null;
    }

    private Unit sendMessage(List<MatchPlayer> players, String message) {
        for (MatchPlayer player : players) {
            sendMessage(player.getUuid(), message, NamedTextColor.WHITE);
        }
        return null;
    }

    private void sendMessage(String uuid, String message, NamedTextColor color) {
        Optional<Player> playerOpt = velocityServer.getPlayer(UUID.fromString(uuid));
        if (playerOpt.isEmpty()) {
            log.warn("Got match created for player {} but did not find them in the server", uuid);
        } else {
            playerOpt.get().sendMessage(Component.text(message, color));
        }
    }

}
