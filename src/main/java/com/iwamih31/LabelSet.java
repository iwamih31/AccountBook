package com.iwamih31;

public class LabelSet {

	public static Set[] daily_Set = {
			set("科目",	 10),
			set("適用",	 25),
			set("収入",	  7),
			set("支出",		7),
			set("残高",		7)
		};

	public static Set[] action_List_Set = {
			set("日付",		8),
			set("科目",		10),
			set("適用",		20),
			set("収入",	  8),
			set("支出",		8),
	};

	public static Set[] actionInsert_Set = {
			set("日付",		8),
			set("科目",		10),
			set("適用",		20),
			set("収入",	  8),
			set("支出",		8),
	};

	public static Set[] actionUpdate_Set = {
			set("ID",		  4),
			set("日付",		6),
			set("科目",		6),
			set("適用",		20),
			set("収入",	  5),
			set("支出",		5),
	};

	public static Set[] actionDelete_Set = {
			set("ID",		  4),
			set("日付",		6),
			set("科目",		6),
			set("適用",		20),
			set("収入",	  6),
			set("支出",		6),
	};

	public static Set[] officeReport_Set = {
			set("ID",				6),
			set("項目",			4),
			set("内容",			8)
		};
	public static Set[] cash_Set = {
			set("金種",10),
			set("枚数",10)
	};


	public static Set set(String label_Name, int width) {
		return new Set(label_Name, width);
	}
}
