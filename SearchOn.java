package system.xmlprocess;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class SearchOn {
	// SETTING INSTANCE VARIABLE
	protected String[] KeyWords;
	protected String DataDir;
	private String[] DataArray;
	private String[] XFields;
	protected String Type;
	protected int RelenceRate;
	public String[] searchHits;
	DjadeUtil util;
	
	/*************** LETS CONSTRUCT THE SEARCH CLASS 
	 * @throws IOException **************/
	public SearchOn(String[] keyWords, String XData, int relenceRate) throws IOException{
		KeyWords=keyWords;
		DataDir=XData;
		Type="ft";
		RelenceRate=relenceRate;
		// CALLING SEARCH HACK FROM CONSTRUCTOR
		searchHack();
		// End of constructor
	}
	
	/************ SECOND CONSTRUCTOR FOR ACCEPTING ARRAY 
	 * @throws IOException *********/
	public SearchOn(String[] keyWords, String[] dataArray, int relenceRate) throws IOException{
		KeyWords=keyWords;
		DataArray=dataArray;
		Type="at";
		RelenceRate=relenceRate;
		// CALLING SEARCH HACK FROM CONSTRUCTOR
		searchHack();
		// End of constructor
	}
	
	
	
	/*************** SETTING PUBLIC METHOD TO TEST THIS HANDLER 
	 * @throws IOException **************/
	public static void main(String[] args) throws IOException{
		// Lets call the class constructor
		String[] keywords={"smitha", "jobs-alert", "Servlets", "Database", "Manager"};
		// Call
		SearchOn search=new SearchOn(keywords, "cores/core/testData/datadir/", 30);
		// NOW LETS PRINT RESULT
		System.out.println(Arrays.toString(search.dataOutput()));
		System.out.println("\n");
		System.out.println(Arrays.toString(search.hitsOutput()));
	}
	
	
	
	/**************** HERE WE CONSTRUCT THE SEARCHHACK METHOD ****************/
	public void searchHack() throws IOException{
		/*********** CHECKING TYPE TO KNOW HOW TO READ ************/
		// Lets get the datas
		String[][] documents=preparedData();
		// String[] dataFields={};
		String[] xFields={};
		// NOW LETS CHECK TYPE OF PROCESSING TO KNOW HOW TO GET DATA
		if(Type.equals(new String("ft"))){
			// Here we know we are processing file datas
			// Lets make an array of the data and pass it to keyword handle
			// for(int i=0; i<documents[0].length; i++){
			//	dataFields[i]=documents[0][i];
			// } // End of loop
			
			// NOW WE CALL CALL KEYWORD HANDLE ON IH
			KeywordHandle keyhandle=new KeywordHandle(KeyWords, documents[0], "ih", RelenceRate);
			// Now lets use returned data
			String handleData=keyhandle.scoreHits();
			
			/********* NOW WE ARE GOING TO PROCESS THE HITS STRING TO FIND THE DATA *******/
			String[] splitData=handleData.split("\\+");
			String[] inSplit;
			// Now lets further process split data
			if(splitData.length>0){
			// Setting Counter
			int	docCount=0;
			searchHits=new String[splitData.length];
			xFields=new String[splitData.length];
			for(String data:splitData){
				// NOW LETS FURTHER PROCESS
				inSplit=data.split("\\]");
				// Now we can get our array qualified index and hits
				int index=Integer.parseInt(inSplit[0].substring(1));
				searchHits[docCount]=inSplit[1].substring(1);
				// For the index we use it to select the qualified data
					xFields[docCount]=documents[1][index];
							
							// Increment count
							docCount++;
			} // End of loop
			} // End of split count check
			
		}
		else if(Type.equals(new String("at"))){
			// Here we know we are processing as string array
			// NOW WE CALL CALL KEYWORD HANDLE ON IH
			KeywordHandle keyhandle=new KeywordHandle(KeyWords, documents[0], "hw", RelenceRate);
			// Now lets use returned data
			String[] handleData=keyhandle.scoreWords();
			
			/********* NOW WE ARE GOING TO PROCESS THE HITS STRING TO FIND THE DATA *******/
			if(handleData.length>0){
			// Setting counter	
			int	docCount=0;
			String[] splitData;
			xFields=new String[handleData.length];
			searchHits=new String[handleData.length];
			// Now lets loop	
			for(String data:handleData){
				// NOW LETS GET THE DATA AND STORE TO XFIELDS AND STORE HITS TO HITS
				splitData=data.split("\\]");
				// Now lets get each parts
				searchHits[docCount]=splitData[0].substring(1);
				xFields[docCount]=splitData[1];
				// Lets increment doc count
				docCount++;
			} // End of foreach loop
			} // End of handle data check
		}
		
		/************ NOW LETS ASSIGN XFIELD VALUE TO THE ENVIRONMENT *************/
		if(xFields.length>0){
			XFields=xFields;
		} // End of xfields count check
		// End of method
	}
	
	
	
	
	
	
	/**************** HERE WE CONSTRUCT THE SEARCHHACK METHOD ****************/
	protected String[][] preparedData() throws IOException{
		/*********** WE START PROCESSING ************/
		String[][] returnArray = null;
		String[] dataArray={};
		String[] pathArray={};
		// Checking the type
		if(Type.equals(new String("ft"))){
			// WE ITERATE THROUGH FILE TO GET DATAS IN ARRAY FORM
			// Lets check if xData is a Directory, file or String
			final Path docDir = Paths.get(DataDir);
			// Now lets check if data Dir is directory
			if(Files.isDirectory(docDir)){
			// HERE WE PROCESS AS FOLDER TO RETRIEVE FILES DATA
				File[] files=new File(DataDir).listFiles();
				util=new DjadeUtil(); // Instantiating util class
				// Now lets call the get file method to get file
				File[] allFiles=util.getFiles(files);
				// NOW LETS ITERATE THROUGH SAVED FILES TO GET OUR VALUE
				if(allFiles.length>0){
					// Now let us get individual file
					int fileCounter=0;
					dataArray=new String[allFiles.length];
					pathArray=new String[allFiles.length];
					// Now lets loop
					for(File file:allFiles){
						// Lets start processing
						try{
				        	// WE READ AND STORE FILE IN DATA BEFORE STORING
				            String contentData=util.readByScanner(file.toString());
				            // NOW LETS USE THE DATA WE JUST READ AND SEARCH
				            dataArray[fileCounter]=contentData;
				            pathArray[fileCounter]=file.getPath();
				        	}
				        	catch(IOException catche){
				        		// don't index files that can't be read.	
				        	}
						fileCounter++;
					} // End of loop
				} // end of file length check
				
				
				
			}
			else{
	 	    // Lets check if query is a file
	 	    File cfile=new File(DataDir);
	 	    // Now lets check
	 	    if(cfile.isFile()){
	 	    // HERE WE PROCESS AS A FILE TO RETRIEVE FILE DATA
	 	    InputStream stream = Files.newInputStream(docDir);
	 	    // WE READ AND STORE FILE IN DATA BEFORE STORING
	 	    BufferedReader br=new BufferedReader(new InputStreamReader(stream));
	 	    String strLine;
	 	    String contentData="";
	 	    // Now lets loop
	 	    while((strLine=br.readLine())!=null){
         	  // Now lets now
         	  contentData+=strLine;
	 	    }
	 	    // NOW LETS USE THE DATA WE JUST READ AND SEARCH
            dataArray[0]=contentData;
            pathArray[0]=docDir.toString();
	 	    
	 	    }
	 	    else{
	 	    // HERE WE PROCESS AS A STRING OF TEXT
	 	    dataArray[0]=DataDir;
	        pathArray[0]=null;
	 	    }
			} // End of directory check else
	 	    
			/************ NOW LETS ADD TO THE TWO DIMENSIONAL ARRAY **************/
			int dataLength=dataArray.length;
			int pathLength=pathArray.length;
			// NOW LETS CHECK
			if(dataLength>0 && pathLength>0){
				// NOW LETS INITIATE THE ARRAY AND ADD
				returnArray=new String[2][dataLength];
				// Now lets add the datas
					for(int i=0; i<dataLength; i++){
					returnArray[0][i]=dataArray[i];
					returnArray[1][i]=pathArray[i];
				} // End of static loop
			}
		}
		else if(Type.equals(new String("at"))){
			// WE ASSIGN THE ARRAY TO THE RETURNER AFTER CHECKING ARRAY
			if(DataArray.length>0){
				// Now we can add to the array knowing the size
				// NOW LETS INITIATE THE ARRAY AND ADD
				returnArray=new String[1][DataArray.length];
				// Now lets add the datas
				for(int i=0; i<DataArray.length; i++){
				returnArray[0][i]=DataArray[i];
				} // End of loop
			}
		}
		
		return returnArray;
		// End of method
	}
	
	
	
	/****************** LETS CONSTRUCT THE RETURN METHOD *****************/
	public String[] dataOutput(){
	// LETS RETURN THE PREPARED FIELDS TO USE	
	return XFields;	
	}
	
	
	
	/****************** LETS CONSTRUCT THE HITS RETURN METHOD ************/
	public String[] hitsOutput(){
	// LETS RETURN THE HITS STORED IN ARRAY
	return searchHits;
	}
	
	
	
// END OF CLASS	
}
