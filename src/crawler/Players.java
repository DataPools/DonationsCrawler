package crawler;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Players {
	public static int getPlayerCount(String ip) throws IOException {
		Document page = Jsoup.connect("https://mcapi.ca/query/"+ ip + "/players").ignoreContentType(true).get();
		String statusstring = page.toString().split("\"status\": ")[1].split(",")[0].trim();
		boolean status = Boolean.parseBoolean(statusstring);
		if(status==true) {
			String playercount = page.toString().split("\"online\": ")[1].split(",")[0];
			int players = Integer.parseInt(playercount);
			return players;
		}
		else {
			return 0;
		}
		
	}

}
