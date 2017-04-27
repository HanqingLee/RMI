package test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RMIPracticeImpl implements RMIPractice {
	// Connect to database
	private Database db;
	private Statement statement;

	public RMIPracticeImpl() {
		db = new Database();
		statement = db.creatConnect();
	}

	public RMIPracticeImpl(String dbname, String user, String pw) {
		db = new Database(dbname, user, pw);
		statement = db.creatConnect();
	}

	public int Register(String username, String pw) {
		int success = -1;
		int result;
		String sql;

		try {
			// check if there has existed the same user name
			ResultSet rs = null;
			sql = "select * from USER_INFO where name='" + username + "'";
			rs = statement.executeQuery(sql);

			if (!rs.next()) {
				// if there is no repeated name create new user in database
				sql = "insert into USER_INFO values(NULL,'" + username + "','" + pw + "')";
				result = statement.executeUpdate(sql);
				if (result == 1) {
					// Successfully created
					success = 0;
					System.out.println("Register success");
				} else {
					// Creation failed
					System.out.println("Register failed!");
					success = 1;
				}
			} else {
				// if there is repeated user name
				success = 2;
				System.out.println("The user is already exist");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}

	public int Login(String username, String pw) {
		int success = -1;
		String sql;

		try {
			// check if there is the user name required
			ResultSet rs = null;
			sql = "select * from USER_INFO where name='" + username + "'";
			rs = statement.executeQuery(sql);

			if (rs.next()) { // if there is the user name, check the password
				String password = rs.getString(3);
				if (pw.equals(password)) { // password correct
					System.out.println("Login success!");
					success = 0;
				} else { // password incorrect
					System.out.println("Failed! Password is incorrect!");
					success = 1;
				}
			} else { // there is not a user required
				System.out.println("Failed! User is not in existence!");
				success = 2;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}
}
