# GitHub User Activity CLI

A simple Java CLI tool to fetch and display a GitHub user's recent activity.

## Usage

1. Build the project (using Maven):

```bash
mvn clean package
```

2. Run the application:

```bash
java -jar target/githubuseractivitycli-0.0.1-SNAPSHOT.jar <github-username>
```

Example: 

```bash
java -jar target/githubuseractivitycli-0.0.1-SNAPSHOT.jar ToniPavlovic
```

Example Output:

```bash
Performed PullRequestEvent on ToniPavlovic/libraryManagerAPI
Performed CreateEvent on ToniPavlovic/libraryManagerAPI
Performed DeleteEvent on ToniPavlovic/libraryManagerAPI
Pushed to ToniPavlovic/libraryManagerAPI
```

## Inispiration
[GitHub User Activity](https://roadmap.sh/projects/tmdb-cli)
