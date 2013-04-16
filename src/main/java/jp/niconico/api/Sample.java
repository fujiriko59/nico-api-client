package jp.niconico.api;

import java.util.List;

import jp.niconico.api.client.NiconicoApiClient;
import jp.niconico.api.entity.SearchResult;

/**
 * Hello world!
 *
 */
public class Sample 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        
        NiconicoApiClient client = new NiconicoApiClient();
        
        client.login("", "");
        List<SearchResult> results = client.search("初音ミク", "m", 1, "d", true);
        int counter = 0;
        for(SearchResult result: results) {
        	System.out.println("id:" + result.id);
        	System.out.println("title:" + result.title);
        	System.out.println("view:" + result.viewCounter);
        	System.out.println("comment:" + result.numRes);
        	System.out.println("mylist:" + result.mylistCounter);
        	System.out.println(result.descriptionShort);
        	System.out.println("");
        	counter++;
        }
        System.out.println("total:" + counter);
    }
}
