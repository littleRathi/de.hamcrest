package de.bs.hamcrest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

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
}
