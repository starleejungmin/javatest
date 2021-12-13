package Semicon;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import Source_Common.Common;
public class Semicon_Newsroom_New {
    
    
    
    
    public static final int startColumn = 5;
    
    public static ArrayList<String> Spec_Title_List = new ArrayList<String>();
    public static String sComment = "";
    public static Actions action;
    
    public static void main(String[] args)
    {
        
        runPartPD();
        
    }
    
    public static void runPartPD()
    {               
        Common.SetSelenium();
        action = new Actions(Common.GetDriver);
        
        String strTestDataExcel = "file//Semicon//Part_PD_Workflow_List.xlsx";
        
        Common.Set_DataExcelFile(strTestDataExcel);
        XSSFSheet sheet = Common.Open_TestData_Seet(); 
        Common.SetSheet(sheet); 
        
        int rows = sheet.getLastRowNum() + 1;
        for(int i = 1 ; i < rows; i++)
        {           
            Common.SetRowIndex(i); 
            Common.WriteExcel_title(startColumn, "Comment");
            // 엑셀 URL 가져오기 
            String Asis_URL = Common.Get_Excel_Data(sheet, 3, i); 
            String Tobe_URL = Common.Get_Excel_Data(sheet, 4, i); 
    
            Common.GetDriver.get(Asis_URL);
            Common.Wait_Page();
            
            compareSections("As_is");
                        
            Common.GetDriver.get(Tobe_URL);         
            loginAEM(Tobe_URL);
            Common.Wait_Page();
            compareSections("To_be");
            
            Common.WriteExcel(startColumn, sComment);
            sComment = "";
        }
        
        Common.QuitDriver();
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
            findElementInNewsRoom(type+"_Title",1,".//h2[@id='productPartNo']");           
            findElementInNewsRoom(type+"_OverView_Header",3,".//div[@class='overview-header__title']/h2");        
            findElementInNewsRoom(type+"_OverView_Text",5,".//div[@class='overview-header__title']/p");
            
        }
        else {
        	
        	

            
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
    
    
    public static boolean findElementInNewsRoom(String name ,int col, String xPath) {
        try {
            String[] Path = new String[]{xPath};
            WebElement Element = Common.Find_Element_Xpath(Path, 1);        
            try {                   
                if(Common.g_RowIndex<2) Common.WriteExcel_title(startColumn+col, name);
                
                if(name.contains("Title")) Common.WriteExcel(startColumn+col, Element.getText());
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