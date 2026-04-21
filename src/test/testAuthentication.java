package test;

import junit.framework.TestCase;
import user.User;
import Repository.UserRepository;
import authentication.Authentication;

/**
 * Unit tests for {@link authentication.Authentication}.
 *
 * @author NJ
 * @version TODO
 */
public class testAuthentication extends TestCase {
	public Authentication auth;
	private UserRepository repository;

	protected void setUp() throws Exception {
		super.setUp();
		repository = new UserRepository();
		repository.saveUser(new User("John", "Pork", null));
		auth = new Authentication(repository);
	}

	//test for succesfull login
	public void testlogin() {
	 	assertFalse(auth.login("John","password"));
	 	assertTrue(auth.login("John","Pork"));
	}

	// test for user is not in repository
	public void testLoginUserDoesNotExist() {
		boolean result = auth.login("nonExistingUser", "somePassword");
		assertFalse(result);
	}
	
	//test for get autorized user
	public void testauthorisedUser() {
		assertTrue(auth.login("John","Pork"));
		assertEquals("John", auth.getauthenticatedUser().getUsername());
	}

	//test logout
	public void testlogout() {
		assertTrue(auth.login("John","Pork"));
		auth.logout();
		assertNull(auth.getauthenticatedUser());
	}
	
	

	

}
