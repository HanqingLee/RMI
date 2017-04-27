package test;

public interface RMIPractice {
	//return 0 means register success, -1 means cannot connect to databse, 1 means cannot add user to database, 2 means user is already exist.
	public int Register(String username, String pw);
	//return 0 means login success, -1 means cannot connect to database, 1 means the password is incorrect, 2 means user is not exist.
	public int Login(String username, String pw);
}
