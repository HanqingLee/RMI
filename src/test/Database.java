package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Database {
	private String driver = "com.mysql.jdbc.Driver";
	
	private String url = "jdbc:mysql://localhost:3306/";
	private String dbuser = "root";
	private String dbpw = "Gerenyinsi";
	
	public Database(){
		this.url += "rmi_server";
	}
	
	public Database(String dbname, String user, String pw){
		this.url += dbname;
		this.dbuser = user;
		this.dbpw = pw;
	}
	
	public Statement creatConnect(){
		Statement statement = null;
		try{
			Class.forName(driver);
	        Connection connection = DriverManager.getConnection(url, dbuser, dbpw);	 
	        statement = connection.createStatement();	        
	        System.out.println("Success connected to MySQL");  
		} catch (Exception e){
			e.printStackTrace();
		}
		return statement;
	}
}
