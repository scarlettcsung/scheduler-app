package Authentication;
import User.User;
import UserRepository.UserRepository;



public class Authentication {
	//Authenticated User, the User that will have their details presented
	private User authenticatedUser;
	//Instanstiating repository
	private UserRepository repository;
	
	public Authentication() {
		repository = UserRepository.getInstance();
		this.authenticatedUser=null;
	}

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
		else {System.out.println("Username not found");
			return false;}
		}
	public void logout() {
		authenticatedUser=null;}
	
	public User getauthenticatedUser() {
		return authenticatedUser ;
	}
		
	}
