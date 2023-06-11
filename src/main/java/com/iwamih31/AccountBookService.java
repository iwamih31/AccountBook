package com.iwamih31;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

	public Office new_Office() {
		Office new_Office = new Office(next_Office_Id(), "", "");
		if (new_Office.getId() == 1)
			set_Office();
		return new Office(next_Office_Id(), "", "");
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

	public int next_Action_Id() {
		int nextId = 1;
		Action lastElement = getLastElement(actionRepository.findAll());
		if (lastElement != null)
			nextId = lastElement.getId() + 1;
		___consoleOut___("next_Office_Id = " + nextId);
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

	// 今日の日付を取得
	public LocalDate today() {
		return LocalDate.now();
	}

	public State state(int id, String name, String date) {
		return new State(id, name, to_LocalDate(date));
	}

	private LocalDate to_LocalDate(String date) {
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
		List<Action> actions = actionRepository.subjects();
		List<String> subjects = new ArrayList<>();
		subjects.add("　　　　");
		if(actions != null) {
			for (Action action : actions) {
				subjects.add(action.getSubject());
			}
		}
		return subjects;
	}

	public int remainder(String date) {
		int income_total = 0;
		int spending_total = 0;
		List<Action> actions = actionRepository.up_to_date(to_LocalDate(date));
		for (Action action : actions) {
			income_total += action.getIncome();
			spending_total += action.getSpending();
		}
		return income_total - spending_total;
	}

	public int carryover(String date) {
		LocalDate localDate = to_LocalDate(date);
		String yesterday = localDate.minusDays(1).toString();
		return remainder(yesterday);
	}

	public Map<String, Integer> account(String date) {
		Map<String, Integer> account = new HashMap<>();
		LocalDate localDate = to_LocalDate(date);
		int carryover = carryover(date);
		int remainder = remainder(date);
		int income_today = 0;
		int spending_today = 0;
		int income_tihs_year = 0;
		int spending_tihs_year = 0;
		List<Action> today_List = action_List(date);
		for (Action action : today_List) {
			income_today += action.getIncome();
			spending_today += action.getSpending();
		}
		List<Action> year_List = actionRepository.action_List(localDate.withDayOfYear(1), localDate);
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
		return account;
	}


}
