package system.xmlprocess;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

//import org.apache.poi.hwpf.HWPFDocument;
//import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;


public class TextHandle {
// INITIALIZING INSTANCE VARIABLES	
private String[] StringPaths;
private String Handle;

// CONSTRUCTING THE CLASS
public TextHandle(String[] filePaths, String handle) throws IOException, SAXException, ParserConfigurationException{ //String type constructor
// Here we assign values to variables
StringPaths=filePaths;
Handle=handle;
}



/****************** CREATING MAIN METHOD TO RUN PROGRAM 
 * @throws ParserConfigurationException 
 * @throws SAXException 
 * @throws IOException *************************/
public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException{
	// Now lets instantiate class
	String[] paths={"cores/core/log/DJade_Tmp/google/tmp_yh/tmp_content/file_201735847.html", "cores/core/log/DJade_Tmp/google/tmp_yh/tmp_content/home.html"};
	// String[] paths={"cores/core/log/DJade_Tmp/google/tmp_yh/tmp_content/file.xml"};
	// String[] paths={"cores/core/log/DJade_Tmp/google/tmp_yh/tmp_content/file_201735847.pdf"};
	// String[] paths={"cores/core/log/DJade_Tmp/google/tmp_yh/tmp_content/file_201735847.docx"};
	TextHandle handle=new TextHandle(paths, "html");
	System.out.println(Arrays.toString(handle.getText()));
}




/********************* LETS LOG A SWITCH PROCESSING 
 * @throws IOException 
 * @throws ParserConfigurationException 
 * @throws SAXException *************************/
public String[] getText() throws IOException, SAXException, ParserConfigurationException{
	// NOW LETS PROCESS THE HANDLE
	String[] content = {};
	
	// LETS CHECK HANDLE
			switch(Handle){
			case "html":
			// Lets call the html converter
			content=htmlConverter();
			break;
			case "pdf":
			// Lets call the pdf converter
			content=pdfConverter();
			break;
			case "xml":
			// Lets call the pdf converter
			content=xmlConverter();
			break;
			case "docx":
			// Lets call the pdf converter
			content=docxConverter();
			// End of switch
			}
	// Lets return content
	return content;
}





/************** CONSTRUCTING THE TEXT CONVERTERS WIZARD METHODS * @throws IOException **********/
private String[] htmlConverter() throws IOException{
	// LETS PROCESS CONVERTING HTML DOCUMENT INTO TEXT
	String[] returnData=new String[StringPaths.length];
	String htmlText="";
	org.jsoup.nodes.Document doc;
	// LETS START MAIN PROCESSING
	if(StringPaths.length>0){
	for(int i=0; i<StringPaths.length; i++){
	// Setting index variables	
	int indexHtml=StringPaths[i].indexOf("htm");
	int indexPath=StringPaths[i].indexOf("/");	
	// We make sure file is html
	if(indexHtml>=0 && indexPath>=0){
    // We process
	doc=Jsoup.parse(new File(StringPaths[i]), "UTF-8");
	htmlText=new HtmlToPlainText(doc).formatted();
	// Lets check the content of the passed html
	if(htmlText.length()>0){
		returnData[i]=htmlText;
	}
	else{
		returnData=null;
	}
	}
	
	} // End of loop
	} // End of doc path check
	
	return returnData;
}




/******************** PDF TO TEXT CONVERTER METHOD ***********************/
private String[] pdfConverter() throws IOException{
	// LETS PROCESS CONVERTING PDF DOCUMENT INTO TEXT
	String[] returnData=new String[StringPaths.length];
	String pdfText="";
	// LETS START MAIN PROCESSING
	if(StringPaths.length>0){
	for(int i=0; i<StringPaths.length; i++){
	// Setting index variables
	int indexPdf=StringPaths[i].indexOf("pdf");
	int indexPath=StringPaths[i].indexOf("/");
	// We make sure file is html
	if(indexPdf>=0 && indexPath>=0){
	// Now lets process
		
		//Loading an existing document
		File file = new File(StringPaths[i]);
		PDDocument doc = PDDocument.load(file);
		//Instantiate PDFTextStripper class
		PDFTextStripper pdfStripper = new PDFTextStripper();
		//Retrieving text from PDF document
		pdfText = pdfStripper.getText(doc);
		//Closing the document
		doc.close();
		
		// Lets check the content of the passed html
		if(pdfText.length()>0){
			returnData[i]=pdfText.replaceAll("(?m)^[\t]*\r?\n", "\n");
		}
		else{
			returnData=null;
		}
	}
	
	} // End of loop
	} // End of doc path check
	
	return returnData;
}




/*********************** XML TO TEXT CONVERTER METHOD ****************************/
private String[] xmlConverter() throws SAXException, IOException, ParserConfigurationException{
	// LETS PROCESS XML DOCUMENT INTO TEXT
	String[] returnData=new String[StringPaths.length];
	String xmlText="";
	String newLines=System.getProperty("line.separator");
	// Setting Xml variables
	if(StringPaths.length>0){
	for(int i=0; i<StringPaths.length; i++){
	// Setting index variables
	int indexXml=StringPaths[i].indexOf("xml");
	int indexPath=StringPaths[i].indexOf("/");
	// We make sure file is html
	if(indexXml>=0 && indexPath>=0){
	DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
	DocumentBuilder builder=factory.newDocumentBuilder();
	// Now lets parse document
	Document doc=builder.parse(new File(StringPaths[i]));
	// Now lets get all tags
	doc.getDocumentElement().normalize();
	NodeList nodeList=doc.getElementsByTagName("*");
	// Now lets loop
	for(int j=0; j<nodeList.getLength(); j++){
	// Get element
		Element element=(Element)nodeList.item(j);
		// Now lets log
		xmlText+=element.getFirstChild().getTextContent()+newLines;
	} // End of loop
	
	// Lets check the content of the passed html
	if(xmlText.length()>0){
	returnData[i]=xmlText.replaceAll("[\r\n]+", "\n");
	}
	else{
	returnData=null;
	}
	}
	
	} // End of loop
	} // End of doc path check
	
	return returnData;
}




/************************** DOCX TO TEXT CONVERTER **********************************/
private String[] docxConverter(){
	// NOW LETS START PROCESSING THE READING OF DOCX
	String[] returnData=new String[StringPaths.length];
	String xmlText="";
	String newLines=System.getProperty("line.separator");
	// Setting Xml variables
	if(StringPaths.length>0){
	for(int i=0; i<StringPaths.length; i++){
		// Setting index variables
		int indexXml=StringPaths[i].indexOf("docx");
		int indexPath=StringPaths[i].indexOf("/");
		// We make sure file is html
		if(indexXml>=0 && indexPath>=0){
			try {
				File file = new File(StringPaths[i]);
				FileInputStream fis = new FileInputStream(file.getAbsolutePath());

				XWPFDocument document = new XWPFDocument(fis);

				List<XWPFParagraph> paragraphs = document.getParagraphs();
		
				// System.out.println("Total no of paragraph "+paragraphs.size());
				// Inner loop to get each paragraph
				for (XWPFParagraph para : paragraphs) {
					// Lets log data to string
					xmlText+=para.getText()+newLines;
				} // End of loop
				// Lets close document
				fis.close();
				document.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// Lets check the content of the passed html
			if(xmlText.length()>0){
			returnData[i]=xmlText.replaceAll("[\r\n]+", "\n");
			}
			else{
			returnData=null;
			}
			
		} // End of type check
	} // End of loop
	} // End of string path check
	
	return returnData;
}


// END OF CLASS
}
