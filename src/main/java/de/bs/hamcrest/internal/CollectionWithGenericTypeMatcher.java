package de.bs.hamcrest.internal;

import java.util.Collection;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class CollectionWithGenericTypeMatcher<C extends Collection<T>, T> extends TypeSafeDiagnosingMatcher<Object> {
	private Class<C> collectionType;
	private Class<T> genericType;
	private Matcher<C> matcher;
	
	public CollectionWithGenericTypeMatcher(final Class<C> collectionType, final Class<T> genericType, final Matcher<C> matcher) {
		this.collectionType = collectionType;
		this.genericType = genericType;
		this.matcher = matcher;
	}

	public static class CollectionWithGenericTypeMatcherAnd<C extends Collection<T>, T> {
		private Class<C> collectionType;
		private Class<T> genericType;
		public CollectionWithGenericTypeMatcherAnd(final Class<C> collectionType, final Class<T> genericType) {
			this.collectionType = collectionType;
			this.genericType = genericType;
		}
		public Matcher<Object> and(final Matcher<C> matcher) {
			return new CollectionWithGenericTypeMatcher<C, T>(collectionType, genericType, matcher);
		}
	}
	
	// static "fluent" method
	public static <C extends Collection<T>, T> CollectionWithGenericTypeMatcherAnd<C, T> collectionWithGenericType(final Class<C> collectionType, final Class<T> genericType) {
		return new CollectionWithGenericTypeMatcherAnd<C, T>(collectionType, genericType);
	}
	
	public void describeTo(Description description) {
		description.appendText("should be a collection from type ").appendValue(collectionType).appendText(" and generic type ")
			.appendValue(genericType).appendText(" ");
		matcher.describeTo(description);
	}

	@Override
	protected boolean matchesSafely(Object item, Description mismatchDescription) {
		if (collectionType.isAssignableFrom(item.getClass())) {
			@SuppressWarnings("unchecked")
			C collection = (C)item;
			for (Object object: collection) {
				if (!genericType.isAssignableFrom(object.getClass())) {
					mismatchDescription.appendText("found a element in the collection that not match the generic type, with type ").appendValue(object.getClass());
					return false;
				}
			}
		} else {
			mismatchDescription.appendText("given object is not a collection for the given type: ").appendValue(collectionType);
			return false;
		}
		matcher.describeMismatch(item, mismatchDescription);
		return matcher.matches(item);
	}
}
