package Semicon;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.WebElement;

import Source_Common.Common;

public class Semicon_Section_Test {


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		specs();

	}

	public static void specs() {

		String Excle = "file//Semicon//Part_PD_Global.xlsx";
		Common.SetSelenium();
		Common.Set_DataExcelFile(Excle);
		XSSFSheet sheet = Common.Open_TestData_Seet();
		Common.SetSheet(sheet);

		int rows = sheet.getLastRowNum() + 1;

		for (int a = 1; a < rows; a++) {

			Common.SetRowIndex(a);

			String Tobe = Common.Get_Excel_Data(sheet, 4, a);
			Common.GetDriver.get(Tobe);
			login(Tobe);
			Common.Wait_Page();


			try {
				String[] KVpath = { ".//div[@class='PD02_hero__wrap']" };
				String[] overviewpath = {
						".//div[@class='pd-semi-summary aem-GridColumn aem-GridColumn--default--12']" };
				String[] specpath = { ".//div[@class='pd-semi-spec aem-GridColumn aem-GridColumn--default--12']" };
				String[] resourcespath = { ".//section[@id='pd-semi-related-resources']" };
				String[] contentspath = {
						".//div[@class='pd-semi-related-content aem-GridColumn aem-GridColumn--default--12']" };
				;

				WebElement kv = Common.Find_Element_Xpath(KVpath, 2);
				WebElement over = Common.Find_Element_Xpath(overviewpath, 2);
				WebElement spec = Common.Find_Element_Xpath(specpath, 2);
				WebElement resource = Common.Find_Element_Xpath(resourcespath, 2);
				WebElement contents = Common.Find_Element_Xpath(contentspath, 2);

				try {

					if (kv !=null) {
						Common.WriteExcel_title(5, "LV영역");
						Common.WriteExcel(5, "O");
					}
					else
						Common.WriteExcel(5, "x");

				} catch (Exception e) {
					Common.WriteExcel(5, "ERR");

				}
				try {

					if (over != null) {
						Common.WriteExcel_title(6, "Over영역");
						Common.WriteExcel(6, "O");
					}
					else
						Common.WriteExcel(6, "X");

				} catch (Exception e) {
					Common.WriteExcel(6, "ERR");

				}
				try {

					if (spec != null) {
						Common.WriteExcel_title(7, "Spec영역");
						Common.WriteExcel(7, "O");
					}
					else
						Common.WriteExcel(7, "X");
						

				} catch (Exception e) {
					Common.WriteExcel(7, "ERR");

				}
				try {

					if (resource != null) {
						Common.WriteExcel_title(8, "resource영역");
						Common.WriteExcel(8, "O");
					}
					else
						Common.WriteExcel(8, "X");

				} catch (Exception e) {
					Common.WriteExcel(8, "ERR");

				}
				try {

					if (contents != null) {
						Common.WriteExcel_title(9, "contents영역");
						Common.WriteExcel(9, "O");
					}
					else
						Common.WriteExcel(9, "X");

				} catch (Exception e) {
					Common.WriteExcel(9, "ERR");

				}

			} catch (Exception e) {
				Common.WriteExcel_title(10, "Comment");
				Common.WriteExcel(10, "404");
			}

		}

	}

	public static void login(String url) {

		try {
			Common.g_CurrentURL = url;

			if (Common.GetDriver.getCurrentUrl().contains("login.html")) {
				Common.LogIn();
			}

		} catch (Exception e) {

		}
	}

}
