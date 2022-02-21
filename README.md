# COMP442
Compiler design

## Making an executable .jar if not available
1. Open console
2. Enter src directory: `cd src`
3. Compile classes: `javac *.java`
4. Build .jar with: `jar -cvmf MANIFEST.MF <drivername>.jar *.class`
5. Now can execute <drivername>.jar

## FOR GRADING BY TA:
## How to run as executable
1. Open console
2. Enter src directory: `cd src`
3. Run program with `java -jar parsedriver.jar <testfile>`
4. Testfiles are found within the testfiles directory
5. Test 1: `java -jar parsedriver.jar ../testfiles/bubblesort.src`
6. Test 2: `java -jar parsedriver.jar ../testfiles/polynomial.src`
7. Test 3: `java -jar parsedriver.jar ../testfiles/polynomial_EDITED.src`
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

