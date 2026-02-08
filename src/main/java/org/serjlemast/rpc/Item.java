package org.serjlemast.rpc;

import java.time.Instant;

public record Item(String guid, String title, String content, String link, Instant date) {
}
