/*
 * 2021.09.24 DY
 * live에서 breadcrumb 을 찾는다. 
 */

package Semicon;

import java.util.ArrayList;

import javax.activation.CommandMap;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import Source_Common.Common;

public class Semicon_Breadcrumb_Test {

	public static int START_ROW_NUM = 1;

	// 엑셀 행 넘버
	public static int EXCEL_URL = 0;
	public static int EXCEL_BREADCRUMB_RESULT = 7;
	public static int EXCEL_BREADCRUMB_TEST_COMMENT = 4;

	// 엑셀에 저장 되어 있는 MKT PD URL
	public static String g_URL = "";

	// 엑셀에 쓸 커멘트
	public static String g_sComment = "";

	public static void main(String[] args) {

		Breadcrumb_Check();

		/* Common.gmailSend("dykim@wisewires.com", "브레드크럼1 완료", "확인"); */

	}

	public static void Breadcrumb_Check() {
		// 1.엑셀 불러오기
		String strTestDataExcel = "file//Semicon//Breadcrumb_Test.xlsx";

		Common.SetSelenium();

		// 엑셀 세팅
		Common.Set_DataExcelFile(strTestDataExcel);
		XSSFSheet sheet = Common.Open_TestData_Seet();
		Common.SetSheet(sheet);
		/* Setting_Excel_Seet(); */
		/* Setting_Excel_Seet(); */

		// 엑셀에 저장된 URL 리스트 만큼 돌리기
		int rows = sheet.getLastRowNum() + 1;
		// for (int rowIndex = START_ROW_NUM; rowIndex < rows; rowIndex++)
		for (int rowIndex = 1; rowIndex < rows; rowIndex++) {
			/* XSSFCell cell = null; */
			Common.SetRowIndex(rowIndex); // 엑셀에 이미 row 값이 있으니 row를 구지 생성하지 않아도 됨

			String URL = Common.Get_Excel_Data(sheet, 0, rowIndex);
			
			
			System.out.println(URL);
			Common.GetDriver.get(URL);
			System.out.println(URL);
			Common.Wait_Page();
			// 로그인
			loginAEM(URL);
			
	    	
			if(!Common.Check_Error_Page_Semicon(7, Common.GetDriver.getCurrentUrl()))
			{
				
				try {

					/*
					 * .//div//nav[@id='breadcrumb'] <<정상 .//nav[@class='CO09_breadcrumb'] << 정상
					 * .//nav[@id='Breadcrumb'] << 정상 .//nav[@id='cm-semi-breadcrumb'] << 정상
					 */
					((JavascriptExecutor)Common.GetDriver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
					String[] normalpath = { ".//div//nav[@id='breadcrumb']" };
					String[] normalpath2 = { ".//nav[@class='CO09_breadcrumb']" };
					String[] normalpath3 = { ".//nav[@id='Breadcrumb']"};
//					String[] normalpath4 = { ".//nav[@id='cm-semi-breadcrumb']" };
					String[] normalpath5 = {".//nav[@id='breamcrumb']"};
					String[] normalpath6 = {".//nav[@id='breadcrum']"};
					WebElement path1 = Common.Find_Element_Xpath(normalpath, 2);
					WebElement path2 = Common.Find_Element_Xpath(normalpath2, 2);
					WebElement path3 = Common.Find_Element_Xpath(normalpath3, 2);
//					WebElement path4 = Common.Find_Element_Xpath(normalpath4, 2);
					WebElement path5 = Common.Find_Element_Xpath(normalpath5, 2);
					WebElement path6 = Common.Find_Element_Xpath(normalpath6, 2);
					
					
					try {
						if(!path1.getText().isEmpty()) {
							System.out.println("브래드크럼 정상");
							Common.WriteExcel(1, "O");
							
							
						}
						
					}catch (Exception e) {
						Common.WriteExcel(1, "X");
						
						
					}
					
//					--------------------------------------------------------------------------------------
				
					
					try {
						if(!path2.getText().isEmpty()) {
							System.out.println("브래드크럼 정상");
							Common.WriteExcel(2, "O");
							
						}
					}catch (Exception e) {
						Common.WriteExcel(2, "X");
						
					}
					
//					--------------------------------------------------------------------------------------
					
					System.out.println("2번실행");
					try {
						if(!path3.getText().isEmpty()) {
							System.out.println("브래드크럼 정상");
							Common.WriteExcel(3, "O");
						
							
						}
					}catch (Exception e) {
						Common.WriteExcel(3, "X");
						
					}
					
//					--------------------------------------------------------------------------------------
//					
//					try {
//						if(!path4.getText().isEmpty()) {
//							System.out.println("브래드크럼 정상");
//							Common.WriteExcel(4, "O");
//							
//						}
//					}catch (Exception e) {
//						Common.WriteExcel(4, "X");
//						
//
//					}
					
//					--------------------------------------------------------------------------------------
					
					try {
						if(!path5.getText().isEmpty()) {
							System.out.println("브래드크럼 정상");
							Common.WriteExcel(5, "O");
						
						}
					}catch (Exception e) {
						Common.WriteExcel(5, "X");
						

					}
//					--------------------------------------------------------------------------------------		
					
					try {
						if(!path6.getText().isEmpty()) {
							System.out.println("브래드크럼 정상");
							Common.WriteExcel(6, "O");
							
						}
					}catch (Exception e) {
						Common.WriteExcel(6, "X");
						

					}

				}catch (Exception e) {
					
					System.out.println("err");
					Common.WriteExcel(7, "메뉴얼 검증 필요");
					}
			}
				
				
			}

        	



		}

	

	// 엑셀 의 1행 세팅
	public static void Setting_Excel_Seet() {
		Common.CreateRow(0);
		Common.WriteExcel(EXCEL_URL, "URL");
		/* Common.WriteExcel(EXCEL_URL_CODE,"URL_CODE"); */
		Common.WriteExcel(EXCEL_BREADCRUMB_RESULT, "Test Result");
		Common.WriteExcel(EXCEL_BREADCRUMB_TEST_COMMENT, "Test Comment");
	}

	public static void loginAEM(String url) {
		try {
			System.out.println("loginAEM");
			Common.g_CurrentURL = url;

			// 로그인
			if (Common.GetDriver.getCurrentUrl().contains("login.html")) {
				System.out.println("로그인");
				Common.LogIn();
			}
		} catch (Exception e) {
			System.err.println("로그인 에러");
		}
	}

}