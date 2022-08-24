import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

public class App {

	Scanner sc = new Scanner(System.in);
	String choice;
	String str1;
	String str2;
	String user;
	String password;
	String sid;
	String str_date;
	String stid;
	String cidd;
    int time;
    String cid;
    float status;
    int duration;
    String cname;
    int time_new;
    String cid_new;
    float status_new;
    int updatedTime;
    
	public static void main(String[] args) throws ClassNotFoundException, SQLException {

		App a = new App();
		a.login();
	}

	public String login() throws ClassNotFoundException, SQLException {

		Class.forName("com.mysql.jdbc.Driver");
		System.out.println("-----------------------Enter login details------------------------------");
		System.out.println();
		System.out.printf("Enter the username :");
		str1 = sc.nextLine();
		System.out.printf("Enter the password :");
		str2 = sc.nextLine();

		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/courses", "root", "root");
		Statement st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = st.executeQuery(
				"Select * from students where student_name ='" + str1 + "' and log_password ='" + str2 + "'");

		if (rs.next()) {

			System.out.println("Logged in with " + str1);
			sid = rs.getString(2);
			menu();
		}

		else {
			System.out.println("Wrong details entered please login again");
			login();
		}

		return sid;

	}


	public void menu() throws SQLException, ClassNotFoundException {

		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/courses", "root", "root");
		Statement st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
    
        try {
		while (true) {
			
			System.out.println("a.Display all courses" + "\n" + "b.View enrolled courses" + "\n"
					+ "c.Enroll in a course" + "\n" + "d.Clock in time" + "\n" + "e.View Summary" + "\n" + "f.Exit");

			System.out.println();
			
			System.out.printf("Enter your option :");
			
            choice = sc.nextLine();
            
			if (choice.matches("[^a-fA-F]")) {
				throw new IllegalArgumentException();
			}
			else {
			switch (choice) {

			case "a":

				String query_a = "Select * from courses";
				ResultSet rs_a=st.executeQuery(query_a);  

				while (rs_a.next()) {

					System.out.println(rs_a.getString(2) + "\t" + rs_a.getString(3) + "\t" + rs_a.getString(4) + "\t"
							+ rs_a.getInt(5));
				}
				break;

			case "b":

				String query_b = "Select * from viewenrolled where student_id ='"+sid+"'";  
				ResultSet rs_b=st.executeQuery(query_b);  
				
				if (!rs_b.isBeforeFirst()) {

					System.out.println("-------------------------------------------No data found please enroll first--------------------------------- ");
				} else {

					while (rs_b.next()) {

						System.out.println(rs_b.getString(2) + "\t" + rs_b.getString(3) + "\t" + rs_b.getString(4));
					}
				}
				break;

			case "c":

				System.out.printf("Please enter the course id to enroll :");
				cid = sc.nextLine();

				Date edate = new Date();
				SimpleDateFormat f = new SimpleDateFormat("dd-MM-yy");
				str_date = f.format(edate);

				String query_c = "Select * from courses where course_id =?";
				PreparedStatement c = con.prepareStatement(query_c, ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
				c.setString(1, cid);
				ResultSet rs_c = c.executeQuery();
				
				String query_ca= "Select * from viewenrolled where course_id ='"+cid+"' and student_id ='"+sid+"'";
				PreparedStatement ca = con.prepareStatement(query_ca, ResultSet.TYPE_SCROLL_SENSITIVE,
						ResultSet.CONCUR_UPDATABLE);
				ResultSet rs_ca = ca.executeQuery();
				
				if(rs_ca.next()) {
					
					System.out.println("You have already enrolled to the course");
			       
				}
				else {
				while (rs_c.next()) {

					System.out.println("You have succesfully enrolled to " + rs_c.getString("course_name"));
					
					cname = rs_c.getString("course_name");
				
			 	String query_c_a = "insert into viewenrolled values(?,?,?,?,?)";
                PreparedStatement c_a = con.prepareStatement(query_c_a,ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                c_a.setString(1, null);
                c_a.setString(2, cid);
                c_a.setString(3,rs_c.getString("course_name"));
                c_a.setString(4,str_date);
                c_a.setString(5,sid);
                int rs_c_a  = c_a.executeUpdate();               	
                System.out.println(rs_c_a+" rows affected in viewenrolled");
                
			}
				}	
				
				break;

			case "d":

				System.out.printf("Please enter the course id : ");
			    cidd = sc.nextLine();
			    
			    System.out.printf("Enter the clock in time :");
			    time = sc.nextInt();
			    
				String query_d = "Select * from courses where course_id =?";
				PreparedStatement d = con.prepareStatement(query_d, ResultSet.TYPE_SCROLL_SENSITIVE,
						ResultSet.CONCUR_UPDATABLE);
				d.setString(1, cidd);
				ResultSet rs_d = d.executeQuery();
				
				String query_i = "Select * from viewsummary where course_id ='"+cidd+"' and student_id ='"+sid+"'";
				PreparedStatement i = con.prepareStatement(query_i, ResultSet.TYPE_SCROLL_SENSITIVE,
						ResultSet.CONCUR_UPDATABLE);
				ResultSet rs_i = i.executeQuery();
				
				if(rs_i.next()) {
				
				      System.out.println("Previous enrollment found, we will update your clock-in time");
				      
					  if(Integer.toString(time).matches("[^0-12]")){
						  throw new InputMismatchException();
					  }
					  else {
					  updatedTime = rs_i.getInt("time_spent")+time;
					  
					  status_new =((float)updatedTime)*100/rs_i.getInt("duration");
					  
				      String query_c_f = "update viewsummary set time_spent ='"+updatedTime+"',status ='"+status_new+"' where student_id ='"+sid+"' and course_id='"+cidd+"'";
		              PreparedStatement c_f = con.prepareStatement(query_c_f,ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				
		              int rs_c_f  = c_f.executeUpdate();               	
		              System.out.println(rs_c_f+" rows updated in viewsummary");
				      }
				      }
				else {
				if (rs_d.next()) {
					
					System.out.println("No previous enrollment records found");
					
					if(Integer.toString(time).matches("[^0-12]")) {
				           throw new InputMismatchException();
					}
					else {
						
					System.out.println("Succesfully clocked-in " + time + " hr for " + rs_d.getString("course_name"));
				
					duration = rs_d.getInt("Duration");
				
					status = ((float)time)*100/duration;
							
					String query_c_e = "insert into viewsummary values(?,?,?,?,?,?,?)";
	                PreparedStatement c_e = con.prepareStatement(query_c_e,ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
	                
	                c_e.setString(1, null);
	                c_e.setString(2, cidd);
	                c_e.setString(3,cname);
	                c_e.setInt(4,duration);
	                c_e.setString(7,sid);
	                c_e.setInt(5, time);
	                c_e.setFloat(6, status);
	                
	                int rs_c_e  = c_e.executeUpdate();               	
	                System.out.println(rs_c_e+" rows affected in viewsummary");
					}
				}
				}
				choice = sc.nextLine();
				System.out.println();
				break;
						
			case "e":
				
				String query_g = "Select * from viewsummary where student_id ='"+sid+"'";  
				ResultSet rs_g=st.executeQuery(query_g);  

				if (!rs_g.isBeforeFirst()) {

					System.out.println("--------------------------------No data found please enroll-------------------------------------------");
				} else {

					while (rs_g.next()) {

						System.out.println(rs_g.getString(2) + "\t" + rs_g.getString(3) + "\t" + rs_g.getInt(4)+ "\t" + rs_g.getInt(5)
						+"\t"+rs_g.getFloat(6)+"\t"+rs_g.getString(7));
						
					}
				}
				break;
				

			case "f":

				System.out.println("Logged out");
				login();
				break;
			
		    default:
		    	System.out.println("This is default");
		    	break;
			}
			}
			
		} 
		}   catch(IllegalArgumentException e) {
			System.out.println("-------------------IllegalArgumentException!!!---------------------------");
			System.out.println("Invalid User Input. Please enter a value from a to f :");
			System.out.println();
			menu();
	}  
            catch(InputMismatchException e){
	    	   try {
	    	        throw new NumberFormatException();
	    	  }
	    	   catch(NumberFormatException x) {
	    		  System.out.println(x);
	    		  System.out.println("----------------------Non-numeric!!!-----------------------------");
	    		  System.out.println();
	    		  choice = sc.nextLine();
	    	      menu();
	    	  }
	      }
        
} 
}