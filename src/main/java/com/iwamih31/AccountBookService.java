package com.iwamih31;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.JapaneseChronology;
import java.time.chrono.JapaneseDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountBookService {

	@Autowired
	private ActionRepository actionRepository;
	@Autowired
	private OfficeRepository officeRepository;
	@Autowired
	private CashRepository cashRepository;
	@Autowired
	private SubjectRepository subjectRepository;

	/** アクションリスト（1日分） */
	public List<Action> action_List(String date) {
		___consoleOut___("date = " + date);
		LocalDate local_Date = to_LocalDate(date);
		return actionRepository.action_List(local_Date, local_Date);
	}

	/** アクションリスト（1カ月分） */
	public List<Action> action_List_Monthly(String date) {
		LocalDate local_Date = to_LocalDate(date);
		LocalDate start_date = local_Date.withDayOfMonth(1);
		LocalDate end_date = start_date.plusMonths(1).minusDays(1);
		return actionRepository.action_List(start_date, end_date);
	}

	/** アクションリスト（月初めから date まで分） */
	public List<Action> month_Till_Date(String date) {
		LocalDate local_Date = to_LocalDate(date);
		LocalDate start_date = local_Date.withDayOfMonth(1);
		LocalDate end_date = local_Date;
		return actionRepository.action_List(start_date, end_date);
	}

	public String name() {
		List<String> name = officeRepository.item_value("事業所名");
		if (name.isEmpty()) {
			Office office_Item = new Office(null, "事業所名", "出納帳");
			officeRepository.save(office_Item);
		}
		return officeRepository.item_value("事業所名").get(0);
	}

	public Action action(int id) {
		return actionRepository.getReferenceById(id);
	}

	public Office office(int id) {
		return officeRepository.getReferenceById(id);
	}

	public List<Office> office_All() {
		if (next_Office_Id() == 1)
			set_Office();
		return officeRepository.findAll();
	}

	public String office_Insert(Office office_Item, int id) {
		office_Item.setId(id);
		String message = "ID = " + office_Item.getId() + " の事業所データ登録";
		try {
			officeRepository.save(office_Item);
			message += "が完了しました";
		} catch (Exception e) {
			message += "が正常に行われませんでした";
			e.printStackTrace();
		}
		return message;
	}

	public String subject_Insert(Subject subject_Item, int id) {
		subject_Item.setId(id);
		String message = "ID = " + subject_Item.getId() + " の科目データ登録";
		try {
			subjectRepository.save(subject_Item);
			message += "が完了しました";
		} catch (Exception e) {
			message += "が正常に行われませんでした";
			e.printStackTrace();
		}
		return message;
	}

	public String action_Insert(Action action) {
		int id = next_Action_Id();
		action.setId(id);
		String message = "ID = " + action.getId() + " の出納データ登録";
		try {
			actionRepository.save(action);
			message += " が完了しました";
		} catch (Exception e) {
			message += "が正常に行われませんでした";
			e.printStackTrace();
		}
		return message;
	}

	public String office_Update(Office office, int id) {
		office.setId(id);
		String message = "ID = " + office.getId() + " の事業所データ更新";
		try {
			officeRepository.save(office);
			message += "が完了しました";
		} catch (Exception e) {
			message += "が正常に行われませんでした";
			e.printStackTrace();
		}
		return message;
	}

	public String action_Update(Action action, int id) {
		action.setId(id);
		String message = "ID = " + action.getId() + " の出納データ更新";
		try {
			actionRepository.save(action);
			message += "が完了しました";
		} catch (Exception e) {
			message += "が正常に行われませんでした";
			e.printStackTrace();
		}
		return message;
	}

	public String action_Delete(int id) {
		String message = "ID = " + id + " のデータ削除";
		try {
			actionRepository.deleteById(id);
			message += "が完了しました";
		} catch (Exception e) {
			message += "が正常に行われませんでした" + e.getMessage();
		}
		___consoleOut___(message);
		return message;
	}

	public String[] office_Item_Names() {
		String[] item_Names = { "事業所名", "部署名" };
		return item_Names;
	}

	public List<Office> office_Report() {
		return officeRepository.findAll();
	}

	public List<Subject> subject_Report() {
		return subjectRepository.findAll();
	}

	public Office new_Office() {
		Office new_Office = new Office(next_Office_Id(), "", "");
		if (new_Office.getId() == 1)
			set_Office();
		return new Office(next_Office_Id(), "", "");
	}

	public Subject new_Subject() {
		return new Subject(next_Subject_Id(), "", "");
	}

	public Action new_Action() {
		return new Action(next_Action_Id(), null, "", "", 0, 0);
	}

	public Action new_Action(String date) {
		return new Action(next_Action_Id(), to_LocalDate(date), "", "", 0, 0);
	}

	public int next_Office_Id() {
		int nextId = 1;
		Office lastElement = getLastElement(officeRepository.findAll());
		if (lastElement != null)
			nextId = lastElement.getId() + 1;
		___consoleOut___("next_Office_Id = " + nextId);
		return nextId;
	}

	public int next_Subject_Id() {
		int nextId = 1;
		Subject lastElement = getLastElement(subjectRepository.findAll());
		if (lastElement != null)
			nextId = lastElement.getId() + 1;
		___consoleOut___("next_Office_Id = " + nextId);
		return nextId;
	}

	public int next_Action_Id() {
		int nextId = 1;
		Action lastElement = getLastElement(actionRepository.findAll());
		if (lastElement != null)
			nextId = lastElement.getId() + 1;
		___consoleOut___("next_Office_Id = " + nextId);
		return nextId;
	}

	public int next_Cash_Id() {
		int nextId = 1;
		Action lastElement = getLastElement(actionRepository.findAll());
		if (lastElement != null)
			nextId = lastElement.getId() + 1;
		___consoleOut___("next_Cash_Id = " + nextId);
		return nextId;
	}

	private void set_Office() {
		String[] item_Names = office_Item_Names();
		officeRepository.save(new Office(1, item_Names[0], ""));
		officeRepository.save(new Office(2, item_Names[1], ""));
	}

	/** 事業所データをExcelファイルとして出力 */
	public String office_Output_Excel(HttpServletResponse response) {
		Excel excel = new Excel();
		String message = null;
		String[] column_Names = Set.get_Name_Set(LabelSet.officeReport_Set);
		int[] column_Width = Set.get_Value_Set(LabelSet.officeReport_Set);
		List<Office> office_Report = office_Report();
		String[][] table_Data = new String[office_Report.size()][];
		for (int i = 0; i < table_Data.length; i++) {
			Office office = office_Report.get(i);
			table_Data[i] = new String[] {
					String.valueOf(office.getId()),
					String.valueOf(office.getItem_name()),
					String.valueOf(office.getItem_value())
			};
		}
		message = excel.output_Excel("事業所", column_Names, column_Width, table_Data, response);
		return message;
	}

	/** 出納帳（1日分）をExcelファイルとして出力 */
	public String daily_Output_Excel(String date, HttpServletResponse response) {
		Excel excel = new Excel();
		String message = null;
		int[] column_Width = Set.get_Value_Set(LabelSet.daily_Output_Set);
		String[][] output_Data = daily_Sheet(date);
		String sheet_Name = date;
		WorkSheet workSheet = new DailyWorkSheet(sheet_Name, column_Width, output_Data);
		message = excel.output_Excel_Sheet("出納帳" + sheet_Name + "-", workSheet, response);
		return message;
	}

	/** 月別出納一覧をExcelファイルとして出力 */
	public String monthly_Output_Excel(String date, HttpServletResponse response) {
		Excel excel = new Excel();
		String message = null;
		int[] column_Width = Set.get_Value_Set(LabelSet.monthly_Output_Set);
		String[][] output_Data = monthly_Sheet(date);
		String sheet_Name = japanese_Date(date, "M月");
		WorkSheet workSheet = new MonthlyWorkSheet(sheet_Name, column_Width, output_Data);
		message = excel.output_Excel_Sheet("現金出納帳出納帳" + japanese_Year(date) + "-", workSheet, response);
		return message;
	}

	private String[][] daily_Sheet(String date) {
		List<String[]> head_Rows = head_Rows_Daily(date);
		String[] labels = Set.get_Name_Set(LabelSet.daily_Set);
		List<String[]> data_Rows = data_Row_Values_Daily(date);
		List<String[]> foot_Rows = foot_Rows_Daily();
		return make_Sheet(head_Rows, labels, data_Rows, foot_Rows);
	}

	private String[][] monthly_Sheet(String date) {
		List<String[]> head_Rows = head_Rows_Monthly(date);
		String[] labels = Set.get_Name_Set(LabelSet.monthly_Set);
		List<String[]> data_Rows = data_Row_Values_Monthly(date);
		List<String[]> foot_Rows = new ArrayList<>();
		return make_Sheet(head_Rows, labels, data_Rows, foot_Rows);
	}

	private List<String[]> head_Rows_Daily(String date) {
		date = japanese_Date(date);
		String com_ = office_item_value("事業所名");
		String[][] head_Rows = {
				{ "", com_, "", "", ""},
				{ "", "", "", "", ""},
				{ "", "", "", "", date}
		};
		return to_List(head_Rows);
	}

	private List<String[]> head_Rows_Monthly(String date) {
		date = japanese_Date(date, "Gy年M月分");
		String com_ = office_item_value("事業所名");
		String[][] head_Rows = {
				{ date, "", "", "", com_, ""},
				{ "", "", "", "", "", ""}
		};
		return to_List(head_Rows);
	}

	private List<String[]> foot_Rows_Daily() {
		String[][] head_Rows = {
				{ "", "", "", "", ""},
				{ "【領収証添付】", "", "", "", ""}
		};
		return to_List(head_Rows);
	}

	/** Excelシート（実績）作成用値データ */
	public String[][] make_Sheet(
			List<String[]> head_Rows,
			String[] labels,
			List<String[]> data_Rows,
			List<String[]> foot_Rows) {
		// 列追加用リスト作成
		List<String[]> sheet_Rows = new ArrayList<>();
		// ヘッド部分追加
		sheet_Rows.addAll(head_Rows);
		// ラベル行追加
		sheet_Rows.add(labels);
		// データ行追加
		sheet_Rows.addAll(data_Rows);
		// フッター部分追加
		sheet_Rows.addAll(foot_Rows);
		return to_Array(sheet_Rows);
	}

	/** 表部分 データ行（１日分） */
	List<String[]> data_Row_Values_Daily(String date) {
		// その日のデータ取得
		Map<String, Integer> account = account_Monthly(date);
		Integer carryover = account.get("carryover");
		Integer income_today = account.get("income_today");
		Integer spending_today = account.get("spending_today");
		Integer remainder = account.get("remainder");
		Integer income_cumulative = account.get("income_cumulative");
		Integer spending_cumulative = account.get("spending_cumulative");
		// データ格納用リスト作成
		List<String[]> data_Row_Values = new ArrayList<>();
		data_Row_Values.add(data_Row_Daily_carryover(carryover));
		// List<Action> を取得
		List<Action> action_List = action_List(date);
		// List<Action> があれば
		if (action_List.size() > 0) {
			// List<Action> 数分ループ
			for (Action action : action_List) {
				// Actionに応じた行を作成
				data_Row_Values.add(data_Row_Daily(action));
			}
		}
		data_Row_Values.add(data_Row_total(income_today, spending_today, remainder));
		data_Row_Values.add(data_Row_cumulative(income_cumulative, spending_cumulative));
		return data_Row_Values;
	}

	/** 表部分 データ行（ひと月分） */
	List<String[]> data_Row_Values_Monthly(String date) {
		Integer carryover = carryover(date);
		Integer remainder = carryover;
		Integer income_cumulative = 0;
		Integer spending_cumulative = 0;
		// データ格納用リスト作成
		List<String[]> data_Row_Values = new ArrayList<>();
		data_Row_Values.add(data_Row_Monthly_carryover(carryover));
		// List<Action> を取得
		List<Action> action_List = action_List_Monthly(date);
		// List<Action> があれば
		if (action_List.size() > 0) {
			// List<Action> 数分ループ
			for (Action action : action_List) {
				income_cumulative += action.getIncome();
				spending_cumulative += action.getSpending();
				remainder = carryover + income_cumulative - spending_cumulative;
				// Actionに応じた行を作成
				data_Row_Values.add(data_Row_Monthly(action, remainder));
			}
		}
		data_Row_Values.add(data_Row_carried_forward(remainder));
		data_Row_Values.add(data_Row_total_amount(income_cumulative, spending_cumulative));
		return data_Row_Values;
	}

	private String[] data_Row_Daily_carryover(int carryover) {
		return new String[] {"", "前日繰越", "", "", make_String(carryover)};
	}

	private String[] data_Row_Monthly_carryover(int carryover) {
		return new String[] {"", "", "前日繰越", "", "", make_String(carryover)};
	}

	private String[] 	data_Row_Daily(Action action) {
		return new String[] {
				make_String(action.getSubject()),
				make_String(action.getApply()),
				make_String(Zero_Blank(action.getIncome())),
				make_String(Zero_Blank(action.getSpending())),
				make_String("")
			};
	}

	private String[] 	data_Row_Monthly(Action action, Integer remainder) {
		return new String[] {
				japanese_Date(action.getDate().toString(), "M月d日"),
				make_String(action.getSubject()),
				make_String(action.getApply()),
				make_String(Zero_Blank(action.getIncome())),
				make_String(Zero_Blank(action.getSpending())),
				make_String(remainder)
		};
	}

	private String[] data_Row_total(Integer income_today, Integer spending_today, Integer remainder) {
		return new String[] {
				"",
				"計",
				make_String(income_today),
				make_String(spending_today),
				make_String(remainder)
			};
	}

	private String[] data_Row_total_amount(Integer income_cumulative, Integer spending_cumulative) {
		return new String[] {
				"",
				"",
				"合計",
				make_String(income_cumulative),
				make_String(spending_cumulative),
				""
		};
	}

	private String[] data_Row_cumulative(Integer income_cumulative, Integer spending_cumulative) {
		return new String[] {"", "累計", make_String(income_cumulative), make_String(spending_cumulative), ""};
	}

	private String[] data_Row_carried_forward(Integer remainder) {
		return new String[] {"", "", "翌月繰越", "", "", make_String(remainder)};
	}

	private Object Zero_Blank(int number) {
		if (number == 0) return "";
		return number;
	}

	private String make_String(Object object) {
		String make_String = "";
		if (object != null)
			make_String = String.valueOf(object);
		return make_String;
	}

	private String[][] to_Array(List<String[]> list) {
		// Listから2次配列へ変換
		String[][] array = new String[list.size()][];
		for (int i = 0; i < array.length; i++) {
			array[i] = list.get(i);
		}
		return array;
	}

	private List<String[]> to_List(String[][] array) {
		// Listから2次配列へ変換
		List<String[]> list = new ArrayList<>();
		for (int i = 0; i < array.length; i++) {
			list.add(array[i]);
		}
		return list;
	}

	private String office_item_value(String item_name) {
		String value = "";
		List<String> item_value = officeRepository.item_value(item_name);
		if (item_value.size() > 0)
			value = officeRepository.item_value(item_name).get(0);
		return value;
	}

	/** List の最後の Element を返すジェネリックメソッド */
	public <E> E getLastElement(List<E> list) {
		E lastElement = null;
		if (list.size() > 0) {
			int lastIdx = list.size() - 1;
			lastElement = list.get(lastIdx);
		}
		return lastElement;
	}

	/** コンソールに String を出力 */
	public void ___consoleOut___(String message) {
		System.out.println("*");
		System.out.println(message);
		System.out.println("");
	}

	// 今日の日付の文字列を取得
	public String today() {
		// 今日の日付を取得
		LocalDate now = LocalDate.now();
		// 表示形式を指定
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return dateTimeFormatter.format(now);
	}

	// 和暦に変換
	public String japanese_Date(String date, String format_Pattern) {
		Integer[] split_Date = split_Date(date);
		JapaneseDate japaneseDate = JapaneseDate.of(split_Date[0], split_Date[1], split_Date[2]);
		// 表示形式を指定
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format_Pattern);
		return dateTimeFormatter.format(japaneseDate);
	}

	// 和暦に変換
	public String japanese_Date(String date) {
		return japanese_Date(date, "G y 年 M 月 d 日");
	}

	// 和暦に変換
	public String japanese_Year(String date) {
		Integer[] split_Date = split_Date(date);
		JapaneseDate japaneseDate = JapaneseDate.of(split_Date[0], split_Date[1], split_Date[2]);
		// 表示形式を指定
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("G y 年");
		return dateTimeFormatter.format(japaneseDate);
	}

	// 和暦に変換
	public String japanese_Date(LocalDate localDate) {
		return japanese_Date(localDate.toString());
	}

	// 和暦の文字列をLocalDateに変換
	public LocalDate to_LocalDate(String japanese_Date, String format_Pattern) {

    // DateTimeFormatterオブジェクトを生成し、和暦のパターンを設定する
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format_Pattern, Locale.JAPAN)
            .withChronology(JapaneseChronology.INSTANCE)
            .withResolverStyle(ResolverStyle.SMART);

    // 和暦の文字列を JapaneseDate に変換する
    JapaneseDate wareki = JapaneseDate.from(formatter.parse(japanese_Date));

    // JapaneseDateからLocalDateに変換する
    return LocalDate.from(wareki);
}

	public State state(int id, String name, String date) {
		return new State(id, name, to_LocalDate(date));
	}

	public LocalDate to_LocalDate(String date) {
		Integer[] split_Date = split_Date(date);
		int year = split_Date[0];
		int month = split_Date[1];
		int day = split_Date[2];
		return to_LocalDate(year, month, day);
	}

	private LocalDate to_LocalDate(int year, int month, int day) {
		LocalDate localDate = LocalDate.of(year, month, day);
		___consoleOut___("localDate = " + localDate);
		return localDate;
	}

	private Integer[] split_Date(String date) {
		Integer[] split_Date = null;
		LocalDateTime localDateTime = LocalDateTime.now();
		if (date != null) {
			int year = localDateTime.getYear();
			int month = localDateTime.getMonthValue();
			int day = 1;
			date = date.trim();
			date = date.split(" ")[0];
			date = date.split("T")[0];
			date = date.replace("年", "-");
			date = date.replace("月", "-");
			date = date.replace("日", "-");
			date = date.replace("/", "-");
			String[] array = date.split("-");
			if (array.length > 0 && is_Int(array[0]))
				year = Integer.parseInt(array[0]);
			if (array.length > 1)
				month = Integer.parseInt(array[1]);
			if (array.length > 2)
				day = Integer.parseInt(array[2]);
			split_Date = new Integer[] { year, month, day };
		}
		return split_Date;
	}

	private boolean is_Int(String string) {
		boolean is_Int = true;
		try {
			___consoleOut___("String = " + Integer.parseInt(string) + " は Integer に変換出来ます");
		} catch (Exception e) {
			___consoleOut___("string = " + string + " は Integer に変換出来ません");
			is_Int = false;
		}
		return is_Int;
	}

	public OptionData options() {
		return new OptionData();
	}

	public List<String> subjects() {

		List<String> subjects = actionRepository.subjects();
		subjects.add(0, "");
		return subjects;
	}

	public List<String> applys() {
		List<String> applys = actionRepository.applys();
		applys.add(0, "");
		return applys;
	}

	public String[] accounts() {
		return new String[] {"支出", "収入"};
	}

	public int remainder(String date) {
		int income_total = 0;
		int spending_total = 0;
		List<Action> actions = actionRepository.up_to_date(to_LocalDate(date));
		if (actions.size() < 1) return 0;
		for (Action action : actions) {
			income_total += action.getIncome();
			spending_total += action.getSpending();
		}
		return income_total - spending_total;
	}

	/** 1日前の残高を返す */
	public int carryover(String date) {
		LocalDate localDate = to_LocalDate(date);
		String yesterday = localDate.minusDays(1).toString();
		return remainder(yesterday);
	}

	// localDate の年の1日から volume 年分の Action リストを返す
	public List<Action> year_List(LocalDate localDate, int volume) {
		LocalDate start_date = localDate.withDayOfYear(1);
		LocalDate end_date = start_date.plusYears(volume).minusDays(1);
		return actionRepository.action_List(start_date, end_date);
	}

	// localDate の年の1日から volume 年分の Action リストを返す（subject 指定）
	public List<Action> year_List(LocalDate localDate, int volume, String subject) {
		LocalDate start_date = localDate.withDayOfYear(1);
		LocalDate end_date = start_date.plusYears(volume).minusDays(1);
		if(subject == null || subject.equals("")) {
			return actionRepository.action_List(start_date, end_date);
		}
		return actionRepository.action_List(start_date, end_date, subject);
	}

	// localDate の月の1日から volume 月分の Action リストを返す
	public List<Action> monthly_List(LocalDate localDate, int volume) {
		LocalDate start_date = localDate.withDayOfMonth(1);
		LocalDate end_date = start_date.plusMonths(volume).minusDays(1);
		return actionRepository.action_List(start_date, end_date);
	}

	// date の年の1日から volume 月分の Action リストを返す
	public List<Action> year_List(String date, int volume) {
		LocalDate localDate = to_LocalDate(date);
		return year_List(localDate, volume);
	}

	public Object year_List(String year, int volume, String subject) {
		LocalDate localDate = to_LocalDate(year);
		return year_List(localDate, volume, subject);
	}

	// date の月の1日から volume 月分の Action リストを返す
	public List<Action> monthly_List(String date, int volume) {
		LocalDate localDate = to_LocalDate(date);
		return monthly_List(localDate, volume);
	}

	// localDate の年の1日から localDate までの Action リストを返す
	public List<Action> year_List(LocalDate localDate) {
		return actionRepository.action_List(localDate.withDayOfYear(1), localDate);
	}

	// localDate の月の1日から localDate までの Action リストを返す
	public List<Action> monthly_List(LocalDate localDate) {
		return actionRepository.action_List(localDate.withDayOfMonth(1), localDate);
	}

	// date の年の1日から date までの Action リストを返す
	public List<Action> year_List(String date) {
		LocalDate localDate = to_LocalDate(date);
		return actionRepository.action_List(localDate.withDayOfYear(1), localDate);
	}

	// date の月の1日から date までの Action リストを返す
	public List<Action> monthly_List(String date) {
		LocalDate localDate = to_LocalDate(date);
		return actionRepository.action_List(localDate.withDayOfYear(1), localDate);
	}

	public Map<String, Integer> account(String date) {
		Map<String, Integer> account = new HashMap<>();
		int carryover = carryover(date);
		int remainder = remainder(date);
		int income_today = 0;
		int spending_today = 0;
		int income_tihs_year = 0;
		int spending_tihs_year = 0;
		int balance = cash_Balance(date, cash(date));
		List<Action> today_List = action_List(date);
		for (Action action : today_List) {
			income_today += action.getIncome();
			spending_today += action.getSpending();
		}
		List<Action> year_List = year_List(date);
		for (Action action : year_List) {
			income_tihs_year += action.getIncome();
			spending_tihs_year += action.getSpending();
		}
		account.put("carryover", carryover);
		account.put("remainder", remainder);
		account.put("income_today", income_today);
		account.put("spending_today", spending_today);
		account.put("income_tihs_year", income_tihs_year);
		account.put("spending_tihs_year", spending_tihs_year);
		account.put("balance", balance);
		return account;
	}

	public Map<String, Integer> account_Monthly(String date) {
		Map<String, Integer> account = new HashMap<>();
		int carryover = carryover(date);
		int remainder = remainder(date);
		int income_today = 0;
		int spending_today = 0;
		int income_cumulative = 0;
		int spending_cumulative = 0;
		int balance = cash_Balance(date, cash(date));
		List<Action> today_List = action_List(date);
		for (Action action : today_List) {
			income_today += action.getIncome();
			spending_today += action.getSpending();
		}
		List<Action> monthly_List = month_Till_Date(date);
		for (Action action : monthly_List) {
			income_cumulative += action.getIncome();
			spending_cumulative += action.getSpending();
		}
		account.put("carryover", carryover);
		account.put("remainder", remainder);
		account.put("income_today", income_today);
		account.put("spending_today", spending_today);
		account.put("income_cumulative", income_cumulative);
		account.put("spending_cumulative", spending_cumulative);
		account.put("balance", balance);
		return account;
	}

	public Cash cash(String date) {
		Cash cash;
		LocalDate localDate = to_LocalDate(date);
		___consoleOut___("localDate = " + localDate);
		if(cashRepository.cash(localDate).size() < 1) {
			int id = next_Cash_Id();
			cashRepository.save(new Cash(id, localDate, 0, 0, 0, 0, 0, 0, 0, 0, 0));
		}
		cash = cashRepository.cash(localDate).get(0);
		return cash;
	}

	public int cash_Total(Cash cash) {
		int total = 0;
		total += cash.getMan1() * 10000;
		total += cash.getSen5() * 5000;
		total += cash.getSen1() * 1000;
		total += cash.getHyaku5() * 500;
	  total += cash.getHyaku1() * 100;
	  total += cash.getJyuu5() * 50;
	  total += cash.getJyuu1() * 10;
	  total += cash.getEn5() * 5;
	  total += cash.getEn1() * 1;
		return total;
	}

	public String cash_Update(Cash cash) {
		String message = cash.getDate() + " の現金残高更新";
		try {
			cashRepository.save(cash);
			message += "が完了しました";
		} catch (Exception e) {
			message += "が正常に行われませんでした";
			e.printStackTrace();
		}
		return message;
	}

	public int cash_Balance(String date, Cash cash) {
		int remainder = remainder(date);
		int cash_Total = cash_Total(cash);
		return cash_Total - remainder;
	}

	public String this_Year_Month() {
		// 今日の日付を取得
		LocalDateTime now = LocalDateTime.now();
		// 表示形式を指定
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM");
		return dateTimeFormatter.format(now);
	}

	public String this_Year() {
		// 今日の日付を取得
		LocalDateTime now = LocalDateTime.now();
		// 表示形式を指定
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy");
		return dateTimeFormatter.format(now);
	}

	public String this_Month() {
		// 今日の日付を取得
		LocalDateTime now = LocalDateTime.now();
		// 表示形式を指定
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM");
		return dateTimeFormatter.format(now);
	}

	public List<Integer> income_List(String year_month, int volume) {
		int income = 0;
		List<Action> actions = monthly_List(year_month, volume);
		List<Integer> income_List = new ArrayList<>();
		for (Action action : actions) {
			income += action.getIncome();
			income_List.add(income);
		}
		return income_List;
	}

	public List<Integer> spending_List(String year_month, int volume) {
		int spending = 0;
		List<Action> actions = monthly_List(year_month, volume);
		List<Integer> spending_List = new ArrayList<>();
		for (Action action : actions) {
			spending += action.getSpending();
			spending_List.add(spending);
		}
		return spending_List;
	}

	public Object years() {
		int size = 100;
		LocalDate localDate = LocalDate.now();
		localDate = localDate.minusYears(size);
		List<String> years = new ArrayList<>();
		for (int i = 0; i <= size; i++) {
			years.add(japanese_Year(localDate.plusYears(i).toString()));
		}
		return years;
	}

	public String year(String year, String format_Pattern) {
		___consoleOut___("year = " + year + "format_Pattern = " + format_Pattern );
		LocalDate localDate = to_LocalDate(year, format_Pattern);
		return String.valueOf(localDate.getYear());
	}

}
