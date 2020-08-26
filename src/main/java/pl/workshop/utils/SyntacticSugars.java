package pl.workshop.utils;

public class SyntacticSugars {

    public static <T> T first(Iterable<T> items) {
        return items.iterator().next();
    }
}
