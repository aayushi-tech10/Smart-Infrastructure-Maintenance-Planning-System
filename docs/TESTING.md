# Testing Guide

This guide covers manual checks for the current console prototype. The sample records are synthetic and are intended only for demonstration.

## 1. Compile and launch

From the repository root, using JDK 17 or later:

```bash
javac -d out src/main/java/Main.java
java -cp out Main
```

Expected: the menu appears without a compilation error.

## 2. Manual test cases

| Test | Action | Expected result |
|---|---|---|
| T01 | Choose `1` | All four sample records are displayed |
| T02 | Choose `2`, enter `I001` | The Main Road record is displayed |
| T03 | Choose `2`, enter `I999` | A “No asset found” message is displayed |
| T04 | Choose `3` | Pending/in-progress tasks appear by ascending priority number |
| T05 | Choose `5` | All records are sorted by priority, lowest number first |
| T06 | Choose `4`, enter `I002`, then `Completed` | Status changes for this session |
| T07 | After T06, choose `3` | The completed record is omitted from the maintenance queue |
| T08 | Choose `4`, enter an unknown ID | A “No asset found” message is displayed |
| T09 | Choose `4`, enter a valid ID and invalid status | Invalid status is rejected and no change is made |
| T10 | Choose `0` | The program exits |

## 3. Data-file checks

- The default CSV path is `data/sample-infrastructure-data.csv`, relative to the working directory.
- To load a different file, pass its path as the first argument to `Main`.
- Each row must contain eight comma-separated fields matching the header.
- This simple reader does not support commas embedded inside quoted field values.
- If the file cannot be read or a row has an invalid priority value, the application reports an error.

## 4. Known limitation

Status updates are stored in memory only. They are not written back to the CSV file and are lost when the program exits. The project does not yet include automated unit tests.
