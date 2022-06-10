This program contains 1 source file which is the EMS.java file. The input folder contains input txt files which is comma seperated integer. Program will try to read all the txt file. Feel free to add or edit the input txt files.

Before excuting the program please update the inputFilePath.txt file to the "Median\\input" folder position on local drive.

Please use command "java -jar --enable-preview Median.jar" to run the program. This approach involved in file IO operations, please grant permission to the program.

I'm using the External Merge Sort idea to solve this problem. The program will first read and sort all the input Integer files and save the sorted Integers into files on hard drive. I'm using multiple thread to handle the IO and sorting. The working thread will also record the number of Integer in a input file and write the count at the begining of output file. There is a maxThread number to limit the max number of threads running at the same time. We can set it accordingly based on our RAM limitation/CPU capacity. Since the sorted list is on the hard drive and we can limit the total number of running threads, We could solve this problem with limited RAM resources.

After generate all the sorted files. Program will try to merge half the sorted list. In order to save memory, We will only record the largest and second largest number during merge. When it traversed half of all integers we are able to calculate the median for all input Integers.
