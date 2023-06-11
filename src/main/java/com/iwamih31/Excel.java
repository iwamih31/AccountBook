package com.iwamih31;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Excel {

	MissingCellPolicy cellPolicy = MissingCellPolicy.CREATE_NULL_AS_BLANK;


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
