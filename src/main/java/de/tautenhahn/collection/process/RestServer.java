package de.tautenhahn.collection.process;

import static spark.Spark.get;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.tautenhahn.collection.cards.CardApplicationContext;
import spark.Request;
import spark.Response;

/**
 * Feeds the RestServer with content.
 * 
 * @author jean
 *
 */
public class RestServer {
	/**
	 * Port ist 4567
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		CardApplicationContext.init();
		
		get("/search/:type", RestServer::search);

		Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("Server aborting")));
		System.out.println("end of main");
	}

	static Object search(Request req, Response res) throws Exception {
		res.type("text/plain");
		String type = req.params(":type");
		SearchProcess proc = ProcessScheduler.getInstance().getCurrentSearch(type);
		req.queryParams().forEach(p -> proc.setAttribute(p, req.queryParams(p)));
		Search result = proc.execute();
		Gson gson= new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(result);
	}
	
}
