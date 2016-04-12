package de.bs.hamcrest.internal;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class OfType<T> extends TypeSafeDiagnosingMatcher<Object> {
	private Matcher<Class<T>> typeMatcher;
	private Matcher<T> matcher;

	public OfType(final Matcher<Class<T>> typeMatcher, final Matcher<T> matcher) {
		this.typeMatcher = typeMatcher;
		this.matcher = matcher;
	}
	
	public static class OfTypeAnd<T> {
		private Matcher<Class<T>> typeMatcher;
		public OfTypeAnd(final Matcher<Class<T>> typeMatcher) {
			this.typeMatcher = typeMatcher;
		}
		public Matcher<Object> and(final Matcher<T> matcher) {
			return new OfType<T>(typeMatcher, matcher);
		}
	}
	
	// static "fluent" method
	public static <T> OfTypeAnd<T> ofType(final Matcher<Class<T>> typeMatcher) {
		return new OfTypeAnd<T>(typeMatcher);
	}
	
	public void describeTo(Description description) {
		typeMatcher.describeTo(description);
		if (matcher != null) {
			description.appendText(" with ");
			matcher.describeTo(description);
		}
	}

	@Override
	protected boolean matchesSafely(Object item, Description mismatchDescription) {
		if (typeMatcher.matches(item.getClass())) {
			boolean isMatch = matcher.matches(item);
			matcher.describeMismatch(item, mismatchDescription);
			return isMatch;
		}
		typeMatcher.describeMismatch(item.getClass(), mismatchDescription);
		return false;
	}
}
