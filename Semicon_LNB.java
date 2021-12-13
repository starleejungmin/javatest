package Semicon;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import Source_Common.Common;

public class Semicon_LNB {
	
	
	public static final int startColumn = 2;
	
	public static ArrayList<String> Spec_Title_List = new ArrayList<String>();
	public static String sComment = "";
	public static Actions action;
	
	
	public static void main(String[] args)
	{
		runLNBTest();
	}
	
	public static void runLNBTest()
	{				
		Common.SetSelenium();
		action = new Actions(Common.GetDriver);
		
		String strTestDataExcel = "file//Semicon//LNB.xlsx";
		
		Common.Set_DataExcelFile(strTestDataExcel);
		XSSFSheet sheet = Common.Open_TestData_Seet(); 
		Common.SetSheet(sheet); 
		
		int rows = sheet.getLastRowNum() + 1;
		for(int i = 1 ; i < rows; i++)
		{			
			Common.SetRowIndex(i); 
			Common.WriteExcel_title(startColumn, "Comment");
    		// 엑셀 URL 가져오기  
    		String Tobe_URL = Common.Get_Excel_Data(sheet, 0, i); // 
						
			Common.GetDriver.get(Tobe_URL);			
			loginAEM(Tobe_URL);
			Common.Wait_Page();
			
			checkLNB();
			
			Common.WriteExcel(startColumn, sComment);
			sComment = "";
		}
		
		Common.QuitDriver();
	}
	public static void checkLNB() {		
		
		int currentCol = 3;
		
		findElementInLNB("LNB Type", currentCol++, ".//div[contains(@class,'lnb')]/nav");    //5
		findElementInLNB("LNB Title", currentCol++, ".//div[contains(@class,'lnb')]/nav//dfn");	 //6	
				
		
		String[] menuPath = {".//div[@role='list']/div/a"};
		
		List<WebElement> menuElement = 	Common.Find_Elements_Xpath(menuPath, 1);
		
		Common.WriteExcel_title(currentCol, "Menu Count"); //7
		Common.WriteExcel(currentCol++, ""+menuElement.size()); //7
		
		for(int i = 0 ; i< menuElement.size() ; i++){						
			
			Common.WriteExcel_title(currentCol, "Menu"+i+"_Name");
			Common.WriteExcel(currentCol++, menuElement.get(i).getText());
			
			Common.WriteExcel_title(currentCol, "Menu"+i+"_Link");
			Common.WriteExcel(currentCol++, menuElement.get(i).getAttribute("href"));
			
			
			Common.WriteExcel_title(currentCol, "Menu"+i+"_Position");
			
			if(menuElement.get(i).getAttribute("href").contains("#")) {
				menuElement.get(i).click();
				Common.Wait(1);
				Common.WriteExcel(currentCol++, getCurrentPosition(Common.GetDriver)+"");
			}
			else {				
				Common.WriteExcel(currentCol++, "N/A");
			}
			
		}
		
		
		
		
	}
    
	public static void loginAEM(String url) {
  		try {
	  		System.out.println("loginAEM");
	  		Common.g_CurrentURL = url;
	  		
	  		// 로그인 
	    	if(Common.GetDriver.getCurrentUrl().contains("login.html"))
	    	{
	    		System.out.println("로그인");
	    		Common.LogIn(); 
	    	}
  		}
  		catch (Exception e) {
  			System.err.println("로그인 에러");
		}
	}
	
	
	public static long getCurrentPosition(WebDriver driver) {
		JavascriptExecutor javaScriptExe = (JavascriptExecutor)driver;
		
		return (Long) javaScriptExe.executeScript("return window.pageYOffset;");
	}
	
	public static boolean findElementInLNB(String name ,int col, String xPath) {
		try {
			String[] Path = new String[]{xPath};
			WebElement Element = Common.Find_Element_Xpath(Path, 1);		
			try {					
				if(Common.g_RowIndex<2) Common.WriteExcel_title(col, name);
				
				if(name.contains("Title")) Common.WriteExcel(col, Element.getText());
				else if(name.contains("Type")) Common.WriteExcel(col, Element.getAttribute("class"));
				else {
					if(Element!=null) Common.WriteExcel(col, "O");
					else Common.WriteExcel(col, "X");
				}
				
				return true;
			}
			catch (Exception e) {
				Common.WriteExcel_title(col, name);
				Common.WriteExcel(col, "X");
				return false;
			}
			
		}catch (Exception e) {
			sComment+= "Unknown Error";
			return false;
		}
				
		
	}        
    
}