# COMP442
Compiler design

## Important note if nullpointer error is returned: 
If the program parses entirely but finishes at a nullpointer, this is because the test files are missing EOF symbols!
To fix this, manually add an EOF symbol, or simply open the testfile in a text editor and hit save for auto EOF symbol adding.
The files are NOT modified! They are only updated with an EOF

## Making an executable .jar if not available
1. Open console
2. Enter src directory: `cd src`
3. Compile classes: `javac *.java`
4. Build .jar with: `jar -cvmf MANIFEST.MF <drivername>.jar *.class`
5. Now can execute <drivername>.jar


# Assignment 5 (Current)
## FOR GRADING BY TA:
1. Open console
2. Enter src directory: `cd src`
3. Run program with `java -jar semanticanalyzerdriver.jar <testfile>`
4. Testfiles are found within the testfiles directory
5. Test 1: `java -jar compilerdriver.jar ../testfiles/bubblesort.src`
6. Test 2: `java -jar compilerdriver.jar ../testfiles/polynomial.src`
7. Test 3: `java -jar compilerdriver.jar ../testfiles/test2_func_code.src`
8. Test 4: `java -jar compilerdriver.jar ../testfiles/simplemain.src`
9. Memory Symbol Table and Moon output files are found within the same testfiles directory

## How to run without executable
1. Open console
2. Change directory to /src: `cd /src`
3. Build project with: `javac *.java`
4. Run program with `java Parser <testfile>`
5. Testfiles are found within testfiles directory
6. Test 1: `java Parser ../testfiles/bubblesort.src`
7. Test 2: `java Parser ../testfiles/polynomial.src`
8. Test 3: `java Parser ../testfiles/test2_func_code.src`
9. Test 4: `java Parser ../testfiles/simplemain.src`
10. Memory Symbol Table and Moon output files are found within the same testfiles directory





## -------------------------------------------------------------------------
# Assignment 4 (OLD)
## Making an executable .jar if not available
## How to run as executable
1. Open console
2. Enter src directory: `cd src`
3. Run program with `java -jar semanticanalyzerdriver.jar <testfile>`
4. Testfiles are found within the testfiles directory
6. Test 1: `java -jar semanticanalyzerdriver.jar ../testfiles/testClass.src`
5. Test 2: `java -jar semanticanalyzerdriver.jar ../testfiles/bubblesort.src`
6. Test 3: `java -jar semanticanalyzerdriver.jar ../testfiles/polynomial.src`
8. AST tree output files are found within the same testfiles directory

## How to run without executable
1. Open console
2. Change directory to /src: `cd /src`
3. Build project with: `javac *.java`
4. Run program with `java Parser <testfile>`
5. Testfiles are found within testfiles directory
7. Test 1: `java Parser ../testfiles/testClass.src`
6. Test 2: `java Parser ../testfiles/bubblesort.src`
7. Test 3: `java Parser ../testfiles/polynomial.src`
8. AST tree output files are found within the same testfiles directory








# Assignment 3 (Old)
## FOR GRADING BY TA:
## How to run as executable
1. Open console
2. Enter src directory: `cd src`
3. Run program with `java -jar ASTdriver.jar <testfile>`
4. Testfiles are found within the testfiles directory
5. Test 1: `java -jar ASTdriver.jar ../testfiles/bubblesort.src`
6. Test 2: `java -jar ASTdriver.jar ../testfiles/polynomial.src`
6. Test 2: `java -jar ASTdriver.jar ../testfiles/test.src`
8. AST tree output files are found within the same testfiles directory

## How to run without executable
1. Open console
2. Change directory to /src: `cd /src`
3. Build project with: `javac *.java`
4. Run program with `java Parser <testfile>`
5. Testfiles are found within testfiles directory
6. Test 1: `java Parser ../testfiles/bubblesort.src`
7. Test 2: `java Parser ../testfiles/polynomial.src`
7. Test 2: `java Parser ../testfiles/test.src`
8. AST tree output files are found within the same testfiles directory







# Assignment 2 (Old)
## Making an executable .jar if not available
## FOR GRADING BY TA:
## How to run as executable
1. Open console
2. Enter src directory: `cd src`
3. Run program with `java -jar parserdriver.jar <testfile>`
4. Testfiles are found within the testfiles directory
5. Test 1: `java -jar parserdriver.jar ../testfiles/bubblesort.src`
6. Test 2: `java -jar parserdriver.jar ../testfiles/polynomial.src`
7. Test 3: `java -jar parserdriver.jar ../testfiles/polynomial_EDITED.src`
8. Derivation and error files are all found within the same testfiles directory

## How to run without executable
1. Open console
2. Change directory to /src: `cd /src`
3. Build project with: `javac *.java`
4. Run program with `java Parser <testfile>`
5. Testfiles are found within testfiles directory
6. Test 1: `java Parser ../testfiles/bubblesort.src`
7. Test 2: `java Parser ../testfiles/polynomial.src`
8. Test 3: `java Parser ../testfiles/polynomial_EDITED.src`
9. Derivation and error files are all found within the same testfiles directory

