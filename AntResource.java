package system.xmlprocess;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Random;

public class AntResource {
	public DjadeUtil Util = new DjadeUtil();
	public String ResourcePath;
	private int Rate = 30;
	// HERE WE CONSTRUCT CLASS
	public AntResource(){
		try {
			String resource = new File(AntResource.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
			resource = resource.substring(0, (resource.replace("\\", "/").lastIndexOf("/")+1));
			resource = resource.replace("\\", "/");
			ResourcePath = resource;
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/*************** HERE WE IMPLEMENT MAIN METHOD TO TEST ******************/
	public static void main(String[] args){
		AntResource resource = new AntResource();
		try {
			System.out.println(resource.get("setting/config.set"));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/*** HERE WE CREATE ACCESSIBLE METHODS * @throws URISyntaxException ****/
	
	public boolean check() throws URISyntaxException{
		return new Manager().checkResource();
	}
	
	
	public String get(String uri) throws URISyntaxException{
		// Output: C:\Users\DELL\Desktop\PROJECT MAIL ANT\bin
		return new Manager().getResource(uri);
	}
	
	
	/****************** HERE WE CREATE THE RESOURCE CLASS ******************/
	public class Manager{
		private String getResource(String path) throws URISyntaxException{
			// HERE WE CONSTRUCT THE GET RESOURCE PROCESS
			String resourcePath = "";
			String resource = "";
			String create = "";
			// HERE WE START PROCESSING
			if(path!=null){
				if(path.length()>0){
					if(checkResource()){
						// Here we find resource
						resourcePath = ResourcePath+path;
						if(new File(resourcePath).exists()){
							// Common file check
							resource = resourcePath;
						}
						else{
							// Advance file check
							resource = trim(path, find(path, ResourcePath));
						}
					}
					else{
						// Here we create and return resource
						create = createResource();
						if(create!=null){
							if(create.indexOf("C:")>=0 || create.indexOf("c:")>=0){
								if(!create.endsWith("/")){
									create += "/";
								}
								// Here we find created resource
								resource = trim(path, find(path, create));
							}
						}
					}
				}
			}
			// Here we return
			return resource;
		}
		
		private String createResource() throws URISyntaxException{
			// HERE WE CONSTRUCT THE RESOURCE CREATION
			DjadeUtil util = new DjadeUtil();
			String path = "core/craft.txt";
			Make fileMaker = new Make();
			String resourcePath = null;
			String directive = "";
			String craftData = "";
			String resource = "";
			String[] parts = {};
			String parent = "";
			int check = 0;
			// NOW LETS START PROCESSING
			try {
				craftData = util.readBuffer(path, "eclipse");
				if(craftData!=null){
					if(craftData.length()>0){
						// Now lets start process
						if(craftData.indexOf("(")>=0){
							parts = craftData.split("\\(");
							parent = parts[0]+new Random().nextInt(9999999);
							directive = parts[1].substring(0, (parts[1].length()-1));
							// Now lets create parent folder
							resource = ResourcePath;
							resource += parent;
							// NOW LETS MAKE DIRECTORY
							if(fileMaker.makeDir(resource)){
								resource += "/";
								check = fileMaker.makeWizard(resource, directive);
								if(check > 0){
									resourcePath = resource;
								}
							}
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// Here we return
			return resourcePath;
		}
		
		private boolean checkResource() throws URISyntaxException{
			// HERE WE START CONSTRUCTING RESOURCE CHECKER
			String term = "antData.v1_0.2018";
			String resource = "";
			boolean isSet = false;
			// NOW LETS START PROCESSING
			if(ResourcePath!=null){
				if(ResourcePath.indexOf("C:")>=0 || ResourcePath.indexOf("c:")>=0){
					resource = find(term, ResourcePath);
					// Now lets check
					if(resource!=null && resource.length()>0){
						if(resource.indexOf("/")>=0 && resource.indexOf("antDatav1_02018")>=0){
							isSet = true;
						}
					}
				}
			}
			// Here we return
			return isSet;
		}
		
		private String find(String query, String resourcePath){
			// HERE WE CONSTRUCT THE FINDER IMPLEMENTER
			String foundItem = "";
			String[] keywords = {};
			Finder finder;
			// HERE WE START PROCESSING
			if(query!=null && resourcePath!=null){
				if(query.indexOf("/")>=0){
					keywords = query.split("\\/");
				}
				else if(query.indexOf(".")>=0){
					keywords = query.split("\\.");
				}
				else{
					keywords = new String[]{query};
				}
				
				// NOW LETS CALL FINDER
				if(resourcePath.indexOf("C:")>=0 || resourcePath.indexOf("c:")>=0){
					if(keywords.length>0){
						try {
							finder = new Finder(keywords, resourcePath, Rate);
							foundItem = finder.path();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			// Here we return
			return foundItem;
		}
		
		private String trim(String queryPath, String resourcePath){
			String resource = "";
			if(queryPath!=null && resourcePath!=null){
				if(queryPath.endsWith("/")){
					resource = resourcePath.substring(0, resourcePath.indexOf(queryPath)+queryPath.length());
				}
				else{
					if(!resourcePath.contains("empty.txt")){
						resource = resourcePath;
					}
					else{
						resource = resourcePath.substring(0, resourcePath.lastIndexOf("/")+1);
					}
				}
			}
			
			// Here we return
			return resource;
		}
		
		// ENS OF INNER CLASS
	}
	
	
	/****************** HERE WE CREATE SEARCH CLASS ************************/
	public class Finder extends SearchOn{
		// HERE WE CONSTRUCT PUBLIC VARIABLE
		public String XChoice;
		// HERE WE CONSTRUCT CLASS
		public Finder(String[] keyWords, String XData, int relenceRate) throws IOException {
			super(keyWords, XData, relenceRate);
			// TODO Auto-generated constructor stub
		}
		
		
		/**** HERE WE OVERRIDE THE SEARCH HACK * @throws IOException ******/
		@Override
		public void searchHack() throws IOException{
			// HERE WE OVERRIDE THE SEARCH HACK METHOD
			String[][] documents = preparedData();
			String[] data = {};
			int[][] hitData = {};
			String[][] xFields = {};
			String xChoice = null;
			String hits = "";
			// HERE WE START PROCESSING
			if(documents != null){
				if(documents.length > 0){
					data = new String[documents.length];
					for(int b=0; b<documents.length; b++){
						data[b] = documents[b][0];
					} // end of loop
					
					// NOW WE CALL CALL KEYWORD HANDLE ON IH
					KeywordHandle keyhandle = new KeywordHandle(KeyWords, data, "ih", RelenceRate);
					hits = keyhandle.scoreHits();
					if(hits.length() > 0 && hits.indexOf("][")>=0){
						hitData = Util.hitsProcessor(hits);
						if(hitData.length>0){
							xFields = new String[hitData.length][2];
							for(int i=0; i<xFields.length; i++){
								xFields[i][0] = data[hitData[i][0]];
								xFields[i][1] = String.valueOf(hitData[i][1]);
							} // end of loop
							
							// Now lets get xChoice
							xChoice = choose(xFields);
						}
					}
					// Here we set returner
					if(xChoice!=null && xChoice.indexOf("C:")>=0){
						XChoice = xChoice;
					}
				}
			}
		}
		
		
		/******************* HERE WE OVERRIDE THE PREPARE DATA **************/
		@Override
		protected String[][] preparedData() throws IOException{
			// HERE WE RECONSTRUCT THE PREPARE DATA PROCESSES
			String[][] matchItems = {};
			File[] fileLists;
			File basePath;
			File[] fileitems;
			// HERE WE START PROCESSING
			if(DataDir != null){
				basePath = new File(DataDir);
				if(basePath.exists()){
					fileLists = Util.fileFilter(basePath.listFiles());
					if(fileLists != null){
						if(fileLists.length > 0){
							fileitems = Util.getFiles(fileLists);
							if(fileitems != null){
								if(fileitems.length>0){
									matchItems = new String[fileitems.length][1];
									for(int i=0; i<fileitems.length; i++){
										matchItems[i][0] = fileitems[i].toString().replace("\\", "/");
									}
								}
							}
						}
					}
				}
			}
			// Here we return
			return matchItems;
		}
		
		private String choose(String[][] contents){
			String choice = "";
			int[] hits = {};
			int max = 0;
			// Now lets start
			if(contents!=null){
				if(contents.length>0){
					hits = new int[contents.length];
					for(int i=0; i<contents.length; i++){
						hits[i] = Integer.parseInt(contents[i][1]);
					} // end of loop
					
					// Lets get maximum choice
					max = Util.max(hits);
					for(int j=0; j<hits.length; j++){
						if(max == Integer.parseInt(contents[j][1])){
							choice = contents[j][0];
							break;
						}
					}
				}
			}
			// Here we return
			return choice;
		}
		
		public String path(){
			return XChoice;
		}
		// END OF INNER CLASS
	}
	
	
	/*********** HERE WE CREATE CLASS THAT MAKES DIRECTORY ***************/
	public class Make{
		private int makeWizard(String resource, String directive){
			boolean isSingle = false;
			String[] dimension = {};
			String madePath = "";
			String[] parts = {};
			String[] subs = {};
			String main = "";
			int checkValue = 0;
			int trueValue = 0;
			int check = 0;
			if(resource!=null && directive!=null){
				if(directive.indexOf(";")>=0){
					dimension = directive.split("\\;");
					// Now lets loop through each directive to create
					for(int i=0; i<dimension.length; i++){
						if(dimension[i].indexOf("->")>=0){
							// Here folder has sub folders
							parts = dimension[i].split("\\->");
							main = parts[0];
							if(parts[1].indexOf(",")>=0){
								subs = parts[1].split(",");
							}
							else{
								subs = new String[]{parts[1]};
							}
							// Here we set is single
							isSingle = false;
						}
						else{
							// Folder is single
							main = dimension[i];
							isSingle = true;
						}
						
						// HERE WE CHECK IF DIRECTIVE CONTAIN FILE
						madePath = maker(resource, main);
						if(madePath != null){
							if(!isSingle){
								// Here we call the process writer
								if(!madePath.endsWith("/")){
									madePath += "/";
								}
								// Now lets make sub folders
								check = processMaker(madePath, subs);
							}
							
							// re-check the check
							if(check > 0){
								checkValue++;
							}
						}
					} // end of loop
					
					// Here we set the true vale
					if(checkValue >= dimension.length){
						trueValue = 1;
					}
				}
			}
			// Here we return
			return trueValue;
		}
		
		public int processMaker(String resource, String[] sub){
			// HERE WE CONSTRUCT THE PROCESS MAKER
			int trueValue = 0;
			String madePath = "";
			int check = 0;
			// HERE WE START PROCESSING
			if(sub!=null){
				if(sub.length>0){
					for(int i=0; i<sub.length; i++){
						madePath = maker(resource, sub[i]);
						if(madePath!=null){
							check++;
						}
					}
					
					// Here lets check
					if(check > 0){
						if(check == sub.length){
							trueValue = 1;
						}
					}
				}
			}
			// Here we return
			return trueValue;
		}
		
		public String maker(String resource, String data){
			String[] parts = {};
			String filePath = "";
			String filename = "";
			boolean hasFile = false;
			boolean made = false;
			String madePath = null;
			// HERE WE START PROCESSING
			if(resource!=null && data!=null){
				if(data.length()>0){
					// Here we check data
					if(data.indexOf(":")>=0 && data.indexOf("/")>=0){
						// Data has file in it
						parts = data.split("\\:");
						resource += parts[0];
						filePath = parts[1];
						hasFile = true;
					}
					else{
						// Data has no file in it
						resource += data;
						hasFile = false;
						
					}
					
					// HERE WE WRITE FOLDER AND FILE
					made = makeDir(resource);
					// Here lets check file
					if(made){
						if(hasFile){
							// File specified
							try {
								filename = filePath.substring(filePath.lastIndexOf("/"), filePath.length());
								makeFile(filePath, resource+filename);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						madePath = resource;
					}
				}
			}
			
			// Here we return
			return madePath;
		}
		
		private boolean makeDir(String path){
			// HERE WE CONSTRUCT MAKE DIRECTORY
			boolean made = false;
			// HERE WE WRITE FOLDER AND FILE
			File dir = new File(path);
			if(!dir.exists()){
				dir.setReadable(true, false);
				dir.setWritable(true, false);
				dir.mkdir();
			}
			// Here lets check file
			if(dir.exists()){
				made = true;
			}
			
			// Here we return
			return made;
		}
		
		private void makeFile(String in, String out) throws IOException{
			// HERE WE CONSTRUCT THE FILE WRITER PROCESS
			DjadeUtil util = new DjadeUtil();
			FileOutputStream outputStream;
			InputStream inputStream = null;
			// HERE WE START PROCESSING
			if(in!=null && out!=null){
				// Opens input stream from the HTTP connection
				// opens an output stream to save into file
				inputStream = util.getResourceStream(this.getClass(), in);
				outputStream = new FileOutputStream(out);
				int bytesRead = -1;
				byte[] buffer = new byte[4096];
				// We loop to store file
				while((bytesRead=inputStream.read(buffer))!=-1){
					outputStream.write(buffer, 0, bytesRead);
				} // End of while loop
				// Lets close file streams
				outputStream.close();
				inputStream.close();
			}
		}
		
		// END OF INNER CLASS
	}
	
	// END OF MAIN CLASS
}
