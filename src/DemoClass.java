import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/* 
 * 1. Import the package
 * 2. Load and register the driver
 * 3. Create connection con
 * 4. Create statement st
 * 5. Fire the query
 * 6. Get the results
 * 7. close st and con
 * 
 */



public class DemoClass {

	public static void main(String[] args) throws Exception 
	{
	    String url ="jdbc:mysql://localhost:3306/courses";
	    String uname ="root";
	    String pass ="root";
	    String query="Select * from students";
		Class.forName("com.mysql.cj.jdbc.Driver");
		//Connection con = DriverManager.getConnection(url, uname, pass);
		//PreparedStatement st = con.prepareStatement(query,ResultSet.TYPE_SCROLL_SENSITIVE, 
                                                    //ResultSet.CONCUR_UPDATABLE);
		//Statement st = con.createStatement();
		//ResultSet rs  = st.executeQuery();
		
		Scanner sc = new Scanner(System.in);
		System.out.println("-----------------------Enter login details------------------------------");
		System.out.println();
		System.out.printf("Enter the username :");
		String str1=sc.nextLine();
		System.out.printf("Enter the password :");
		String str2 = sc.nextLine();
		
		int k=1;
		boolean flag = true;
		while(flag) {
		
		Connection con = DriverManager.getConnection(url, uname, pass);
		PreparedStatement st = con.prepareStatement(query,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
		ResultSet rs  = st.executeQuery();
		
		while(rs.next()) {
			
			rs.absolute(k);
			String user = rs.getString(3);
			String password = rs.getString(4);		
		    
		    
			//System.out.println(user);
			//System.out.println(password);
			if(str1.equals(user) && str2.equals(password)) {
				
				System.out.println("Logged in with "+user);
				
				System.out.println();
				
				System.out.println("a.Display all courses" +"\n" + "b.View enrolled courses" +"\n" + "c.Enroll in a course" +
				"\n" + "d.Clock in time" +"\n" + "e.View Summary" + "\n" + "f.Exit");
				
				System.out.println();
				
				System.out.printf("Enter your option :");
				String choice = sc.nextLine();
				
				switch(choice) {
				
				case "a":
					 String query_a = "Select * from courses";
                     PreparedStatement a = con.prepareStatement(query_a,ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                     ResultSet rs_a  = a.executeQuery();
                     
                     while(rs_a.next()) {
                    	 
                    	 System.out.println(rs_a.getString(2)+"\t"+rs_a.getString(3)+"\t"+rs_a.getString(4)+"\t"+rs_a.getInt(5));
                     }
                     break;
				case "b":
					
					String query_b = "Select * from viewenrolled";
                    PreparedStatement b = con.prepareStatement(query_b,ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    ResultSet rs_b  = b.executeQuery();
                    
                    if(!rs_b.isBeforeFirst()) {
                    	
                    	System.out.println("No data found");
                    }
                    else {
                    	 
                        while(rs_b.next()) {
                        	 
                        	 System.out.println(rs_b.getString(2)+"\t"+rs_b.getString(3)+"\t"+rs_b.getString(4));
                         }	
                    }
                    break;
                    
				case "c":
					
					System.out.printf("Please enter the course id :");
					String cid = sc.nextLine();
					
					Date edate = new Date();
				    SimpleDateFormat f = new SimpleDateFormat("dd/MM/yy");
				    String str_date = f.format(edate);
					
					String query_c = "Select course_name from courses where course_id =?";
                    PreparedStatement c = con.prepareStatement(query_c,ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    c.setString(1, cid);
                    ResultSet rs_c  = c.executeQuery();
              
                    while(rs_c.next()) {
              
                   	System.out.println("You have succesfully enrolled to "+rs_c.getString("course_name")); 
                   	
                   	String query_c_a = "insert into viewenrolled values(?,?,?,?,?)";
                    PreparedStatement c_a = con.prepareStatement(query_c_a,ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    c_a.setString(1, null);
                    c_a.setString(2, cid);
                    c_a.setString(3,rs_c.getString("course_name"));
                    c_a.setString(4,str_date);
                    c_a.setString(5,rs.getString(2));
                    int rs_c_a  = c_a.executeUpdate();               	
                    System.out.println(rs_c_a+" rows affected");
                    }
                       
                   break;   
                  
				case "d":
					
					System.out.printf("Please enter the course id : ");
					String cidd = sc.nextLine();
					
					System.out.printf("Enter the clock in time : ");
					int time = sc.nextInt();
					
					String query_d = "Select course_name from courses where course_id =?";
                    PreparedStatement d = con.prepareStatement(query_d,ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    d.setString(1, cidd);
                    ResultSet rs_d  = d.executeQuery();
                    
                    while(rs_d.next()) {
                    	
                    	System.out.println("Succesfully clocked-in "+time+ " hr for "+rs_d.getString("course_name"));
                    }
                   break;
				
				case "e":
					
					
                   
                   
                   
				}
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
			
		}
			else if (!str1.equals(user) || !str2.equals(password)) {
		        if(k<2) {
				System.out.println("Moving to next row now");
				k++;
		        }
		        else {
		        	System.out.println("No such value found");
		        	flag=false;
		        }
			}
			
	}
	
	
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		//ResultSetMetaData rsmd=rs.getMetaData(); 
		//st.setInt(1,id);
		//int count = st.executeUpdate();
		
		//System.out.println(count + " rows affected");
		
		//System.out.println("Total columns: "+rsmd.getColumnCount());  
		//System.out.println("Column Name of 1st column: "+rsmd.getColumnName(2));  
		//System.out.println("Column Type Name of 1st column: "+rsmd.getColumnTypeName(2)); 
		
		/*while(rs.next()) {
		
		String studentData = rs.getInt(1) + " " + rs.getString(2);
		
		System.out.println(studentData);
		
		}*/
		
		st.close();
		con.close();

	}

}
}