package Semicon;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import Source_Common.Common;
public class Semicon_test {
	
	
	/*
	 *  As-is 
	 *  큰거:		".//table[@id='product-table-list']"
	 *  제목:		".//table[@id='product-table-list']//tr/th"
	 *  데이터:	".//table[@id='product-table-list']//tr/td"
	 * 
	 *  To-be
	 *  큰거:		".//ul[@class='PD06_spec__list']"
	 *  제목:		".//ul[@class='PD06_spec__list']//li/dl/dt" <<잘안될수도있음
	 *  데이터:	".//ul[@class='PD06_spec__list']//li/dl/dd"
	 */
	
	//1111 기타 수정있음
	
	
	public static final int startColumn = 5;
	
	public static ArrayList<String> Spec_Title_List = new ArrayList<String>();
	public static String sComment = "";
	public static Actions action;
	
	public static void main(String[] args)
	{
//		String[] list = {"display-ic", "dram", "estorage", "mcp", "power-ic", "security-solution","ssd","All"};
//		
//		for(int i = 0 ; i < 8; i ++ ) {
//			runPartPD(list[i].toString());
//			Spec_Title_List.clear();
//		}
		
		runPartPD("test");
		
	}
	
	public static void runPartPD(String list)
	{				
		Common.SetSelenium();
		action = new Actions(Common.GetDriver);
		
//		String strTestDataExcel = "file//Semicon//Part_PD//Part_PD_"+list+".xlsx";
		String strTestDataExcel = "file//Semicon//Part_PD_Global.xlsx";
		
		Common.Set_DataExcelFile(strTestDataExcel);
		XSSFSheet sheet = Common.Open_TestData_Seet(); 
		Common.SetSheet(sheet); 
		
		int rows = sheet.getLastRowNum() + 1;
		for(int i = 1 ; i < rows; i++)
		{			
			Common.SetRowIndex(i); 
			Common.WriteExcel_title(startColumn, "Comment");
    		// 엑셀 URL 가져오기 
    		String Asis_URL = Common.Get_Excel_Data(sheet, 3, i); //1111 엑셀 소스 변경함 
    		String Tobe_URL = Common.Get_Excel_Data(sheet, 4, i); // 
    		
			Common.GetDriver.get(Asis_URL);
			Common.Wait_Page();
			get_Spec_Data(i, "As_is");
						
			Common.GetDriver.get(Tobe_URL);			
			loginAEM(Tobe_URL);
			Common.Wait_Page();
			get_Spec_Data(i, "To_be");
			
			Common.WriteExcel(startColumn, sComment);
			sComment = "";
		}
		
		Common.QuitDriver();
	}

    
    public static void get_Spec_Data(int row, String type) 
    {
    	System.out.println(Common.GetDriver.getCurrentUrl());
		System.out.println(type+"시작");
    	
		// Status Code 얻어오기	
		int statusCode = Common.getHttpCode(Common.GetDriver.getCurrentUrl()); 
		System.out.println("statusCode: "+statusCode);
		if(statusCode!=200) {
			sComment+=type+": "+statusCode+"/";
			return;
		}
		
    	List<WebElement> title = null;
    	List<WebElement> contents = null;
    	
    	String[] SpecPath = null; 
    	List<WebElement> Spec = null;
    	
    	if(type.equals("As_is")) {        		
    		SpecPath = new String[]{".//table[@id='product-table-list']"};
    		Spec = Common.Find_Elements_Xpath(SpecPath, 1);
        	title = Common.GetDriver.findElements(By.xpath(".//table[@id='product-table-list']//tr/th"));
        	contents = Common.GetDriver.findElements(By.xpath(".//table[@id='product-table-list']//tr/td"));        	
    	}
    	else if(type.equals("To_be")) {
    		SpecPath = new String[]{".//ul[@class='PD06_spec__list']"};
    		Spec = Common.Find_Elements_Xpath(SpecPath, 1);
    		title = Common.GetDriver.findElements(By.xpath(".//ul[@class='PD06_spec__list']//li/dl/dt"));
			contents = Common.GetDriver.findElements(By.xpath(".//ul[@class='PD06_spec__list']//li/dl/dd"));
    	}
    	
	  	//Spec 영역 체크 
		try {
			action.moveToElement(Spec.get(0));
			Common.Wait(1);
			System.out.println("move spec");
			System.out.println(type+" Spec 영역 있음");    
		}catch (Exception e) {
			sComment += ""+type+ " Spec Area Error";      			
			System.out.println(type+" Spec 영역 없음");
			Common.WriteExcel(startColumn, sComment);
			return;
		}    			
	
		if(type.equals("To_be")) {    			
    		try {
    			// View more 클릭
        		String[] viewMorePath = {".//div[contains(@class,'spec')]//div[@class='btn-group btn-center']/button"}; 
    			WebElement viewMoreEl = Common.Find_Element_Xpath(viewMorePath, 1);
    			viewMoreEl.sendKeys(Keys.ENTER);    			
    			Common.Wait(1);    			
    			System.out.println("View more 클릭");

    		}catch (Exception e) {
    			System.out.println("View more X");
			}
		}

		//Spec 영역 내부 세부 스펙
    	for(int i = 0 ; i < title.size(); i ++ ) {  
    		String t = type+"_"+title.get(i).getText(); //    <<제목 
    		String c = contents.get(i).getText(); // <<내용
    		if(title.get(i).getText().length()>1) { //빈테이블 방지
    			int result = matchTitle(Spec_Title_List,t);
        		if(result == -1) { // 매칭이 안되면, 
        			Spec_Title_List.add(t);
        			
        			Common.WriteExcel_title(startColumn+Spec_Title_List.size(), t);
        			Common.WriteExcel(startColumn+Spec_Title_List.size(), c);
        			System.out.println("NewData");
        			System.out.println(t+"/"+c);
        		}
        		else { //매칭이 되면 
        			System.out.println("Matched");
        			System.out.println(t+"/"+c);
        			Common.WriteExcel(startColumn+result+1, c);
        		}
    		}
    		
    	}
    	
    }
    
    public static int matchTitle(ArrayList<String> array, String data) {    	
    	for(int j = 0 ; j < array.size(); j++) {
			if(array.get(j).matches(data)) {				
				return j;
			}
		}
    	return -1;
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
    
    
    
}