# simple-interpreter
Interpreter for a made-up programming language.

_Note: This project was for coding practice, it likely does not contain any groundbreaking new features!_

## Input format

The file [input.txt](input.txt) should contain each instruction on a new line

Our simple programming language allows:

- variables which are a single letter `e.g. A`
- whole numbers `e.g. -5, 0, 20`
- assignment (=) `e.g. A = 5, B = A`
- addition of 2 elements `e.g. B = A + 1, C = A + B`
- returning a value `e.g. A, B, 20`


## Example operation

### Input

For an example of how the input should be formatted, see [input.txt](input.txt).

The initial contents of the input file are:

```
A = 2
B = 8 + 7
C = A + B
D = C + A
D
```

### Output

```
Interpreter output:
19
```

## How to run

- Download the repository
- Compile the .java file and then run it
- Check the console for the expected output above
- Try changing the _input.txt_ file and running again
