package system.xmlprocess;

//import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
//import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

//import java.io.File;
//import java.io.IOException;

public class HtmlToPlainText {
	// SETTING INSTANCE VARIABLES
    Document Doc;
	private String Selector;
	private String Type;
	
	
	/******************* LETS CONSTRUCT CLASS *************************************/
	public HtmlToPlainText(Document doc){
		Doc=doc;
		Type="all";
	}
	
	
	public HtmlToPlainText(Document doc, String selector){
		Doc=doc;
		Selector=selector;
		Type="selected";
	}
    
    /******************* SETTING MAIN METHOD TO TEXT CLASS ********
    public static void main(String[] args) throws IOException {
        // Setting the file path
    	File htmlFile=new File("cores/core/log/DJade_Tmp/google/tmp_yh/tmp_content/file_201735847.html");
        // fetch the specified URL and parse to a HTML DOM
        Document doc = Jsoup.parse(htmlFile, "UTF-8");

        HtmlToPlainText formatter = new HtmlToPlainText(doc);
        
        String plainText = formatter.formatted();
        System.out.println(plainText);
    }
   ****************/

    
    
    /******************* CONSTRUCTING THE FORMATTING INITIALZER METHOD ******************/
    public String formatted(){
    	// NOW LETS START PROCESSING TEXT FORMATER
    	String plainText="";
    	// Now lets check parts
    	if (Type.equals(new String("selected"))) {
            Elements elements = Doc.select(Selector); // get each element that matches the CSS selector
            for (Element element : elements) {
            	if(plainText.length() > 0){
            		plainText += "\n\n"+getPlainText(element);
            	}
            	else{
            		plainText = getPlainText(element);
            	}
            }
        } 
    	else { // format the whole doc
            plainText = getPlainText(Doc);
        }
    	
    	// Now lets return
    	return plainText;
    }
    
    
	/** FORMAT AN ELEMENT TO PLAIN TEXT* @param root element to format* @return format ***/
	private String getPlainText(Element element) {
		FormattingVisitor formatter = new FormattingVisitor();
		NodeTraversor traversor = new NodeTraversor(formatter);
		traversor.traverse(element); // walk the DOM, and call .head() and .tail() for each node

    	return formatter.toString();
	}

    
    
    
	/**********THE FORMATTING RULES, implemented in a breadth-first DOM traverse ********/
	private class FormattingVisitor implements NodeVisitor {
		private static final int maxWidth = 80;
		private int width = 0;
		private StringBuilder accum = new StringBuilder(); // holds the accumulated text

    
    
		/******************** HIT WHEN THE NODE IS FIRST SEEN *************************/
		public void head(Node node, int depth) {
			String name = node.nodeName();
			if (node instanceof TextNode)
				append(((TextNode) node).text()); // TextNodes carry all user-readable text in the DOM.
            else if (name.equals("li"))
                append("\n * ");
            else if (name.equals("dt"))
                append("  ");
            else if (StringUtil.in(name, "p", "h1", "h2", "h3", "h4", "h5", "tr"))
                append("\n");
  		}

  
  
		/******* hit when all of the node's children (if any) have been visited ********/
		public void tail(Node node, int depth) {
			String name = node.nodeName();
			if (StringUtil.in(name, "br", "dd", "dt", "p", "h1", "h2", "h3", "h4", "h5"))
				append("\n");
			else if (name.equals("a"))
				append(String.format(" ***%s***", node.absUrl("href")));
  		}

  
  
		/******** appends text to the string builder with a simple word wrap method *****/
		private void append(String text) {
			if (text.startsWith("\n"))
				width = 0; // reset counter if starts with a newline. only from formats above, not in natural text
      			if (text.equals(" ") && (accum.length() == 0 || StringUtil.in(accum.substring(accum.length() - 1), " ", "\n")))
      				return; // don't accumulate long runs of empty spaces

      			if (text.length() + width > maxWidth) { // won't fit, needs to wrap
      				String words[] = text.split("\\s+");
      				for (int i = 0; i < words.length; i++) {
      					String word = words[i];
      					boolean last = i == words.length - 1;
      					if (!last) // insert a space if not the last word
      						word = word + " ";
      					if (word.length() + width > maxWidth) { // wrap and reset counter
      						accum.append("\n").append(word);
      						width = word.length();
      					} else {
      						accum.append(word);
      						width += word.length();
      					}
      				}
      			}
      			else { // fits as is, without need to wrap text
      				accum.append(text);
      				width += text.length();
      			}
  		}

  

		/******************* SETTING THE TOSTRING METHOD *************************/ 
		@Override
		public String toString() {
			return accum.toString();
    	}
	
	// End of private class
    }
	
	// End of public class
}
