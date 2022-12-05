# [MatchedUp](https://matchedup.io)

## Installing

#### Maven
```xml
<dependencies>
    <dependency>
        <groupId>io.matchedup</groupId>
        <!-- Modify this line to target the loader you wish to use. -->
        <artifactId>api</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
</dependencies>
```

#### Gradle
```groovy
repositories {
    mavenCentral()
}

dependencies {
    compile 'io.matchedup:api:0.0.1-SNAPSHOT'
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
        playerUuid, 
        matchName, 
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
- **MatchRequestedEvent**: Match was requested successfully
- **MatchRequestCancelledEvent**: Match request was cancelled
- **MatchRequestTimedOutEvent**: Match request did not get into match and timed out
- **MatchRequestFailedEvent**: An error occurred when requesting a match
- **MatchCreatedEvent**: Match was created successfully
- **UserErrorEvent**: A user error has occurred
- **ClientErrorEvent**: A client (consumer of the SDK) error has occurred
- **ServerErrorEvent**: An unexpected MatchedUp server error has occurred