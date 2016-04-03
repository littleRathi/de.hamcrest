# Matchers

## ClassMatchers

	Class<?> equalToType(Class<?>)
	Class<?> extendsType(Class<?>)
	Object fullQualifiedName(Matcher<String>) (fun)
	Object simpleClassName(Matcher<String>) (fun)
	Object isTypeOf(Class<?>).and()
	
## ArrayMatchers
	hasLength(Integer)
	length(Matcher<Integer>)
	hasItem(T)
	hasItems(T...)