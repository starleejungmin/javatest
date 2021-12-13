package Semicon;

import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.WebElement;

import Source_Common.Common;

public class test {

	public static void main(String[] args) {
		
		test();
	
	}
	
	public static void test() {
		
		String path ="file//Semicon//Test.xlsx";
		Common.SetSelenium();
		Common.Set_DataExcelFile(path);
		XSSFSheet sheet = Common.Open_TestData_Seet();
		Common.SetSheet(sheet);
		
		int row = sheet.getLastRowNum()+1;
		
		for(int rows =1; rows < row; rows ++) {
			String URL = Common.Get_Excel_Data(sheet, 0, rows);
			Common.GetDriver.get(URL);
			System.out.println(URL);
			login(URL);
			
			String[] path2 = {".//div[@class='CO07_lnb_static--list__child']/div/a"};

			
			List<WebElement> el = Common.Find_Elements_Xpath(path2,2);
			
			System.out.println(el);
			System.out.println(el.get(1));
			System.out.println("el 엘리먼트 하나 체크");
			
			try {
				
				for(int a =1 ; a < el.size() ; a++) {
					System.out.println(a);
					System.out.println(el.size());
					try {
						if(el.get(a) != null) 
							el.get(a).click();
						
					}catch(Exception e) {
						System.out.println(" 엘리먼트 없음.");
					}
			}
				
		}
			catch(Exception e) {
				System.out.println("LNB 영역이 없음.");
			}
		}
		}
		
	
    public static void Click_Element(WebElement el) 
    {
    	System.out.println("------------"+ ((WebElement) el).getText() +" Click");	
    	try {
    		el.click();
   
    	}catch(Exception e) {	
    		System.err.println("Click_Element : Click 실패");	
    	}	
    }
	
	
	public static void login(String url) {
		try {
			Common.g_CurrentURL =url;
			
			if(Common.GetDriver.getCurrentUrl().contains("login.html")) {
				Common.LogIn();
			}
		}catch(Exception e){
			
		}
	}

}


