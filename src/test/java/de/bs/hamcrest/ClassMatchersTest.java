package de.bs.hamcrest;

import static de.bs.hamcrest.ClassMatchers.equalToType;
import static de.bs.hamcrest.ClassMatchers.extendsType;
import static de.bs.hamcrest.ClassMatchers.fullQualifiedName;
import static de.bs.hamcrest.ClassMatchers.simpleClassName;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.containsString;

import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Collection;

import org.junit.Test;

public class ClassMatchersTest {
	
	// equalToClass
	@Test
	public void testEqualToSuccess() {
		Class<String> onlyClass = String.class;
		
		assertThat(onlyClass, equalToType(onlyClass));
	}

	@Test
	public void testEqualToError() {
		Class<?> firstClass = String.class;
		Class<?> secondClass = Integer.class;
		
		assertThat(firstClass, not(equalToType(secondClass)));
	}
	
	// extendsClass
	@Test
	public void testExtendsClassSame() {
		Class<?> testClass = Collection.class;
				
		assertThat(testClass, extendsType(testClass));
	}
	
	@Test
	public void testExtendsClassSubclass() {
		Class<?> subClass = List.class;
		Class<?> superClass = Collection.class;
		
		assertThat(subClass, extendsType(superClass));
	}
	
	@Test
	public void testExtendsClassWrong() {
		Class<?> subClass = String.class;
		Class<?> superClass = Collection.class;
		
		assertThat(subClass, not(extendsType(superClass)));
	}
	
	// fullQualifiedName
	@Test
	public void testFullQualifiedNameEqualToCorrect() {
		Class<?> testee = String.class;
		String name = String.class.getName();
		
		assertThat(testee, fullQualifiedName(equalTo(name)));
	}
	
	@Test
	public void testFullQualifiedNameEqualToWrong() {
		Class<?> testee = String.class;
		String name = Integer.class.getName();
		
		assertThat(testee, not(fullQualifiedName(equalTo(name))));
	}
	
	@Test
	public void testFullQualifiedNameStartsWithCorrect() {
		Class<?> testee = String.class;
		String name = "java.";
		
		assertThat(testee, fullQualifiedName(startsWith(name)));
	}
	
	@Test
	public void testFullQualifiedNameStartsWithWrong() {
		Class<?> testee = String.class;
		String name = "String";
		
		assertThat(testee, not(fullQualifiedName(startsWith(name))));
	}
	
	@Test
	public void testFullQualifiedNameEndsWithCorrect() {
		Class<?> testee = String.class;
		String name = "String";
		
		assertThat(testee, fullQualifiedName(endsWith(name)));
	}
	
	@Test
	public void testFullQualifiedNameEndsWithWrong() {
		Class<?> testee = String.class;
		String name = "java";
		
		assertThat(testee, not(fullQualifiedName(endsWith(name))));
	}
	
	@Test
	public void testFullQualifiedNameContainsStringCorrect() {
		Class<?> testee = String.class;
		String name = "lang";
		
		assertThat(testee, fullQualifiedName(containsString(name)));
	}
	
	@Test
	public void testFullQualifiedNameContainsStringWrong() {
		Class<?> testee = String.class;
		String name = "abcdefghijklmnopqrstuvwxyz";
		
		assertThat(testee, not(fullQualifiedName(containsString(name))));
	}
	
	// simpleClassName
	@Test
	public void testSimpleClassNameEqualToCorrect() {
		Class<?> testee = String.class;
		String name = String.class.getSimpleName();
		
		assertThat(testee, simpleClassName(equalTo(name)));
	}
	
	@Test
	public void testSimpleClassNameEqualToWrong() {
		Class<?> testee = String.class;
		String name = Integer.class.getSimpleName();
		
		assertThat(testee, not(simpleClassName(equalTo(name))));
	}
	
	@Test
	public void testSimpleClassNameStartsWithCorrect() {
		Class<?> testee = String.class;
		String name = "Str";
		
		assertThat(testee, simpleClassName(startsWith(name)));
	}
	
	@Test
	public void testSimpleClassNameStartsWithWrong() {
		Class<?> testee = String.class;
		String name = "ing";
		
		assertThat(testee, not(simpleClassName(startsWith(name))));
	}
	
	@Test
	public void testSimpleClassNameEndsWithCorrect() {
		Class<?> testee = String.class;
		String name = "ing";
		
		assertThat(testee, simpleClassName(endsWith(name)));
	}
	
	@Test
	public void testSimpleClassNameEndsWithWrong() {
		Class<?> testee = String.class;
		String name = "java";
		
		assertThat(testee, not(simpleClassName(endsWith(name))));
	}
	
	@Test
	public void testSimpleClassNameContainsStringCorrect() {
		Class<?> testee = String.class;
		String name = "trin";
		
		assertThat(testee, simpleClassName(containsString(name)));
	}
	
	@Test
	public void testSimpleClassNameContainsStringWrong() {
		Class<?> testee = String.class;
		String name = "abcdefghijklmnopqrstuvwxyz";
		
		assertThat(testee, not(simpleClassName(containsString(name))));
	}
}
