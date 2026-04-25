package test;

import junit.framework.TestCase;
import repository.UserRepository;
import user.User;
import authentication.Authentication;

/**
 * Unit tests for {@link authentication.Authentication}.
 *
 * @author NJ
 * @version TODO
 */
public class TestAuthentication extends TestCase {
	public Authentication auth;
	private UserRepository repository;

	protected void setUp() throws Exception {
		super.setUp();
		repository = new UserRepository();
		repository.saveUser(new User("John", "Pork"));
		auth = new Authentication(repository);
	}

	//test for succesfull login
	public void testLogin() {
	 	assertFalse(auth.login("John","password"));
	 	assertTrue(auth.login("John","Pork"));
	}

	// test for user is not in repository
	public void testLoginUserDoesNotExist() {
		boolean result = auth.login("nonExistingUser", "somePassword");
		assertFalse(result);
	}
	
	//test for get autorized user
	public void testAuthenticatedUser() {
		assertTrue(auth.login("John","Pork"));
		assertEquals("John", auth.getAuthenticatedUser().getUsername());
	}

	//test logout
	public void testLogout() {
		assertTrue(auth.login("John","Pork"));
		auth.logout();
		assertNull(auth.getAuthenticatedUser());
	}
	
	

	

}
