package crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;

public class Crawler {
	public static void index(String url) throws IOException {
		Document page = Jsoup.connect(url).get();
		if(page.select(".form-control.input-lg").size() > 0) {
			//If it encounters an IGN form
			Connection.Response res = Jsoup.connect(url)
					.timeout(30000)
					.method(Connection.Method.GET)
					.execute();
			page = res.parse();
			Element potentialForm = page.select("form").first();
			FormElement form = (FormElement) potentialForm;
			Element loginField = form.select("input[name=\"ign\"]").first();
			form.addElement(loginField);
			loginField.val("Notch");
			Document searchResults = form.submit().post();
			page = searchResults;
		}
		Elements packages = page.select(".package");
		if(page.select("table.table-hover.table-striped").size() > 0) {
			packages = page.select("tr");
		}
		for(int i=0;i<packages.size();i++) {
			Element specificpackage = packages.get(i);
			String discount = "";
			String packagename = specificpackage.select(".name").text();
			//String packagename = specificpackage.select("[style=\"width:45%;\"]").text();
			String priceforprocess = specificpackage.select(".price").text();
			//String priceforprocess = specificpackage.select("[style=\"width:35%; text-align:center;\"]").text();
			//Turns out discounts are stored in price and .discount is just previous price
			if(specificpackage.select(".discount").size() > 0) {
				//Set this back to .discount if not using Saico
				discount = specificpackage.select(".discount").text();
			}
			priceforprocess = priceforprocess.replace(discount, "").trim();
			//Compiler errors suck, it took process from somewhere else
			priceforprocess = priceforprocess.replace("USD", "").trim();
			priceforprocess = priceforprocess.replace("GBP", "").trim();
			String process = priceforprocess.replace("EUR", "").trim();
			if(process.equals(null) || process.equals("")) {
				process = "0";
			}
			double price = Double.parseDouble(process);
			FileWriter filewriter = new FileWriter(Main.items, true);
			filewriter.write(packagename + ":" + price + "\n");
			filewriter.close();
			
		}
		
	}
	public static String getNewPage(String url) throws IOException {
		Document page = Jsoup.connect(url).get();
		Elements nav;
		if(page.select("ul[class=\"nav navbar-nav\"]").size() > 0) {
		nav = page.select("ul[class=\"nav navbar-nav\"]");
		}
		else if(page.select(".nav").size() > 0) {
			nav = page.select(".nav");
		}
		else {
			nav = page.select("ul");
		}
		Elements links = nav.select("li");
		File linksfile = new File("links.txt");
		if(!linksfile.exists()) {
			linksfile.createNewFile();
		}
		for(int i=0;i<links.size();i++) {
			Element li = links.get(i);
			Elements as = li.select("a");
			for(int b=0;b<as.size();b++) {
				String afurther = as.attr("href");
				String a = Main.baseurl + afurther;
			
			if(Main.checkFile(afurther, linksfile) == false) {
				//index(a);
				FileWriter filewriter = new FileWriter(linksfile,true);
				filewriter.write(afurther + "\n");
				filewriter.close();
				return a;
			}
		  }
		}
		return null;
		
	}
	public static double getPrice(String rank) throws NumberFormatException, IOException {
		 FileReader in = new FileReader(Main.items);
		 BufferedReader br = new BufferedReader(in);
		 String line;
		 while ((line = br.readLine()) != null) {
			 if(line.split(":")[0].contains(rank)) {
				 br.close();
				 line = line.split(":")[1];
				 double price = Double.parseDouble(line);
				 return price;
			 }
		 }
		 br.close();
		 return 0.0;
	}

}
