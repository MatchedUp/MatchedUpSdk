package io.matchedup.examples.velocity.listeners;

import io.matchedup.api.MatchedUpClient;
import io.matchedup.api.events.error.ClientErrorEvent;
import io.matchedup.api.events.error.ServerErrorEvent;
import io.matchedup.api.events.error.ThrottledRequestErrorEvent;
import io.matchedup.api.events.error.UserErrorEvent;
import io.matchedup.api.events.match.MatchRequestCancelledEvent;
import io.matchedup.api.events.match.MatchCreatedEvent;
import io.matchedup.api.events.match.MatchRequestedEvent;
import io.matchedup.api.events.match.MatchRequestTimedOutEvent;
import io.matchedup.api.events.state.ConnectedEvent;
import io.matchedup.api.events.state.DisconnectedEvent;
import io.matchedup.api.events.state.FailedToConnectEvent;
import io.matchedup.api.events.state.ReconnectingEvent;
import io.matchedup.api.resources.MatchPlayer;
import io.matchedup.api.resources.MatchTicket;
import kotlin.Unit;

import java.util.Map;

public class MatchedUpEventListener {

    private final MatchedUpClient matchedUpClient;

    public MatchedUpEventListener(MatchedUpClient matchedUpClient) {
        this.matchedUpClient = matchedUpClient;
        registerListeners();
    }

    private void registerListeners() {
        matchedUpClient.getEventBus().registerListener(ConnectedEvent.class, this::onConnected);
        matchedUpClient.getEventBus().registerListener(FailedToConnectEvent.class, this::onFailedToConnect);
        matchedUpClient.getEventBus().registerListener(DisconnectedEvent.class, this::onDisconnected);
        matchedUpClient.getEventBus().registerListener(ReconnectingEvent.class, this::onReconnecting);

        matchedUpClient.getEventBus().registerListener(MatchRequestedEvent.class, this::onMatchRequested);
        matchedUpClient.getEventBus().registerListener(MatchRequestTimedOutEvent.class, this::onMatchTimedOut);
        matchedUpClient.getEventBus().registerListener(MatchRequestCancelledEvent.class, this::onMatchCancelled);
        matchedUpClient.getEventBus().registerListener(MatchCreatedEvent.class, this::onMatchCreated);

        matchedUpClient.getEventBus().registerListener(UserErrorEvent.class, this::onUserError);
        matchedUpClient.getEventBus().registerListener(ClientErrorEvent.class, this::onClientError);
        matchedUpClient.getEventBus().registerListener(ThrottledRequestErrorEvent.class, this::onThrottledRequestError);
        matchedUpClient.getEventBus().registerListener(ServerErrorEvent.class, this::onServerError);
    }

    private Unit onConnected(ConnectedEvent event) {
        System.out.println("Connected to MatchedUps servers");
        return null;
    }

    private Unit onFailedToConnect(FailedToConnectEvent event) {
        System.out.printf("Failed to connect to MatchedUps servers: code=%s reason='%s'%n", event.getCode(), event.getReason());
        return null;
    }

    private Unit onDisconnected(DisconnectedEvent event) {
        System.out.println("Disconnected from MatchedUps servers");
        return null;
    }

    private Unit onReconnecting(ReconnectingEvent event) {
        System.out.println("Reconnecting to MatchedUps servers");
        return null;
    }

    private Unit onMatchRequested(MatchRequestedEvent event) {
        for (MatchPlayer player : event.players) {
            System.out.printf("Player '%s' has requested to join match '%s' with ticket: %s %n", player.uuid, event.matchType, event.ticketId);
        }
        return null;
    }

    private Unit onMatchTimedOut(MatchRequestTimedOutEvent event) {
        for (MatchTicket ticket : event.tickets) {
            for (MatchPlayer player : ticket.players) {
                System.out.printf("Match request for player '%s' has timed out %n", player.uuid);
            }
        }
        return null;
    }

    private Unit onMatchCancelled(MatchRequestCancelledEvent event) {
        for (MatchTicket ticket : event.tickets) {
            for (MatchPlayer player : ticket.players) {
                System.out.printf("Match request for player '%s' has been cancelled %n", player.uuid);
            }
        }
        return null;
    }

    private Unit onMatchCreated(MatchCreatedEvent event) {
        System.out.printf("Match '%s' created with ID '%s' %n", event.matchType, event.getMatchId());
        for (Map.Entry<String,?> matchAttrib : event.matchAttribs.entrySet()) {
            System.out.printf("  Match Attribute: '%s' = '%s' %n", matchAttrib.getKey(), matchAttrib.getValue());
        }
        for (MatchTicket ticket : event.tickets) {
            for (MatchPlayer player : ticket.players) {
                System.out.printf("  Player '%s' has joined with ticket: %s %n", player.uuid, ticket.id);
            }
        }
        return null;
    }

    private Unit onUserError(UserErrorEvent event) {
        System.out.printf("User error for '%s': %s %n", event.uuid, event.error);
        return null;
    }

    private Unit onClientError(ClientErrorEvent event) {
        System.out.printf("Error: We did something wrong when making a request to MatchedUp: %s %n", event.error);
        return null;
    }

    private Unit onThrottledRequestError(ThrottledRequestErrorEvent event) {
        System.out.println(event.error);
        return null;
    }

    private Unit onServerError(ServerErrorEvent event) {
        System.out.printf("Error: Something went wrong on MatchedUps server: %s %n", event.error);
        return null;
    }

}
