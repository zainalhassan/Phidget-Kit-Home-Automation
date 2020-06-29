package Server;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

@WebServlet("/androidAppValidation")
public class androidAppValidation extends HttpServlet {

	private static final long serialVersionUID = 1L;
	//AndroidData androidInfo = new AndroidData(null, null, null, 0);
	Connection conn = null;
	Statement stmt;
	ResultSet rs;
	Gson gson = new Gson();

	private void getConnection() {
		String user = "alhassz";
		String password = "sawpRint4";
		String url = "jdbc:mysql://mudfoot.doc.stu.mmu.ac.uk:6306/" + user;

		// Loads the database driver
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception E) {
			System.out.println(E);
		}
		// gets the connection with the user/pass provided
		try {
			conn = DriverManager.getConnection(url, user, password);
			System.out.println("Connection to Database- Status: Successful.");
			stmt = conn.createStatement();
		} catch (SQLException SE) {
			System.out.println(SE);
			System.out.println("\nConnection to Database- Status: Failed.");
		}
	}

	private void closeConnection() {
		try {
			conn.close();
		} catch (Exception E) {
			System.out.println(E);
			System.out.println("Error occurred while closing the connection.");
		}

	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setStatus(HttpServletResponse.SC_OK);
		String getdata = req.getParameter("pinNumber");
		//String getdata2 = req.getParameter("roomid");
		System.out.println("pin number is"+getdata);
		String json = androidInfo(getdata);
		resp.getWriter().write(json);
	}

	private String androidInfo(String getdata) {
		String androidJSON = "";
		String androidSelect = "SELECT * FROM android WHERE pin = '"+ getdata +"';";
		System.out.println("DEBUG: SELECT Query, androidSelect: " + androidSelect);
		try {
			getConnection();
			rs = stmt.executeQuery(androidSelect);
			System.out.println("DEBUG: Select Query, androidSelect, Status: Successful. \n");
			while (rs.next()) {
				AndroidData androidNew = new AndroidData("uknown", "unknown", "unknown", 0000);
				androidNew.setPinNumber(rs.getInt("pin"));
				androidNew.setRoomId(rs.getString("room"));
				androidNew.setTagId(rs.getString("tagid"));
				androidNew.setAndroidId(rs.getString("androidid"));
				androidJSON = gson.toJson(androidNew);
				//androidInfo = androidNew;
				System.out.println(androidJSON);

			}
			closeConnection();
		} catch (SQLException SE) {
			System.out.println(SE);
			System.out.println("DEBUG: Select Query, androidSelect, Status: Failed. ");
		}
		return androidJSON;
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

	}

}