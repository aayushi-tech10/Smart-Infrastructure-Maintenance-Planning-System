# DSA Algorithm Design

This document records which DSA techniques are implemented in the current Java prototype and which remain planned.

## 1. Infrastructure record storage

**Data structure:** `ArrayList<InfrastructureAsset>`

The CSV rows are loaded into a list so the menu can display, search and sort the records.

- Loading: O(n) records, assuming bounded row length.
- Iterating through all records: O(n).
- Appending a record to an ArrayList: amortized O(1), although adding records through the menu is not implemented yet.

## 2. Search by asset ID

**Algorithm:** linear search using a stream filter.

Pseudocode:

```text
INPUT targetId
FOR each asset in assets
    IF asset.id equals targetId (ignoring case)
        DISPLAY asset
        STOP searching
DISPLAY "No asset found"
```

- Time: O(n) worst case.
- Extra space: O(1) auxiliary space for the search itself.

## 3. Maintenance priority queue

**Data structure:** Java `PriorityQueue` (heap-backed).

Ordering rule: lower numeric priority means more urgent; ties are broken by asset ID for repeatable output. Records marked Completed are not added to the queue.

Pseudocode:

```text
CREATE empty priority queue
FOR each asset
    IF asset.status is not Completed
        INSERT asset into queue
WHILE queue is not empty
    DISPLAY and REMOVE the highest-urgency asset
```

For n eligible records:
- Building the queue by repeated insertion: O(n log n).
- Each insertion/removal: O(log n).
- Displaying the complete queue: O(n log n) overall.
- The queue may hold O(n) records.

## 4. Sorting by priority

**Algorithm:** Java stream sorting with a comparator on priority, then ID.

- Time: O(n log n) typical comparison-sort bound.
- The displayed order is deterministic for equal priorities because ID is the tie-breaker.

## 5. Planned graph extension (not implemented yet)

The infrastructure network may later be represented as an adjacency list.

| Algorithm | Potential use | Complexity |
|---|---|---|
| BFS | Explore nearby connected assets by number of edges | O(V + E) |
| DFS | Explore a connected infrastructure region | O(V + E) |
| Connected components | Identify separate network regions | O(V + E) |
| Dijkstra | Find a shortest route when edge weights are non-negative | O((V + E) log V) with a suitable priority queue and adjacency list |

Here, V is the number of vertices and E is the number of edges. These graph algorithms are proposed extensions, not features of the current source code.

## 6. Design limitations

- Priority is read from the CSV; the program does not calculate urgency automatically.
- The sample data is synthetic.
- Status changes are session-only.
- The prototype is for academic demonstration, not real-world safety decisions.
