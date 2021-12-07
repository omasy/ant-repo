package system.xmlprocess;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;

import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;

import org.apache.commons.io.FileUtils;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.NumberingDefinitionsPart;
import org.docx4j.org.xhtmlrenderer.util.XRLog;
import org.docx4j.wml.RFonts;


public class DjadeConverter {
	// NOW LETS SET INSTANCE VARIABLES
	private File[] FilePaths;
	private String Ctype;
	private String DefaultPath="cores/core/log/DjadeExtras/";
	
	
	/************************* LETS CONSTRUCT CLASS ***************************/
	public DjadeConverter(File[] filePaths, String ctype){
		// Now lets set values
		FilePaths=filePaths;
		Ctype=ctype;
	}
	
	public DjadeConverter(File filePath, String ctype){
		// Now lets set values
		FilePaths = new File[]{filePath};
		Ctype=ctype;
	}
	
	
	
	
	/**** SETTING MAIN METHOD TO TEST CLASS * @throws DocumentException * @throws IOException 
	 * @throws Docx4JException 
	 * @throws JAXBException 
	 * @throws DocumentException ****/
	public static void main(String[] args) throws IOException, JAXBException, Docx4JException, DocumentException{
		// Now lets call all helper classes
		File files=new File("cores/core/log/DJade_Tmp/michael2837256/tmp_yh/tmp_content/file_26450402.xhtml");
		// File[] FilePaths=files.listFiles();
		// Now lets call our converter
		// DjadeConverter converter=new DjadeConverter(FilePaths, "html2xhtml");
		DjadeConverter converter=new DjadeConverter(files, "xhtml2pdf");
		// DjadeConverter converter=new DjadeConverter(FilePaths, "xhtml2docx");
		System.out.println(converter.convert(null));
	}
	
	
	
	
	/**** CONSTRUCTING THE CONVERTER METHOD * @throws IOException * @throws DocumentException 
	 * @throws Docx4JException 
	 * @throws JAXBException 
	 * @throws DocumentException ****/
	public String convert(String outPutPath) throws IOException, JAXBException, Docx4JException, DocumentException{
		// WE PROCESS TO RETURN A TRACK STRING OF FILES CONVERTERD AND COUNT IN TOTAL
		String[] pdfResponse={};
		int xhtmlResponse=0;
		String ResponseData=null;
		String parentPaths="";
		String fileNames="";
		// Lets check the convert type
		if(Ctype.equals(new String("html2xhtml"))){
			// We process as html to xhtml converter
			// LETS MAKES SURE THAT ALL THE FILES WE ARE PROCCESSING IS HTML
			if(FilePaths.length>0){
				// We start processing
				// Now we can call our converter
				xhtmlResponse=html2Xhtml();
				// Preparing response data
				if(xhtmlResponse>0){
					// We we know how many files got proccessed
					if(xhtmlResponse==FilePaths.length){
						//Lets makes response
						for(int k=0; k<FilePaths.length; k++){
							// Lets add the parentPaths	
							if(parentPaths.indexOf(FilePaths[k].toString().substring(0, FilePaths[k].toString().lastIndexOf("\\")-1))<0){
								if(parentPaths.length()>0){
									parentPaths+=","+FilePaths[k].toString().substring(0, FilePaths[k].toString().lastIndexOf("\\"));
								}
								else{
									parentPaths+=FilePaths[k].toString().substring(0, FilePaths[k].toString().lastIndexOf("\\"));
								}
							}
							// NOW LETS GET ALL FILE NAMES
							if(fileNames.length()>0){
								fileNames+=","+FilePaths[k].toString().substring(FilePaths[k].toString().lastIndexOf("\\")+1, FilePaths[k].toString().length());
							}
							else{
								fileNames+=FilePaths[k].toString().substring(FilePaths[k].toString().lastIndexOf("\\")+1, FilePaths[k].toString().length());
							}
						} // End of loop
						
						// NOW LETS MAKE A COMPLETE RESPONSE
						ResponseData="["+xhtmlResponse+"->"+FilePaths.length+"]["+parentPaths+"]["+fileNames+"]";
					}
				}
				
			}
		}
		else if(Ctype.equals(new String("xhtml2pdf"))){
			// We process as xhtml to pdf converter
				// LETS MAKES SURE THAT ALL THE FILES WE ARE PROCCESSING IS HTML
				if(FilePaths.length>0){
					// We start processing
					// Now we can call our converter
					if(outPutPath==null){
					pdfResponse=xhtml2Pdf(null);
					}
					else{
					pdfResponse=xhtml2Pdf(outPutPath);	
					}
					
					// LETS CHECK THE CONTENTS OF THE RESPONSES
					if(pdfResponse.length>0){
						// Our pdf response is greater than zero
						for(int i=0; i<pdfResponse.length; i++){
							// Lets get parent paths and file names
							// Lets add the parentPaths	
							if(parentPaths.indexOf(pdfResponse[i].substring(0, pdfResponse[i].lastIndexOf("/")-1))<0){
							if(parentPaths.length()>0){
								parentPaths+=","+pdfResponse[i].substring(0, pdfResponse[i].lastIndexOf("/"));
							}
							else{
								parentPaths+=pdfResponse[i].substring(0, pdfResponse[i].lastIndexOf("/"));
							}
							}
							// NOW LETS GET ALL FILE NAMES
							if(fileNames.length()>0){
								fileNames+=","+pdfResponse[i].substring(pdfResponse[i].lastIndexOf("/")+1, pdfResponse[i].length());
							}
							else{
								fileNames+=pdfResponse[i].substring(pdfResponse[i].lastIndexOf("/")+1, pdfResponse[i].length());
							}
							} // End of loop
							
							// NOW LETS MAKE A COMPLETE RESPONSE
							ResponseData="["+pdfResponse.length+"->"+FilePaths.length+"]["+parentPaths+"]["+fileNames+"]";
					}
					
				} // End of FilePaths Check
		}
		else if(Ctype.equals(new String("xhtml2docx"))){
			// We process as xhtml to pdf converter
				// LETS MAKES SURE THAT ALL THE FILES WE ARE PROCCESSING IS HTML
				if(FilePaths.length>0){
					// We start processing
					// Now we can call our converter
					if(outPutPath==null){
					pdfResponse=xhtml2Docx(null);
					}
					else{
					pdfResponse=xhtml2Docx(outPutPath);	
					}
					
					// LETS CHECK THE CONTENTS OF THE RESPONSES
					if(pdfResponse.length>0){
						// Our pdf response is greater than zero
						for(int i=0; i<pdfResponse.length; i++){
							// Lets get parent paths and file names
							// Lets add the parentPaths	
							if(parentPaths.indexOf(pdfResponse[i].substring(0, pdfResponse[i].lastIndexOf("/")-1))<0){
							if(parentPaths.length()>0){
								parentPaths+=","+pdfResponse[i].substring(0, pdfResponse[i].lastIndexOf("/"));
							}
							else{
								parentPaths+=pdfResponse[i].substring(0, pdfResponse[i].lastIndexOf("/"));
							}
							}
							// NOW LETS GET ALL FILE NAMES
							if(fileNames.length()>0){
								fileNames+=","+pdfResponse[i].substring(pdfResponse[i].lastIndexOf("/")+1, pdfResponse[i].length());
							}
							else{
								fileNames+=pdfResponse[i].substring(pdfResponse[i].lastIndexOf("/")+1, pdfResponse[i].length());
							}
							} // End of loop
							
							// NOW LETS MAKE A COMPLETE RESPONSE
							ResponseData="["+pdfResponse.length+"->"+FilePaths.length+"]["+parentPaths+"]["+fileNames+"]";
					}
					
				} // End of FilePaths Check
		}
		
		// Here we return
		return ResponseData;
	}
	
	
	
	/************************ CONSTRUCTING XHTML CONVERTER * @throws IOException ******************/
	private int html2Xhtml() throws IOException{
		// HERE WE CONSTRUCT HTML TO XHTML AFTER CLEANING WITH JTIDY
		int trueValue=0;
		String extName=".html";
		InputStream in;
		FileOutputStream fos;
		String FileName="";
		String FilePath="";
		File newFilePath;
		Document xmlDoc;
		// NOW LETS CHECK THE FILE ARRAY
		if(FilePaths!=null && FilePaths.length>0){
			// WE START PROCESSING AND CONVERTING
			for(int i=0; i<FilePaths.length; i++){
				// WE ARE GOING TO MAKE SURE FILE IS HTML
				if(FilePaths[i].toString().indexOf("html")>=0){ // Yes file is html
				FilePath=FilePaths[i].toString().substring(0, FilePaths[i].toString().lastIndexOf("\\"));
				FileName=FilePaths[i].toString().substring(FilePaths[i].toString().lastIndexOf("\\")+1, FilePaths[i].toString().length());
					// NOW LETS START CONVERTING
					try{
					in=new FileInputStream(FilePaths[i]);
					// Lets store the new file
					newFilePath=new File(FilePath+"/"+FileName.substring(0, FileName.length()-extName.length())+".xhtml");
					fos=new FileOutputStream(newFilePath.toString());
					// Now lets call JTidy
					Tidy tidy=new Tidy();
					// Setting Tidy Header
					tidy.setShowWarnings(false);
					tidy.setShowErrors(0);
					// Lets ensure we call the setXmlTag only on the second conversion
					//tidy.setXmlTags(true);	
					tidy.setInputEncoding("UTF-8");
					tidy.setOutputEncoding("UTF-8");
					tidy.setXHTML(true);
					tidy.setQuiet(true);
					tidy.setMakeClean(true);
					tidy.setForceOutput(true);
					// Now lets transform
					xmlDoc=tidy.parseDOM(in, null);
					tidy.pprint(xmlDoc, fos);
					
					// NOW LETS CLOSE THE FILE OUTPUT
					in.close();
					fos.close();
								
					// HERE WE DELETE THE OLD HTML FILE
					FilePaths[i].delete();
					
					}
					catch(IOException e){
					e.printStackTrace();	
					}
				// LETS INCREMENT THE TRUE VALUE
				trueValue++;
				}
			} // End of loop
		}
		
		// Here we return
		return trueValue;
	}
	
	
	
	/**** CONSTRUCTING XHTML TO PDF CONVERTER * @throws DocumentException * @throws IOException 
	 * @throws JAXBException 
	 * @throws Docx4JException ***/
	private String[] xhtml2Docx(String outPutPath) throws IOException, JAXBException, Docx4JException{
		// LETS DEPLOY FLYING SAUCER TO CONVERT XHTML TO PDF FILE
		String[] trueReturns=new String[FilePaths.length];
		String fileName="";
		String exto="xhtml";
		String extn="docx";
		String toPath="";
		String stringFromFile="";
		// NOW LETS CHECK THE FILE ARRAY
		if(FilePaths!=null && FilePaths.length>0){
			// NOW LETS LOOP THROUGH THE FILES AND PROCESS INDIVIDUALLY
			for(int i=0; i<FilePaths.length; i++){
				// NOW LETS MAKE SURE INDIVIDUAL FILE IS AN XHTML
				if(FilePaths[i].toString().indexOf("xhtml")>=0){ // Yes file is xhtml
					fileName=FilePaths[i].toString().substring(FilePaths[i].toString().lastIndexOf("\\")+1, FilePaths[i].toString().length()-exto.length())+extn;
					// WE START PROCESSING AND CONVERTING
					if(outPutPath==null){
						toPath=DefaultPath+fileName;
					}
					else{
						toPath=outPutPath+fileName;
					}
					// NOW LETS TRY CONVERTING
					try {
						// Now lets read the xhtml file to convert
						stringFromFile = FileUtils.readFileToString(new File(FilePaths[i].toString()), "UTF-8");
						String unescaped = stringFromFile;
						// Preventing error log
						XRLog.setLoggingEnabled(false);
						// Setup font mapping
						RFonts rfonts = Context.getWmlObjectFactory().createRFonts();
						rfonts.setAscii("Century Gothic");
				        XHTMLImporterImpl.addFontMapping("Century Gothic", rfonts);
				        
				        // Create an empty docx package
				        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
						// WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(new File(System.getProperty("user.dir") + "/styled.docx"));
		
						
						NumberingDefinitionsPart ndp = new NumberingDefinitionsPart();
						wordMLPackage.getMainDocumentPart().addTargetPart(ndp);
						ndp.unmarshalDefaultNumbering();		
									
						// Convert the XHTML, and add it into the empty docx we made
				        XHTMLImporterImpl XHTMLImporter = new XHTMLImporterImpl(wordMLPackage);
				        
				        XHTMLImporter.setHyperlinkStyle("Hyperlink");
						wordMLPackage.getMainDocumentPart().getContent().addAll(XHTMLImporter.convert(unescaped, null) );
						wordMLPackage.save(new java.io.File(toPath) );
						
						trueReturns[i]=toPath;
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
				} // End of file path check
			} // End of loop
		}
		
		// Here we return
		return trueReturns;
	}
	
	
	
	
	/**** CONSTRUCTING XHTML TO PDF CONVERTER * @throws DocumentException * @throws IOException 
	 * @throws JAXBException 
	 * @throws Docx4JException ***/
	private String[] xhtml2Pdf(String outPutPath) throws DocumentException, IOException{
	    // LETS DEPLOY FLYING SAUCER TO CONVERT XHTML TO PDF FILE
	    String[] trueReturns=new String[FilePaths.length];
	    String fileName="";
	    String exto="xhtml";
	    String extn="pdf";
	    String url="";
	    FileOutputStream fos=null;
	    String toPath="";
	    // NOW LETS CHECK THE FILE ARRAY
	    if(FilePaths!=null && FilePaths.length>0){
	    	// NOW LETS LOOP THROUGH THE FILES AND PROCESS INDIVIDUALLY
	    	for(int i=0; i<FilePaths.length; i++){
	    		// NOW LETS MAKE SURE INDIVIDUAL FILE IS AN XHTML
	    		if(FilePaths[i].toString().indexOf("xhtml")>=0){ // Yes file is xhtml
	    			url=FilePaths[i].toURI().toURL().toString();
	    			fileName=FilePaths[i].toString().substring(FilePaths[i].toString().lastIndexOf("\\")+1, FilePaths[i].toString().length()-exto.length())+extn;
	    			// WE START PROCESSING AND CONVERTING
	    			if(outPutPath==null){
	    				toPath=DefaultPath+fileName;
	    			}
	    			else{
	    				toPath=outPutPath+fileName;
	    			}
	    			// NOW LETS TRY CONVERTING
	    			try {
	    				fos=new FileOutputStream(toPath);
	    				// Now lets call the ItextRenderer
	    				ITextRenderer renderer=new ITextRenderer();
	    				renderer.setDocument(url);
	    				renderer.layout();
	    				// NOW LETS CREATE PDF
	    				renderer.createPDF(fos);
	    				trueReturns[i]=toPath;
	    			} catch (FileNotFoundException e) {
	    				// TODO Auto-generated catch block
	    				e.printStackTrace();
	    			}
	    			finally {
	    	            if (fos != null) {
	    	                try {
	    	                	// NOW LETS CLOSE THE FILE OUTPUT
	    	    				fos.close();
	    	                } catch (IOException e) { /*ignore*/ }
	    	            }
	    	        }
	    		} // End of file path check
	    	} // End of loop
	    }
	    
	    // Here we return
	    return trueReturns;
	}


// END OF CLASS	
}
