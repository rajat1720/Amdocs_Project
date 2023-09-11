package com.amdocs.petsearch.doa;
import java.sql.*;

 public class MakeConnection {
	
	public void callConnection(){
		Connection con;
		Statement stmt;
		ResultSet rs;
		System.out.println("Inside Make Connection2");
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver"); //registration 
			System.out.println("Inside try after class.forname");
			con=DriverManager.getConnection("Jdbc:Oracle:thin:@localhost:1521:orcl","scott","tiger"); //connection

			System.out.println("after getConnection statement");
			stmt=con.createStatement();
			System.out.println("after createstatement");
	
			rs=stmt.executeQuery("Select petcategory, color from pet");
			System.out.println("After select statement");

			while(rs.next())
				System.out.println(rs.getString(1)+" "+rs.getString(2));		
			}
		catch(Exception e){e.printStackTrace();
		}
	}
}
	
