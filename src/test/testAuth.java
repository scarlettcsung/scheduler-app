package test;

import junit.framework.TestCase;
import User.User;
import Authentication.Authentication;
import UserRepository.UserRepository;



public class testAuth extends TestCase {
	public Authentication auth;	
	public void testlogin() {
	 	assertFalse(auth.login("John","password"));
	 	assertTrue(auth.login("John","Pork"));
	 	}
	public void testauthorisedUser() {
		auth.login("John","Pork");
		assertEquals(auth.getauthenticatedUser().getUsername(),"John");}
	public void testlogout() {
		auth.login("John","Pork");
		auth.logout();
		assertNull(auth.getauthenticatedUser());}
		

	

}
