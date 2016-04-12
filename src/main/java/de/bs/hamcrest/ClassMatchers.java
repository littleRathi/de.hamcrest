package de.bs.hamcrest;

import java.util.Collection;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import de.bs.hamcrest.internal.CollectionWithGenericTypeMatcher;
import de.bs.hamcrest.internal.CollectionWithGenericTypeMatcher.CollectionWithGenericTypeMatcherAnd;
import de.bs.hamcrest.internal.OfType;
import de.bs.hamcrest.internal.OfType.OfTypeAnd;

public class ClassMatchers {
	/**
	 * Creates a matcher that compare two Class object, and return true only when they are 
	 * equal. It ignores inheritance, and is for exact equality. The Matchers.equalTo() leads 
	 * to ugly code like: assertThat(classVariable, equalTo((Class)Integer.class));
	 * <p>
	 * For example:
	 * <pre>
	 *  assertThat(Integer.class, equalToType(Integer.class));
	 *  assertThat(Integer.class, not(equalToType(String.class));
	 *  assertThat(ArrayList.class, not(equalToType(List.class));
	 * </pre>
	 * @param otherClass
	 * @return
	 */
	public static Matcher<Class<?>> equalToType(final Class<?> otherClass) {
		return new TypeSafeDiagnosingMatcher<Class<?>>() {
			public void describeTo(Description description) {
				description.appendText("Class should be ").appendValue(otherClass.getName());
			}
			@Override
			protected boolean matchesSafely(Class<?> item, Description mismatchDescription) {
				mismatchDescription.appendText(" was ").appendValue(item.getName());
				return item.equals(otherClass);
			}
		};
	}
	
	/**
	 * Creates a matcher that matches when the examined Class<?> is a subclass/subinterface
	 * of the the Class<?> given in otherClass. It already exist in hamcrest in form of 
	 * Matchers.typeCompatibleWith(Class<?>)
	 * <p>
	 * For examples:
	 * <pre>
	 *  assertThat(ArrayList.class, extendsType(List.class));
	 *  assertThat(List.class, extendsType(List.class));
	 *  assertThat(String.class, extendsType(String.class)); 
	 * </pre>
	 * @param otherClass
	 * @return
	 */
	public static Matcher<Class<?>> extendsType(final Class<?> otherClass) {
		return new TypeSafeDiagnosingMatcher<Class<?>>() {
			public void describeTo(Description description) {
				description.appendText("Class should be extends/implements ").appendValue(otherClass.getName());
			}
			@Override
			protected boolean matchesSafely(Class<?> item, Description mismatchDescription) {
				mismatchDescription.appendText(" following class/interface ").appendValue(item.getName()).appendText(" does not extends/implements these class/interface ");
				return otherClass.isAssignableFrom(item);
			}
		};
	}
	
	/**
	 * Creates a matcher that extract from a class the full qualified name and pass it to the
	 * given matcher, for more flexibility. See the examples.
	 * <p>
	 * For examples:
	 * <pre>
	 *  assertThat(String.class, fullQualifiedName(startsWith("java.lang")));
	 *  assertThat(Integer.class, fullQualifiedName(equalTo("java.lang.Integer")));
	 *  assertThat(Character.class, fullQualifiedName(endsWith("cter")));
	 * </pre>
	 * @param matcher
	 * @return
	 */
	public static Matcher<Class<?>> fullQualifiedName(final Matcher<? extends String> matcher) {
		return new TypeSafeDiagnosingMatcher<Class<?>>() {
			public void describeTo(Description description) {
				description.appendText("full qualified name, ");
				matcher.describeTo(description);
			}
			@Override
			protected boolean matchesSafely(Class<?> item, Description mismatchDescription) {
				String fullQualifiedName = item.getName();
				matcher.describeMismatch(fullQualifiedName, mismatchDescription);
				return matcher.matches(fullQualifiedName);
			}
		};
	}
	
	/**
	 * Creates a matcher that extract the class name from the class object and pass it to
	 * the given matcher
	 * <p>
	 * For examples:
	 * <pre>
	 * 	assertThat(String.class, simpleClassName(startsWith("Str")));
	 * 	assertThat(Integer.class, simpleClassName(equalTo("Integer")));
	 * </pre>
	 * @param matcher
	 * @return
	 */
	public static Matcher<Class<?>> simpleClassName(final Matcher<String> matcher) {
		return new TypeSafeDiagnosingMatcher<Class<?>>() {
			public void describeTo(Description description) {
				description.appendText("simple class name, ");
				matcher.describeTo(description);
			}
			@Override
			protected boolean matchesSafely(Class<?> item, Description mismatchDescription) {
				String simpleName = item.getSimpleName();
				matcher.describeMismatch(item, mismatchDescription);
				return matcher.matches(simpleName);
			}
		};
	}
	
	/**
	 * Creates a matcher that check if the item is from a given class (first parameter), whereas it must 
	 * heritage must be from Collection<?> interface. The second parameter is the expected generic type 
	 * from the collection.
	 * <p>
	 * For examples:
	 * <pre>
	 * 	assertThat(Arrays.asList("a", "b"), collectionWithGenericType(List.class, String.class));
	 * </pre>
	 * @param collectionType
	 * @param genericType
	 * @return
	 */
	public static <T extends Collection<?>, S> Matcher<Object> collectionWithGenericType(final Class<T> collectionType, final Class<S> genericType) {
		return new TypeSafeDiagnosingMatcher<Object>() {
			public void describeTo(Description description) {
				description.appendText("should be a collection from type ").appendValue(collectionType).appendText(" and generic type ")
					.appendValue(genericType);
			}
			@Override
			protected boolean matchesSafely(Object item, Description mismatchDescription) {
				if (collectionType.isAssignableFrom(item.getClass())) {
					Collection<?> collection = (Collection<?>)item;
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
				return true;
			}
		};
	}

	/**
	 * Creates a object that check if the given item extends from a given Collection class, first 
	 * parameter. Then checks the elements of the collection against the second parameter, that
	 * is the expected generic type of the collection. The returned object, provides a method to
	 * push the item object to a another matcher (method and(Matcher<C extends Collection<T>>)). But only if item is from the right collection
	 * and all elements in the collection match against the the generic type, also.
	 * <p>
	 * Warning: When using this matcher, it will lead to a warning. Ignore it or use @SuppressWarnings("unchecked"). 
	 * This is due the handling of generic by this matcher. Please also note, do not use not(...), like 
	 * not(collection(...).and(...)).
	 * <p>
	 * For examples:
	 * <pre>
	 * 	@SuppressWarnings("unchecked")
	 * 	assertThat(Arrays.asList("a", "b"), collection(List.class, String.class).and(hasItems("a", "b")));
	 * </pre>
	 * @param collectionType
	 * @param genericType
	 * @return
	 */
	public static <C extends Collection<T>, T> CollectionWithGenericTypeMatcherAnd<C, T> collection(final Class<C> collectionType, final Class<T> genericType) {
		return CollectionWithGenericTypeMatcher.collectionWithGenericType(collectionType, genericType);
	}
	
	/**
	 * Create a matcher, if the given object matches the ´type´ matcher, if it matches, than the object is
	 * passed to the next matcher.
	 * <p>
	 * For examples:
	 * <pre>
	 * 	assertThat("abc", ofType(equalTo(String.class)).and(equalTo("abc")));
	 * 	assertThat("test text and some more", ofType(equalTo(String.class)).and(startsWith("test")));
	 * </pre> 
	 */
	public static <T> OfTypeAnd<T> ofType(final Matcher<Class<T>> typeMatcher) {
		return OfType.ofType(typeMatcher);
	}
}
