package test;

import junit.framework.TestCase;
import Authentication.Authentication;


public class testAuth extends TestCase {
	public Authentication auth;
	
	public void testlogin() {
	 	assertFalse(auth.login("John","password"));}
	public void testauthorisedUser() {
		auth.login("John","Pork");
		assertEquals(auth.getauthenticatedUser().getUsername(),"John");}
	public void testlogout() {
		auth.login("John","Pork");
		auth.logout();
		assertNull(auth.getauthenticatedUser());}
		

	

}
