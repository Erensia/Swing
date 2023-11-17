package DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Connectionオブジェクト
 * postgreSQLとの接続のクラス
 * 
 * @author 3030905
 * @version 1.0.0
 */


/**
 * instanceの為のクラス
 * 
 * @author 3030905
 * @version 1.0.0
 */
public class ConnectionUnit {
	/**
	 *フィールド
	 */
	Connection conn = null;
	ResultSet rs = null;
	PreparedStatement pst = null;

	/**
	 * コンストラクタ
	 */
	public ConnectionUnit() {
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres",
					"postgresql");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

/**
 * DBとの接続を行うメッソド
 * 
 * @author 3030905
 * @version 1.0.0
 */	
	public Connection getConn() {
		return conn;
	}

/**
 * DBとの接続を切る為のメッソド
 * 
 * @author 3030905
 * @version 1.0.0
 */
	public void disConn() {
		try {
			if (rs != null) {
				rs.close();
			}
			if (pst != null) {
				pst.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		} finally {

		}
	}

}
