package coursera

import com.splunk.Job
import com.splunk.JobArgs
import com.splunk.ResultsReaderXml
import com.splunk.ServiceArgs
import com.splunk.Service

class SplunkDemo {

	static main(args) {
			
		def loginArgs = new ServiceArgs();
		loginArgs.setHost("localhost");
		loginArgs.setPort(8089); //port is important - not 8000 as in web UI
		/*
		 * Set user and pwd if required!
		 * loginArgs.setUsername("")
		 * loginArgs.setPassword("")
		 */
		
		def service = Service.connect(loginArgs);

		InputStream results_oneshot =  service.oneshotSearch("search source=buy-clicks.csv | stats avg(price) by userId");
		try {
			def resultsReader = new ResultsReaderXml(results_oneshot);
			def first = 0
			resultsReader.each { event ->

				if(first==0) { //if first time then print a header with column names
					event.each {						
						print "$it.key\t"
						
					}
					print "\n"
				}
				
				event.each {
					
					print "$it.value\t"
					
				}
				print "\n"
				first++
			}
			resultsReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}