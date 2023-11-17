package DTO;

/**
 * Data Transfer Object(DTO)
 * データの交換の為のクラス
 * 
 * @author 3030905
 * @version	 1.0.0
 */
public class TableDTO {
	private String name_sei;
	private String name_name;
	private String yuubinn;
	private String email;
	private int age;

	/**
	 * デフォルトコンストラクタ
	 */
	public TableDTO() {
	};

	/**
	 * DTOのオブジェクト
	 * 
	 * @param name_sei
	 * @param name_name
	 * @param yuubinn
	 * @param email
	 * @param age
	 */
	public TableDTO(String name_sei, String name_name, String yuubinn, String email, int age) {
		this.name_sei = name_sei;
		this.name_name = name_name;
		this.yuubinn = yuubinn;
		this.email = email;
		this.age = age;
	}

	/**
	 * DTOオブジェクトのデータを表示する為のtoStringのOverride
	 */
	@Override
	public String toString() {
		return "氏名(姓)=" + name_sei + ", 氏名(名)=" + name_name + ", 郵便番号=" + yuubinn + ", Eメール="
				+ email + ", 年齢=" + age;
	}

	/**
	 * この下はgetterやsetterの部分です
	 */
	public String getName_sei() {
		return name_sei;
	}

	public void setName_sei(String name_sei) {
		this.name_sei = name_sei;
	}

	public String getName_name() {
		return name_name;
	}

	public void setName_name(String name_name) {
		this.name_name = name_name;
	}

	public String getYuubinn() {
		return yuubinn;
	}

	public void setYuubinn(String yuubinn) {
		this.yuubinn = yuubinn;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

}
