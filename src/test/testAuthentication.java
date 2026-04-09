package test;

import junit.framework.TestCase;
import User.User;
import Authentication.Authentication;
import Repository.UserRepository;



public class testAuthentication extends TestCase {
	public Authentication auth;
	private UserRepository repository;

	protected void setUp() throws Exception {
		super.setUp();
		repository = new UserRepository();
		repository.saveUser(new User("John", "Pork", null));
		auth = new Authentication(repository);
	}

	public void testlogin() {
	 	assertFalse(auth.login("John","password"));
	 	assertTrue(auth.login("John","Pork"));
	}

	public void testauthorisedUser() {
		assertTrue(auth.login("John","Pork"));
		assertEquals("John", auth.getauthenticatedUser().getUsername());
	}

	public void testlogout() {
		assertTrue(auth.login("John","Pork"));
		auth.logout();
		assertNull(auth.getauthenticatedUser());
	}
		

	

}
