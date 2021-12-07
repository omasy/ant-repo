package system.xmlprocess;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
//import java.net.MalformedURLException;
import java.net.URL;
//import java.util.Arrays;
import java.util.Random;

public class DjadeTmp {
// LETS INITIALIZE PUBLIC VARIABLES
private URL Url;
private URL[] Urls;
private File FilePath;
private File[] FilePaths;
private String Content;
private String[] Contents;
private String TmpMeta;
private URL BaseURL;
private String Type;
private String Control;
private String Extension;
private String[] TmpDatas;
private String TMP_DIR="cores/core/log/DJade_Tmp/";
DjadeUtil utils;
// LETS CONSTRUCTOR THE CLASS
public DjadeTmp(URL url, String tmp_meta){
// Lets set variables
Url=url;
TmpMeta=tmp_meta;
Type="single";
Control="us"; // url single
}



public DjadeTmp(URL[] url, String tmp_meta){
// Lets set variables
Urls=url;
TmpMeta=tmp_meta;
Type="multiple";
Control="um"; // url multiple
}



public DjadeTmp(File filePath, String tmp_meta){
// Lets set variables
FilePath=filePath;
TmpMeta=tmp_meta;
Type="single";
Control="fs"; // File single
}



public DjadeTmp(File[] filePaths, String tmp_meta){
// Lets set variables
FilePaths=filePaths;
TmpMeta=tmp_meta;
Type="multiple";
Control="fm"; // File multiple
}



public DjadeTmp(String content, String extension, URL baseUrl, String tmp_meta){
// Lets set variables
Content=content;
Extension=extension;
TmpMeta=tmp_meta;
BaseURL=baseUrl;
Type="single";
Control="cs"; // File single	
}



public DjadeTmp(String[] contents, String extension, String tmp_meta){
// Lets set variables
Contents=contents;
Extension=extension;
TmpMeta=tmp_meta;
Type="multiple";
Control="cm"; // File single	
}



public DjadeTmp(){} // A neutral constructor for instantiating DjadeTmp for delete extras




/**************** LETS CONSTRUCT A MAIN METHOD TO TEST CLASS * @throws MalformedURLException****/
public static void main(String[] args){
	try {
		DjadeTmp tmpWiz=new DjadeTmp(new URL("https://www.tutorialspoint.com"), "[michael2][This is the content of the temporal data header]");
		try {
			String[] after = tmpWiz.save(null);
			System.out.println(after[0]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}




	
/******************** CREATING THE SAVE METHOD 
 * @throws IOException *********************/
public String[] save(String exTmpPath) throws IOException{
// NOW LETS CONSTRUCT LOGIC FOR TEMPORAL SAVE OF FILES OR DOCUMENT
	// HERE WE SET VARIABLES TO CONTAIN FILE AND URL TMP_DIR
	String[] fileReturn={};
	String FileData="";
	String tmpName="";
	String headerData="";
	String headerName="";
	boolean headerCreate=false;
	String[] metaData;
	String tmpPart="";
	String headerPart="";
	String tmpDir="";
	String deDir="";
	String onContent="tmp_content/";
	File theDir;
	File newTheDir;
	// Lets initiate util
	utils=new DjadeUtil(); // Instantiating the util
	// LETS CHECK THE TEMPORAL NAME TO KNOW HOW TO NAME
	if(TmpMeta!=null){
	// Lets process the tmpMedata
	metaData=TmpMeta.split("]");
	tmpPart=metaData[0].substring(1);
	headerPart=metaData[1].substring(1);
	// Now lets check the tmp Name part
	if(!tmpPart.equals(new String("n"))){
		// We set the tmpName as the name passed
		tmpName=tmpPart;
	}
	else{
		tmpName="tmp_"+"_"+new Random().nextInt(999999999);
	}
	// Now lets check for the header data
	if(!headerPart.equals(new String("n"))){
		headerData=headerPart;
		headerCreate=true;
	}
	else{
		headerCreate=false;
	}
	// End of meta data logic	
	}
	else{
	tmpName="tmp_"+"_"+new Random().nextInt(999999999);
	}
	// LETS MODIFY THE TEMPORAL NAME
	if(headerCreate==true){ // Here we add the header directory
		tmpName+="/tmp_yh/tmp_content/";
	}
	else{ // Here we add just the content path
		tmpName+="/tmp_nh/tmp_content/";
	}
	// NOW LETS PROCEED TO CREATING DIRECTORIES
	if(exTmpPath==null){
	tmpDir=TMP_DIR+tmpName;
	// Lets create a new directory to save temporal downloaded file
	theDir=new File(TMP_DIR+tmpName);
	}
	else{
	tmpDir=exTmpPath+tmpPart;
	// Lets create a new directory to save temporal downloaded file
	theDir=new File(exTmpPath);	
	}
	boolean cfResult=false;
	if(!theDir.exists()){
		try{
			// Lets set file permissions
			theDir.setReadable(true, false);
			theDir.setWritable(true, false);
			theDir.mkdirs(); // here we have made directory	
			cfResult=true; // here we initiate result
		}
		catch(SecurityException se){
			// Handle
			se.printStackTrace();
		}
	}
	else{
		// We make sure we are not trying to log same name
		if(exTmpPath==null){ // Checking is extmpPath is sent
		deDir=TMP_DIR+tmpPart+new Random().nextInt(9999999)+tmpName.substring(tmpPart.length(), tmpName.length());
		tmpDir=deDir;
		newTheDir=new File(deDir);
		if(!newTheDir.exists()){
			try{
				// Lets set file permissions
				newTheDir.setReadable(true, false);
				newTheDir.setWritable(true, false);
				newTheDir.mkdirs(); // here we have made directory	
				cfResult=true; // here we initiate result
			}
			catch(SecurityException se){
				// Handle
				se.printStackTrace();
			}
		}
		} // END OF EXTERNAL TMP PATH CHECK
	}
	
	
	// LETS LOG THE HEADER DATA PROVIDED
	if(headerCreate==true){
	// Here lets create the data for the header
			headerName="tmp_header_Instruct.txt";
			utils.writeByBuffer(headerData, tmpDir.substring(0, tmpDir.length()-onContent.length())+headerName);
			// Now lets log
	}
	
	/***************** HERE WE CAN IMPLEMENT CONTROL AND TYPE LOGICS ************/
	// Lets first check the process type
	if(Type.equals(new String("single"))){ // We process as single
		// Let us check control type
		if(Control.equals(new String("us"))){ // We are running url single
			if(exTmpPath==null){ // Checking is extmpPath is sent
			// Now lets download and log file in the temporal directory
			if(cfResult==true){
			FileData+=utils.download(Url.toString(), tmpDir); // Lets download file
			}
			}
			else{
			FileData+=utils.download(Url.toString(), tmpDir); // Lets download file	
			}
			
		}
		else if(Control.equals(new String("fs"))){ // We are running url single
			if(exTmpPath==null){ // Checking is extmpPath is sent
			// Lets save file to directory
			if(cfResult==true){
				FileData+=utils.saveFile(FilePath.toString(), tmpDir);
			} // end of cfresult
			}
			else{
				FileData+=utils.saveFile(FilePath.toString(), tmpDir);
			}
		}
		else if(Control.equals(new String("cs"))){ // We are running content single
			if(exTmpPath==null){ // Checking is extmpPath is sent
			// Lets save file to directory
			if(cfResult==true){
				// Lets check base url for content single
				if(BaseURL!=null){
				FileData+=utils.writeStringAsStream(Content, BaseURL, tmpDir+new Random().nextInt(999999999)+"."+Extension);
				}
				else{
				FileData+=utils.writeStringAsStream(Content, null, tmpDir+new Random().nextInt(999999999)+"."+Extension);	
				}
			} // end of cfresult
			}
			else{
				if(BaseURL!=null){
				FileData+=utils.writeStringAsStream(Content, BaseURL, tmpDir+new Random().nextInt(999999999)+"."+Extension);
				}
				else{
				FileData+=utils.writeStringAsStream(Content, null, tmpDir+new Random().nextInt(999999999)+"."+Extension);	
				}
			}
		}
	}
	else if(Type.equals(new String("multiple"))){ // We process as multiple
		// Let us check control type
		if(Control.equals(new String("fm"))){ // We are running url multiple
		// Let us check control type
		if(cfResult==true){
		// We have to loop to download all url sent
		for(int i=0; i<FilePaths.length; i++){
		// Lets download individual Files
		if(FileData.length()>0){	
		FileData+="->"+utils.saveFile(FilePaths[i].toString(), tmpDir); // Lets save file
		}
		else{
		FileData+=utils.saveFile(FilePaths[i].toString(), tmpDir); // Lets save file	
		}
		} // End of loop
		} // end of cfresult
		}
		else if(Control.equals(new String("um"))){ // We are running url multiple
			// Now lets download and log file in the temporal directory
			if(cfResult==true){
			// We have to loop to download all url sent
			for(int i=0; i<Urls.length; i++){
			// Lets download individual Files
			if(FileData.length()>0){	
			FileData+="->"+utils.download(Urls[i].toString(), tmpDir); // Lets download file
			}
			else{
			FileData+=utils.download(Urls[i].toString(), tmpDir); // Lets download file	
			}
			} // End of loop
			}
		}
		else if(Control.equals(new String("cm"))){ // We are running url multiple
			// Now lets download and log file in the temporal directory
			if(cfResult==true){
			// We have to loop to download all url sent
			for(int i=0; i<Contents.length; i++){
			// Lets download individual Files
			if(FileData.length()>0){	
			FileData+="->"+utils.writeStringAsStream(Contents[i], null, tmpDir+new Random().nextInt(999999999)+"."+Extension); // Lets download file
			}
			else{
			FileData+=utils.writeStringAsStream(Contents[i], null, tmpDir+new Random().nextInt(999999999)+"."+Extension); // Lets download file	
			}
			} // End of loop
			}
		}
	}
	
	/******************* SAVE MAIN PROCESSING BEGINS HERE ********************/
	if(Control.equals(new String("fs")) || Control.equals(new String("fm")) || Control.equals(new String("cs"))){
		// We set data returned
		fileReturn=new String[1];
		fileReturn[0]=FileData;
	}
	else{
		// We process data return
		String[] FileDatas;
		String[] getEach;
		String FileString;
		int keys=0;
		// We first check
		if(FileData.indexOf("->")>=0){ // Here we check if file is many
		// Lets break
		FileDatas=FileData.split("\\->");
		fileReturn=new String[FileDatas.length];
		// Now lets loop
		for(String data:FileDatas){
		getEach=data.split("\\+");
		FileString=getEach[0];
		// Now lets get each path
		if(FileString.length()>0 && FileString.indexOf("/")>=0){
		String path=FileString.substring(0, FileString.lastIndexOf("/"));
		String file=FileString;
		// Now lets join
		fileReturn[keys]="["+path+"]=["+file+"]";
		} // End of fileString Check
		// Lets increment key
		keys++;
		} // End of loop
		}
		else{
		// We process as single
		fileReturn=new String[1];
		getEach=FileData.split("\\+");
		FileString=getEach[0];
		// Now lets get each path
		// Now lets get each path
		if(FileString.length()>0 && FileString.indexOf("/")>=0){
		String path=FileString.substring(0, FileString.lastIndexOf("/"));
		String file=FileString;
		// Now lets join
		fileReturn[0]="["+path+"]=["+file+"]";
		} // End of fileString check
		}
	}
	// Now lets assign the file data to the environment so we can delete it
	TmpDatas=fileReturn;
	
	// Now lets return
	return fileReturn;
}


/******************** CREATING THE SAVE AS REAL FILE METHOD 
 * @throws IOException *********************/
public String[] rSave(String savePath) throws IOException{
	// HERE ALL WE DO IS CHANGE THE PATH TO THE TEMPORAL DIRECTORY
	String[] fileReturn;
	TMP_DIR=savePath;
	// Now lets call the save method
	fileReturn=save(null);
	// Now lets return
	return fileReturn;
}



/******************** CREATING THE SAVE METHOD *********************/
public int delete(String[] tmp_Dir){
	// LETS CONSTRUCT A DELETE MECHANISM FOR TEMPORAL FILES TmpDatas
	String[] tmpPaths={};
	String[] tmpFiles;
	String tmpDir="";
	String onContent="tmp_content/";
	String motherDir="tmp_yh/tmp_content/";
	String metaPath="";
	int returner=0;
	// Lets process the tmp_files
	if(tmp_Dir.length>0 && tmp_Dir!=null){
		// Here we assume you provided paths to delete
		tmpPaths=new String[tmp_Dir.length];
		for(int j=0; j<tmp_Dir.length; j++){
			// Now let us store the passed file to the method
			tmpPaths[j]=tmp_Dir[j];
		} // End of loop
	}
	else{
		// Here we are using the tmpDatas to process
		// We first have to process file data
		if(TmpDatas.length>0){
			// Lets set the path array length
			tmpPaths=new String[TmpDatas.length];
			// Lets loop to get individual paths
			for(int j=0; j<TmpDatas.length; j++){
				// Now lets get individual datas
				tmpFiles=TmpDatas[j].split("=");
				tmpDir=tmpFiles[0];
				// Now lets assign to the paths
				tmpPaths[j]=tmpDir;
			} // End of loop
		}
		
	}
	
	// NOW LETS DELETE INDIVIDUAL FOLDERS
	for(String dir:tmpPaths){
		// Now lets delete after processing to file
		File Dir=new File(dir);
		File mDir=new File(dir.substring(0, dir.length()-motherDir.length()));
		if(Dir.exists()){ // Here we check if directory exists
		// Lets check if header file is setted
		metaPath=dir.substring(0, dir.length()-onContent.length());
		File FileMeta=new File(metaPath);
		
		// Lets get the files of the directories
		File[] DirFiles=Dir.listFiles();
		if(DirFiles.length>0){ // Here we know we have files i the folder
			// Now lets delete individual files in the temporal directory
			for(File f:DirFiles){
				f.delete();
			} // End of loop
			Dir.delete(); // We delete folder
		}
		else{ // Here we know file is not in the folder
			Dir.delete(); // We delete folder
		}
		
		// Now lets check if Temporal directory is deleted
		if(!Dir.exists()){
			// Here we try to delete the meta directory
			File[] metaFiles=FileMeta.listFiles();
			if(metaFiles.length>0){
				// Now lets delete files of the meta
				for(File mf:metaFiles){
					mf.delete();
				}
				// Now lets delete meta path
				FileMeta.delete();
			}
			else{ // Here we know file is not in the folder
				FileMeta.delete();
			}
				
			// Now lets check if Meta directory is deleted
			if(!FileMeta.exists()){
				// Here we try to delete the parent directory
				File[] parentFiles=mDir.listFiles();
				if(parentFiles.length>0){
					// Here we delete files in the parent
					for(File pf:parentFiles){
						pf.delete();
					}
					// Now lets delete the parent
					mDir.delete();
				}
				else{ // Here we know file is not in the folder
					 mDir.delete();
				}
			}
				
			// NOW IF PARENT IS DELETED LETS INCREMENT COUNT
			if(!mDir.exists()){
				returner++; // Here we incremented the counter
			}
		}
		} // End of Directory check metaPath
		
	} // End of loop
	
	// HERE LETS RETURN COUNTER
	return returner;
}

// END OF CLASS	
}
