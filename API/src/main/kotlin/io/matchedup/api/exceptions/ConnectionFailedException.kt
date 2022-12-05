package io.matchedup.api.exceptions

import java.lang.Exception

class ConnectionFailedException(e: Exception): Exception("Could not connect to MatchedUp server: $e")