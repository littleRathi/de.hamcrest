package de.bs.hamcrest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;

import static de.bs.hamcrest.ArrayMatchers.arrayHasLength;
import static de.bs.hamcrest.ArrayMatchers.arrayLength;
import static de.bs.hamcrest.ArrayMatchers.arrayElementAt;
import static de.bs.hamcrest.ArrayMatchers.arrayHasItems;

import org.junit.Test;

public class ArrayMatchersTest {
	private String[] notInArray = new String[]{"d", "e", "f"};
	private String[] validArray = new String[]{"a", "b", "c"};
	private String[][][] multiArray = new String[][][]{{{"a", "b"}}};
	
	// arrayHasLength(int)
	@Test
	public void testHasLengthEmpty() {
		String[] empty = new String[0];
		
		assertThat(empty, arrayHasLength(0));
	}
	
	@Test
	public void testHasLengthMore() {
		String[] array = new String[]{"a", "b", "c"};
		
		assertThat(array, arrayHasLength(3));
	}
	
	// arrayLength(Matcher<Integer>)
	@Test
	public void testLengthEqualTo() {
		String[] array = new String[]{"a", "b"};
		
		assertThat(array, arrayLength(equalTo(2)));
	}
	
	@Test
	public void testLengthNotEqualTo() {
		String[] array = new String[]{"a"};
		
		assertThat(array, not(arrayLength(equalTo(2))));
	}
	
	@Test
	public void testLengthGreaterThan() {
		String[] array = new String[]{"a", "b"};
		
		assertThat(array, arrayLength(greaterThan(1)));
	}
	
	@Test
	public void testLengthLessThan() {
		String[] array = new String[]{"a"};
		
		assertThat(array, arrayLength(lessThan(2)));
	}
	
	// arrayElementAt(int, Matcher<T>)
	@Test
	public void testArrayElementAt() {
		assertThat(multiArray, arrayElementAt(0, arrayElementAt(0, arrayHasLength(2))));
		assertThat(multiArray, arrayElementAt(0, arrayElementAt(0, arrayElementAt(0, equalTo("a")))));
	}
	
	@Test
	public void testArrayElementAtIndexOutOfBound() {
		assertThat(multiArray, not(arrayElementAt(0, arrayElementAt(2, arrayHasLength(2)))));
	}
	
	@Test
	public void testArrayElementAtWrongMatcher() {
		assertThat(multiArray, not(arrayElementAt(0, arrayElementAt(0, arrayHasLength(3)))));
	}
	
	@Test
	public void testArrayHasItemsOnlyOne() {
		assertThat(validArray, arrayHasItems("a"));
	}
	
	@Test
	public void testArrayHasItemsSame() {
		assertThat(validArray, arrayHasItems(validArray));
	}
	
	@Test
	public void testArrayHasItemsNone() {
		assertThat(validArray, not(arrayHasItems(notInArray)));
	}
}	
