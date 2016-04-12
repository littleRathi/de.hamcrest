package de.bs.hamcrest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class ArrayMatchers {
	/**
	 * Create a matcher that match examined array has the size of the operand. It 
	 * also can operate on multiarrays, but be careful, on which dimension.
	 * <p>
	 * For examples:
	 * <pre>
	 * 	assertThat(new String[]{"a", "b"}, arrayHasLength(2));
	 *	assertThat(new String[][]{{"a", "b"}}, arrayHasLength(1));
	 * </pre>
	 * @param length that the examined array is expected to have.
	 * @return
	 */
	public static <T> Matcher<T[]> arrayHasLength(final int length) {
		return new TypeSafeDiagnosingMatcher<T[]>() {
			public void describeTo(Description description) {
				description.appendText("Array Length should be ").appendValue(length);
			}
			@Override
			protected boolean matchesSafely(T[] item, Description mismatchDescription) {
				mismatchDescription.appendText(" array length was ").appendValue(item.length);
				return item.length == length;
			}
		};
	}
	
	/**
	 * Create a matcher that take the size of the examined array and matches it 
	 * against the Matcher for integer. Useful when only soft condition are give, 
	 * like the array size must be greater than 0.
	 * <p>
	 * For examples:
	 * <pre>
	 * 	assertThat(new String[]{"a", "b"}, arrayLength(equalTo(2)));
	 *	assertThat(new String[]{"a", "b"}, arrayLength(greaterThan(1)));
	 * </pre>
	 * @param matcher that matches the size of the array (must be Matcher<Integer>)
	 * @return
	 */
	public static <T> Matcher<T[]> arrayLength(final Matcher<Integer> matcher) {
		return new TypeSafeDiagnosingMatcher<T[]>() {
			public void describeTo(Description description) {
				description.appendText("array length, ");
				matcher.describeTo(description);
			}
			@Override
			protected boolean matchesSafely(T[] item, Description mismatchDescription) {
				matcher.describeMismatch(item.length, mismatchDescription);
				return matcher.matches(item.length);
			}
		};
	}
	
	/**
	 * Create a matcher that access a element within a the examined array on that 
	 * the given Matcher is assigned to.
	 * <p>
	 * For example:
	 * <pre>
	 *  assertThat(new String[][]{{"a", "b"}}, arrayElementAt(0, arrayElementAt(0, equalTo("a"))));
	 *  assertThat(new String[][]{{"a", "b"}}, arrayElementAt(0, arrayHasLength(2)));
	 * </pre>
	 * @param index
	 * @param matcher
	 * @return
	 */
	public static <T> Matcher<T[]> arrayElementAt(final int index, final Matcher<T> matcher) {
		return new TypeSafeDiagnosingMatcher<T[]>() {
			public void describeTo(Description description) {
				description.appendText("array at ").appendValue(index).appendText(", ");
				matcher.describeTo(description);
			}
			@Override
			protected boolean matchesSafely(T[] item, Description mismatchDescription) {
				if (index >= 0 && index < item.length) {
					mismatchDescription.appendText("[").appendText(String.valueOf(index)).appendText("]");
					matcher.describeMismatch(item[index], mismatchDescription);
					return matcher.matches(item[index]);
				}
				mismatchDescription.appendText("[").appendValue(index).appendText(": index is not in range from ").appendValue(0)
					.appendText(" to ").appendValue(item.length).appendText("]");
				return false;
			}
		};
	}
	
	/**
	 * Create a matcher that examined the array, if it contains a given list of elements.
	 * <p>
	 * For examle:
	 * <pre>
	 * 	assertThat(new String[]{"a", "b"}, arrayHasItems("a", "b"));
	 * 	assertThat(new String[]{"a", "b"}
	 * </pre>
	 * @param excpectedItems
	 * @return
	 */
	public static <T> Matcher<T[]> arrayHasItems(final T... excpectedItems) {
		return new TypeSafeDiagnosingMatcher<T[]>() {
			public void describeTo(Description description) {
				description.appendText("must contain following elements ").appendValueList("[", ",", "]", excpectedItems);
			}
			@Override
			protected boolean matchesSafely(T[] item, Description mismatchDescription) {
				if (excpectedItems.length > 0 && excpectedItems[0].getClass().isArray()) {
					throw new IllegalArgumentException("Cannot check for arrays");
				}
				Set<T> values = new HashSet<T>(Arrays.asList(item));
//				Set<T> expected = new HashSet<T>(Arrays.asList(excpectedItems));
//				expected.removeAll(values);
				mismatchDescription.appendText("Following Elements are in the array ").appendValueList("[", ",", "]", values);
				return values.containsAll(Arrays.asList(excpectedItems));
			}
		};
	}
}





