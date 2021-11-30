package aoc.runner;

import aoc.utils.Config;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

public class Scraper {
    private static final Logger logger = LoggerFactory.getLogger(Scraper.class);

    private static final String sessionID = Config.sessionCookie();
    private static final HttpClient httpClient = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build();

    private static final String URI_TEMPATE = "https://adventofcode.com/%s/day/%s/input";

    private static Optional<Stream<String>> getDataFromServer(final int year, final int day) {
        final URI address = aocInputURI(year,day);
        try {
            HttpResponse<Stream<String>> response = httpClient.send(
                    authenticatedRequest(address),
                    HttpResponse.BodyHandlers.ofLines());

            return Optional.of(response.body());

        } catch (IOException e) {
            logger.error("Failed to connect to server: '{}'", address, e);
        } catch (InterruptedException e) {
            throw new IllegalStateException("Session Cookie invalid", e);
        }
        return Optional.empty();
    }
    private static @NotNull String uriFor(final int year, final int day) {
        return URI_TEMPATE.formatted(year,day);
    }

    private static URI aocInputURI(final int year, final int day) {
        try {
            return new URI(uriFor(year, day));
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Attempted to construct a Malformed URI: " + URI_TEMPATE, e);
        }
    }

    private static HttpRequest authenticatedRequest(URI uri) {
        return HttpRequest.newBuilder(uri)
                .header("Cookie", "session=" + sessionID)
                .build();
    }

    public static void main(String[] args) {
        final int year = Config.year();
        Time.getCurrentAoCDay().ifPresent( day -> {

            List<String> input = getDataFromServer(year,day)
                    .orElseThrow(() -> new NoSuchElementException("Unable to retrieve AoC input for %s-%s from the server".formatted(year,day)))
                    .toList();

            DayRunner.writeOrCreateFile(input, Days.dayFromInt(day));
        });
    }
}
