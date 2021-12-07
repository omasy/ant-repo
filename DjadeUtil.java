package system.xmlprocess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang.ArrayUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class DjadeUtil {
	// SETTING INSTANCE VARIABLE
	private static final int BUFFER_SIZE=4096;
	public String DumpReturns;
	List<String> DataLists=new ArrayList<String>();
	List<String> PathLists=new ArrayList<String>();

	// SETTING A DEFAULT CONSTRUCTOR FOR THIS CLASS
	public DjadeUtil(){};

	
/************************** CREATING THE MAXIMUM VALUE METHOD ****************************/
public int max(int[] array){
	int max=array[0]; // Initiating max value
	// Lets loop
	for(int i=1; i<array.length; i++){
		if(array[i]>max){
			max=array[i];
		}
	} // End of loop
	// Now lets return
	return max;
}



/************************* CREATING THE MINIMUM VALUE METHOD ****************************/
public int min(int[] array){
	int min=array[0]; // Initiating min value
	// Lets loop
	for(int i=1; i<array.length; i++){
		if(array[i]<min){
			min=array[i];
		}
	} // End of loop
	// Now lets return
	return min;
}



/*************************** CREATING THE RANGE VALUE METHOD ******************************/
public String range(int[] array, int scale){
	// Here we start constructing the range system
	String range="";
	double ratio=0.0;
	double equiv=0.0;
	double plusmax=0;
	double minusmax=0;
	int positive=0;
	int negative=0;
	int midVal=0;
	int oddeven=0;
	int value=0;
	int num=0;
	// Now lets start processing
	if(array.length>0){
		Arrays.sort(array); // Here we sort array
		oddeven=(array.length%2);
		// Now lets make condition
		if(scale>=0){
			num=scale;
		}
		else{
			num=-scale;
		}
		// Here we are checking odd and even number
		if(oddeven==1){
			// here we have odd count
			midVal=((array.length-1)/2)+1;
			ratio=(100/(midVal-1));
			equiv=num*ratio;
			plusmax=((((midVal-1)*equiv)/100)+(midVal-1));
			minusmax=((((midVal-1)*equiv)/100)-(midVal-1));
		}
		else{
			// Here we even count
			midVal=(array.length/2);
			ratio=(100/midVal);
			equiv=num*ratio;
			plusmax=(((midVal*equiv)/100)+midVal);
			minusmax=(((midVal*equiv)/100)-midVal);
		}
		
		// Now lets calculate for plus range
		// Here we check our values to know what to assign
		if(scale==0){
			range=min(array)+"->"+max(array);
		}
		else if(scale==1){
			if(midVal==(array.length-1)){
				range=array[midVal-1]+"->"+max(array);
			}
			else{
				range=array[midVal]+"->"+max(array);
			}
		}
		else{
			// Here we are selecting in range
			if(scale>=0){
				if(scale>1){
					// Here we are at positive line
					positive=new Double(plusmax).intValue();
					if(positive>=(array.length-1)){
						range=min(array)+"->"+max(array);
					}
					else{
						value=array[positive];
						range=value+"->"+max(array);
					}
				}
			}
			else{
				// Here we have negative line
				negative=-new Double(minusmax).intValue();
				if(negative==0){
					range=min(array)+"->"+max(array);
				}
				else{
					value=array[negative];
					range=value+"->"+max(array);
				}
			}
		}
		
	}
	// Now lets return
	return range;
}



/*********** CREATING ALL TYPES OF FILE READERS * @throws FileNotFoundException **************/
public String readByScanner(String file) throws FileNotFoundException{
	// CONSTRUCTING SCANNER READER
	Scanner scan=new Scanner(getResourceStream(this.getClass(), file));
	scan.useDelimiter("\\Z");
	String content=scan.next();
	// Lets close scanner
	scan.close();
	// Now lets return content
	return content;
}




public String readByBuffer(String file) throws IOException{
	// CONSTRUCTING BUFFER READER
	BufferedReader bufferReader=new BufferedReader(new InputStreamReader(getResourceStream(this.getClass(), file)));
	String line=bufferReader.readLine();
	String content="";
	// Now lets loop
	while(line!=null){
		content+=line;
		// Lets add Line
		line=bufferReader.readLine();
	} // end of loop
	
	// Lets close reader
	bufferReader.close();
	
	// Now lets return
	return content;
}




public String readBuffer(String file, String mode) throws IOException, URISyntaxException{
	// CONSTRUCTING BUFFER READER
	StringBuilder sb=new StringBuilder();
	String sCurrentLine="";
	// NOW LETS TRY READING DATA
	if(file!=null && mode!=null){
		if(mode.equals("eclipse")){
			try(BufferedReader br=new BufferedReader(new InputStreamReader(getResourceStream(this.getClass(), file)))){
				while((sCurrentLine=br.readLine())!=null){
					sb.append(sCurrentLine);
				}
			}
		}
		else if(mode.equals("ant")){
			try(BufferedReader br=new BufferedReader(new FileReader(file))){
				while((sCurrentLine=br.readLine())!=null){
					sb.append(sCurrentLine);
				}
			}
		}
	}
	
	// NOW LETS RETURN STRING
	return sb.toString();
}



/************************ CREATING TEXT WRITER METHOD ***********************************/
public boolean writeByBuffer(String content, String savePath){
	// Setting variables
	String FILENAME = savePath;
	boolean success=false;
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {

			fw = new FileWriter(FILENAME);
			bw = new BufferedWriter(fw);
			bw.write(content);
			success=true;

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
		
		return success;
}





/************************** CREATING STRING STREAM WRITER ********************************/
public String writeStringAsStream(String content, URL BaseURL, String savePath){
	// Constructing the String Stream writer
	FileOutputStream fop=null;
	File file=new File(savePath);
	LinkPatch patch;
	String baseUrl="";
	String HttpMode="";
	String protocol="";
	String authority="";
	// Lets try creating
	try{
		// LETS CHECK CONTENTS
		if(content!=null){
		fop=new FileOutputStream(file);
		// If file doesnt exists, then create it
		if(!file.exists()){
			file.createNewFile();
		}
		// Get the content in bytes
		byte[] contentInBytes=content.getBytes("UTF-8");
		// Now lets write
		fop.write(contentInBytes);
		
		// LETS ADD THE URL PATCHING FUNCTIONALITY HERE
		if(BaseURL!=null){
			// HERE WE PROCESS TO ADD URL
			// Lets assign url parts values
			protocol=BaseURL.getProtocol();
			authority=BaseURL.getAuthority();
			// Now lets set base url
			baseUrl=protocol+"://"+authority;
			// Now lets set the http secured
			if(protocol.equals(new String("https"))){
				HttpMode="secured";
			}
			else{
				HttpMode="unsecured";
			}
			// Now lets patch
			patch=new LinkPatch(savePath, baseUrl, HttpMode, "saved");
			// Now lets dump to the environment
			DumpReturns=patch.patched();
			// System.out.println(patch.patched());
		} // End of baseurl check
		// Lets close resource
		fop.flush();
		fop.close();
		} // End of content null check
		}
	catch(IOException e){
		e.printStackTrace();
	}
	finally{
		try{
			if(fop!=null){
				fop.close();
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	
	return savePath;
}



/************************ CREATING THE FILE DOWNLOAD METHOD ON UTIL **********************/
public String download(String fileUrl, String saveDir)throws IOException{
	// SETTING RETURN FILE AND PATH
	String fileSaved="";
	LinkPatch patch;
	String baseUrl="";
	String HttpMode="";
	String protocol="";
	String authority="";
	// Install the all-trusting trust manager
	SSLContext sc = null;
	/**
	 * Downloads a file from a URL
	 * @param fileURL HTTP URL of the file to be download
	 * @param saveDir path of the directory to save the file
	 * @throws IOException
	 */
	
	// LETS DISABLE SSL CERTIFICATE
	// Create a trust manager that does not validate certificate chains
	TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}
			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}
			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		}
	};

	try {
		sc = SSLContext.getInstance("SSL");
	} catch (NoSuchAlgorithmException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
	} catch (KeyManagementException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

	// Create all-trusting host name verifier
	HostnameVerifier allHostsValid = new HostnameVerifier() {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};

	// Install the all-trusting host verifier
	HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
			
	/********************* NOW LETS START DOWNLOADING OUR PAGE *********************/
	
	URL url=new URL(fileUrl);
	HttpURLConnection httpConn=(HttpURLConnection) url.openConnection();
	httpConn.connect();
	int responseCode=httpConn.getResponseCode();
	
	// always check HTTP response code first
	if(responseCode==HttpURLConnection.HTTP_OK){
		String fileName="file_"+new Random().nextInt(999999999);
		String contentType=httpConn.getContentType();
		//int contentLength=httpConn.getContentLength();
		//System.out.println(httpConn.getHeaderFields().keySet());
		
		if(contentType!=null){
			// Lets check for index of Content type to know how to save
			if(contentType.indexOf("html")>=0){
				// We save file with html extension
				fileName+=".html";
			}
			else if(contentType.indexOf("pdf")>=0){
				// We save file with pdf extention
				fileName=".pdf";
			}
			else if(contentType.indexOf("xml")>=0){
				// We save file with pdf extention
				fileName+=".xml";
			}
			else{
				// we save with default extention in the url
				fileName=fileUrl.substring(fileUrl.lastIndexOf("/")+1, fileUrl.length());
			}
			// Opens input stream from the HTTP connection
			InputStream inputStream=httpConn.getInputStream();
			String saveFilePath=saveDir+fileName;
			// opens an ouput stream to save into file
			FileOutputStream outputStream=new FileOutputStream(saveFilePath);
			int bytesRead=-1;
			byte[] buffer=new byte[BUFFER_SIZE];
			// We loop to store file
			while((bytesRead=inputStream.read(buffer))!=-1){
				outputStream.write(buffer, 0, bytesRead);
			} // End of while loop
			// Lets clode file streams
			outputStream.close();
			inputStream.close();
			// HERE WE LOG THE RETURN OF FILE THAT WAS DOWNLOADED
			fileSaved=saveFilePath+"+["+contentType+"]";
			
			/******************** HERE WE PATCH THE HTML WITH THE ACTUAL LINKS ***************/
			if(contentType.indexOf("html")>=0){
			// Lets assign url parts values
			protocol=url.getProtocol();
			authority=url.getAuthority();
			// Now lets set base url
			baseUrl=protocol+"://"+authority;
			// Now lets set the http secured
			if(protocol.equals(new String("https"))){
				HttpMode="secured";
			}
			else{
				HttpMode="unsecured";
			}
			// Now lets patch
			patch=new LinkPatch(saveFilePath, baseUrl, HttpMode, "saved");
			// Now lets dump to the environment
			DumpReturns=patch.patched();
			// System.out.println(patch.patched());
			} // End of content check
		}
		else{
		// HERE WE LOG NULL CAUSE NO FILE WAS DOWNLOAD
			fileSaved=null;
		}
		// Lets close connection
		httpConn.disconnect();
	}
	
	return fileSaved;
	
}



/************** CREATING THE HTML GET CONTENT METHOD TO GET HTML ***************************/
public String getHtml(String url, int timeout, int tryTimes){
	// HERE WE START CONSTRUCTING THE HTML GET PROCESS
	String inputLine;
	StringBuffer html = new StringBuffer();
	String pageData = "";
	
	// Install the all-trusting trust manager
	SSLContext sc = null;
		
	// LETS DISABLE SSL CERTIFICATE
	// Create a trust manager that does not validate certificate chains
	TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}
			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}
			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		}
	};

	try {
		sc = SSLContext.getInstance("SSL");
	} catch (NoSuchAlgorithmException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
	} catch (KeyManagementException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	// Create all-trusting host name verifier
	HostnameVerifier allHostsValid = new HostnameVerifier() {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};

	// Install the default certificate
	HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		
	// Install the all-trusting host verifier
	HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		
	// HERE WE SET OUR URL TO READ CONTENT
	// Here we get url content
	URL obj;
	try {
		obj = new URL(url);
		HttpURLConnection conn;
		
		// HERE WE TRY TO READ SOME SPECIFIC TIME DUE TO TIME OUT
		for (int i = 0; i < tryTimes; i++) {
			try {
				conn = (HttpURLConnection) obj.openConnection();
				conn.setDoOutput(true);
				conn.setConnectTimeout(timeout);
				conn.setReadTimeout(timeout*2);
				conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
				conn.addRequestProperty("User-Agent", "Mozilla");
				conn.addRequestProperty("Referer", "google.com");
			
				// Setting the redirect to false
				boolean redirect = false;
				// Setting the http accepted code array
				int[] accepted = {200, 302, 301, 303};
				// normally, 3xx is redirect
				int status = conn.getResponseCode();
				// getting page length
				// int length = conn.getContentLength();
				
				// NOW LETS CHECK TO MAKE SURE WE GET FOR URL THAT IS OK
				if(ArrayUtils.contains(accepted, status)){
					if (status != HttpURLConnection.HTTP_OK) {
						if (status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM || status == HttpURLConnection.HTTP_SEE_OTHER){
							redirect = true;
						}
					}

					// Here we check the redirect to know when to initiate
					if (redirect) {
						// get redirect url from "location" header field
						String newUrl = conn.getHeaderField("Location");

						// get the cookie if need, for login
						String cookies = conn.getHeaderField("Set-Cookie");

						// open the new connection again
						conn = (HttpURLConnection) new URL(newUrl).openConnection();
						conn.setDoOutput(true);
						conn.setConnectTimeout(timeout);
						conn.setReadTimeout(timeout*2);
						conn.setRequestProperty("Cookie", cookies);
						conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
						conn.addRequestProperty("User-Agent", "Mozilla");
						conn.addRequestProperty("Referer", "google.com");
					}

					// Here we start reading content
					BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					// Here we loop to get content
					while ((inputLine = in.readLine()) != null) {
					html.append(inputLine);
					}
					// Here we get page data and close
					pageData = html.toString();
					in.close();
				}
				else{
					// Here we set page data to null
					pageData = null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				continue;
			}
			
			// Here we return
			return pageData;
		} // End of for loop
		
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (Exception e){
		e.printStackTrace();
	}
	
	// Here we return
	return null;
	// End of method
}


/*********** CREATING THE SAVE FILE METHOD FOR SAVING FILES * @throws IOException **********/
public String saveFile(String src, String dst) throws IOException{
	// We have to check if src is a path and if its we log all its files
	File source=new File(src);
	File destination=new File(dst);
	String Type=null;
	String fileCopied="["+destination+"]";
	String filesOn="";
	// Now lets check if file is directory
	if(source.isDirectory()){
		Type="d2d";
	}
	else{
		Type="f2d";
	}
	// NOW LETS CHECK TYPE AND USE IT TO CALL COPY
	if(Type.equals(new String("d2d"))){
		// HERE WE PROCESS BY COPYING THE ENTIRE DIRECTORY
		for(File file:source.listFiles()){
			Files.copy(file.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
			// NOW LETS ADD THE FILE NAMES
			if(filesOn.length()>2){
				filesOn+=","+file.toPath();
			}
			else{
				filesOn+=file.toPath();	
			}
		
		} // End of loop
	}
	else if(Type.equals(new String("f2d"))){
		// HERE WE PROCESS BY COPYING INDIVIDUAL FILE
		Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
		// NOW LETS ADD THE FILE NAMES
		filesOn+=source.toPath();
	}
	
	// NOW LETS ADD UP RETURN
	fileCopied+="=["+filesOn+"]";
	// Now lets return copied file feedback
	return fileCopied;
	
}



/***************************** LETS CONSTRUCT THE HITS PROCESSOR ***************************/
public int[][] hitsProcessor(String hitsData){
	// NOW LETS START PROCESSING THE HITS
	int ochits=0;
	int khits=0;
	int onekey=0;
	int hitSum=0;
	int[][] hitIndex={};
	String[] hitsParts;
	String[] hitBreak;
	String[] processHits;
	String hitString="";
	// Lets process score hits data
		if(hitsData.length()>0){
			if(hitsData.indexOf("+")>=0){
				// Here we are processing many file hits
				processHits=hitsData.split("\\+");
				// Setting array counts
				hitIndex=new int[processHits.length][2];
				if(processHits.length>0){
					for(int j=0; j<processHits.length; j++){
					// Now lets get all part of the hits
					hitsParts=processHits[j].split("\\]");
					onekey=Integer.parseInt(hitsParts[0].substring(1, hitsParts[0].length()));
					hitString=hitsParts[1].substring(1, hitsParts[1].length());
					// Now lets futher break hits
					hitBreak=hitString.split("\\->");
					khits=Integer.parseInt(hitBreak[0]);
					ochits=Integer.parseInt(hitBreak[1]);
					// Now lets sum up both key hits and occurrence hits and add
					hitSum=khits+ochits;
					// Adding to hits index
					hitIndex[j][0]=onekey;
					hitIndex[j][1]=hitSum;
					} // End of loop
				} // End of check	
			}
			else{
				// Here we are processing single file hits
				// Setting array counts
				hitIndex=new int[1][2];
				// Now lets get all part of the hits
				hitsParts=hitsData.split("\\]");
				onekey=Integer.parseInt(hitsParts[0].substring(1, hitsParts[0].length()));
				hitString=hitsParts[1].substring(1, hitsParts[1].length());
				// Now lets futher break hits
				hitBreak=hitString.split("\\->");
				khits=Integer.parseInt(hitBreak[0]);
				ochits=Integer.parseInt(hitBreak[1]);
				// Now lets sum up both key hits and occurrence hits and add
				hitSum=khits+ochits;
				// Adding to hits index
				hitIndex[0][0]=onekey;
				hitIndex[0][1]=hitSum;
				
			}
		}
			
	return hitIndex;
}



/***************************** HERE WE CONSTRUCT POSITION PROCESSOR ************************/
public int[][] positionHandle(String poscoord){
	// HERE WE START PROCESSING THE POSITION HANDLE
	int[][] masterGroup={};
	String[] coordSeperator;
	String[] hitGroups;
	String[] HitPositions;
	String TrimHitScores="";
	String trimHitScores="";
	String TrimHitCounts="";
	String trimHitCounts="";
	int unidex=0;
	int groupIndex=0;
	// HERE WE START PROCESSING
	if(poscoord.length()>0 && poscoord.indexOf("+")>=0){
		// NOW LETS START IMPLEMENTING MASTER PROCESSING AND LOGIC
		coordSeperator=poscoord.split("\\+");
		trimHitCounts=coordSeperator[0].replace("[", "");
		trimHitScores=coordSeperator[1].replace("[", "");
		// Now lets keep trimming
		TrimHitCounts=trimHitCounts.replace("]", "");
		TrimHitScores=trimHitScores.replace("]", "");
		// LETS FURTHER PROCESS THE HITS
		if(TrimHitCounts.indexOf("->")>=0 && TrimHitScores.indexOf(",")>=0){
			hitGroups=TrimHitCounts.split("->");
			HitPositions=TrimHitScores.split(",");
			// lets set multidimensional array
			masterGroup=new int[hitGroups.length][];
			// NOW LETS MAKE SURE THE GROUP IS NOT ABOVE POSITIONS
			if(hitGroups.length<HitPositions.length){
				for(int i=0; i<hitGroups.length; i++){
					// NOW LETS CHECK POSITION ON ARRAY TO ASSIGN	
					groupIndex=Integer.parseInt(hitGroups[i]);
					masterGroup=new int[hitGroups.length][groupIndex];
					// LETS CHECK THE COUNT OF INDEX TO THE GROUP
					if(unidex<HitPositions.length){
						// Implementing internal loop to get values	
						for(int j=unidex; j<HitPositions.length; j++){
							// NOW LETS CHECK IN ORDER TO LOG PROPERLY
							masterGroup[i][j]=Integer.parseInt(HitPositions[j]);
						} // End of static loop 2
						// Lets increment unidex with the group index
						unidex=(unidex+groupIndex);
					} // End of the group compare check
				} // End of static loop 1
			}
		}
		else{
			// HERE WE HAVE ONLY ONE HANDLE REQUEST
			if(TrimHitScores.indexOf(",")>=0){
				// Here we find many occurrence
				HitPositions=TrimHitScores.split(",");
				// lets set multidimensional array
				masterGroup=new int[1][];
				// NOW LETS CHECK POSITION ON ARRAY TO ASSIGN	
				groupIndex=Integer.parseInt(TrimHitCounts);
				masterGroup=new int[1][groupIndex];
				// Implementing internal loop to get values	
				for(int j=0; j<HitPositions.length; j++){
					// NOW LETS CHECK IN ORDER TO LOG PROPERLY
					masterGroup[0][j]=Integer.parseInt(HitPositions[j]);
				} // End of static loop 2
			}
			else{
				// We find only one occurence
				// lets set multidimensional array
				masterGroup=new int[1][1];
				// NOW LETS CHECK IN ORDER TO LOG PROPERLY
				masterGroup[0][0]=Integer.parseInt(TrimHitScores);
			}
		}
	}
	// Here we return hash map
	return masterGroup;
}


/*********************** POSITION HANDLE WITH INDEX **************************************/
public int[][][] positionIndexHandle(String poscoord){
	// HERE WE START PROCESSING THE POSITION HANDLE
	int[][][] masterGroup={};
	String[] coordSeperator;
	String[] hitGroups;
	String[] itemParts;
	String[] HitPositions;
	String TrimHitScores="";
	String trimHitScores="";
	String TrimHitCounts="";
	String trimHitCounts="";
	int unidex=0;
	int groupIndex=0;
	int itemIndex=0;
	// HERE WE START PROCESSING
	if(poscoord.length()>0 && poscoord.indexOf("+")>=0){
		// NOW LETS START IMPLEMENTING MASTER PROCESSING AND LOGIC
		coordSeperator=poscoord.split("\\+");
		trimHitCounts=coordSeperator[0].replace("[", "");
		trimHitScores=coordSeperator[1].replace("[", "");
		// Now lets keep trimming
		TrimHitCounts=trimHitCounts.replace("]", "");
		TrimHitScores=trimHitScores.replace("]", "");
		// LETS FURTHER PROCESS THE HITS
		if(TrimHitCounts.indexOf("->")>=0 && TrimHitScores.indexOf(",")>=0){
			hitGroups=TrimHitCounts.split("->");
			HitPositions=TrimHitScores.split(",");
			// lets set multidimensional array
			masterGroup=new int[hitGroups.length][2][];
			// NOW LETS MAKE SURE THE GROUP IS NOT ABOVE POSITIONS
			if(hitGroups.length<HitPositions.length){
				for(int i=0; i<hitGroups.length; i++){
					// NOW LETS CHECK POSITION ON ARRAY TO ASSIGN
					itemParts=hitGroups[i].split("@");
					groupIndex=Integer.parseInt(itemParts[0]);
					itemIndex=Integer.parseInt(itemParts[1]);
					masterGroup=new int[hitGroups.length][2][groupIndex];
					masterGroup[i][0][0]=itemIndex;
					// LETS CHECK THE COUNT OF INDEX TO THE GROUP
					if(unidex<HitPositions.length){
						// Implementing internal loop to get values	
						for(int j=unidex; j<HitPositions.length; j++){
							// NOW LETS CHECK IN ORDER TO LOG PROPERLY
							masterGroup[i][1][j]=Integer.parseInt(HitPositions[j]);
						} // End of static loop 2
						// Lets increment unidex with the group index
						unidex=(unidex+groupIndex);
					} // End of the group compare check
				} // End of static loop 1
			}
		}
		else{
			// HERE WE HAVE ONLY ONE HANDLE REQUEST
			if(TrimHitScores.indexOf(",")>=0){
				// Here we find many occurrence
				itemParts=TrimHitCounts.split("@");
				groupIndex=Integer.parseInt(itemParts[0]);
				itemIndex=Integer.parseInt(itemParts[1]);
				HitPositions=TrimHitScores.split(",");
				// NOW LETS CHECK POSITION ON ARRAY TO ASSIGN
				masterGroup=new int[1][2][groupIndex];
				masterGroup[0][0][0]=itemIndex;
				// Implementing internal loop to get values	
				for(int j=0; j<HitPositions.length; j++){
					// NOW LETS CHECK IN ORDER TO LOG PROPERLY
					masterGroup[0][1][j]=Integer.parseInt(HitPositions[j]);
				} // End of static loop 2
			}
			else{
				// We find only one occurence
				// lets set multidimensional array
				itemParts=TrimHitCounts.split("@");
				groupIndex=Integer.parseInt(itemParts[0]);
				itemIndex=Integer.parseInt(itemParts[1]);
				masterGroup=new int[1][2][groupIndex];
				// NOW LETS CHECK IN ORDER TO LOG PROPERLY
				masterGroup[0][0][0]=itemIndex;
				masterGroup[0][1][0]=Integer.parseInt(TrimHitScores);
			}
		}
	}
	// Here we return hash map
	return masterGroup;
}



/****************************** LETS SET FILE ITERATION METHOD *****************************/
public File[] getFiles(File[] files){
	// HERE WE SET RETURN DATA
	File[] returnFiles={};
	String[] fileString={};
	if(files != null){
		// Now lets loop
		for(File file:files){
			if(file.isDirectory()){
				// LETS ITERATE AGAIN
				getFiles(file.listFiles());
			}
			else{
				// LETS LOG THE FILES THAT ARE NOT DIRECTORY
				DataLists.add(file.toString());
			}
		} // End of foreach loop
		// HERE WE CHECK IF WE HAVE LOGGED FILES
		if(!DataLists.isEmpty()){
			// Now we change data of array
			// Theres an error here
			fileString=DataLists.toArray(new String[DataLists.size()]);
			returnFiles=new File[fileString.length];
			for(int i=0; i<fileString.length; i++){
				returnFiles[i]=new File(fileString[i]);
			}
		}
	}
	
	// NOW LETS RETURN FOUND FILE
	return returnFiles;
}






/****************************** LETS SET PATH ITERATION METHOD *****************************/
public File[] getPaths(File[] files){
	// HERE WE SET RETURN DATA
	File[] returnFiles={};
	String[] fileString={};
	if(files != null){
		// Now lets loop
		for(File file:files){
			if(file.isDirectory()){
				// LETS LOG THE FILES THAT ARE NOT DIRECTORY
				PathLists.add(file.toString());
				// LETS ITERATE AGAIN
				getPaths(file.listFiles());
			}
		} // End of foreach loop
		// HERE WE CHECK IF WE HAVE LOGGED FILES
		if(!PathLists.isEmpty()){
			// Now we change data of array
			// Theres an error here
			fileString=PathLists.toArray(new String[PathLists.size()]);
			returnFiles=new File[fileString.length];
			for(int i=0; i<fileString.length; i++){
				returnFiles[i]=new File(fileString[i]);
			}
		}
	}
			
	// NOW LETS RETURN FOUND FILE
	return returnFiles;
}



/******************* CONSTRUCTING THE LIST CHOOSER *****************/
public File[] fileFilter(File[] files){
	// HERE WE START CONSTRUCTING THE FILE FILTER
	List<File> fileList = new ArrayList<File>();
	String[] filters = {"bin", "src", "empty.txt"};
	String fileString = "";
	File[] filtered = null;
	String lastFix = "";
	// HERE WE START CONSTRUCTING
	if(files!=null){
		if(files.length>0){
			for(int i=0; i<files.length; i++){
				fileString = files[i].toString().replace("\\", "/");
				lastFix = fileString.substring(fileString.lastIndexOf("/")+1, fileString.length());
				if(!lastFix.startsWith(".")){
					if(!Arrays.asList(filters).contains(lastFix)){
						fileList.add(files[i]);
					}
				}
			} // end of loop
			
			// HERE WE CONVERT TO ARRAY
			if(fileList.size()>0){
				filtered = fileList.toArray(new File[fileList.size()]);
			}
		}
	}
	// Here we return
	return filtered;
}



/*********************** CREATING THE PDF TITLE RETRIVER METHOD ****************************/
public String pdfTitle(File filePage){
	// NOW LETS START PROCESSING DOCUMENT
	String Title="";
	// NOW LETS START PROCESSING
	try {
		PDDocument doc=PDDocument.load(filePage);
		PDDocumentInformation info=doc.getDocumentInformation();
		Title=info.getTitle();
	} catch (InvalidPasswordException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	// NOW LETS RETURN TITLE
	return Title;
}




/************************* CREATING THE DOCX TITLE RETRIEVER METHOD ***********************/
public String docxTitle(File filePage){
	// NOW LETS CONSTRUCT THE DOCX TITLE RETRIEVER
	String Title="";
	// NOW LETS START PROCESSING
	try {
		WordprocessingMLPackage wordMLPackage=WordprocessingMLPackage.load(filePage);
		org.docx4j.openpackaging.parts.DocPropsCorePart docPropsCorePart=wordMLPackage.getDocPropsCorePart();
		@SuppressWarnings("deprecation")
		org.docx4j.docProps.core.CoreProperties coreProps=(org.docx4j.docProps.core.CoreProperties) docPropsCorePart.getJaxbElement();
		java.util.List<String> list=coreProps.getTitle().getValue().getContent();
		if(list.size()>0){
			Title=list.get(0);
		}
		
	} catch (Docx4JException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	// LETS RETURN TITLE
	return Title;
}




/**************** CREATING THE SYNTAX INTERPRETER METHOD FOR XML READER *******************/
public String[] syntaxInterpreter(String SyntaxData){
	// LETS START PROCESSING THE SYNTAX INTERPRETER
	String[] returnData={};
	List<String> list=new ArrayList<String>();
	String[] syntaxArray;
	String[] attrPaths;
	String[] SyntaxPaths;
	
	// NOW LETS START PROCESSING
	if(SyntaxData!=null && SyntaxData.length()>0){
		// Now lets break the data
		syntaxArray=SyntaxData.split("\\;");
		// Now lets check length of array
		if(syntaxArray.length>0){
			// We start processing
			for(int i=0; i<syntaxArray.length; i++){
				// Lets make sure we break only those with attribute instruction
				if(syntaxArray[i].indexOf("~")>=0){
					// We handle as one with attribute instruction
					SyntaxPaths=syntaxArray[i].split("\\~");
					if(SyntaxPaths[0].indexOf("@val")>=0 || SyntaxPaths[0].indexOf(":")>=0){
						// This means tag has value
						list.add(SyntaxPaths[0]);
						// HERE WE CHECK IF THE ATTRIBUTE VALUE HAS MULTIPLE INSTRUCTION
						if(SyntaxPaths[1].indexOf(",")>=0){
							// Here we have multiple attribute
							// We further break the attribute instruction
							attrPaths=SyntaxPaths[1].split(",");
							// Now lets loop and add
							for(int k=0; k<attrPaths.length; k++){
								if(SyntaxPaths[0].indexOf("@val")>=0){
									list.add(SyntaxPaths[0].substring(0, SyntaxPaths[0].indexOf("@val")-1)+"~"+attrPaths[k]);
								}
								else{
									list.add(SyntaxPaths[0].substring(0, SyntaxPaths[0].indexOf(":")-1)+"~"+attrPaths[k]);	
								}
							} // End of loop
						}
						else{
							// Here we have single attributes
							if(SyntaxPaths[0].indexOf("@val")>=0){
								list.add(SyntaxPaths[0].substring(0, SyntaxPaths[0].indexOf("@val")-1)+"~"+SyntaxPaths[1]);
							}
							else{
								list.add(SyntaxPaths[0].substring(0, SyntaxPaths[0].indexOf(":")-1)+"~"+SyntaxPaths[1]);
							}
						}
					}
					else{
						// Tag does not have value
						// So here we set the tag name as a tracker
						// HERE WE CHECK IF THE ATTRIBUTE VALUE HAS MULTIPLE INSTRUCTION
						if(SyntaxPaths[1].indexOf(",")>=0){
							// Here we have multiple attribute
							// We further break the attribute instruction
							attrPaths=SyntaxPaths[1].split(",");
							// Now lets loop and add
							for(int k=0; k<attrPaths.length; k++){
								if(SyntaxPaths[0].indexOf("@val")>=0){
									list.add(SyntaxPaths[0].substring(0, SyntaxPaths[0].indexOf("@val")-1)+"~"+attrPaths[k]);
								}
								else{
									// We have to check if colon exists
									if(SyntaxPaths[0].indexOf(":")>=0){
										list.add(SyntaxPaths[0].substring(0, SyntaxPaths[0].indexOf(":")-1)+"~"+attrPaths[k]);
									}
									else{
										list.add(SyntaxPaths[0]+"~"+attrPaths[k]);
									}
								}
							} // End of loop
						}
						else{
							// Here we have single attributes
							if(SyntaxPaths[0].indexOf("@val")>=0){
								list.add(SyntaxPaths[0].substring(0, SyntaxPaths[0].indexOf("@val")-1)+"~"+SyntaxPaths[1]);
							}
							else{
								if(SyntaxPaths[0].indexOf(":")>=0){
									list.add(SyntaxPaths[0].substring(0, SyntaxPaths[0].indexOf(":")-1)+"~"+SyntaxPaths[1]);
								}
								else{
									list.add(SyntaxPaths[0]+"~"+SyntaxPaths[1]);
								}
							}
						}
					}
				}
				else{
					// We handle as one with no attribute instruction
					list.add(syntaxArray[i]);
				}
			} // End of loop
		} // End of syntax array check
	} // End of syntax data check
	
	
	// NOW LETS SET THE RETURN ARRAY FROM THE CONTENTS OF THE LIST
	// Now we change data of array
	returnData=list.toArray(new String[list.size()]);
	
	// NOW LETS REMOVE ANY NULL IN THE ARRAY
	// HERE WE TRY TO COMPOSE ARRAY TO REMOVE NULL
	List<String> data=new ArrayList<String>();
	for(String s:returnData){
		if(s!=null && s.length()>0){
			data.add(s);
		}
	} // End of loop
				
	// Now we change data of array
	returnData=list.toArray(new String[data.size()]);
	
	// NOW LETS RETURN DATA ARRAY
	return returnData;
}



/******************** CREATING THE STRING INTEGER SUM CALCULATOR ***************************/
public int sisCal(String integerString){
	// NOW LETS START PROCESSING
	String a=integerString;
	int sum=0;
	for(char c:a.replaceAll("\\D", "").toCharArray()){
		int digit=c-'0';
		sum+=digit;
	}
	
	// LETS RETURN SUM
	return sum;
}



/***************** CREATING THE RESOURCE FILE GETTING FROM ECLIPSE PROGRAM *****************/
@SuppressWarnings("rawtypes")
public InputStream getResourceStream(Class cl, String path) {
	InputStream in = cl.getClassLoader().getResourceAsStream(path);
	// Here we return
	return in;
  }



/*********************** NOW LETS GET FILE FROM RESOURCE ***********************************/
@SuppressWarnings("rawtypes")
public File getResourceFile(Class cl, String path){
	//Get file from resources folder
	ClassLoader classLoader = cl.getClassLoader();
	File file = new File(classLoader.getResource(path).getFile());
	// Now lets return
	return file;
}



/*********************** NOW LETS GET FILE FROM RESOURCE ***********************************/
@SuppressWarnings("rawtypes")
public String getResourceString(Class cl, String path){
	//Get file from resources folder
	ClassLoader classLoader = cl.getClassLoader();
	String file = classLoader.getResource(path).getFile();
	// Now lets return
	return file;
}


/************** HERE WE GET THE ANT RESOURCE * @throws URISyntaxException *****************/
public String antResource(String path) throws URISyntaxException{
	AntResource resource = new AntResource();
	return resource.get(path);
}


// END OF CLASS
}
