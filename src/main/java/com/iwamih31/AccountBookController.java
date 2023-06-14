package com.iwamih31;

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


	@GetMapping("/OfficeInsert")
	public String officeInsert(
			Model model) {
		add_View_Data_(model, "officeInsert", "新規項目追加");
		model.addAttribute("office", service.new_Office());
		model.addAttribute("next_id", service.next_Office_Id());
		return "view";
	}

	@PostMapping("/ActionInsert")
	public String actionInsert(
			@RequestParam("date")String date,
			Model model) {
		add_View_Data_(model, "actionInsert", "新規出納追加");
		model.addAttribute("date", date);
		model.addAttribute("name", service.name());
		model.addAttribute("action", service.new_Action(date));
		model.addAttribute("next_id", service.next_Action_Id());
		model.addAttribute("label_Set_List", LabelSet.actionInsert_Set);
		return "view";
	}

	@PostMapping("/Office/Insert")
	public String office_Insert(
			@RequestParam("post_id")int id,
			@ModelAttribute("office")Office office,
			RedirectAttributes redirectAttributes) {
		String message = service.office_Insert(office, id);
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:" + req() + "/OfficeSetting";
	}

	@PostMapping("/Action/Insert")
	public String action_Insert(
			@RequestParam("date")String date,
			@ModelAttribute("office")Action action,
			RedirectAttributes redirectAttributes) {
		String message = service.action_Insert(action);
		redirectAttributes.addFlashAttribute("message", message);
		redirectAttributes.addFlashAttribute("date", date);
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
		add_View_Data_(model, "actionUpdate", "事業所情報更新");
		model.addAttribute("guide", "内容変更後更新ボタンを押してください");
		model.addAttribute("do_Name", "更新");
		model.addAttribute("cancel_url", req("/Daily"));
		model.addAttribute("do_url", req("/Action/Update"));
		model.addAttribute("id", id);
		model.addAttribute("date", date);
		model.addAttribute("object", service.action(id));
		model.addAttribute("label_Set_List", LabelSet.actionUpdate_Set);
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
		return redirect("/Daily?date=" + date);
	}

	@PostMapping("/Office/Update")
	public String office_Update(
			@RequestParam("post_id")int id,
			@ModelAttribute("office")Office office,
			RedirectAttributes redirectAttributes) {
		String message = service.office_Update(office, id);
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:" + req() + "/OfficeSetting";
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
		return "redirect:" + req() + "/CareRecord/OfficeReport";
	}

	@GetMapping("/Monthly")
	public String monthly(
			@Param("year_month")String year_month,
			Model model) {
		add_View_Data_(model, "monthly", "月別出納一覧");
		if(year_month  == null) year_month = service.this_Year_Month();
		model.addAttribute("name", service.name());
		model.addAttribute("year_month", year_month);
		model.addAttribute("action_List", service.monthly_List(year_month, 1));
		model.addAttribute("carryover", service.carryover(year_month));
		model.addAttribute("label_Set_List", LabelSet.action_List_Set);
		model.addAttribute("income", service.income_List(year_month, 1));
		model.addAttribute("spending",service.spending_List(year_month, 1));
		return "view";
	}

	@GetMapping("/Year")
	public String year(
			@Param("year")String year,
			Model model) {
		add_View_Data_(model, "monthly", "年度別出納一覧");
		if(year  == null) year = service.this_Year();
		model.addAttribute("name", service.name());
		model.addAttribute("year_month", year);
		model.addAttribute("action_List", service.year_List(year, 1));
		model.addAttribute("carryover", service.carryover(year));
		model.addAttribute("label_Set_List", LabelSet.action_List_Set);
		model.addAttribute("income", 0);
		model.addAttribute("spending", 0);
		return "view";
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
		if (date == null) date = service.today().toString();
		add_View_Data_(model, "daily", "出納帳");
		model.addAttribute("name", service.name());
		model.addAttribute("date", date);
		model.addAttribute("action_List", service.action_List(date));
		model.addAttribute("account", service.account(date));
		model.addAttribute("label_Set_List", LabelSet.daily_Set);
		return "view";
	}

	@PostMapping("/Daily/Date")
	public String daily_Date(
			@RequestParam("date") String date,
			Model model) {
//		if (date == null) date = service.today().toString();
		add_View_Data_(model, "date", "日付選択");
		model.addAttribute("date", date);
		model.addAttribute("label", "日付を選んでください");
		model.addAttribute("url", req("/Daily"));
		return "view";
	}

	@PostMapping("/DailyCash")
	public String daily_Cash(
			@RequestParam("date") String date,
			Model model) {
		add_View_Data_(model, "cash", "現金残高入力");
		model.addAttribute("date", date);
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
