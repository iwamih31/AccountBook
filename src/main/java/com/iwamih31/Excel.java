package com.iwamih31;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Excel {

	MissingCellPolicy cellPolicy = MissingCellPolicy.CREATE_NULL_AS_BLANK;


	/** alignment_Pattern に応じてセルの alignment を設定 */
	public CellStyle alignment_Apply(CellStyle cellStyle, String alignment_Pattern) {
		if(alignment_Pattern != null) {
			switch(alignment_Pattern) {
				case "｜":
					cellStyle.setAlignment(HorizontalAlignment.CENTER);
						break;
				case "CS":
					cellStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
						break;
				case "DD":
					cellStyle.setAlignment(HorizontalAlignment.DISTRIBUTED);
						break;
				case "FL":
					cellStyle.setAlignment(HorizontalAlignment.FILL);
						break;
				case "JY":
					cellStyle.setAlignment(HorizontalAlignment.JUSTIFY);
						break;
				case "←":
					cellStyle.setAlignment(HorizontalAlignment.LEFT);
						break;
				case "→":
					cellStyle.setAlignment(HorizontalAlignment.RIGHT);
						break;
				default:
					cellStyle.setAlignment(HorizontalAlignment.GENERAL);
			}
		}
		return cellStyle;
	}

	/** border_Pattern に応じてセルの 罫線 を設定 */
	public CellStyle border_Apply(CellStyle cellStyle, String border_Pattern) {
		if(border_Pattern != null) {
			switch(border_Pattern) {
			case "□":
				cellStyle.setBorderTop(BorderStyle.THIN);
				cellStyle.setBorderBottom(BorderStyle.THIN);
				cellStyle.setBorderLeft(BorderStyle.THIN);
				cellStyle.setBorderRight(BorderStyle.THIN);
				break;
			case "￣":
				cellStyle.setBorderTop(BorderStyle.THIN);
				cellStyle.setBorderBottom(BorderStyle.NONE);
				cellStyle.setBorderLeft(BorderStyle.NONE);
				cellStyle.setBorderRight(BorderStyle.NONE);
				break;
			case "＿":
				cellStyle.setBorderTop(BorderStyle.NONE);
				cellStyle.setBorderBottom(BorderStyle.THIN);
				cellStyle.setBorderLeft(BorderStyle.NONE);
				cellStyle.setBorderRight(BorderStyle.NONE);
				break;
			case " |":
				cellStyle.setBorderTop(BorderStyle.NONE);
				cellStyle.setBorderBottom(BorderStyle.NONE);
				cellStyle.setBorderLeft(BorderStyle.NONE);
				cellStyle.setBorderRight(BorderStyle.THIN);
				break;
			case "| ":
				cellStyle.setBorderTop(BorderStyle.NONE);
				cellStyle.setBorderBottom(BorderStyle.NONE);
				cellStyle.setBorderLeft(BorderStyle.THIN);
				cellStyle.setBorderRight(BorderStyle.NONE);
				break;
			case "二":
				cellStyle.setBorderTop(BorderStyle.THIN);
				cellStyle.setBorderBottom(BorderStyle.THIN);
				cellStyle.setBorderLeft(BorderStyle.NONE);
				cellStyle.setBorderRight(BorderStyle.NONE);
				break;
			case "||":
				cellStyle.setBorderTop(BorderStyle.NONE);
				cellStyle.setBorderBottom(BorderStyle.NONE);
				cellStyle.setBorderLeft(BorderStyle.THIN);
				cellStyle.setBorderRight(BorderStyle.THIN);
				break;
			case "冂":
				cellStyle.setBorderTop(BorderStyle.THIN);
				cellStyle.setBorderBottom(BorderStyle.NONE);
				cellStyle.setBorderLeft(BorderStyle.THIN);
				cellStyle.setBorderRight(BorderStyle.THIN);
				break;
			case "凵":
				cellStyle.setBorderTop(BorderStyle.NONE);
				cellStyle.setBorderBottom(BorderStyle.THIN);
				cellStyle.setBorderLeft(BorderStyle.THIN);
				cellStyle.setBorderRight(BorderStyle.THIN);
				break;
			case "匚":
				cellStyle.setBorderTop(BorderStyle.THIN);
				cellStyle.setBorderBottom(BorderStyle.THIN);
				cellStyle.setBorderLeft(BorderStyle.THIN);
				cellStyle.setBorderRight(BorderStyle.NONE);
				break;
			case "コ":
				cellStyle.setBorderTop(BorderStyle.THIN);
				cellStyle.setBorderBottom(BorderStyle.THIN);
				cellStyle.setBorderLeft(BorderStyle.NONE);
				cellStyle.setBorderRight(BorderStyle.THIN);
				break;
			case "ノ":
				cellStyle.setBorderTop(BorderStyle.NONE);
				cellStyle.setBorderBottom(BorderStyle.THIN);
				cellStyle.setBorderLeft(BorderStyle.NONE);
				cellStyle.setBorderRight(BorderStyle.THIN);
				break;
			case "乚":
				cellStyle.setBorderTop(BorderStyle.NONE);
				cellStyle.setBorderBottom(BorderStyle.THIN);
				cellStyle.setBorderLeft(BorderStyle.THIN);
				cellStyle.setBorderRight(BorderStyle.NONE);
				break;
			case "ｒ":
				cellStyle.setBorderTop(BorderStyle.THIN);
				cellStyle.setBorderBottom(BorderStyle.NONE);
				cellStyle.setBorderLeft(BorderStyle.THIN);
				cellStyle.setBorderRight(BorderStyle.NONE);
				break;
			case "¬":
				cellStyle.setBorderTop(BorderStyle.THIN);
				cellStyle.setBorderBottom(BorderStyle.NONE);
				cellStyle.setBorderLeft(BorderStyle.NONE);
				cellStyle.setBorderRight(BorderStyle.THIN);
				break;
			case "  ":
				cellStyle.setBorderTop(BorderStyle.NONE);
				cellStyle.setBorderBottom(BorderStyle.NONE);
				cellStyle.setBorderLeft(BorderStyle.NONE);
				cellStyle.setBorderRight(BorderStyle.NONE);
				break;
			}
		}
		return cellStyle;
	}

	/** 1シート分のデータを 1つのExcelファイル として出力 */
	public String output_Excel_Sheet(String name_Head, WorkSheet workSheet, HttpServletResponse response) {
		___console_Out___("output_Excel_Sheet() 開始");
		String file_Name = with_Now(name_Head) + ".xlsx";
		String message = file_Name + " のダウンロード";
		try (
				Workbook workbook = new XSSFWorkbook();
				OutputStream outputStream = response.getOutputStream()){
			// Sheetを 作成
			sheet_Making(workbook, workSheet);
			// ファイル名を指定して保存
			if (response_Making(response, file_Name)) {
				workbook.write(outputStream);
				message += "が完了しました";
			}
			workbook.close();
			message += " workbook を close() しました";
		} catch (IOException e) {
			message += "が正常に完了出来ませんでした";
			___console_Out___(e.getMessage());
		}
		___console_Out___("output_Excel() 終了");
		return message;
	}

	/** Sheetを 作成 */
	public Sheet sheet_Making(Workbook workbook, WorkSheet work_Sheet) {
		___console_Out___("sheet_Making() 開始");
		/* シートを作成 */
		// シート名取得
		String sheet_Name = work_Sheet.getSheet_Name();
		___console_Out___("sheet_Name = " + sheet_Name);
		// シート名 sheet_Name のシートを作成
		Sheet sheet = workbook.createSheet(sheet_Name);
		String[][] value_Data = work_Sheet.getValue_Data();
		// 行数取得
		int row_Size = value_Data.length;
		// 書式定義用データのリスト取得
		List<Map<String, String[]>> row_Format = work_Sheet.row_Format(row_Size);
		/* 使用するフォントを定義 */
		// フォント定義用データリスト取得
		List<Map<String, String>> work_Sheet_Fonts = work_Sheet.fonts();
		// フォント格納用リスト作成
		List<Font>fonts = new ArrayList<>();
		// フォント格納用リスト分ループ
		for (Map<String, String> work_Sheet_Font : work_Sheet_Fonts) {
			// 新しいフォント作成
			Font font = workbook.createFont();
			// フォント名取得
			font.setFontName(work_Sheet_Font.get("fontName"));
			// フォントの高さ取得
			font.setFontHeight((short) Integer.parseInt(work_Sheet_Font.get("fontHeight")));
			// フォント格納用リストにフォントをセット
			fonts.add(font);
		}
		// 行ループ
		for (int i = 0; i < row_Size; i++) {
			// 行を定義
			Row row = sheet.createRow(i);
			// 行フォーマット
			Map<String, String[]> row_Map = row_Format.get(i);
			// 行の高さを取得
			int height = Integer.parseInt(row_Map.get("height")[0]);
			// 取得した高さを行にセット
			row.setHeight((short) height);
			// 列数取得
			int column_Size = value_Data[i].length;
			// 列数分ループ
			for (int j = 0; j < column_Size; j++) {
				// セルを定義
				Cell cell = row.createCell(j);
				String value = value_Data[i][j];
				// セルに値をセット
				if(is_Double(value)){
					cell.setCellValue(Double.parseDouble(value));
				} else {
					if (!value.equals("")) cell.setCellValue(value);
				}
				// セルスタイルを定義
				CellStyle cellStyle = workbook.createCellStyle();
				// セルのフォントスタイルのリスト番号を取得
				int font_Num = Integer.parseInt(row_Map.get("font")[0]);
				// 取得した番号からセルのフォント決定
				Font font = fonts.get(font_Num);
				// 決定したフォントを cellStyle にセット
				cellStyle.setFont(font);
				// 背景色指定
				if (row_Map.get("bg_color") != null) {
					bg_color_Apply(cellStyle, row_Map.get("bg_color")[0]);
				}
				// 縦配置
				cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				// 横配置
				alignment_Apply(cellStyle, row_Map.get("align")[j]);
				// 罫線指定
				border_Apply(cellStyle,  row_Map.get("border")[j]);
				// セルにセルスタイルを適用
				cell.setCellStyle(cellStyle);
				// 最後の行のみ
				if (i == row_Size - 1) {
					int[] column_Width = work_Sheet.getColumn_Width();
					if (column_Width != null) {
						// 列幅設定（1文字分の横幅 × 文字数 ＋ 微調整分の幅）
						sheet.setColumnWidth(j, 380 * column_Width[j] + 0);
					}
					// 列幅設定（オート）
					// sheet.autoSizeColumn(j);
					// 最後の列のみ印刷範囲設定
					if(j == column_Size - 1) {
						// 印刷範囲設定
						workbook.setPrintArea(0, 0, j, 0, i);
					}
				}
			}
		}
//		sheet.setColumnBreak(7); // 改ページ位置設定
//		sheet.removeColumnBreak(4);

		PrintSetup printSetup = sheet.getPrintSetup();
    printSetup.setLandscape(work_Sheet.printSetup);
		printSetup.setFitWidth((short) 1);
		printSetup.setFitHeight((short) 1);
		sheet.setAutobreaks(true);


//		for (int rowBreak : sheet.getRowBreaks()) {
//	    sheet.removeRowBreak(rowBreak);
//	}
//		for (int colBreak : sheet.getColumnBreaks()) {
//	    sheet.removeColumnBreak(colBreak);
//	}

		___console_Out___("sheet_Making() 終了");
		return sheet;
	}

	// 色指定
	private void bg_color_Apply(CellStyle cellStyle, String color_Index) {
		if(color_Index != null) {
			short bg_Index = (short) Integer.parseInt(color_Index);
			// color_Index が AUTOMATIC のindexだったら何もしない
			if(bg_Index != IndexedColors.AUTOMATIC.getIndex()) {
				cellStyle.setFillForegroundColor((short) Integer.parseInt(color_Index));
				cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			}
		}
	}

	private boolean response_Making(HttpServletResponse response, String file_Name) {
		boolean is_Make = false;
		String encodedFilename = with_Now("create") + ".xlsx";
		try {
			encodedFilename = URLEncoder.encode(file_Name, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		___console_Out___("file_Name を " + encodedFilename + "に設定しました");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	  response.setHeader("Content-Disposition", "attachment;filename=\"" + encodedFilename + "\"");
	  response.setCharacterEncoding("UTF-8");
	  is_Make = true;
		return is_Make;
	}

	/** Table データを Excel として出力 */
	public String output_Excel(String name_Head, String[] column_Names, int[] column_Width, String[][] table_Data, HttpServletResponse response) {
		___console_Out___("output_Excel() 開始");

		String file_Name = with_Now(name_Head) + ".xlsx";
		String sheet_Name = with_Now(name_Head);
		String message = file_Name + " のダウンロード";
		try (Workbook workbook = new XSSFWorkbook();
        	OutputStream outputStream = response.getOutputStream()){
			Sheet sheet = workbook.createSheet(sheet_Name);
			// 使用するフォントを定義
			Font font = workbook.createFont();
			font.setFontName("游ゴシック");

			// ヘッダー行
			// セルスタイルを定義
			CellStyle header_CellStyle = workbook.createCellStyle();
			// 色指定
			header_CellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			header_CellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			// 罫線指定
			header_CellStyle.setBorderTop(BorderStyle.THIN);
			header_CellStyle.setBorderBottom(BorderStyle.THIN);
			header_CellStyle.setBorderLeft(BorderStyle.THIN);
			header_CellStyle.setBorderRight(BorderStyle.THIN);
			// 中央揃え
			header_CellStyle.setAlignment(HorizontalAlignment.CENTER);
			// フォントをセット
			header_CellStyle.setFont(font);

			// column_Names 分ループ
			Row row = sheet.createRow(0);
			for (int i = 0; i < column_Names.length; i++) {
				Cell cell = row.createCell(i);
				cell.setCellValue(column_Names[i]);
				// セルにセルスタイルを適用
				cell.setCellStyle(header_CellStyle);
				if (column_Width != null) {
					// 列幅設定（1文字分の横幅 × 文字数 ＋ 微調整分の幅）
					sheet.setColumnWidth(i, 512 * column_Width[i] + 0);
				}
			}

			// データ行
			// セルスタイルを定義
			CellStyle data_CellStyle = workbook.createCellStyle();
			// 罫線指定
			data_CellStyle.setBorderTop(BorderStyle.THIN);
			data_CellStyle.setBorderBottom(BorderStyle.THIN);
			data_CellStyle.setBorderLeft(BorderStyle.THIN);
			data_CellStyle.setBorderRight(BorderStyle.THIN);
			// フォントをセット
			data_CellStyle.setFont(font);
			for (int i = 0; i < table_Data.length; i++) {
				// 行を指定
				row = sheet.createRow(i + 1);
				for (int j = 0; j < table_Data[i].length; j++) {
					// セルを定義
					Cell cell = row.createCell(j);
					String value = table_Data[i][j];
					// セルに値をセット
					if(is_Double(value)){
						cell.setCellValue(Double.parseDouble(value));
					} else {
						cell.setCellValue(value);
					}
					// セルにセルスタイルを適用
					cell.setCellStyle(data_CellStyle);
					// 最後の行のみ
					if (i == table_Data.length - 1) {
						// 列幅設定（オート）
						sheet.autoSizeColumn(j);
					}
				}
			}

	    // ファイル名を指定して保存
			String encodedFilename = URLEncoder.encode(file_Name, "UTF-8");
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	    response.setHeader("Content-Disposition", "attachment;filename=\"" + encodedFilename + "\"");
	    response.setCharacterEncoding("UTF-8");
	    workbook.write(outputStream);
			message += "が完了しました";
			workbook.close();
			message += " workbook を close() しました";
		} catch (IOException e) {
			message += "が正常に完了出来ませんでした";
			___console_Out___(e.getMessage());
		}
		___console_Out___("output_Excel() 終了");
		return message;
	}


	private String with_Now(String head_String) {
		String now = now().replaceAll("[^0-9]", ""); // 現在日時の数字以外を "" に変換
//	String now = now().replaceAll("[^\\d]", "");  ←こちらでもOK
		now = now.substring(0, now.length()-3); // 後ろから3文字を取り除く
		return head_String + now;
	}

	public String now() {
		// 現在日時を取得
		LocalDateTime now = LocalDateTime.now();
		// 表示形式を指定
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS");
		return dateTimeFormatter.format(now);
	}

	private static boolean is_Double(String string) {
		boolean is_Double = true;
		try {
			___console_Out___("String = " + Double.parseDouble(string) + " は Double に変換出来ます");
		} catch (Exception e) {
			___console_Out___("string = " + string + " は Double に変換出来ません");
			is_Double = false;
		}
		return is_Double;
	}

	/** コンソールに String を出力 */
	public static void ___console_Out___(String message) {
		System.out.println(message);
		System.out.println("*");
	}
}
