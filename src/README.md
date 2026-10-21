# Source Code

## Current implementation
The first Java prototype is in `src/main/java/Main.java`.

Implemented:
- CSV loading for the supplied sample data.
- Infrastructure asset model.
- Listing and ID-based search.
- Priority queue using Java's `PriorityQueue`.
- Sorting by numeric priority.
- In-session maintenance-status updates.

Not yet implemented:
- Persistent updates to the CSV file or a database.
- Graph representation, BFS, DFS, connected components or Dijkstra.
- Automated priority scoring, GUI or production integrations.

## Compile and run
From the repository root, with JDK 17 or later:

```bash
javac -d out src/main/java/Main.java
java -cp out Main
```

The application expects `data/sample-infrastructure-data.csv` relative to the current working directory unless a CSV path is supplied as the first command-line argument.
