# COMP442
Compiler design project which compiles Rust code to Moon (Lua). 
This functions by:
1. Tokenizing a Rust source file
2. Parsing the derivations based on a grammar
3. Generating the AST from the parse tree
4. Generating the symbol tables from the AST using a visitor pattern
5. Allocating memory to the symbol tables
6. Performing semantic checks from the symbol tables
7. Generating the Moon (Lua) output code

## Making an executable .jar if not available
1. Open console
2. Enter src directory: `cd src`
3. Compile classes: `javac *.java`
4. Build .jar with: `jar -cvmf MANIFEST.MF <drivername>.jar *.class`
5. Now can execute <drivername>.jar

# Final Submission
## FOR GRADING BY TA:
1. Open console
2. Enter src directory: `cd src`
3. Run program with `java -jar compiler.jar <testfile>`
4. Testfiles are found within the testfiles directory
5. Test 1: `java -jar compiler.jar ../testfiles/Assn5_Tests/bubblesort.src`
6. Test 2: `java -jar compiler.jar ../testfiles/Assn5_Tests/polynomial.src`
7. Test 3: `java -jar compiler.jar ../testfiles/Assn5_Tests/test2_func_code.src`
8. Test 4: `java -jar compiler.jar ../testfiles/Assn5_Tests/simplemain.src`
9. Test 5: `java -jar compiler.jar ../testfiles/Assn5_Tests/test_addop_main.src`
NOTE: `test_addop_main.src` displays the incorrect AddOP operation between two different types
10. Memory Symbol Table and Moon output files are found within the same testfiles directory

## How to run without executable
1. Open console
2. Change directory to /src: `cd /src`
3. Build project with: `javac *.java`
4. Run program with `java Parser <testfile>`
5. Testfiles are found within testfiles directory
6. Test 1: `java Parser ../testfiles/Assn5_Tests/bubblesort.src`
7. Test 2: `java Parser ../testfiles/Assn5_Tests/polynomial.src`
8. Test 3: `java Parser ../testfiles/Assn5_Tests/test2_func_code.src`
9. Test 4: `java Parser ../testfiles/Assn5_Tests/simplemain.src`
9. Test 5: `java Parser ../testfiles/Assn5_Tests/test_addop_main.src`
10. Memory Symbol Table and Moon output files are found within the same testfiles directory

## Running on Moon Machine
1. Enter Moon source folder: `Resources/moon/source`
2. Run Moon file from testfiles: `./moon ../../../testfiles/Assn5_Tests/<filename>.moon`
3. Test 1: `./moon ../../../testfiles/Assn5_Tests/test2_func_code.moon`
4. Test 2: `./moon ../../../testfiles/Assn5_Tests/bubblesort.moon`
5. Test 3: `./moon ../../../testfiles/Assn5_Tests/polynomial.moon`
6. Test 4: `./moon ../../../testfiles/Assn5_Tests/simplemain.moon`
6. Test 4: `./moon ../../../testfiles/Assn5_Tests/test_addop_main.moon`




## Important note if nullpointer error is returned: 
If the program parses entirely but finishes at a nullpointer, this is because the test files are missing EOF symbols!
To fix this, manually add an EOF symbol, or simply open the testfile in a text editor and hit save for auto EOF symbol adding.
The files are NOT modified! They are only updated with an EOF


