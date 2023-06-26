package com.iwamih31;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("/AccountBook")
public class AccountBookController {

	@Autowired
	private AccountBookService service;

	/** RequestMappingのURL */
	public String req() {
		return "/AccountBook";
	}

	/** RequestMappingのURL */
	public String req(String path) {
		return req() + path;
	}

	/** このクラスの@GetMapping(path)にredirect */
	public String redirect(String path) {
		return "redirect:" + req() + path;
	}

	@GetMapping("/")
	public String index() {
		return redirect("/Main");
	}

	@GetMapping("/Main")
	public String main(
			Model model) {
		model.addAttribute("title", "メイン画面");
		return "main";
	}

	@GetMapping("/Setting")
	public String setting(
			Model model) {
		add_View_Data_(model, "setting", "各種設定");
		return "view";
	}

	@GetMapping("/List")
	public String list(
			Model model) {
		add_View_Data_(model, "list", "各種一覧");
		return "view";
	}

	@GetMapping("/Office")
	public String office(
			@Param("date")String date,
			Model model) {
		add_View_Data_(model, "office", "事業所情報");
		String[] item_Names = service.office_Item_Names();
		model.addAttribute("name", item_Names[0]);
		model.addAttribute("department", item_Names[1]);
		model.addAttribute("office_data", service.office_All());
		return "view";
	}

	@GetMapping("/OfficeSetting")
	public String officeSetting(
			Model model) {
		add_View_Data_(model, "officeSetting", "事業所設定");
		model.addAttribute("officeList", service.office_Report());
		return "view";
	}

	@GetMapping("/SubjectSetting")
	public String subjectSetting(
			Model model) {
		add_View_Data_(model, "subjectSetting", "科目設定");
		model.addAttribute("subjectList", service.subject_Report());
		return "view";
	}

	@GetMapping("/OfficeInsert")
	public String officeInsert(
			Model model) {
		add_View_Data_(model, "officeInsert", "新規項目追加");
		model.addAttribute("office", service.new_Office());
		model.addAttribute("next_id", service.next_Office_Id());
		return "view";
	}

	@GetMapping("/SubjectInsert")
	public String subjectInsert(
			Model model) {
		add_View_Data_(model, "subjectInsert", "新規項科目追加");
		model.addAttribute("subject", service.new_Subject());
		model.addAttribute("next_id", service.next_Subject_Id());
		return "view";
	}

	@PostMapping("/ActionInsert")
	public String actionInsert(
			@RequestParam("date")String date,
			Model model) {
		add_View_Data_(model, "actionInsert", "新規出納追加");
		model.addAttribute("date", date);
		model.addAttribute("japanese_Date", service.japanese_Date(date));
		model.addAttribute("name", service.name());
		model.addAttribute("action", service.new_Action(date));
		model.addAttribute("next_id", service.next_Action_Id());
		model.addAttribute("label_Set_List", LabelSet.actionInsert_Set);
		model.addAttribute("subjects", service.subjects());
		return "view";
	}

	@PostMapping("/ActionInput")
	public String actionInput(
			@RequestParam("post_date")String date,
			@ModelAttribute("action")Action action,
			Model model) {
		add_View_Data_(model, "actionInput", "新規出納追加");
		model.addAttribute("date", date);
		model.addAttribute("japanese_Date", service.japanese_Date(date));
		model.addAttribute("name", service.name());
		model.addAttribute("action", action);
		model.addAttribute("next_id", service.next_Action_Id());
		model.addAttribute("label_Set_List", LabelSet.actionInsert_Set);
		model.addAttribute("subjects", service.subjects());
		return "view";
	}

	@PostMapping("/ActionSubject/Select")
	public String actionSubject_Select(
			@RequestParam("date")String date,
			Model model) {
		add_View_Data_(model, "select", "科目選択");
		model.addAttribute("url", "/ActionSubject/Input");
		model.addAttribute("date", date);
		model.addAttribute("displayed_Date", service.japanese_Date(date));
		model.addAttribute("name", service.name());
		model.addAttribute("guide", "科目を選択して下さい");
		Action object = service.new_Action(date);
		model.addAttribute("object", object);
		model.addAttribute("field", object.getSubject());
		model.addAttribute("options", service.subjects());
		return "view";
	}

	@PostMapping("/ActionSubject/Input")
	public String actionSubject_Input(
			@RequestParam("post_date")String date,
			@RequestParam("field")String subject,
			@ModelAttribute("object")Action action,
			Model model) {
		add_View_Data_(model, "input", "科目入力");
		model.addAttribute("url", "/ActionApply/Select");
		model.addAttribute("date", date);
		model.addAttribute("displayed_Date", service.japanese_Date(date));
		model.addAttribute("name", service.name());
		model.addAttribute("guide", "科目を入力して下さい");
		model.addAttribute("object", action);
		model.addAttribute("field", subject);
		return "view";
	}

	@PostMapping("/ActionApply/Select")
	public String actionApply_Select(
			@RequestParam("post_date")String date,
			@RequestParam("field")String subject,
			@ModelAttribute("object")Action action,
			Model model) {
		add_View_Data_(model, "select", "適用選択");
		model.addAttribute("url", "/ActionApply/Input");
		model.addAttribute("date", date);
		model.addAttribute("displayed_Date", service.japanese_Date(date));
		model.addAttribute("name", service.name());
		model.addAttribute("guide", "適用を選択して下さい");
		action.setSubject(subject);
		model.addAttribute("object", action);
		model.addAttribute("field", action.getApply());
		model.addAttribute("options", service.applys());
		return "view";
	}

	@PostMapping("/ActionApply/Input")
	public String actionApply_Input(
			@RequestParam("post_date")String date,
			@RequestParam("field")String apply,
			@ModelAttribute("object")Action action,
			Model model) {
		add_View_Data_(model, "input", "適用入力");
		model.addAttribute("url", "/ActionAccount/Select");
		model.addAttribute("date", date);
		model.addAttribute("displayed_Date", service.japanese_Date(date));
		model.addAttribute("name", service.name());
		model.addAttribute("guide", "適用を入力して下さい");
		model.addAttribute("object", action);
		model.addAttribute("field", apply);
		return "view";
	}

	@PostMapping("/ActionAccount/Select")
	public String actionAccount_Select(
			@RequestParam("post_date")String date,
			@RequestParam("field")String apply,
			@ModelAttribute("object")Action action,
			Model model) {
		add_View_Data_(model, "select", "収支選択");
		model.addAttribute("url", "/ActionAccount/Input");
		model.addAttribute("date", date);
		model.addAttribute("displayed_Date", service.japanese_Date(date));
		model.addAttribute("name", service.name());
		model.addAttribute("guide", "収支を選択して下さい");
		action.setApply(apply);
		model.addAttribute("object", action);
		model.addAttribute("field", "");
		model.addAttribute("options", service.accounts());
		return "view";
	}

	@PostMapping("/ActionAccount/Input")
	public String actionAccount_Input(
			@RequestParam("post_date")String date,
			@RequestParam("field")String account,
			@ModelAttribute("object")Action action,
			Model model) {
		add_View_Data_(model, "input", "金額入力");
		model.addAttribute("date", date);
		model.addAttribute("displayed_Date", service.japanese_Date(date));
		model.addAttribute("name", service.name());
		model.addAttribute("guide", account +"金額を入力して下さい");
		model.addAttribute("object", action);
		switch (account) {
			case "収入":
				model.addAttribute("url", "/ActionIncome/Insert");
				model.addAttribute("field", action.getIncome());
				break;
			case "支出":
				model.addAttribute("url", "/ActionSpending/Insert");
				model.addAttribute("field", action.getSpending());
				break;
		}
		return "view";
	}


	@PostMapping("/Office/Insert")
	public String office_Insert(
			@RequestParam("post_id")int id,
			@ModelAttribute("office")Office office,
			RedirectAttributes redirectAttributes) {
		String message = service.office_Insert(office, id);
		redirectAttributes.addFlashAttribute("message", message);
		return redirect("/OfficeSetting");
	}

	@PostMapping("/Subject/Insert")
	public String subject_Insert(
			@RequestParam("post_id")int id,
			@ModelAttribute("subject")Subject subject,
			RedirectAttributes redirectAttributes) {
		String message = service.subject_Insert(subject, id);
		redirectAttributes.addFlashAttribute("message", message);
		return redirect("/SubjectSetting");
	}

	@PostMapping("/Action/Insert")
	public String action_Insert(
			@RequestParam("date")String date,
			@ModelAttribute("action")Action action,
			RedirectAttributes redirectAttributes) {
		String message = service.action_Insert(action);
		redirectAttributes.addFlashAttribute("message", message);
		return redirect("/Daily?date=" + date);
	}

	@PostMapping("/ActionIncome/Insert")
	public String actionIncome_Insert(
			@RequestParam("post_date")String date,
			@RequestParam("field")int income,
			@ModelAttribute("object")Action action,
			RedirectAttributes redirectAttributes) {
		action.setIncome(income);
		action.setSpending(0);
		String message = service.action_Insert(action);
		redirectAttributes.addFlashAttribute("message", message);
		return redirect("/Daily?date=" + date);
	}

	@PostMapping("/ActionSpending/Insert")
	public String actionSpending_Insert(
			@RequestParam("post_date")String date,
			@RequestParam("field")int spending,
			@ModelAttribute("object")Action action,
			RedirectAttributes redirectAttributes) {
		action.setIncome(0);
		action.setSpending(spending);
		service.___consoleOut___(action.toString());
		String message = service.action_Insert(action);
		redirectAttributes.addFlashAttribute("message", message);
		return redirect("/Daily?date=" + date);
	}

	@PostMapping("/OfficeUpdate")
	public String officeUpdate(
			@RequestParam("id")int id,
			Model model) {
		add_View_Data_(model, "officeUpdate", "事業所情報更新");
		model.addAttribute("id", id);
		model.addAttribute("office", service.office(id));
		return "view";
	}

	@PostMapping("/ActionUpdate")
	public String actionUpdate(
			@RequestParam("id")int id,
			@RequestParam("date")String date,
			Model model) {
		add_View_Data_(model, "actionUpdate", "出納情報更新");
		model.addAttribute("guide", "内容変更後更新ボタンを押してください");
		model.addAttribute("do_Name", "更新");
		model.addAttribute("cancel_url", req("/Daily"));
		model.addAttribute("do_url", req("/Action/Update"));
		model.addAttribute("id", id);
		model.addAttribute("date", date);
		model.addAttribute("object", service.action(id));
		model.addAttribute("label_Set_List", LabelSet.actionUpdate_Set);
		model.addAttribute("subjects", service.subjects());
		return "view";
	}

	@PostMapping("/Action/Update")
	public String action_Update(
			@RequestParam("post_id")int id,
			@RequestParam("post_date")String date,
			@ModelAttribute("action")Action action,
			RedirectAttributes redirectAttributes) {
		String message = service.action_Update(action, id);
		redirectAttributes.addFlashAttribute("message", message);
		LocalDate localDate = service.to_LocalDate(date);
		return redirect("/Daily?date=" + localDate);
	}

	@PostMapping("/Office/Update")
	public String office_Update(
			@RequestParam("post_id")int id,
			@ModelAttribute("office")Office office,
			RedirectAttributes redirectAttributes) {
		String message = service.office_Update(office, id);
		redirectAttributes.addFlashAttribute("message", message);
		return redirect("/OfficeSetting");
	}

	@PostMapping("/ActionDelete")
	public String actionDelete(
			@RequestParam("id")int id,
			@RequestParam("date")String date,
			Model model) {
		add_View_Data_(model, "delete", "出納情報削除");
		model.addAttribute("guide", "この行を削除してもよろしいですか？");
		model.addAttribute("cancel_url", req("/Daily"));
		model.addAttribute("delete_url", req("/Action/Delete"));
		model.addAttribute("id", id);
		model.addAttribute("date", date);
		model.addAttribute("object", service.action(id));
		model.addAttribute("label_Set_List", LabelSet.actionDelete_Set);
		return "view";
	}

	@PostMapping("/Action/Delete")
	public String action_Delete(
			@RequestParam("id")int id,
			@RequestParam("date")String date,
			RedirectAttributes redirectAttributes) {
		String message = service.action_Delete(id);
		redirectAttributes.addFlashAttribute("message", message);
		return redirect("/Daily?date=" + date);
	}


	@PostMapping("/OfficeReport")
	public String officeReport() {
		return "redirect:/CareRecord/RoutineReport";
	}

	@GetMapping("/OfficeReport")
	public String officeReport(
			Model model) {
		add_View_Data_(model, "officeReport", "事業所情報印刷");
		model.addAttribute("label_Set", LabelSet.officeReport_Set);
		model.addAttribute("office_Report", service.office_Report());
		return "view";
	}

	@PostMapping("/Office/Output/Excel")
	public String office_Output_Excel(
			HttpServletResponse httpServletResponse,
			RedirectAttributes redirectAttributes) {
		String message = service.office_Output_Excel(httpServletResponse);
		redirectAttributes.addFlashAttribute("message", message);
		return redirect("/OfficeReport");
	}

	@PostMapping("/LastMonth")
	public String lastMonth(
			@RequestParam("date")String date,
			RedirectAttributes redirectAttributes) {
		LocalDate lastMonth = service.to_LocalDate(date).minusMonths(1);
		return redirect("/Monthly?date=" + lastMonth);
	}

	@PostMapping("/NextMonth")
	public String nextMonth(
			@RequestParam("date")String date,
			RedirectAttributes redirectAttributes) {
		LocalDate nextMonth = service.to_LocalDate(date).plusMonths(1);
		return redirect("/Monthly?date=" + nextMonth);
	}

	@PostMapping("/Monthly")
	public String monthly(
			@RequestParam("date")String date,
			RedirectAttributes redirectAttributes) {
		return redirect("/Monthly?date=" + date);
	}

	@GetMapping("/Monthly")
	public String monthly(
			@Param("year_month")String date,
			Model model) {
		add_View_Data_(model, "monthly", "月別出納一覧");
		if(date  == null) date = service.this_Year_Month();
		model.addAttribute("name", service.name());
		model.addAttribute("date", date);
		model.addAttribute("japanese_Date", service.japanese_Date(date, "G y 年 M 月"));
		model.addAttribute("carryover", service.carryover(date));
		List<Action> action_List = service.monthly_List(date, 1);
		model.addAttribute("action_List", action_List);
		model.addAttribute("income", service.income_List(action_List));
		model.addAttribute("spending",service.spending_List(action_List));
		model.addAttribute("label_Set_List", LabelSet.action_List_Set);
		return "view";
	}

	@PostMapping("/Year")
	public String year(
			@RequestParam("year")String year,
			@RequestParam("subject")String subject,
			RedirectAttributes redirectAttributes) {
		redirectAttributes.addAttribute("subject", subject);
		year = service.year(year + " 1 月 1 日", "G y 年 M 月 d 日");
		return redirect("/Year?year=" + year);
	}

	@GetMapping("/Year")
	public String year(
			@Param("year")String year,
			@Param("subject")String subject,
			Model model) {
		add_View_Data_(model, "year", "年度別出納一覧");
		if(year  == null) year = service.this_Year();
		model.addAttribute("name", service.name());
		model.addAttribute("year", service.japanese_Date(year, "G y 年"));
		year += "/01";
		model.addAttribute("carryover", service.carryover(year));
		List<Action> action_List = service.year_List(year, 1, subject);
		model.addAttribute("action_List", action_List);
		model.addAttribute("income", service.income_List(action_List));
		model.addAttribute("spending", service.spending_List(action_List));
		model.addAttribute("label_Set_List", LabelSet.action_List_Set);
		if(subject  == null) subject = "全科目";
		if(subject.equals("")) subject = "全科目";
		model.addAttribute("subject", subject);
		return "view";
	}

	@PostMapping("/Subject")
	public String subject(
			@RequestParam("year")String year,
			@RequestParam("subject")String subject,
			Model model) {
		add_View_Data_(model, "subject", "科目指定");
		if(year  == null) year = service.this_Year();
		model.addAttribute("year", year);
		model.addAttribute("name", service.name());
		model.addAttribute("japanese_year", service.japanese_Date(year, "G y 年"));
		model.addAttribute("guide", "科目を選択して下さい");
		if(subject.equals("全科目")) subject = "";
		model.addAttribute("subject", subject);
		model.addAttribute("options", service.subjects());
		return "view";
	}

	@PostMapping("/SelectYear")
	public String selectYear(
			@RequestParam("year") String year,
			@RequestParam("subject") String subject,
			Model model) {
		add_View_Data_(model, "selectYear", "年度選択");
		model.addAttribute("selected_year", year);
		model.addAttribute("subject", subject);
		model.addAttribute("years", service.years());
		return "view";
	}

	@PostMapping("/Daily/Output/Excel")
	public String daily_Output_Excel(
			@RequestParam("date") String date,
			HttpServletResponse httpServletResponse,
			RedirectAttributes redirectAttributes) {
		String message = service.daily_Output_Excel(date, httpServletResponse);
		redirectAttributes.addFlashAttribute("message", message);
	return redirect("/Daily?date=" + date);
	}

	@PostMapping("/Monthly/Output/Excel")
	public String monthly_Output_Excel(
			@RequestParam("date") String date,
			HttpServletResponse httpServletResponse,
			RedirectAttributes redirectAttributes) {
		String message = service.monthly_Output_Excel(date, httpServletResponse);
		redirectAttributes.addFlashAttribute("message", message);
		return redirect("/Monthly?date=" + date);
	}

	@PostMapping("/Year/Output/Excel")
	public String year_Output_Excel(
			@RequestParam("year") String date,
			@RequestParam("subject")String subject,
			HttpServletResponse httpServletResponse,
			RedirectAttributes redirectAttributes) {
		String message = service.year_Output_Excel(date, subject, httpServletResponse);
		redirectAttributes.addFlashAttribute("message", message);
		return redirect("/Year?date=" + date);
	}

	@PostMapping("/Daily")
	public String daily(
			@RequestParam("date") String date) {
		return redirect("/Daily?date=" + date);
	}

	@GetMapping("/Daily")
	public String daily(
			@Param("date") String date,
			Model model) {
		if (date == null) date = service.today();
		add_View_Data_(model, "daily", "出納帳");
		model.addAttribute("date", date);
		model.addAttribute("japanese_Date", service.japanese_Date(date));
		model.addAttribute("name", service.name());
		model.addAttribute("action_List", service.action_List(date));
		model.addAttribute("account", service.account_Monthly(date));
		model.addAttribute("label_Set_List", LabelSet.daily_Set);
		return "view";
	}

	@PostMapping("/Daily/Date")
	public String daily_Date(
			@RequestParam("date") String date,
			Model model) {
		add_View_Data_(model, "date", "日付選択");
		model.addAttribute("date", date);
		model.addAttribute("label", "日付を選んでください");
		model.addAttribute("url", req("/Daily"));
		return "view";
	}

	@PostMapping("/SelectMonth")
	public String selectMonth(
			@RequestParam("date") String date,
			Model model) {
		add_View_Data_(model, "date", "日付選択");
		model.addAttribute("date", date);
		model.addAttribute("label", "日付を選んでください");
		model.addAttribute("url", req("/Monthly"));
		return "view";
	}

	@PostMapping("/DailyCash")
	public String daily_Cash(
			@RequestParam("date") String date,
			Model model) {
		add_View_Data_(model, "cash", "現金残高入力");
		model.addAttribute("date", date);
		model.addAttribute("japanese_Date", service.japanese_Date(date));
		model.addAttribute("cash", service.cash(date));
		model.addAttribute("label_Set_List", LabelSet.cash_Set);
		return "view";
	}

	@PostMapping("/DailyCashResult")
	public String dailyCashResult(
			@RequestParam("post_date") String date,
			@ModelAttribute("cash") Cash cash,
			Model model) {
		add_View_Data_(model, "cashResult", "現金残高確認");
		model.addAttribute("date", date);
		model.addAttribute("japanese_Date", service.japanese_Date(date));
		model.addAttribute("total", service.cash_Total(cash));
		model.addAttribute("balance", service.cash_Balance(date, cash));
		model.addAttribute("url", req("/DailyCash/Update"));
		model.addAttribute("label_Set_List", LabelSet.cash_Set);
		return "view";
	}

	@PostMapping("/DailyCash/Update")
	public String dailyCash_Update(
			@RequestParam("post_date") String date,
			@ModelAttribute("cash") Cash cash,
			RedirectAttributes redirectAttributes) {
			String message = service.cash_Update(cash);
			redirectAttributes.addFlashAttribute("message", message);
		return redirect("/Daily?date=" + date);
	}

	/** view 表示に必要な属性データをモデルに登録 */
	private void add_View_Data_(Model model, String template, String title) {
		model.addAttribute("library", template + "::library");
		model.addAttribute("main", template + "::main");
		model.addAttribute("title", title);
		model.addAttribute("req", req());
		System.out.println("template = " + template);
	}


}
