# COMP442
Compiler design

## Making an executable .jar if not available
1. Open console
2. Enter src directory: `cd src`
3. Compile classes: `javac *.java`
4. Build .jar with: `jar -cvmf MANIFEST.MF lexdriver.jar *.class`
5. Now can execute lexdriver.jar

## FOR GRADING BY TA:
## How to run as executable
1. Open console
2. Enter src directory: `cd src`
3. Run program with `java -jar lexdriver.jar <testfile>`
4. Testfiles are found within testfiles directory
5. Test 1: `java -jar lexdriver.jar ../testfiles/lexpositivegradingNEW.src`
6. Test 2: `java -jar lexdriver.jar ../testfiles/MODDEDlexpositivegrading.src`
7. Test 3: `java -jar lexdriver.jar ../testfiles/MODDEDlexnegativegrading.src`
8. Test 4: `java -jar lexdriver.jar ../testfiles/lexpositivegrading.src`
9. Test 5: `java -jar lexdriver.jar ../testfiles/lexnegativegrading.src`

## How to run without executable
1. Open console
2. Change directory to /src: `cd /src`
3. Build project with: `javac Lexer.java TokenType.java BufferFuncs.java`
4. Run program with `java Lexer <testfile>`
5. Testfiles are found within testfiles directory
6. Test 1: `java Lexer ../testfiles/lexpositivegradingNEW.src`
7. Test 2: `java Lexer ../testfiles/MODDEDlexpositivegrading.src`
8. Test 3: `java Lexer ../testfiles/MODDEDlexnegativegrading.src`
9. Test 4: `java Lexer ../testfiles/lexpositivegrading.src`
10. Test 5: `java Lexer ../testfiles/lexnegativegrading.src`


