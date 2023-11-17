package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import DBConnection.ConnectionUnit;
import DTO.TableDTO;

/**
 * Data Access Object(DAO)
 * データベースへのビジネスロジックを担当
 * 
 * @author 3030905
 * @version 1.0.0 
 * 
 * @version 1.0.1
 * Searchメソッド追加
 * 
 * @version 1.0.2
 * betweenメソッド追加
 */
public class TableDAO {
	/**
	 * フィールド
	 */
	Connection conn = null;
	ResultSet rs = null;
	PreparedStatement pst = null;

	/**
	 * DataTable select all
	 * 全てのデータを呼び出すメソッド
	 * 
	 * @return list
	 */
	public ArrayList<TableDTO> selectTalbe() {
		ArrayList<TableDTO> list = new ArrayList<TableDTO>();
		String sql = "select * from kensyuuname order by \"年齢\" asc";

		try {
			conn = new ConnectionUnit().getConn();
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();

			while (rs.next()) {
				String name_sei = rs.getString("氏名(姓)");
				String name_name = rs.getString("氏名(名)");
				String yuubinn = rs.getString("郵便番号");
				String email = rs.getString("Eメール");
				int age = rs.getInt("年齢");

				TableDTO dto = new TableDTO(name_sei, name_name, yuubinn, email, age);
				list.add(dto);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			new ConnectionUnit().disConn();
		}
		return list;

	}

	/**
	 * CRUD(insert-C(create))
	 * データをインサートするメッソド
	 * 
	 * @param dto
	 * @return int
	 */
	public int insert(TableDTO dto) {
		String sql = "insert into kensyuuname (\"氏名(姓)\", \"氏名(名)\", 郵便番号, \"Eメール\", \"年齢\")\n"
				+ "values (?,?,?,?,?);";

		int result = 0;

		try {
			conn = new ConnectionUnit().getConn();
			pst = conn.prepareStatement(sql);
			pst.setString(1, dto.getName_sei());
			pst.setString(2, dto.getName_name());
			pst.setString(3, dto.getYuubinn());
			pst.setString(4, dto.getEmail());
			pst.setInt(5, dto.getAge());
			result = pst.executeUpdate();
		} catch (SQLException e) {

			if (dto.getName_sei() != null) {
				result = 0;
			} else {
				result = -1;
			}
			System.out.println("データに重複 = " + result);

		} finally {
			new ConnectionUnit().disConn();
		}

		System.out.println("insert result = " + result);
		return result;

	}

	/**
	 * CRUD(D-delete)
	 * データを削除するメッソド
	 * 
	 * @param sei
	 * @return int
	 */
	public int delete(String sei) {
		String sql = "delete from kensyuuname where \"氏名(姓)\" = ?;";

		int result = 0;

		try {
			conn = new ConnectionUnit().getConn();
			pst = conn.prepareStatement(sql);
			pst.setString(1, sei);
			result = pst.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			new ConnectionUnit().disConn();
		}

		return result;
	}

	/**
	 * CRUD(U-update)
	 * データをアップデートするメッソド
	 * 
	 * @param dto
	 * @return int
	 */
	public int update(TableDTO dto) {
		String sql = "update kensyuuname set \"氏名(姓)\"=?, \"氏名(名)\"=?, 郵便番号=?, \"Eメール\"=?, \"年齢\"=?\n"
				+ "where \"氏名(姓)\"=?;";

		int result = 0;

		try {
			conn = new ConnectionUnit().getConn();
			pst = conn.prepareStatement(sql);
			pst.setString(1, dto.getName_sei());
			pst.setString(2, dto.getName_name());
			pst.setString(3, dto.getYuubinn());
			pst.setString(4, dto.getEmail());
			pst.setInt(5, dto.getAge());
			pst.setString(6, dto.getName_sei());

			result = pst.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			new ConnectionUnit().disConn();
		}

		return result;
	}

	/**
	 * Search method
	 * 入力された姓であたりのデータを検索するメッソド
	 * 
	 * @param sei
	 * @return int
	 * @version 1.0.1
	 * 姓を一文字入力された場合でも検索できるように処理
	 * 
	 * @version 1.0.2
	 * 姓だけではなく名や郵便番号やEメールで検索が可能
	 */
	public ArrayList<TableDTO> search(String sei, int r) {
		ArrayList<TableDTO> list = new ArrayList<TableDTO>();
		String seisql = "select * from kensyuuname where \"氏名(姓)\" ";
		String namesql = "select * from kensyuuname where \"氏名(名)\" ";
		String yuubinnsql = "select * from kensyuuname where \"郵便番号\" ";
		String emailsql = "select * from kensyuuname where \"Eメール\" ";

		try {
			conn = new ConnectionUnit().getConn();

			if (r == 0) {
				/**
				 * 一文字か一文字じゃないのかを判別
				 */
				if (sei.length() == 1) {
					seisql += "like ?";
					sei = "%" + sei + "%";
				} else {
					seisql += "=?";
				}
				pst = conn.prepareStatement(seisql);
				pst.setString(1, sei);

				rs = pst.executeQuery();

			} else if (r == 1) {
				if (sei.length() == 1) {
					namesql += "like ?";
					sei = "%" + sei + "%";
				} else {
					namesql += "=?";
				}
				pst = conn.prepareStatement(namesql);
				pst.setString(1, sei);

				rs = pst.executeQuery();

			} else if (r == 2) {
				if (sei.length() == 3) {
					yuubinnsql += "like ?";
					sei = sei + "%";
				} else if (sei.length() == 4) {
					yuubinnsql += "like ?";
					sei = "%" + sei;
				} else {
					yuubinnsql += "=?";
				}
				pst = conn.prepareStatement(yuubinnsql);
				pst.setString(1, sei);

				rs = pst.executeQuery();

			} else if (r == 3) {
				emailsql += "like ?";
				sei = "%" + sei + "%";

				pst = conn.prepareStatement(emailsql);
				pst.setString(1, sei);

				rs = pst.executeQuery();

			}

			while (rs.next()) {

				String name_sei = rs.getString("氏名(姓)");
				String name_name = rs.getString("氏名(名)");
				String yuubinn = rs.getString("郵便番号");
				String email = rs.getString("Eメール");
				int age = rs.getInt("年齢");

				TableDTO dto = new TableDTO(name_sei, name_name, yuubinn, email, age);
				list.add(dto);
				System.out.println(dto);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			new ConnectionUnit().disConn();

		}

		return list;
	}

	/**
	 * 年齢が指定された範囲内のデータをリターンするメソッド
	 * 
	 * @param minage
	 * @param maxage
	 * @return list
	 */
	public ArrayList<TableDTO> between(int minage, int maxage) {
		ArrayList<TableDTO> list = new ArrayList<TableDTO>();
		String sql = "select * from kensyuuname where \"年齢\" between ? and ?;";

		try {
			conn = new ConnectionUnit().getConn();
			pst = conn.prepareStatement(sql);
			pst.setInt(1, minage);
			pst.setInt(2, maxage);

			rs = pst.executeQuery();

			while (rs.next()) {
				String name_sei = rs.getString("氏名(姓)");
				String name_name = rs.getString("氏名(名)");
				String yuubinn = rs.getString("郵便番号");
				String email = rs.getString("Eメール");
				int age = rs.getInt("年齢");

				TableDTO dto = new TableDTO(name_sei, name_name, yuubinn, email, age);
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
