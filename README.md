# [MatchedUp](https://saas.matchedup.io)

## Installing

#### Maven
```xml
<dependencies>
    <dependency>
        <groupId>io.matchedup</groupId>
        <!-- Modify this line to target the loader you wish to use. -->
        <artifactId>api</artifactId>
        <version>0.0.5</version>
    </dependency>
</dependencies>
```

#### Gradle
```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation 'io.matchedup:api:0.0.5'
}
```

## Usage

#### Create the client & connect
```java
MatchedUpClient client = new MatchedUpClient("ACCESS_KEY", "SECRET_KEY");
client.connect(); 
```

#### Make match request
```java
String playerUuid = getPlayerUuid(); // get this off the player
String matchName = "my-awesome-match"; // Name of a match you've configured on https://matchedup.io
Map<String, PlayerAttributeType> playerAttributes = new HashMap<>(); // any player attributes
playerAttributes.put("level", 10);

// Submit the request to join a match
client.submitAction(
    new RequestMatch(
        null, // or provide UUID for the ticket ID
        matchName,
        playerUuid, 
        playerAttributes
    )
);
```

#### Listen for events
```java
public void registerListeners(MatchedUpClient client) {
    client.getEventBus().registerListenerInternal(MatchCreatedEvent.class, this::onMatchCreated);
} 

public Unit onMatchCreated(MatchCreatedEvent event) {
    System.out.println(String.format("Match (ID '%s') was created", event.getMatchId()));

    for (MatchTicket ticket : event.tickets) {
        for (MatchPlayer player : ticket.getPlayers()) {
            System.out.println(String.format("Player '%s' is in the match on team '%s'", player.getUuid(), player.getTeam()));
        }
    }
    return null;
}
```

## List of events

- **ConnectedEvent**: Connected to the MatchedUp servers
- **DisconnectedEvent**: Disconnected from MatchedUp servers
- **ReconnectingEvent**: Triggered when connection to the MatchedUp servers was lost and reconnection has started
- **FailedToConnectEvent**: Triggered when the initial connection to the MatchedUp server fails
- **MatchRequestedEvent**: Match was requested successfully
- **MatchRequestCancelledEvent**: Match request was cancelled
- **MatchRequestTimedOutEvent**: Match request did not get into match and timed out
- **MatchRequestFailedEvent**: An error occurred when requesting a match
- **MatchCreatedEvent**: Match was created successfully
- **UserErrorEvent**: A user error has occurred
- **ClientErrorEvent**: A client (consumer of the SDK) error has occurred
- **ThrottledRequestErrorEvent**: Error signifying that you've reached your hourly match request limit (if on Free Tier) 
- **ServerErrorEvent**: An unexpected MatchedUp server error has occurred

## State of Development

MatchedUp is currently in its beta stage and fully usable, but continuously under development.
More features are constantly being planned and added to the service, and you can track their progress 
on the [public Trello board](https://trello.com/b/2LaSu2oa/matchedup)