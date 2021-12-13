package Semicon;

import java.util.List;

import javax.swing.Action;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.WebDriver;
import Source_Common.Common;

public class Example2 {

	public static void main(String[] args) {
		ex();

	}

	public static void ex() {

		
		String Excel = "file//Semicon//Part_PD_Global.xlsx";
		Common.SetSelenium();
		Common.Set_DataExcelFile(Excel);
		XSSFSheet sheet = Common.Open_TestData_Seet();
		Common.SetSheet(sheet);

		int rows = sheet.getLastRowNum() + 1;
		Actions action = new Actions(Common.GetDriver);
		for (int a = 1; a < rows; a++) {
			Common.SetRowIndex(a);

			String Tobe = Common.Get_Excel_Data(sheet, 4, a);
			Common.GetDriver.get(Tobe);
			login(Tobe);
			Common.Wait_Page();

			String[] LNBpath = { ".//div[@class='PD01_lnb--list__child']/div[@role='listitem']" };
			List<WebElement> LNB = Common.Find_Elements_Xpath(LNBpath, 2);
			
			int col = 5;
			for (int b = 0; b < LNB.size(); b++) {

				System.out.println(b);
				Common.WriteExcel_title(col, "LNB영역" + b);
				Common.WriteExcel(col++, LNB.get(b).getText());
				
				String[] hrefpath = { ".//div[@class='PD01_lnb--list__child']/div[@role='listitem']/a" };
				List<WebElement> href = Common.Find_Elements_Xpath(hrefpath, 2);
				
				Common.WriteExcel_title(col, "URL" + b);
				Common.WriteExcel(col++, href.get(b).getAttribute("href"));
				
				String t = href.get(b).getAttribute("href");		
				int idx = t.indexOf("#");
				String ac = href.get(b).getAttribute("href").substring(idx+1);

				try {
					String[] targetpath = { ".//section[@id='"+ac+"']"};
					WebElement target = Common.Find_Element_Xpath(targetpath, 2);
					action.moveToElement(target);

					Common.WriteExcel_title(col, "Check" + b);
					Common.WriteExcel(col++,"O");
					
				} catch (Exception e) {
					Common.WriteExcel_title(col, "Check" + b);
					Common.WriteExcel(col++,"X");
				}

			}

		}
		Common.QuitDriver();

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
