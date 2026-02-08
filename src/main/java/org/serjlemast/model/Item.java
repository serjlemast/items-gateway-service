package org.serjlemast.model;

import java.time.Instant;

public record Item(String guid, String title, String content, String link, Instant date) {
}
