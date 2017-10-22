
import java.io.IOException;
import java.util.List;

import org.apache.lucene.queryparser.classic.ParseException;

import iep.lucene.SearchFiles;
import iep.lucene.SearchResult;

public class Test {
	
    SearchFiles searchFiles;
    
	public static void main(String[] args) {
		new Test().run();
	}
	public Test() {
		searchFiles = new SearchFiles();
	}
	public void run() {
		List<SearchResult> searchResults;
		try {
			searchResults = searchFiles.query("philosophy");
			if ( searchResults.size() > 0 ) {
				System.out.println("Found results: " + searchResults.size());
				for ( SearchResult searchResult: searchResults ) {
					System.out.println(searchResult);
				}
				System.out.println("Best result\n"+searchResults.get(0).preamble);
			}
		} catch (ParseException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
