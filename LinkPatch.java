package system.xmlprocess;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LinkPatch {
// SETTING INSTANCE VARIABLES
private String FilePath;
private String BaseUrl;
private String HttpType;
private String Process;
private String patchReturn;
/**************************** LETS CONSTRUCT CLASS * @throws IOException *******************/
public LinkPatch(String filePath, String baseURL, String httpType, String process) throws IOException{
// Process is a command for it to either save or return as a string	
FilePath=filePath;
BaseUrl=baseURL;
HttpType=httpType;
Process=process;
// Lets call the handler
handler();
}



/******************** LETS CONSTRUCT THE STATIC METHOD * @throws IOException ********
public static void main(String[] args) throws IOException{
	// NOW LETS SET HTML TO CONVERT
	String file="cores/core/log/DJade_Tmp/google/tmp_yh/tmp_content/file_201735847.html";
	String baseUrl="http://www.tutorialspoint.com";
	// Lets instantiate class
	LinkPatch patch=new LinkPatch(file, baseUrl, "unsecured", "saved");
	// Now lets print
	System.out.println(patch.patched());
}
**********/


/****************** LETS CONSTRUCT THE HANDLER METHOD * @throws IOException ******************/
private void handler() throws IOException{
// NOW LETS HANDLE PATCH PROCESSING
// Lets start patching
String htmlString=patcher();
String htmlReturn="";
// NOW LETS CHECK THE PROCESS
if(Process.equals(new String("saved"))){
// Now lets save
htmlReturn=savePatched(htmlString);
patchReturn=htmlReturn;
}
else{
htmlReturn=htmlString;
patchReturn=htmlReturn;
}

// End of method
}



/******************* LETS CONSTRUCT THE PATCHER METHOD * @throws IOException *****************/
private String patcher() throws IOException{
	// Here we set the method variables
	String stringValue="";
	File input=new File(FilePath);
	Document doc;
	String attrValue="";
	// LETS START PROCESSING
	doc=Jsoup.parse(input, "UTF-8");
	// NOW LETS GET ELEMENTS
	Elements links=doc.select("a[href]"); // Getting the anchor tags
	Elements medias=doc.select("[src]"); // Getting the image and script tags
	Elements imports=doc.select("link[href]"); // Getting the link tags
	
	// NOW LETS LOOP THROUGH EACH TO PARSE BASE URL IF NOT SETTED
	for(Element a:links){
		// Lets set attribute value
		attrValue=a.attr("href");
		// Now lets check
		if(attrValue.indexOf("http")<0 && attrValue.indexOf("://")<0){
		// We have to check for javascript active string
		if(!attrValue.equals(new String("javascript:void(0);"))){ // We proceed	
		// We update a href
		if(attrValue.indexOf("/")>=0 || attrValue.indexOf("\\")>=0){	
		a.attr("href", BaseUrl+attrValue); // here we edited knowing string has slash
		}
		else if(attrValue.indexOf("../")>=0 || attrValue.indexOf("./")>=0){
		a.attr("href", BaseUrl+attrValue.substring(attrValue.lastIndexOf("./")+1, attrValue.length())); // here we edited knowing string has slash	
		}
		else{
		a.attr("href", BaseUrl+"/"+attrValue); // here we edited knowing string has slash	
		}
		} // End of active javascript string check
		}
		else if(attrValue.indexOf("http")<0 && attrValue.indexOf("www.")>=0){
			// Here we check the httpType
			if(HttpType.equals(new String("secured"))){
			a.attr("href", "https://"+attrValue);	
			}
			else if(HttpType.equals(new String("unsecured"))){
			a.attr("href", "http://"+attrValue);	
			}
		}
		// END OF INNER CHECK
	} // End of loop
	
	
	// LOOPING FOR MEDIA PATCHING
	for(Element src:medias){
		// Lets set attribute value
		attrValue=src.attr("src");
		// Now lets check
		if(attrValue.indexOf("http")<0 && attrValue.indexOf("://")<0){
		// We update a href
		if(attrValue.substring(0, 1).equals(new String("/")) || attrValue.substring(0, 1).equals(new String("\\"))){	
		src.attr("src", BaseUrl+attrValue); // here we edited knowing string has slash
		}
		else if(attrValue.indexOf("../")>=0 || attrValue.indexOf("./")>=0){
		src.attr("src", BaseUrl+attrValue.substring(attrValue.lastIndexOf("./")+1, attrValue.length())); // here we edited knowing string has slash	
		}
		else{
		src.attr("src", BaseUrl+"/"+attrValue); // here we edited knowing string has slash	
		}
		}
		else if(attrValue.indexOf("http")<0 && attrValue.indexOf("www.")>=0){
			// Here we check the httpType
			if(HttpType.equals(new String("secured"))){
			src.attr("src", "https://"+attrValue);	
			}
			else if(HttpType.equals(new String("unsecured"))){
			src.attr("src", "http://"+attrValue);	
			}
		}
		// END OF INNER CHECK
	} // End of loop
	
	
	
	
	// LOOPING FOR IMPORT PATCHING
		for(Element link:imports){
			// Lets set attribute value
			attrValue=link.attr("href");
			// Now lets check
			if(attrValue.indexOf("http")<0 && attrValue.indexOf("://")<0){
			// We update a href
			if(attrValue.substring(0, 1).equals(new String("/")) || attrValue.substring(0, 1).equals(new String("\\"))){	
			link.attr("href", BaseUrl+attrValue); // here we edited knowing string has slash
			}
			else if(attrValue.indexOf("../")>=0 || attrValue.indexOf("./")>=0){
			link.attr("href", BaseUrl+attrValue.substring(attrValue.lastIndexOf("./")+1, attrValue.length())); // here we edited knowing string has slash	
			}
			else{
			link.attr("href", BaseUrl+"/"+attrValue); // here we edited knowing string has slash	
			}
			}
			else if(attrValue.indexOf("http")<0 && attrValue.indexOf("www.")>=0){
				// Here we check the httpType
				if(HttpType.equals(new String("secured"))){
				link.attr("href", "https://"+attrValue);	
				}
				else if(HttpType.equals(new String("unsecured"))){
				link.attr("href", "http://"+attrValue);	
				}
			}
			// END OF INNER CHECK
		} // End of loop
	
	// NOW LETS SET THE STRING VALUES OF THE PROCESS DOCUMENT
	stringValue=doc.html();
	// Lets return
	return stringValue;
}




/********* LETS CONSTRUCT THE SAVING METHOD * @throws UnsupportedEncodingException ************/
private String savePatched(String patchHtml) throws UnsupportedEncodingException{
	// HERE WE LOG THE HTML TO ITS FILE AFTER UPDATING
	String stringValue="";
	FileOutputStream fop=null;
	File file=new File(FilePath);
	// Now lets start processing
	
	// Lets try creating
		try{
			fop=new FileOutputStream(file, false);
			// If file doesnt exists, then create it
			if(!file.exists()){
				file.createNewFile();
			}
			// Get the content in bytes
			byte[] contentInBytes=patchHtml.getBytes("UTF-8");
			// Now lets write
			fop.write(contentInBytes);
			// Lets close resource
			fop.flush();
			fop.close();
			stringValue=file.toString();
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
	
	return stringValue;
}



/******************* LETS CONSTRUCT THE OUTPUT **********************************/
public String patched(){
	// Now lets return
	return patchReturn;
}

}
