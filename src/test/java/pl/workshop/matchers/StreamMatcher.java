package pl.workshop.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import java.util.Set;
import java.util.stream.Stream;
import static java.util.stream.Collectors.toSet;


public class StreamMatcher {

    public static <T> Matcher<Stream<T>> contains(T... items) {
        return new TypeSafeMatcher<>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Expected: " + items);

            }

            @Override
            protected boolean matchesSafely(Stream<T> stream) {
                return Set.of(items).equals(stream.collect(toSet()));
            }
        };
    }
}
