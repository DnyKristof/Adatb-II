package dbbead2;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class DBMethods {
	
	
	
	public Connection Connect() {
		
		 
		Connection conn = null;
		String url = "jdbc:oracle:thin:@//193.6.5.58:1521/xe";
		try {
			conn = DriverManager.getConnection(url,"H23_GQOKMW","GQOKMW_A9");
			System.out.println("Sirkes kapcsolodas");
			return conn;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return conn;
		}
	}
	
	public void Disconnect(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
				System.out.println("Sikeres lekapcsolódás");

			} catch (Exception ex) {
				ex.getMessage();
			}
		}
	}
	public void Reg() {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			System.out.println("Sikeres driver regisztrálás");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	
	public void RANDOM_AUTO(int num) {
		Connection conn = Connect();
		
		String sql = "{CALL MENTO.RANDOM_AUTO(?)}"; 
		try {
		    CallableStatement cstmt = conn.prepareCall(sql);
		    cstmt.setInt(1,num);
		    cstmt.execute(); 
		    cstmt.close();
		    System.out.println("Autok sikeresen feltöltve");
		} catch (SQLException e) {
		    e.printStackTrace();
		}
		Disconnect(conn);
	}
	
	public void FILEBOL_ORVOS() {
		Connection conn = Connect();
		
		String sql = "{CALL MENTO.FILEBOL_ORVOS}"; 
		try {
		    CallableStatement cstmt = conn.prepareCall(sql);
		    cstmt.execute(); 
		    cstmt.close();
		    System.out.println("File sikeresen beolvasva");
		} catch (SQLException e) {
		    e.printStackTrace();
		}
		Disconnect(conn);
		
	}
	public void UJ_HIVAS(String Nev,String telefonszam,String hely,String diszpecser,int kiküldve) {
		Connection conn = Connect();
		
		String sql = "{CALL MENTO.UJ_HIVAS(?,?,?,?,?)}"; 
		try {
		    CallableStatement cstmt = conn.prepareCall(sql);
		    cstmt.setString(1,Nev);
		    cstmt.setString(2,telefonszam);
		    cstmt.setString(3,hely);
		    cstmt.setString(4,diszpecser);
		    cstmt.setInt(5,kiküldve);
		    cstmt.execute(); 
		    cstmt.close();
		    System.out.println("Új hívás sikeres felvitele");
		} catch (SQLException e) {
		    e.printStackTrace();
		}
		Disconnect(conn);
		
	}
	public int NAPI_HIVASSZAM(Date date) {
		Connection conn = Connect();
		
		String sql = "{CALL NAPI_HIVASSZAM(?)}"; 
		try {
		    CallableStatement cstmt = conn.prepareCall(sql);
		    cstmt.setDate(1, date);
		    cstmt.execute();
		    cstmt.close();
		    Disconnect(conn);
		    return cstmt.getInt(1);
		} catch (SQLException e) {
		    e.printStackTrace();
		    Disconnect(conn);
		    return -1;
		}
		
		
	}
	
	public void KIVONLUAS_FELTOLTES(int szam) {
		Connection conn = Connect();
		
		String sql = "{CALL MENTO.KIVONLUAS_FELTOLTES(?)}"; 
		try {
		    CallableStatement cstmt = conn.prepareCall(sql);
		    cstmt.setInt(1, szam);
		    cstmt.execute(); 
		    cstmt.close();
		    System.out.println("Kivonulás sikeresen feltöltve");
		} catch (SQLException e) {
		    e.printStackTrace();
		}
		Disconnect(conn);
		
	}
	public void HIVAS_ADMINISZTRALASA(int pbid,int kimente) {
		Connection conn = Connect();
		
		String sql = "{CALL MENTO.HIVAS_ADMINISZTRALASA(?,?)}"; 
		try {
		    CallableStatement cstmt = conn.prepareCall(sql);
		    cstmt.setInt(1, pbid);
		    cstmt.setInt(2, kimente);
		    cstmt.execute(); 
		    cstmt.close();
		    System.out.println("Bejelentés sikeresen adminisztrálva");
		} catch (SQLException e) {
		    e.printStackTrace();
		}
		Disconnect(conn);
		
	}
	
	public void HIVAS_NEVRE(String Nev) {
		Connection conn = Connect();
		
		String sql = "{CALL MENTO.HIVAS_NEVRE(?)}"; 
		try {
		    CallableStatement cstmt = conn.prepareCall(sql);
		    cstmt.setString(1, Nev);

		    cstmt.execute();
		    ResultSet rs = (ResultSet)cstmt.getObject(1);
		    System.out.println("ID,Dátum,Hívó,Telefonszám,Hely,Diszpécser,Kiküldve");
		    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		    while(rs.next()) {
		    	System.out.println(Integer.toString(rs.getInt(1))+','+dateFormat.format(rs.getDate(2))+','+rs.getString(3)+','+rs.getString(4)+','+rs.getString(5)+','+rs.getString(6)+','+Integer.toString(rs.getInt(7)));
		    }
		    cstmt.close();
		} catch (SQLException e) {
		    e.printStackTrace();
		}
		Disconnect(conn);
	}
	
	public void HIVAS_IDOSZAK(Date min,Date max) {
		Connection conn = Connect();
		
		String sql = "{CALL MENTO.HIVAS_IDOSZAK(?,?)}"; 
		try {
		    CallableStatement cstmt = conn.prepareCall(sql);
		    cstmt.setDate(1, min);
		    cstmt.setDate(2, max);

		    cstmt.execute();
		    ResultSet rs = (ResultSet)cstmt.getObject(2);
		    System.out.println("ID,Dátum,Hívó,Telefonszám,Hely,Diszpécser,Kiküldve");
		    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		    while(rs.next()) {
		    	System.out.println(Integer.toString(rs.getInt(1))+','+dateFormat.format(rs.getDate(2))+','+rs.getString(3)+','+rs.getString(4)+','+rs.getString(5)+','+rs.getString(6)+','+Integer.toString(rs.getInt(7)));
		    }
		    cstmt.close();
		} catch (SQLException e) {
		    e.printStackTrace();
		}
		Disconnect(conn);
	}
		
}
	
	
	
