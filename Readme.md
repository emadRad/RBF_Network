### Assignment PA-C: Radial-Basis-Function network (RBF-network)
#### Requirement
* JDK(Java Development Kit) 1.8

#### How to Run
To run this program in terminal first compile it with javac:
```
$ javac RBF.java
```

then run it with two arguments:
```
$ java RBF File1 File2 
```

for example:
```
$ java RBF Train/training.dat test.dat
```

#### Input Files Format
You need to provide two input files, one for the training data and the other for test data.
The first line of each file contains the number of input data.

The second line of training data file should be the dimension of input(N) a space and the dimension of ouput(M) at the same line.

For example:

350

2	1

which means there are 350 pattern and the input dimension(N) is 2 and the output dimension(M) is one.

Again the first line of test data should be the number of input and the following lines the data. 
The test data shoud match
the configuration of training data(M and N).

For example:

16

0.666667 -1  0.3506 

0.707071 -1  0.373163 


We set the centers randomly from of training data. 
As we used the 'Subset of training data' approach to set centers according to David Kriesel: A brief Introductionon Neural Networks, page 138, the width are fixedly selected. 


Author:Emad Bahrami Rad

Email: emadbahramirad@gmail.com
