package crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Main {
	static File staticlinksfile = new File("links.txt");
	static File items = new File("items.txt");
	static String currency = "USD";
	//MAKE SURE TO DELETE TRAILING SLASHES!
	static String baseurl = "http://store.rebirthcraft.net";
	
	public static void main(String[] args) throws IOException, InterruptedException {
		initalize();
		//MAKE SURE NEWURL EQUALS BASEURL
		String newurl="http://store.rebirthcraft.net";
			while(newurl !=null) {
				if(newurl != baseurl) {
			    Crawler.index(newurl);	
				}
				newurl = Crawler.getNewPage(newurl);
			    System.out.println(newurl);
			}
		Scraper.scrape1();
		initalize();
		baseurl = "http://premium.buycraft.net";
		newurl="http://premium.buycraft.net";
		while(newurl !=null) {
			if(newurl != baseurl) {
		    Crawler.index(newurl);	
			}
			newurl = Crawler.getNewPage(newurl);
		    System.out.println(newurl);
		}
		Scraper.scrape2();
	}
	
	public static void initalize() throws IOException {
		if(!staticlinksfile.exists()) {
			staticlinksfile.createNewFile();
		}
		if(!items.exists()) {
			items.createNewFile();
		}
		FileWriter filewriter = new FileWriter(staticlinksfile);
		filewriter.write("#" + "\n");
		filewriter.write("/" + "\n");
		filewriter.close();
		FileWriter filewriter2 = new FileWriter(items);
		filewriter2.write("");
		filewriter2.close();
		
	}
	public static Boolean checkFile(String query,File file) throws IOException {
		 FileReader in = new FileReader(file);
		 BufferedReader br = new BufferedReader(in);
		 String line;
		 while ((line = br.readLine()) != null) {
			 if(query.equals(line)) {
				 br.close();
				 return true;
			 }
		 }
		 br.close();
		 return false;
	}

}
