package de.weltraumschaf.nano.example.module1.api;

import de.weltraumschaf.nano.api.service.AutoStartingService;

/**
 * A simple reverse echo services: Listens on a TCP port and returns what it receives to the client in reversed order.
 *
 * @author Sven Strittmatter
 * @since 1.0.0
 */
public interface ReverseEcho extends AutoStartingService {
}
