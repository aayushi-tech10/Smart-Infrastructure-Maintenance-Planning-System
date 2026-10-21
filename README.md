# Smart Infrastructure Maintenance Planning System

A beginner-friendly **Java + DSA-II** console prototype that organizes infrastructure records and helps maintenance teams review urgent tasks first.

## Problem statement
Infrastructure assets such as roads, bridges and streetlights need inspection and repair. When many records exist, it can be difficult to find an asset, track its status and decide which task should be handled first.

This prototype demonstrates a small, transparent workflow using a CSV dataset and core data structures. It is an academic prototype, not a production safety or engineering system.

## Features
- Load infrastructure records from `data/sample-infrastructure-data.csv`.
- Display all records.
- Search for an asset by ID.
- Create a maintenance queue using Java's `PriorityQueue`.
- Sort records by priority.
- Update a record's status for the current program session.
- Validate basic input and report malformed CSV rows.

**Priority rule:** a smaller number means a more urgent task (priority 1 is shown first). The current sample values are illustrative and must not be interpreted as real inspection data.

## Requirements
- Java Development Kit (JDK) 17 or later.
- A terminal / command prompt.

Check Java installation:
```bash
java -version
javac -version
```

## Run the program
Run these commands from the repository root.

### Windows (PowerShell or Command Prompt)
```powershell
javac -d out src/main/java/Main.java
java -cp out Main
```

### macOS / Linux
```bash
javac -d out src/main/java/Main.java
java -cp out Main
```

To use a different CSV file, pass its path after the class name:
```bash
java -cp out Main path/to/your-data.csv
```

## Menu
1. View all infrastructure records
2. Search by asset ID
3. Show maintenance queue
4. Update maintenance status
5. Show records sorted by priority
0. Exit

Status changes are held in memory only and reset when the application exits. The source CSV is not modified.

## DSA concepts demonstrated

| Concept | Use in this prototype | Expected complexity |
|---|---|---|
| ArrayList | Stores the loaded asset records | Append: amortized O(1); full scan: O(n) |
| Linear search | Finds an asset by ID | O(n) |
| PriorityQueue (binary heap) | Processes non-completed tasks by ascending priority | Offer/poll: O(log n); peek: O(1) |
| Comparator / sorting | Orders assets by priority, then ID | O(n log n) |
| CSV file input | Loads the initial dataset | O(n) records, assuming bounded row length |

The graph algorithms (BFS, DFS, connected components and Dijkstra) remain planned extensions; they are not claimed as implemented in this initial version.

## Repository structure
```text
data/
  sample-infrastructure-data.csv
docs/
  Project-Documentation.md
  PBL-Progress-Report-I.md
diagrams/
  system-workflow.md
src/
  main/java/Main.java
  README.md
.gitignore
LICENSE
README.md
```

## Dataset format
The CSV header is:
```text
id,infrastructure,type,location,condition,severity,priority,status
```
Use one record per line. Keep values comma-free in this simple CSV reader.

## Limitations and next steps
- The included dataset is synthetic and only for demonstration.
- Status changes are not persisted after exit.
- The current prototype does not calculate priority automatically; it uses the supplied numeric priority.
- It does not yet store network edges or run graph algorithms.
- A future version can add CSV persistence, graph connectivity, BFS/DFS, Dijkstra with non-negative edge weights, unit tests and a GUI.

## Research background
Yamín, D., Medaglia, A. L., & Correal, J. F. (2025). *Transportation infrastructure maintenance planning: An exact column enumeration approach*. Computers & Operations Research, 184, 107246. https://doi.org/10.1016/j.cor.2025.107246

This paper motivates the planning and resource-prioritization problem. Its optimization method is not the same as this prototype's priority queue.

## Academic integrity note
Clearly distinguish implemented features from proposed features in progress reports and viva answers. Replace the synthetic dataset with approved, verified data if your instructor requires real observations.
