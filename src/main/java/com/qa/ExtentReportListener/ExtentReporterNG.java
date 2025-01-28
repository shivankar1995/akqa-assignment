package com.qa.ExtentReportListener;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.crm.qa.base.TestBase;
import com.crm.qa.util.TestUtil;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

public class ExtentReporterNG extends TestBase implements IReporter {

	private ExtentReports extent;

	// Use a static WebDriver instance

	// Default constructor required by TestNG
	public ExtentReporterNG() {}

	@SneakyThrows
    @Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
		// Initialize the ExtentReports instance with ExtentSparkReporter
		ExtentSparkReporter sparkReporter = new ExtentSparkReporter(outputDirectory + File.separator + "Extent.html");
		sparkReporter.config().setDocumentTitle("Test Report");
		sparkReporter.config().setReportName("Automation Results");

		extent = new ExtentReports();
		extent.attachReporter(sparkReporter);

		// Iterate through the suites and results
		for (ISuite suite : suites) {
			Map<String, ISuiteResult> result = suite.getResults();

			for (ISuiteResult r : result.values()) {
				ITestContext context = r.getTestContext();

				buildTestNodes(context.getPassedTests(), Status.PASS);
				buildTestNodes(context.getFailedTests(), Status.FAIL);
				buildTestNodes(context.getSkippedTests(), Status.SKIP);
			}
		}

		// Finalize the report
		extent.flush();
	}

	private void buildTestNodes(IResultMap tests, Status status) throws IOException {
		if (tests.size() > 0) {
			for (ITestResult result : tests.getAllResults()) {
				ExtentTest test = extent.createTest(result.getMethod().getMethodName());

				test.getModel().setStartTime(getTime(result.getStartMillis()));
				test.getModel().setEndTime(getTime(result.getEndMillis()));

				for (String group : result.getMethod().getGroups()) {
					test.assignCategory(group);
				}

				if (result.getThrowable() != null) {
					// Log the exception message if there's an error
					test.log(status, result.getThrowable());

					// Attach the screenshot on failure
					if (status.equals(Status.FAIL)) {
						String screenshotPath = TestUtil.takeScreenshotAtEndOfTest();
						test.addScreenCaptureFromPath(screenshotPath);
					}
				} else {
					test.log(status, "Test " + status.toString().toLowerCase() + "ed");
				}
			}
		}
	}

	private Date getTime(long millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}
}
