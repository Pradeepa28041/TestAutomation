package runner;

//Only extent report  is present with "spark-config" to set theme,title,report name etc in the extent report generated
//import java.text.SimpleDateFormat;
//import java.util.Date;
import org.junit.AfterClass;
import common.WebBrowser;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.junit.Cucumber;
//import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.CucumberOptions;

import org.junit.BeforeClass;
//import com.cucumber.listener.ExtentProperties;

import org.junit.runner.RunWith;

//import cucumber.api.CucumberOptions;
//import cucumber.api.junit.Cucumber;
import com.github.mkolisnyk.cucumber.runner.ExtendedCucumber;
import com.github.mkolisnyk.cucumber.runner.ExtendedCucumberOptions;
//import com.github.mkolisnyk.cucumber.runner.RetryAcceptance;
import common.Hooks;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Date;
import java.util.HashMap;

//@RunWith(ExtendedCucumber.class)
@RunWith(Cucumber.class)
/*
 * @ExtendedCucumberOptions( jsonReport =
 * "target/cucumber-reports/CucumberTestReport.json", retryCount = 0,
 * detailedReport = true, detailedAggregatedReport = true, overviewReport =
 * true, coverageReport = true, jsonUsageReport =
 * "target/cucumber-reports/cucumber-usage.json", usageReport = false, toPDF =
 * true, excludeCoverageTags = {"@flaky" }, includeCoverageTags = {"@passed" },
 * outputFolder = "target/cucumber-reports/extended-report")
 */
@CucumberOptions(features = "src/test/java/features", glue = { "stepdefinitions", "common" },
//        tags = "@test002",
       
		// plugin = {"pretty", "html:out"}
		// plugin =
		// {"ru.yandex.qatools.allure.cucumberjvm.AllureReporter","com.cucumber.listener.ExtentCucumberFormatter:"}
		// features={"."}
//		plugin = { "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:", "common.StepListener"}
plugin = {
        "pretty",                                     // Prints Gherkin steps in the console
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",  // Extent Report
        "junit:target/cucumber-reports/Cucumber.xml"  // JUnit XML Report
})
public class TestRunner {
	public static int retries = 0;

	/*
	 * @BeforeClass public static void setup() {
	 * 
	 * String reportPath=System.getProperty("ReportPath");
	 * 
	 * if(reportPath != null && !reportPath.isEmpty()) {
	 * System.out.println("Report Path is-----------"+reportPath); ExtentProperties
	 * extentProperties = ExtentProperties.INSTANCE;
	 * extentProperties.setReportPath(reportPath); } }
	 */

	@AfterClass
	public static void tearDown() {

		if (WebBrowser.isBrowserOpened()) {
			WebBrowser.closeBrowserInstance();

		}
		if (common.AddLogoToPdf.projectName.toLowerCase().contains("karnataka")) {
			try {
				common.AddLogoToPdf.addLogoToPdf();
			} catch (Exception e) {
				System.out.println("new pdf is not modified");
			}
		}try {
	        File xmlFile = new File("target/cucumber-reports/Cucumber.xml");
	        File newxmlFile = new File(Hooks.lastModified+"/Testmoreport.xml");
	        
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        Document doc = builder.parse(xmlFile);

	        // Get all <testcase> elements
	        NodeList testCases = doc.getElementsByTagName("testcase");

	        // Map containing tags captured during execution
	        //Hooks hks= new Hooks();
	        HashMap<String, String> scenarioTags = Hooks.scenarioTags;

	        for (int i = 0; i < testCases.getLength(); i++) {
	            Element testCase = (Element) testCases.item(i);

	            // Get the scenario name from the test case
	            String scenarioName = testCase.getAttribute("name");

	            // Get dynamic tags for the scenario from the map
	            String tags = scenarioTags.getOrDefault(scenarioName, "No Tags");

	            // Add a <properties> element
	            Element properties = doc.createElement("properties");

	            // Add a <property> for the tags
	            Element property = doc.createElement("property");
	            property.setAttribute("name", "tested-item-id");
	            property.setAttribute("value", tags);
	            property.setAttribute("status", tags);
	            properties.appendChild(property);

	            // Append <properties> to the <testcase>
	            testCase.appendChild(properties);
	        }

	        // Write the updated XML back to file
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
	        DOMSource source = new DOMSource(doc);
	        StreamResult result = new StreamResult(newxmlFile);
	        transformer.transform(source, result);

	        System.out.println("************************Dynamic tags added to JUnit XML file!");	
	       // Report.excelReport(Hooks.lastModified+"/ExtentReports/SparkReport/spark.html", Hooks.lastModified+"/ketryx.xml");
//	        System.out.println("**********************"+Hooks.EnableKetryxIntegration);
//	        if(Hooks.EnableKetryxIntegration.contains("True"))
//			{
//	        	System.out.println("**************************");
//			KetryxUtil.ketryxintegration(Hooks.lastModified+"/Excelreport.xml");
//			System.out.println("**************************1");
//			}    
		}
	    
	    catch(Exception e)
	    {
	    	System.out.println(e);
	    }
		
		 try {
	        // Setting the environment variable
	        String setEnvCommand = "set TESTMO_TOKEN=testmo_api_eyJpdiI6InRUT3F0eHF3S0t3UDlTYlBZYVJtelE9PSIsInZhbHVlIjoiU0hHc0h5K29MNW9pRG1aYU1tUHpVY0xXOWRaeVl1cGRZSWp2a2RSNVZPbz0iLCJtYWMiOiJhZGRhMmE2ZmQyNzY5MjNjZmJlYjRhMjc5ZjBhYTA0MmQyNzRmN2E4YTQxMWY1NGY5OTkyNjU2MDg5ODIyZmJlIiwidGFnIjoiIn0=";
	        ;
	    
	        String filePath = Hooks.lastModified+"/Testmoreport.xml";  // Replace with the actual file path
	        String testmoCommand = String.format("testmo automation:run:submit --instance https://kbdemo.testmo.net --project-id 1 --name \"Test run %s\" --source \"frontend\" --results \"%s\"", generateTimestamp(), filePath);


	  
	        System.out.println("testmoCommand :" + testmoCommand);

	        // Combine the commands into one
	        String combinedCommand = "cmd.exe /c \"" + setEnvCommand + " && " + testmoCommand + "\"";

	        // Run the combined command
	        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", combinedCommand);
	        Process process = processBuilder.start();

	        // Capture the output and errors
	        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

	        String line;
	        // Log the output from the command
	        while ((line = reader.readLine()) != null) {
	            LogMessage("Command Output: " + line);
	        }

	        // Log the errors (if any)
	        while ((line = errorReader.readLine()) != null) {
	            LogMessage("Command Errors: " + line);
	        }

	        // Wait for the process to finish
	        int exitCode = process.waitFor();
	        LogMessage("Process exited with code: " + exitCode);

	    } catch (Exception ex) {
	        System.err.println("Error executing batch command: " + ex.getMessage());
	    }
	}
	
		 public static String generateTimestamp () {
	    // Get the current time and format it as YYYYMMDD_HHMMSS
	    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	    return sdf.format(new Date());
	}

	 public static void LogMessage(String message) {
	    // You can replace this with any logging mechanism you use, e.g., Log4j, System.out, etc.
	    System.out.println(message);  // Example: print to console
		
	}



	

}
