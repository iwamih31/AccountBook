package com.iwamih31;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class MainController {

	@Autowired
	private AccountBookService service;

	@GetMapping("/")
	public String accountBook_index() {
		service.___consoleOut___("@PostMapping(\"/\")開始");
		service.___consoleOut___("@PostMapping(\"/\")終了");
		return "redirect:/AccountBook/";
	}

	@GetMapping("/main")
	public String main0() {
		service.___consoleOut___("@PostMapping(\"/\")開始");
		service.___consoleOut___("@PostMapping(\"/\")終了");
		return "redirect:/AccountBook/";
	}

	@GetMapping("/Main")
	public String main() {
		service.___consoleOut___("@PostMapping(\"/\")開始");
		service.___consoleOut___("@PostMapping(\"/\")終了");
		return "redirect:/AccountBook/";
	}

	/** view 表示に必要な属性データをモデルに登録 */
	private void add_View_Data_(Model model, String template, String title) {
		model.addAttribute("library", template + "::library");
		model.addAttribute("main", template + "::main");
		model.addAttribute("title", title);
	}

}
