package pl.workshop.database;

import org.jooq.ConnectionProvider;

public interface ClosableConnectionProvider extends ConnectionProvider, AutoCloseable {
}
