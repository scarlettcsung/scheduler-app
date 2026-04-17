package Authentication;

import Repository.UserRepository;
import User.User;

public class Authentication {
	//Authenticated User, the User that will have their details presented
	private User authenticatedUser;
	//Instanstiating repository
	private UserRepository repository;
	// changed to 
	public Authentication(UserRepository repository) { 
	    this.repository = repository;
	    this.authenticatedUser = null;
	}
 // changed 
	public boolean login(String UserName, String UserPassword) {
		//
		User currentUser = repository.findUsername(UserName);
		
		if(currentUser!=null){
			if (currentUser.getPassword().equals(UserPassword)) 
			{
				authenticatedUser = currentUser;
				return true;
				}
			else {
	
			return false;
			}
			} 
		else {
			return false;}
		}
	public void logout() {
		authenticatedUser=null;}
	
	public User getauthenticatedUser() {
		return authenticatedUser ;
	}
		
	}
