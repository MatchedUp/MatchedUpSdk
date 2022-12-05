package io.matchedup.api.exceptions

import java.lang.Exception

class InvalidServerException: Exception("Could not connect to MatchedUp server, invalid server provided.")