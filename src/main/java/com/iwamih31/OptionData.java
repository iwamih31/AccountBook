package com.iwamih31;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class OptionData {

  // 部屋番号
  public static Integer[] rooms = nums(1, 20 , 1);

  // 名前
  public static String[] names = names();

  // 日付
  public static String[] dates = dates(40);

  // 時間
  public static String[] times = times(0, 0, 5);

  // 科目
  public static String[] subjects = subjects();

  // 適用
  public static String[] applys = {"パ交", "リ交", "ト有", "ト無", "ト未", "失禁", "拒否"};

  // 利用状況
  public static String[] use = {"利用中", "利用終了"};

  // 曜日
  public static String[] day_of_week = {"月", "火", "水", "木", "金", "土", "日"};

  // 年
  public static Integer[] year = nums(this_Year() - 50, this_Year() , 1);

  // 年/月
  public static String[] year_month = year_month((50 * 12), 1);

  // 月/日
  public static ArrayList<String> month_day = month_day();

  /** start から end まで add ずつ増やした数を代入した配列を作成 */
  public static Integer[] nums(int start, int end, int add) {
		Integer[] nums = new Integer[(end - start) / add + 1];
		for (int i = 0; i < nums.length; i++) {
			nums[i] = i * add + start;
		}
		return nums;
	}

	private static String[] subjects() {
		return null;
	}

	public static String[] names() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	/** 開始時刻の末尾を 0 または 5 に変換し、そこから chopped_Minute 分ずつ増やした時刻の配列を作成 */
	public static String[] times(int start_Hour, int start_Minute, int chopped_Minute) {
		// 表示形式を指定
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		// 開始の 時刻 を設定
		LocalDateTime dateTime = null;
		try {
			dateTime = LocalDateTime.now().with(LocalTime.of(start_Hour, start_Minute));
		} catch (Exception e) {
			dateTime = LocalDateTime.now().with(LocalTime.of(0, 0));
		}
		// 配列を作成し5分刻みの時刻を代入
		String[] times = new String[60 / chopped_Minute * 24];
		for (int i = 0; i < times.length; i++) {
			// フォーマット
			String data = dateTimeFormatter.format(dateTime);
			// 末尾を 0 または 5 に変換して times[i] に代入
			// minutes の1の位を取得
			String minutesLast = data.substring(data.length()-1, data.length());
			// 0 にするか 5 にするか判定
			if (Integer.parseInt(minutesLast) < 5) {
				minutesLast = "0";
			} else {
				minutesLast = "5";
			}
			// 末尾を変換
			times[i] = data.substring(0, data.length()-1) + minutesLast;
			// dateTimeを chopped_Minute 分増やす
			dateTime = dateTime.plusMinutes(chopped_Minute);
		}
		return times;
	}

	public static String now() {
		// 現在日時を取得
		LocalDateTime dateTime = LocalDateTime.now();
		// 表示形式を指定
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
		return dateTimeFormatter.format(dateTime);
	}

	public static int this_Year() {
		// 現在日時を取得
		LocalDateTime dateTime = LocalDateTime.now();
		// 表示形式を指定
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy");
		return Integer.parseInt(dateTimeFormatter.format(dateTime));
	}

	public static String[] dates(int years) {
		// 現在日時を取得
		LocalDateTime dateTime = LocalDateTime.now();
		// dateTimeを years 年減らす
		dateTime = dateTime.minusYears(years);
		// 表示形式を指定
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		// 配列を作成し今日から years 年前迄の日付を代入
		String[] data = new String[years * 366];
		for (int i = 0; i < data.length; i++) {
			// フォーマット
			data[i] = dateTimeFormatter.format(dateTime);
			// dateTimeを1日増やす
			dateTime = dateTime.plusDays(1);
		}
		return data;
	}

	public static String[] days(String month) {
		int lastDay = 31;
		switch(month) {
			case "2":
				lastDay = 29;
				break;
			case "4":
			case "6":
			case "9":
			case "11":
				lastDay = 30;
				break;
		}
		Integer[] days = nums(1, lastDay, 1);
		return arrayAlignment(days, 2, "0");
	}

	public static String[] month() {
		// 1から12の配列を作成
		Integer[] month = nums(1, 12, 1);
		// 配列の中身の文字数を2文字に揃えて返す
		return arrayAlignment(month, 2, "0") ;
	}

	/** String date の年数部分から end まで add づつ増やした年数を 代入した配列を作成 */
	public static String[] years(String date, int end_year, int add) {
		int year = Integer.parseInt(date.split("/")[0].split("_")[0].split("年")[0]);
		Integer[] years = nums(year, end_year, add);
		return arrayAlignment(years, 4, "0");
	}

	/** months_Ago か月前から months_Later か月後迄の [年/月] を取得 */
	public static String[] year_month(int months_Ago, int months_Later) {
		// year_Ago 月前（スタート値）を取得
		LocalDate localDate = LocalDate.now().minusMonths(months_Ago);
		// 表示形式を指定
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM");
		// データ数分の要素を持つ配列を作成し [年/月] を代入
		String[] data = new String[months_Ago + months_Later + 1];
		for (int i = 0; i < data.length; i++) {
			// localDateを "yyyy/MM" 形式に変換
			data[i] = dateTimeFormatter.format(localDate);
			// dateTimeを1月増やす
			localDate = localDate.plusMonths(1);
		}
		return data;
	}

	private static ArrayList<String> month_day() {
		ArrayList<String> monthDay = new ArrayList<String>();
		String[] month = month();
		for (String mon : month) {
			String[] days = days(mon);
			for (String day : days) {
				monthDay.add(mon + "/" + day);
			}
		}
		return monthDay;
	}

	/** 配列の中身を同じ文字数に揃える */
	public static String[] arrayAlignment(Object[] array, int word_count , String fill) {
		// fill を word_count の数だけ繋げる
		String fills = "";
		for (int i = 0; i < word_count; i++) {
			fills += fill;
		}
		// Object[] array と同じ length の String 配列 alignmentArray を作成
		String[] alignmentArray = new String[array.length];
		for (int i = 0; i < alignmentArray.length; i++) {
			// 先頭に fills を付ける
			String string = (fills + array[i]);
			//末尾から word_count の数だけ文字を抜き出し alignmentArray[i] に代入
			alignmentArray[i] = string.substring(string.length() - word_count);
		}
		return alignmentArray;
	}

}
