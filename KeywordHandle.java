package system.xmlprocess;

import java.util.ArrayList;
import java.util.Arrays;
// import java.util.Arrays;
import java.util.List;

public class KeywordHandle {
	// Setting public variable
	private String[] scoreWords={};
	private String scorePostions;
	private String scoreKeys="";
	private String typeSearch;
	private int acceptedRate;
	
	// LETS CONSTRUCT CLASS
	public KeywordHandle(String[] keywords, String[] datafields, String type, int rate){
	// LETS SET THE TYPE
		typeSearch=type;
		acceptedRate=rate;
	// LETS CALL PROCESSOR METHOD TO PROCESS KEYWORDS AND DATA
		keywordProcessor(keywords, datafields);
		// End of constructor
	}
	
	/************* SETTING MAIN METHOD FOR TESTING ********************************/
	 public static void main(String[] args){
	 String[] keywords={"is", "the", "google", "facebook"};
	 String[] datas={"google is a search engine", "facebook is is is a social network", "Twitter the best is to google"};
	 KeywordHandle keyhandle=new KeywordHandle(keywords, datas, "ih", 30);
	 // Now lets display
	 if(keyhandle.typeSearch.equals(new String("hw"))){
	 	System.out.println(Arrays.toString(keyhandle.scoreWords()));
	 }
	 else if(keyhandle.typeSearch.equals(new String("hp")) || keyhandle.typeSearch.equals(new String("hpi"))){
	 	System.out.println(keyhandle.scorePositions());	
	 }
	 else{
	 	System.out.println(keyhandle.scoreHits());
	 }
	 }

	
	/************** SETTING THE KEYWORD PROCESSOR METHOD *************/
	private void keywordProcessor(String[] keyWords, String[] dataFields){
		// Setting local variable we can use to know count
		int countKey=keyWords.length;
		int dataCount=dataFields.length;
		int hitcount=0;
		int ocCount=0;
		int hitIndex=0;
		int keys=0;
		int hitPercent=0;
		int percent=100/1;
		String topData="";
		List<String> positions=new ArrayList<String>();
		String[] aposition={};
		scoreWords=new String[dataCount];
		String cordTrack="";
		// NOW LETS GET THE KEY WORDS AND THE DATAFIELDS TO PROCESS
		for(String data:dataFields){
		// Now lets search for word in individual datafields
		for(int i=0; i<=(countKey-1); i++){
		// Now lets check
			hitIndex=data.indexOf(keyWords[i]);
			// Lets check
			if(hitIndex>=0){ // We found index
				// Lets store positions
				positions.add(String.valueOf(hitIndex));
				// We increment hitcount
				hitcount++;
				// Lets clear hitIndex
				hitIndex=0;
			}
			
			// Getting the occurrences of each keyword
			for(int k=data.indexOf(keyWords[i]); k>=0; k=data.indexOf(keyWords[i], k+1)){
				// Now lets get occurrence
				ocCount++;
			} // End of occurence loop
			
		} // End of static loop
		
		// HERE WE CHECK THE HITS WE FOUND IT MUST BE ABOVE 30 %
		if(hitcount>0 && hitcount<=countKey){
			// Now we get the percentage of hits
			hitPercent=(hitcount*percent/countKey);
			// Now we check the percentage of key before adding
			if(hitPercent>=acceptedRate){
				// Now we are free to add
				if(topData.length()>0){
					topData="";
				}
				
				/********** WE CHECK THE TYPE TO KNOW HOW TO RETURN *********/
				if(typeSearch.equals(new String("hw"))){
				// Now lets set top data
				topData+="["+String.valueOf(hitcount)+"->"+String.valueOf(ocCount)+"]"+data;
				// Now lets add the toData array to the keyScore
				scoreWords[keys]=topData;
				}
				else if(typeSearch.equals(new String("ih"))){
				// Now lets set to keys and hits
				topData+="["+keys+"]["+String.valueOf(hitcount)+"->"+String.valueOf(ocCount)+"]";
				// Now lets set our keys and hits
				if(scoreKeys.length()>0){
					scoreKeys+="+"+topData;	
				}
				else{
					scoreKeys+=topData;
				}
				
				}
				else if(typeSearch.equals(new String("hp"))){
				// Now lets get the coordinates of each hits
				aposition=positions.toArray(new String[positions.size()]);
				String cordinates="";
				// Now let set the count of each keys
				for(String cord:aposition){
					// NOW LETS SET FOR CORDINATES
					if(cordinates.length()>0){
					cordinates+=","+cord;
					}
					else{
					cordinates+=cord;	
					}
				} // End of loop
				
				// NOW LETS GET THE COUNT OF CORDINATES
				if(cordTrack.length()>0){
					cordTrack+="->"+hitcount;
					}
					else{
					cordTrack+=hitcount;	
					}
				
				// Now we set
				scorePostions="["+cordTrack+"]+["+cordinates+"]";
				}
				else if(typeSearch.equals(new String("hpi"))){
					// Now lets get the coordinates of each hits
					aposition=positions.toArray(new String[positions.size()]);
					String cordinates="";
					// Now let set the count of each keys
					for(String cord:aposition){
						// NOW LETS SET FOR CORDINATES
						if(cordinates.length()>0){
						cordinates+=","+cord;
						}
						else{
						cordinates+=cord;	
						}
					} // End of loop
					
					// NOW LETS GET THE COUNT OF CORDINATES
					if(cordTrack.length()>0){
						cordTrack+="->"+hitcount+"@"+keys;
						}
						else{
						cordTrack+=hitcount+"@"+keys;	
						}
					
					// Now we set
					scorePostions="["+cordTrack+"]+["+cordinates+"]";
					}
			}
		} // End of hitcount check
		// Increment key
		keys++;
		// Here we can unset the topData and the hitcount to start process again
		hitcount=0;
		// Here we set occurrence count to zero
		ocCount=0;
		} // End of for loop
		
		// HERE WE TRY TO COMPOSE ARRAY TO REMOVE NULL
		List<String> list=new ArrayList<String>();
		for(String s:scoreWords){
			if(s!=null && s.length()>0){
				list.add(s);
			}
		} // End of loop
		
		// Now we change data of array
		scoreWords=list.toArray(new String[list.size()]);
		
		// End of method
	}

	

	/************** HERE WE SET METHOD TO RETURN THE SCORE KEY ARRAY ***************/
	public String[] scoreWords(){
		// Now we can return scoreHits array
			return scoreWords;
		// End of method
	}
	
	
	public String scoreHits(){
		// Now we can return scoreHits array
			return scoreKeys;
		// End of method
	}
	
	
	public String scorePositions(){
		// Now we can return scorePositions array
			return scorePostions;
		// End of method
	}
	
	
	// End of class
}
