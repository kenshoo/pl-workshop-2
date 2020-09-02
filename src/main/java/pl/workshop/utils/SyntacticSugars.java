package pl.workshop.utils;

import static org.jooq.lambda.Seq.seq;

public class SyntacticSugars {

    public static <T> T first(Iterable<T> items) {
        return items.iterator().next();
    }

    public static <T> T second(Iterable<T> items) {
        return seq(items.iterator()).get(1).get();
    }

}
