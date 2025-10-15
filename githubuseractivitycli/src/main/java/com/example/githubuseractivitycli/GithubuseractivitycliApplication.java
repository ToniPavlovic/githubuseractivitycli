package com.example.githubuseractivitycli;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@SpringBootApplication
public class GithubuseractivitycliApplication implements CommandLineRunner {

	private static final String API_URL = "https://api.github.com/users/";

	public static void main(String[] args) {
		SpringApplication.run(GithubuseractivitycliApplication.class, args);
	}

	@Override
	public void run(String... args) {
		if (args.length < 1) {
			System.out.println("Usage: github-activity <username>");
			System.exit(1);
		}

		String username = args[0];
		try {
			fetchGithubActivity(username);
		} catch (IOException | URISyntaxException | InterruptedException e) {
			System.out.println("Error fetching data: " + e.getMessage());
		}
	}

	private void fetchGithubActivity(String username) throws IOException, URISyntaxException, InterruptedException {
		String url = API_URL + username + "/events";
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI(url))
				.header("Accept", "application/vnd.github+json")
				.GET()
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		if (response.statusCode() == 404) {
			System.out.println("User not found. Please check the username.");
			return;
		}

		if (response.statusCode() != 200) {
			System.out.println("Error: Unable to fetch data for user " + username + ". Status code: " + response.statusCode());
			return;
		}

		parseAndDisplayUseActivity(response.body());
	}

	private void parseAndDisplayUseActivity(String jsonResponse) {
		Gson gson = new Gson();

		JsonArray events = gson.fromJson(jsonResponse, JsonArray.class);

		if (events.isEmpty()) {
			System.out.println("No recent activity found.");
			return;
		}

		for (JsonElement eventElement : events) {
			JsonObject event = eventElement.getAsJsonObject();

			String type = event.has("type") ? event.get("type").getAsString() : "Unknown Event";
			String repositoryName = event.has("repo") && event.get("repo").getAsJsonObject().has("name")
					? event.get("repo").getAsJsonObject().get("name").getAsString()
					: "Unknown Repository";

			switch (type) {
				case "PushEvent":
					System.out.println("Pushed to " + repositoryName);
					break;
				case "IssuesEvent":
					System.out.println("Opened an issue in " + repositoryName);
					break;
				case "WatchEvent":
					System.out.println("Starred " + repositoryName);
					break;
				default:
					System.out.println("Performed " + type + " on " + repositoryName);
					break;
			}
		}
	}
}
