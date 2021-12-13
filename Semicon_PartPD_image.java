package Semicon;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;


import Source_Common.Common;
public class Semicon_PartPD_image {
	
	public static final int startColumn = 5;
	
	public static ArrayList<String> Spec_Title_List = new ArrayList<String>();
	public static String sComment = "";
	public static Actions action;
	
	public static void main(String[] args)
	{
		
		runPartPD("test","kvimage");
		
	}
	
	public static void runPartPD(String list, String testType)
	{				
		Common.SetSelenium();
		action = new Actions(Common.GetDriver);
		
		String strTestDataExcel = "file//Semicon//Part_PD//Part_PD_1202_image.xlsx";
		
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
	
    		if(testType !="kvimage") { // ASIS 테스트 안하는 경우
    			Common.GetDriver.get(Asis_URL);
    			Common.Wait_Page();
    			
    			if(testType=="sections") compareSections("As_is");		
    			else if(testType=="kvimage") System.out.println("As_is_SKIP");
    			else get_Spec_Data(i, "As_is");
    		}
			Common.GetDriver.get(Tobe_URL);			
			loginAEM(Tobe_URL);
			Common.Wait_Page();
			if(testType=="sections") compareSections("To_be");
			else if(testType=="kvimage") checkKVImage();
			else get_Spec_Data(i, "To_be");			
			
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
    
    public static void checkKVImage() {
    	
		// Status Code 얻어오기	
		int statusCode = Common.getHttpCode(Common.GetDriver.getCurrentUrl()); 
		System.out.println("statusCode: "+statusCode);
		if(statusCode!=200) {
			sComment+="Page: "+statusCode+"/";
			return;
		}
    	
    	findElementInPartPD("image", 1, ".//section[@id='pd-semi-hero']//picture//img");
    	
    }
    public static void compareSections(String type) {
    	
    	System.out.println(Common.GetDriver.getCurrentUrl());
		System.out.println(type+"시작");
    	
		// Status Code 얻어오기	
		int statusCode = Common.getHttpCode(Common.GetDriver.getCurrentUrl()); 
		System.out.println("statusCode: "+statusCode);
		if(statusCode!=200) {
			sComment+=type+": "+statusCode+"/";
			return;
		}
		
		if(type.contains("As")) {
			findElementInPartPD(type+"_Title",1,".//h2[@id='productPartNo']");
			
			findElementInPartPD(type+"_OverView_Header",3,".//div[@class='overview-header__title']/h2");		
			findElementInPartPD(type+"_OverView_Text",5,".//div[@class='overview-header__title']/p");
			
			findElementInPartPD(type+"_Spec_Table",7,".//table[@id='product-table-list']");
			
			findElementInPartPD(type+"_Related_resources",9,".//div[@id='relatedResources']");
			
			findElementInPartPD(type+"_Related_insights",11,".//section[@id='pd-semi-related-insights']");
			
		}
		else {
			
			findElementInPartPD(type+"_Title",2,".//h1[@class='PD02_hero__headline-text']");
			
			findElementInPartPD(type+"_OverView_Header",4,".//dfn[@class='PD03_summary--eyebrow']");
			findElementInPartPD(type+"_OverView_Text",6,".//p[@class='PD03_summary--description-text']");
			
			findElementInPartPD(type+"_Spec_Table",8,".//ul[@class='PD06_spec__list']");
			
			findElementInPartPD(type+"_Related_resources",10,".//section[@id='pd-semi-related-resources']");
			
			findElementInPartPD(type+"_Related_Contents",12,".//section[@id='pd-semi-related-content']");
			
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
	
	
	public static boolean findElementInPartPD(String name ,int col, String xPath) {
		try {
			String[] Path = new String[]{xPath};
			WebElement Element = Common.Find_Element_Xpath(Path, 1);		
			try {					
				if(Common.g_RowIndex<2) Common.WriteExcel_title(startColumn+col, name);
				
				if(name.contains("Title")) Common.WriteExcel(startColumn+col, Element.getText()); // Title이 포함된 경우에 gettext
				else if(name.contains("image")) { // case : IMAGE
					try {
						String imageURL = Element.getAttribute("src");
						Common.WriteExcel(startColumn+col, imageURL);
						System.out.println("URL: "+imageURL);
						if(imageURL!=null) {
							int statusCode = Common.getHttpCode(imageURL);
							Common.WriteExcel(startColumn+col+1, statusCode+""); 				
							System.out.println("statusCode: "+statusCode);
						}
						else {
							System.err.println("이미지 없음");
							Common.WriteExcel(startColumn+col, "N/A");
						}
						
					}catch (Exception e) {
						System.err.println("이미지 없음2");
						Common.WriteExcel(startColumn+col, "n/a");
					}										
					
				}
				else {
					if(Element!=null) Common.WriteExcel(startColumn+col, "O");
					else Common.WriteExcel(startColumn+col, "X");
				}
				
				return true;
			}
			catch (Exception e) {
				Common.WriteExcel_title(startColumn+col, name);
				Common.WriteExcel(startColumn+col, "X");
				return false;
			}
			
		}catch (Exception e) {
			sComment+= "Unknown Error";
			return false;
		}
				
		
	}        
    
}