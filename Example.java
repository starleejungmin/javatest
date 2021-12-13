package Semicon;

import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import Source_Common.Common;

public class Example {

	public static WebDriver GetDriver;
	public static String g_ExcelFilePath = "";
	public static XSSFSheet g_ExcelSheet = null;
	public static int g_RowIndex = 0;
	static XSSFWorkbook xlsxWb = null;

	public static void main(String[] args) {
		Ex();
	}

	public static void Ex() {

		String Excel = "file//Semicon//Part_PD_Global.xlsx";
		Common.SetSelenium();
		Common.Set_DataExcelFile(Excel);
		XSSFSheet sheet = Common.Open_TestData_Seet();
		Common.SetSheet(sheet);

		int rows = sheet.getLastRowNum() + 1;
		
	
	   

		for (int rowIndex = 1; rowIndex < rows; rowIndex++) {
			Common.SetRowIndex(rowIndex);

			String Tobe = Common.Get_Excel_Data(sheet, 4, rowIndex);
			Common.GetDriver.get(Tobe);
			login(Tobe);
			Common.Wait_Page();
			
			
			while(true) {
				if(buttonclick()==false) break;
			}
			
			
			
			String[] contentpath = { ".//li[@class='PD09_related-content-grid-item img-hover-effect']" };
			List<WebElement> contentel = Common.Find_Elements_Xpath(contentpath, 2);

			int col = 5;

			for (int i = 0; i < contentel.size(); i++) {

				String[] titlepath = { ".//div[@class='PD09_related-content-grid-title']" };
				List<WebElement> titleel = Common.Find_Elements_Xpath(titlepath, 2);

				Common.WriteExcel_title(col, i + 1 + "번째 Title");
				Common.WriteExcel(col++, titleel.get(i).getText());

				String[] eyebrowpath = { ".//div[@class='PD09_related-content-grid-eyebrow']" };
				List<WebElement> eybrowel = Common.Find_Elements_Xpath(eyebrowpath, 2);

				Common.WriteExcel_title(col, i + 1 + "번째 eyebrow");
				Common.WriteExcel(col++, eybrowel.get(i).getText());

				String[] chippath = { ".//li[@class='PD09_related-content-grid-item img-hover-effect'][" + (i + 1)+ "]/div[@class='PD09_related-content-grid-tags']/a" };
				List<WebElement> chipel = Common.Find_Elements_Xpath(chippath, 2);

				Common.WriteExcel_title(col, i + 1 + "번째 Count");
				Common.WriteExcel(col++, chipel.size() + "");

				String[] imgpath = {
						".//div[@class='PD09_related-content-grid-desc']/a/figure[@class='PD09_related-content-grid-thum']/img" };
				List<WebElement> imgel = Common.Find_Elements_Xpath(imgpath, 2);

				Common.WriteExcel_title(col, i + 1 + "번째 image");
				Common.WriteExcel(col++, imgel.get(i).getAttribute("src"));

			}

		}

		Common.QuitDriver();
	}
	
	public static boolean buttonclick() {
		Common.Wait(2);
		String[] buttonpath = {".//div[@class='btn-group btn-center']/button"};
		WebElement button = Common.Find_Element_Xpath(buttonpath, 2);
		try {
			button.click();
			Common.Scroll_Element(button);
			return true;
		}
		catch(Exception e) {
			System.out.println("err");
			return false;
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
