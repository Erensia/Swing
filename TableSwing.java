package Swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.NumberFormatter;

import DAO.TableDAO;
import DTO.TableDTO;

/**
 * Swingでdataを表示するクラス
 * 
 * @author 3030905
 * @version 1.0.0
 * 
 * @version 1.0.1
 * データを入力する時にデータの有効性検査を追加
 * 
 * @version 1.0.2
 * Search処理の出力画面をDialogからテーブルに変更
 * 
 * @version 1.0.3
 * Search処理中、一文字でも検索できるように変更
 * 
 * @version 1.0.4
 * 全てのデータが見れるようボタンを追加
 * 
 * @version 1.0.5
 * データの入力窓を理解しやすく上に案内のラベルをつけた
 * 
 * @version 1.0.6
 * データを表示するテーブルのセル幅や文字の位置を調整
 * 幅はテーブルのサイズを直接指定したので無理でした
 * 
 * @version 1.0.7
 * BorderLineを追加
 * 
 * @version 1.0.8
 * 色塗
 * 
 * @version 1.0.9
 * 検索機能のセグメンテーション
 */
public class TableSwing extends JFrame implements ActionListener, PropertyChangeListener {

	/**
	 * フィールド
	 */
	String[] columnName = { "氏名(姓)", "氏名(名)", "郵便番号", "Ｅメール", "年齢" };
	String[] selectage = { "10~19", "20~29", "30~39", "40~49", "50~59", "60~69", "70~79", "80~89", "90~99" };

	JTable table;
	DefaultTableModel tablemodel;

	JLabel annnai = new JLabel("<html><center>データの入力はこの下の入力窓を利用してください↓	</center></html>");
	JLabel shitalabel = new JLabel("<html><center>データを選択しなさい<br>姓や名や郵便番号やＥメールでデータを検索する事ができます</center></html>");
	JLabel seilabel = new JLabel("氏名(姓)");
	JTextField seitxf = new JTextField(5);
	JLabel namelabel = new JLabel("氏名(名)");
	JTextField nametxf = new JTextField(5);
	JLabel yuubinnlabel = new JLabel("郵便番号");
	JTextField yuubinntxf = new JTextField(5);
	JLabel emaillabel = new JLabel("Eメール");
	JTextField emailtxf = new JTextField(5);
	JLabel agelabel = new JLabel("年齢");
	JFormattedTextField agetxf = new JFormattedTextField();
	JLabel shitasei = new JLabel("氏名(姓)");
	JTextField shitaseitxf = new JTextField(5);
	JLabel shitaname = new JLabel("氏名(名)");
	JTextField shitanametxf = new JTextField(5);
	JLabel shitayuubinn = new JLabel("郵便番号");
	JTextField shitayuubinntxf = new JTextField(5);
	JLabel shitaemail = new JLabel("Eメール");
	JTextField shitaemailtxf = new JTextField(5);
	JLabel searchannnai = new JLabel("検索はこちら→");

	JButton insertbtn = new JButton("入力");
	JButton deletebtn = new JButton("削除");
	JButton searchbtn = new JButton("検索");
	JComboBox agelist = new JComboBox(selectage);
	JButton allbtn = new JButton("全データ");

	JPanel panel = new JPanel();
	JPanel insertdata = new JPanel();
	JPanel shitapanel = new JPanel();
	JPanel shita = new JPanel();

	/**
	 * コンストラクタ
	 */
	public TableSwing() {
		/**
		 * テーブルのオブジェクトを作成
		 */
		table = new JTable();

		/**
		 * テーブルの主キーの姓をアップデートできないように処理
		 */
		tablemodel = new DefaultTableModel(columnName, 0) {
			/**
			 * 最初の列は姓で、姓はPKとUQのデータなので修正を止める
			 * @param row
			 * @param column
			 * @return boolean
			 */
			public boolean isCellEditable(int row, int column) {
				if (column == 0) {
					return false;
				} else {
					return true;
				}

			}
		};

		/**
		 * テーブルモデルをテーブルにつく
		 */
		table.setModel(tablemodel);

		/**
		 * テーブルのheaderを動かさないように処理
		 */
		table.getTableHeader().setReorderingAllowed(false);

		/**
		 * テーブルのヘッダの色を塗る
		 */
		table.getTableHeader().setBackground(new Color(221, 160, 221));

		/**
		 * テーブルのサイズを設定
		 */
		table.setPreferredScrollableViewportSize(new Dimension(900, 150));
		shitalabel.setPreferredSize(new Dimension(600, 50));

		/**
		 * テーブルのセルの幅や文字の位置を調整
		 * セルの整列のためDefaulttableCellRendererオブジェクトを利用
		 */
		DefaultTableCellRenderer cellaligncenter = new DefaultTableCellRenderer();
		cellaligncenter.setHorizontalAlignment(JLabel.CENTER);

		/**
		 * テーブルのcolumnへ適用
		 */
		for (int i = 0; i < columnName.length; i++) {
			table.getColumn(columnName[i]).setCellRenderer(cellaligncenter);
			table.getColumn(columnName[i]).setPreferredWidth(5);
		}

		/**
		 * テーブルを選択したら選択したセルのデータをラベルに表示
		 */
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.rowAtPoint(e.getPoint());
				int col = table.columnAtPoint(e.getPoint());
				if (row >= 0 && col >= 0) {
					String ColName = columnName[col];
					Object data = tablemodel.getValueAt(row, col);
					String str = String.format("'%sの%s'を選びました", ColName, data);
					String shita = "変更望みならダブルクリックして新しいデータを入力してください";
					/**
					 * labelはhtmlを利用して修正する事ができます。
					 */
					shitalabel.setText("<html><center>" + str + "<br>" + shita + "</center></html>");
				}
				super.mouseClicked(e);
			}
		});

		/**
		 * テーブルをScrollできるように設定
		 */
		JScrollPane scroll = new JScrollPane(table);

		/**
		 * @version 1.0.7
		 * テーブルを囲む線を追加
		 */
		scroll.setBorder(new LineBorder(Color.black, 1, true));

		/**
		 * ボタンにコマンドやアクションを追加
		 */
		insertbtn.setActionCommand("insert");
		insertbtn.addActionListener(this);
		deletebtn.setActionCommand("delete");
		deletebtn.addActionListener(this);
		searchbtn.setActionCommand("search");
		searchbtn.addActionListener(this);
		allbtn.setActionCommand("setall");
		allbtn.addActionListener(this);

		/**
		 * 年齢を入力されるフィールドにint以外の値を入れないように処理
		 */
		NumberFormatter ageformat = new NumberFormatter();
		ageformat.setValueClass(Integer.class);
		ageformat.setMinimum(new Integer(0));
		ageformat.setMaximum(new Integer(99));
		agetxf = new JFormattedTextField(ageformat);
		agetxf.setPreferredSize(new Dimension(57, 20));
		agetxf.setText("0");

		/**
		 * comboboxを追加
		 * 年齢の範囲を指定してその範囲のデータだけを表示するcombobox
		 */
		agelist.setSelectedIndex(0);
		agelist.setBackground(new Color(221, 160, 221));
		agelist.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String agerange = agelist.getSelectedItem().toString();

				String min = agerange.substring(0, 2);
				String max = agerange.substring(3, 5);

				System.out.println("min = " + min);
				System.out.println("max = " + max);

				int minage = Integer.parseInt(min);
				int maxage = Integer.parseInt(max);

				TableDAO dao = new TableDAO();
				ArrayList<TableDTO> list = new ArrayList<TableDTO>();
				list = dao.between(minage, maxage);

				showTable(list, minage, maxage);

			}
		});

		/**
		 * 上に貼り付けるUIをパネルに追加
		 * 
		 * @version 1.0.5
		 * もともと上に貼り付けたパネルの上に案内のラベルを追加
		 * 
		 * @version 1.0.6
		 * レベルを囲む線を追加
		 */
		panel.setLayout(new BorderLayout());
		panel.setBorder(new LineBorder(Color.black, 1, true));
		insertdata.setLayout(new FlowLayout());
		insertdata.add(allbtn);
		insertdata.add(seilabel);
		insertdata.add(seitxf);
		insertdata.add(namelabel);
		insertdata.add(nametxf);
		insertdata.add(yuubinnlabel);
		insertdata.add(yuubinntxf);
		insertdata.add(emaillabel);
		insertdata.add(emailtxf);
		insertdata.add(agelabel);
		insertdata.add(agetxf);
		insertdata.add(agelist);
		insertdata.add(insertbtn);
		insertdata.add(deletebtn);

		panel.add(annnai, BorderLayout.NORTH);
		panel.add(insertdata, BorderLayout.CENTER);

		/**
		 * 検索機能のセグメンテーションのためlabelにLayoutを設定
		 */
		shitapanel.setLayout(new BorderLayout());

		shita.setLayout(new FlowLayout());
		shita.setBorder(new LineBorder(Color.black, 1, true));
		shita.add(searchannnai);
		shita.add(shitasei);
		shita.add(shitaseitxf);
		shita.add(shitaname);
		shita.add(shitanametxf);
		shita.add(shitayuubinn);
		shita.add(shitayuubinntxf);
		shita.add(shitaemail);
		shita.add(shitaemailtxf);
		shita.add(searchbtn);

		shitapanel.add(shita, BorderLayout.NORTH);
		shitapanel.add(shitalabel, BorderLayout.CENTER);

		/**
		 *ラベルでの文字の表示位置を設定
		 */
		annnai.setHorizontalAlignment(JLabel.CENTER);
		shitalabel.setHorizontalAlignment(JLabel.CENTER);

		/**
		 * 全体的のLayoutを設定
		 */
		setLayout(new BorderLayout());

		/**
		 * テーブル、パネル、ラベルを貼り付ける
		 */
		add(scroll, BorderLayout.CENTER);
		add(panel, BorderLayout.NORTH);
		add(shitapanel, BorderLayout.SOUTH);

		/**
		 * テーブルの修正を可能にするためPropertyChangeListenerを追加
		 */
		table.addPropertyChangeListener(this);

		/**
		 * DTO型でDBからデータを呼び出してデーブルに表示するメッソド
		 */
		showTable();

		/**
		 * 基本設定
		 */
		setTitle("TableSwing");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

		/**
		 * 先テーブルのサイズを指定したのでsetSize();のメッソドは不要
		 */
		pack();

		/**
		 *構成するラベルやパネルに色塗のコード
		 *aliceblue(240,248,255)
		 *royalblue(65,105,225)
		 *blueviolet(138,43,226)
		 *자두color(221,160,221)
		 *제비꽃color(238,130,238)
		 *テーブルの選択色(184,207,229)
		 */

		/**
		 * 使った色のオブジェクト
		 */
		Color senntakublue = new Color(184, 207, 229);
		Color pinkviolet = new Color(221, 160, 221);

		/**
		 * データを入力する位置の色を塗る
		 */
		insertdata.setBackground(senntakublue);

		/**
		 * ラベルに色を塗るためにはsetOpaqueの値をtrueに設定する必要がある
		 * 一番上段のラベルの色を塗る
		 */
		annnai.setOpaque(true);
		annnai.setBackground(senntakublue);

		/**
		 * 一番下のラベルに色を塗る
		 */
		shitalabel.setOpaque(true);
		shitalabel.setBackground(senntakublue);
		shita.setBackground(senntakublue);

		/**
		 * 各ボタンに色を塗る
		 */
		allbtn.setBackground(pinkviolet);
		insertbtn.setBackground(pinkviolet);
		deletebtn.setBackground(pinkviolet);
		searchbtn.setBackground(pinkviolet);

	}

	/**
	 * データをテーブルに貼り付けるメソッド
	 */
	public void showTable() {
		tablemodel.setRowCount(0);

		ArrayList<TableDTO> list = new TableDAO().selectTalbe();

		for (TableDTO tdto : list) {
			Object[] row = { tdto.getName_sei(), tdto.getName_name(), tdto.getYuubinn(), tdto.getEmail(),
					tdto.getAge() };

			tablemodel.addRow(row);
		}

	}

	/**
	 * 検索した行だけを表示する為のshowTable()
	 */
	public void showTable(String str, int r) {
		tablemodel.setRowCount(0);
		ArrayList<TableDTO> list = new ArrayList<TableDTO>();

		if (r == 0) {
			list = new TableDAO().search(str, r);
		} else if (r == 1) {
			list = new TableDAO().search(str, r);
		} else if (r == 2) {
			list = new TableDAO().search(str, r);
		} else if (r == 3) {
			list = new TableDAO().search(str, r);
		}

		for (TableDTO tdto : list) {
			Object[] row = { tdto.getName_sei(), tdto.getName_name(), tdto.getYuubinn(), tdto.getEmail(),
					tdto.getAge() };

			tablemodel.addRow(row);
		}

		if (list.isEmpty()) {
			JOptionPane.showMessageDialog(this, "<html><center>検索情報が間違っています。<br>ご確認お願いします。</center></html>");
		}

	}

	/**
	 * 検索条件を年齢の範囲に指定した場合の結果をテーブルに出力
	 */
	public void showTable(ArrayList<TableDTO> list, int minage, int maxage) {
		tablemodel.setRowCount(0);

		list = new TableDAO().between(minage, maxage);

		for (TableDTO tdto : list) {
			Object[] row = { tdto.getName_sei(), tdto.getName_name(), tdto.getYuubinn(), tdto.getEmail(),
					tdto.getAge() };

			tablemodel.addRow(row);
		}

	}

	/**
	 * insertやdeleteを実施するメッソド
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		/**
		 * ボタンのコマンドによって別々の操作を実施
		 */
		String command = e.getActionCommand();

		/**
		 * DAOのメッソドの戻り値がintなのでその引数を取る
		 */
		int result = 0;

		/**
		 * NumberFormatExceptionを処理するためのフィールド
		 */
		int age = 0;

		/**
		 * データの入力が行った場合
		 */
		if (command.equals("insert")) {
			/**
			 * valueOfのNumberFormatExceptionの処理
			 */
			try {
				String sei = seitxf.getText();
				/**
				 * データの有効性検査
				 */
				if (seitxf.getText().equals("")) {
					sei = null;
				}
				String name = nametxf.getText();
				if (nametxf.getText().equals("")) {
					name = null;
				}
				String yuubinn = yuubinntxf.getText();
				if (yuubinntxf.getText().equals("")) {
					yuubinn = null;
				}
				String email = emailtxf.getText();
				if (emailtxf.getText().equals("")) {
					email = null;
				}
				age = Integer.valueOf(agetxf.getText());

				/**
				 * DBとのデータ交換の為DTOオブジェクトを作成
				 */
				TableDTO dto = new TableDTO();
				dto.setName_sei(sei);
				dto.setName_name(name);
				dto.setYuubinn(yuubinn);
				dto.setEmail(email);
				dto.setAge(age);

				/**
				 * ビジネスロジックを実施
				 */
				TableDAO dao = new TableDAO();
				result = dao.insert(dto);

				/**
				 * ビジネスロジックの結果によって違う動作を行う
				 * 1＝入力成功
				 * 0＝姓の重複
				 * -1＝データのエラー
				 */
				if (result == 1) {
					System.out.println("データのインサート完了");
					JOptionPane.showMessageDialog(this, "データの入力が成功しました");
					String str = "データの入力が成功しました";
					String shita = "変更望みならダブルクリックして新しいデータを入力してください";
					shitalabel.setText("<html><center>" + str + "<br>" + shita + "</center></html>");
					seitxf.setText("");
					nametxf.setText("");
					yuubinntxf.setText("");
					emailtxf.setText("");
					agetxf.setText("0");
				} else if (result == 0) {
					System.out.println("入力データに重複発生");
					JOptionPane.showMessageDialog(this, "入力データが重複してます");
					String str = "データの入力が失敗しました";
					String shita = "入力したデータを確認してください";
					shitalabel.setText("<html><center>" + str + "<br>" + shita + "</center></html>");
				} else if (result == -1) {
					System.out.println("データ入力にエラー発生");
					JOptionPane.showMessageDialog(this, "データの入力が失敗しました");
					String str = "データの入力が失敗しました";
					String shita = "入力したデータを確認してください";
					shitalabel.setText("<html><center>" + str + "<br>" + shita + "</center></html>");
				}
				/**
				 * 年齢を入力されるフィールドはint以外の型はNumberFormatExceptionをリターンする
				 * なので他の例外処理を行う
				 */
			} catch (NumberFormatException e2) {
				age = 0;
				result = 0;
				e2.printStackTrace();
				System.out.println("データ入力にエラー発生");
				JOptionPane.showMessageDialog(this, "年齢の入力値を確認してください");
				String str = "データの入力が失敗しました";
				String shita = "入力したデータを確認してください";
				shitalabel.setText("<html><center>" + str + "<br>" + shita + "</center></html>");
			}

			/**
			 * 入力処理が終わったらデータを
			 */
			showTable();

			/**
			 * データの削除機能の場合
			 */
		} else if (command.equals("delete")) {

			/**
			 * まず、データを選択しなけらば削除できなくなるよう設定
			 */
			int rowIndex = table.getSelectedRow();
			if (rowIndex == -1) {
				JOptionPane.showMessageDialog(this, "削除する行を選びなさい");
				return;
			}

			/**
			 * データの選択が成功だった場合、削除を実施します。
			 * でも、その前に本当に削除するかどうかを確認
			 */
			int confirm = JOptionPane.showConfirmDialog(this, "本当にデータを削除しますか");
			if (confirm == JOptionPane.YES_OPTION) {
				String sei = (String) tablemodel.getValueAt(rowIndex, 0);
				TableDAO dao = new TableDAO();
				result = dao.delete(sei);
				if (result == 1) {
					System.out.println("データの削除完了");
				}
			}

			showTable();

			/**
			 * データを検索する機能を追加
			 * 
			 * @version 1.0.3
			 * 年齢の範囲を指定して検索する条件を追加
			 */
		} else if (command.equals("search")) {
			/**
			 * 姓で検索するロジック
			 */
			int r = -1;

			String sei = shitaseitxf.getText();
			String name = shitanametxf.getText();
			String yuubinn = shitayuubinntxf.getText();
			String email = shitaemailtxf.getText();

			System.out.println("sei = " + sei);
			System.out.println("name = " + name);
			System.out.println("yuubinn = " + yuubinn);
			System.out.println("email = " + email);

			if (!sei.equals("")) {
				r = 0;
			} else if (!name.equals("")) {
				r = 1;
			} else if (!yuubinn.equals("")) {
				r = 2;
			} else if (!email.equals("")) {
				r = 3;
			} else {
				r = -1;
			}

			System.out.println("r = " + r);

			switch (r) {
			case 0: {
				showTable(sei, r);
				break;
			}
			case 1: {
				showTable(name, r);
				break;
			}
			case 2: {
				showTable(yuubinn, r);
				break;
			}
			case 3: {
				showTable(email, r);
				break;
			}
			case -1: {
				JOptionPane.showMessageDialog(this, "<html><center>検索情報が間違っています。<br>ご確認お願いします。</center></html>");
			}
			}

			/**
			 * 検索する条件が姓なので姓を入力されるフィールドがnullだったら
			 * エラーメッセージを表示
			 * nullじゃなかったらそのまま検索
			 */
			//			if (sei != null) {
			//				showTable(sei);
			//			} else {
			//				JOptionPane.showMessageDialog(this, "<html><center>入力した氏名(姓)が間違っています。<br>ご確認お願いします。</center></html>");
			//			}

		} else if (command.equals("setall")) {
			showTable();
		}
	}

	/**
	 * Updateを実施するメッソド
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		/**
		 * 修正メッソドの戻り値がintなのでその値を取るために宣言
		 */
		int result = 0;

		/**
		 * テーブルに変更が起きた場合修正内容を反映
		 */
		if (evt.getPropertyName().equals("tableCellEditor") && !table.isEditing()) {
			/**
			 * 選択が行われたセルの位置情報を取る
			 */
			int row = table.getSelectedRow();
			int col = table.getSelectedColumn();

			/**
			 * 受け取った位置情報によって修正が行った行の情報を持ってくる
			 */
			String sei = (String) tablemodel.getValueAt(row, 0);
			String name = (String) tablemodel.getValueAt(row, 1);
			String yuubinn = (String) tablemodel.getValueAt(row, 2);
			String email = (String) tablemodel.getValueAt(row, 3);
			int age = Integer.parseInt(tablemodel.getValueAt(row, 4).toString());

			/**
			 * 受け取った情報をもとにDTOオブジェクトを作成
			 * その後アップデートするメッソドを実施
			 */
			TableDTO dto = new TableDTO(sei, name, yuubinn, email, age);
			result = new TableDAO().update(dto);

			/**
			 * アップデートの結果によってリターンが違う
			 */
			if (result == 1) {
				System.out.println("データの修正が完了しました");
				String ColName = columnName[col];
				Object data = tablemodel.getValueAt(row, col);
				Object rowName = tablemodel.getValueAt(row, 0);
				String str = String.format("'%sの%sを%s'に変更しました", rowName, ColName, data);
				JOptionPane.showMessageDialog(this, str);
				String shita = "変更望みならダブルクリックして新しいデータを入力してください";
				shitalabel.setText("<html><center>" + str + "<br>" + shita + "</center></html>");
			}

			showTable();

			/**
			 * テーブルの選択を初期化
			 */
			table.clearSelection();
		}
	}

	/**
	 * main method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new TableSwing();
	}

}
