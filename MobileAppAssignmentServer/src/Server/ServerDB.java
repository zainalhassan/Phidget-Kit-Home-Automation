package Server;
//ZAIN AL-HASSAN 17106353

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import java.io.*;
import java.sql.*;


/**
 * Servlet implementation class sensorToDB
 */
@WebServlet("/ServerDB")
public class ServerDB extends HttpServlet 
{
	   private static final long serialVersionUID = 1L;
	   rfidData lastRfid = new rfidData("unknown", 0, false, "unknown");
	   rfidData lastValidRfid = new rfidData("unknown", 0, false, "unknown");
	   motorData motorData = new motorData(0, "unknown");
	   AndroidData androidData = new AndroidData(null, null, null, 0);
	   Gson gson = new Gson();
       Connection conn = null;
	   Statement stmt;
	   ResultSet rs;

	  public void init(ServletConfig config) throws ServletException 
	  {
	    super.init(config);
		  System.out.println("Server is up and running\n");	
		  System.out.println("Upload sensor data with http://localhost:8080/MobileAppAssignmentServer/ServerDB?rfidSerialNumber=somerfidvalue");
		  System.out.println("View last sensor reading at  http://localhost:8080/MobileAppAssignmentServer/ServerDB?getdata=true\n\n");		  
	  }

	  private void getConnection() 
	  {
		  // This will load the driver and establish a connection
			String user = "alhassz";
		    String password = "sawpRint4";
		    String url = "jdbc:mysql://mudfoot.doc.stu.mmu.ac.uk:6306/"+user;

			// Load the database driver
			try 
			{  
				Class.forName("com.mysql.jdbc.Driver").newInstance();
	        } 
			catch (Exception e) 
			{
	            System.out.println(e);
		    }
    		try 
    		{
	            conn = DriverManager.getConnection(url, user, password);
	            // System.out.println("DEBUG: Connection to database successful.");
	            stmt = conn.createStatement();
	        } 
    		catch (SQLException se) 
    		{
	            System.out.println(se);
	            System.out.println("\nError connecting with the given username and password?");
	        }
	  }

	  private void closeConnection() 
	  {
        try 
        {
            conn.close();
        } 
        catch (Exception e) 
        {
            System.out.println(e);
        }
	  }

	
    public ServerDB() 
    {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
	    response.setStatus(HttpServletResponse.SC_OK);
	    rfidData oneRfid = new rfidData("unknown",0, false, "unknown");
	    motorData oneMotor = new motorData(0, "unknown");

			String rfidJsonString  = request.getParameter("readerdata");
			String motorJsonString = request.getParameter("motorSerialNumber");

			if (rfidJsonString != null) 
			{
				oneRfid = gson.fromJson(rfidJsonString, rfidData.class);
				PrintWriter out = response.getWriter();
				insertIntoAttempts(oneRfid);
				out.print(checkForValid(oneRfid));
				out.close();
			}
			if (motorJsonString != null) {
				oneMotor = gson.fromJson(motorJsonString, motorData.class);
				PrintWriter out = response.getWriter();
				out.print(checkDoorLookUp(oneMotor));
				out.close();
			}
	    else 
	    {
	    	sendJSONString(response);
    	}
	}
	

	private void sendJSONString(HttpServletResponse response) throws IOException
	{
	      response.setContentType("application/json");
	   
	      String jsonRfid = gson.toJson(lastRfid);	      
	      PrintWriter out = response.getWriter();
	      System.out.println("DEBUG: lastRfid JSON: "+jsonRfid);
	      System.out.println("DEBUG: lastRfid TEXT: "+lastRfid.toString());
	      
	      // Change below to return json or text to the browser
	      out.println("lastRfid JSON"+jsonRfid);
	      out.println("lastRfid TEXT"+lastRfid.toString());
	      out.close();
	}
	
	private void insertIntoAttempts(rfidData oneRfid)
	{
		try 
		{
			String attemptsInsert = "insert into attempts(tagid, readerid, date, valid) values('"+ oneRfid.tagid  + "','" + oneRfid.readerid  + "',now()" + "," + oneRfid.isValid()  + ");";
			System.out.println("DEBUG: INSERT into attempts:  Query..." + attemptsInsert);
			getConnection();
			stmt.executeUpdate(attemptsInsert);
			closeConnection();
			System.out.println("DEBUG: Insert into attempts: SUCCESSFULL.\n");
			lastRfid = oneRfid;
		}
		catch (SQLException se) 
		{
			System.out.println(se);
			System.out.println("\nDEBUG: Insert into attempts: FAILED.\n");
		}
	}	
	
	private String checkDoorLookUp(motorData motorID) 
	{
		String validMotorJSON = "";
		String checkDoorLookUpSelect = "SELECT * FROM doorlookup WHERE motorid = '"+ motorID.motorid + "';";
		System.out.println("DEBUG: Select from checkDoorLookUpSelect: Query..." + checkDoorLookUpSelect);
		try
		{
			getConnection();
			rs = stmt.executeQuery(checkDoorLookUpSelect);
			System.out.println("DEBUG: Select from checkDoorLookUpSelect: SUCCESSFULL.\n");
			while(rs.next())
			{
				motorData motorNew = new motorData(0, "uknown");
				motorNew.setMotorid(rs.getInt("motorid"));
				motorNew.setRoom(rs.getString("room"));
	        	validMotorJSON = gson.toJson(motorNew);
	        	System.out.println(validMotorJSON);
			}
			closeConnection();
		}
		catch (SQLException se) 
		{
		    System.out.println(se);
	        System.out.println("DEBUG: Select from checkDoorLookUpSelect: FAILED.\n");
		}
		return validMotorJSON;
	}
	
	private String checkForValid(rfidData oneRfid)
	{
		String validTagJSON = "";
		String validtagsSelect = "SELECT * FROM validtags WHERE readerid = '"+ oneRfid.readerid + "' AND tagid = '" + oneRfid.tagid + "';";
		System.out.println("DEBUG: Select from validtags: Query..." + validtagsSelect);
		try
		{
			getConnection();
			rs = stmt.executeQuery(validtagsSelect);
			System.out.println("DEBUG: Select from validtags: SUCCESSFULL.\n");
			while(rs.next())
			{
				rfidData rfidNew = new rfidData("uknown", 0, false, "unknown");
	        	rfidNew.setTagid(rs.getString("tagid"));
	        	rfidNew.setReaderid(rs.getInt("readerid"));
	        	rfidNew.setValid(true);
	        	rfidNew.setRoom(rs.getString("room"));
	        	
	        	validTagJSON = gson.toJson(rfidNew);
	        	System.out.println(validTagJSON);
	        	lastRfid = rfidNew;
	        	updateToValid(rfidNew.tagid);
			}
			closeConnection();
		}
		catch (SQLException se) 
		{
		    System.out.println(se);
	        System.out.println("DEBUG: Select from validtags: FAILED.\n");
		}
		return validTagJSON;
	}
	
	private String updateToValid(String idToChange)
	{
		try
		{
			String attemptsUpdate = "UPDATE attempts SET valid = 1 WHERE tagid = '" + idToChange + "';";
			System.out.println("DEBUG: Update attempts: Query..." + attemptsUpdate);
			getConnection();
			int rowsAffected = stmt.executeUpdate(attemptsUpdate);
			if (rowsAffected >= 1)
			{
				System.out.println("DEBUG: Update attempts: SUCCESSFULL.\n");
			}
		}
		catch (SQLException se)
		{
		    System.out.println(se);
	        System.out.println("DEBUG: Update attempts: FAILED.\n"); 
		}
		return idToChange;
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
	    doGet(request, response);
	}
		
}
//ZAIN AL-HASSAN
