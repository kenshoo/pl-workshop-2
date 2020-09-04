package pl.workshop.exercise0;

import org.jooq.DSLContext;
import org.junit.Test;
import pl.workshop.database.JooqProvider;

import static org.junit.Assert.*;

public class CheckDatabaseConnection {

    static final DSLContext jooq = JooqProvider.create();

    @Test public void selectOne() {
        System.out.println(jooq.fetch("select now()").get(0));
    }
}
