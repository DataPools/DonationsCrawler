package crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scraper {
	static File donations = new File("donations.txt");
	static File donations2 = new File("donationseula.txt");
	static String server1 = "https://store.rebirthcraft.net";
	static String ip1 = "play.rebirthcraft.net";
	static String server2 = "http://premium.buycraft.net";
	static String ip2 = "mv2.mineverse.online";
	
	public static void scrape1() throws IOException, InterruptedException {
	if(!donations.exists()) {
		System.out.println("Creating donations.txt");
		donations.createNewFile();
	}
	URL website = new URL(server1);
	URLConnection connection = website.openConnection();
	connection.setConnectTimeout(6000);
	connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
	InputStream in = connection.getInputStream();
	Document storepage = Jsoup.parse(in, "UTF-8", server1);
	Elements payments = storepage.select(".payments");
	if(storepage.select(".payments").size() > 0) {
	  payments = storepage.select(".payments"); 
	}
	else if(storepage.select(".content").select(".recent-donator").size() > 0) {
		payments = storepage.select(".content"); 
	}
	Elements recentdonors = payments.select(".info");
	for(int i=0;i < recentdonors.size();i++) {
		Element donor = recentdonors.get(i);
		//Replacing dots to stop charset conflicts
		//Storepack is for stopping extra dashes which are hard to deal with in a column extractor
		String storepack = donor.select(".extra").text().replace("-", "");
		double price = Crawler.getPrice(donor.select(".extra").text());
		String specificdonor = donor.select(".ign").text().split("•")[0].replace("•", "-") + " - " + storepack.replace("•", "-") + " - " + price + " " + Main.currency;
		if(checkDonationsFile(specificdonor, donations) == false) {
			System.out.println(specificdonor + " - " + Players.getPlayerCount(ip1));
			FileWriter filewriter = new FileWriter(donations, true); 
			filewriter.write(specificdonor + " - " + Players.getPlayerCount(ip1));
			filewriter.write("\n");
			filewriter.close();
		}
	}
	System.out.println("Server One is Done!");
	}
	public static void scrape2() throws IOException { 
    if(!donations2.exists()) {
		System.out.println("Creating donationseula.txt");
		donations2.createNewFile();
     }
	URL website2 = new URL(server2);
	URLConnection connection2 = website2.openConnection();
	connection2.setConnectTimeout(6000);
	connection2.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
	InputStream in2 = connection2.getInputStream();
    Document storepage2 = Jsoup.parse(in2, "UTF-8", server2);
	Elements payments2 = storepage2.select(".payments");
	if(storepage2.select(".payments").size() > 0) {
		  payments2 = storepage2.select(".payments"); 
	}
	else if(storepage2.select(".content").select(".recent-donator").size() > 0) {
			payments2 = storepage2.select(".content"); 
	}
	Elements recentdonors2 = payments2.select(".info");
	for(int i=0;i < recentdonors2.size();i++) {
		Element donor2 = recentdonors2.get(i);
		String name2 = donor2.select(".ign").text();
		String storepack = donor2.select(".extra").text().replace("-", "");
		double price = Crawler.getPrice(donor2.select(".extra").text());
		String specificdonor2 = name2 + " - " + storepack.replace("•", "-") + " - " + price + " " + Main.currency;
		if(checkDonationsFile(specificdonor2, donations2) == false) {
			System.out.println(specificdonor2 + " - " + Players.getPlayerCount(ip2));
			FileWriter filewriter = new FileWriter(donations2, true); 
			filewriter.write(specificdonor2 + " - " + Players.getPlayerCount(ip2));
			filewriter.write("\n");
			filewriter.close();
		}
	}
	System.out.println("Server Two is Done!");
}
	public static Boolean checkDonationsFile(String query,File file) throws IOException {
		 FileReader in = new FileReader(file);
		 BufferedReader br = new BufferedReader(in);
		 String line;
		 while ((line = br.readLine()) != null) {
			 String[] splitted = line.split(" - ");
			 if(line.startsWith("//")) { 
				 br.close();
				 return false;
			 }
			 line = splitted[0] + " - " + splitted[1] +" - " + splitted[2];
			 if(query.equals(line)) {
				 br.close();
				 return true;
			 }
		 }
		 br.close();
		 return false;
	}

}
