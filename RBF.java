import java.util.*;
import java.io.*;

class RBF_Layer{

	/* matrix of centers
	 * KxN:
	 *  N = number of input
	 *  K = number of RBF neurons
	*/
	double [][] centers;

	//matrix of width
	double [] widths;

	//number of RBF neurons
	int K;

	double [] r_k;

	//learning rate
	double eta = 0.2;

	RBF_Layer(int K,int N){
		centers = new double[K][N];
		widths = new double[K];	
		this.K = K;
	}


	/* Setting centers randomly from training data
	 * Input Data Driven: Subset of training data
	 * @input X: the input patterns(training points)
	 */ 
	public void setCenters(ArrayList<double []> X){
		int P = X.size();
		int index;
		Random rand = new Random(68431920);
		for(int k=0;k<K;k++){
			index =(int) RBF.getRandomInRange(0,P-1,rand);
			centers[k] = X.get(index).clone();

		}
	}

	 /* As we used the 'Subset of training data' approach to set centers
	  * according to David Kriesel: A brief Introduction on Neural Networks, page 138
	  * the width are fixedly selected. 
	 */
	public void setWidths(double w ){
		for(int i=0;i<widths.length;i++)
			widths[i]=w;
	}

	/* This function computes the output of each 
	 * neuron at this (RBF)layer
	 * @input: X: the input vector to this layer
	 * @output the r_k vector consist of output of each neuron
	 * 	in RBF layer   
	 */
	public double [] evaluate(double [] X){
		double dist = 0;	
		r_k = new double[K];

		for(int k=0;k<K;k++){
			dist =0;
			for(int i=0;i<X.length;i++){
				dist += Math.pow(X[i] - centers[k][i],2); 
			}
			dist = Math.pow(Math.sqrt(dist),2);
			r_k[k] = computeGaussian(dist,widths[k]);
			//dist = -dist/(2*Math.pow(widths[k],2));
			//r_k[k] = Math.exp(dist);
		}
		return r_k;	
	}
	
	/* Computing the gaussian function
	 * @input d: the computed distance between inputs and centers
	 * , s: the width  
	 *
	 * @output the r_k vector consist of output of each neuron
	 * 	in RBF layer   
	 */ 
	public double  computeGaussian(double d,double width){
		double result = d/(2*Math.pow(width,2));  
		return Math.exp(-result);
	}


}

class RBF{
	PrintWriter out;
	InputReader in;
	
	//input matrix (PxN)
	ArrayList<double []> patterns;

	//a list of teacher values(per patterns PxM) 
	ArrayList<double []> teacher_p;
	
	//matrix of weights (for output layer)
	// KxM matrix
	double [][] weights;


	// Number of patterns
	int P;
	//dimension of inputs
	int N;
	//number of RBF neurons
	int K;
	//dimension of outputs
	int M;

	// the range for wieghts
	final double startRange = -5.0;
	final double endRange = 5.0;
	
	//learning rate
	double eta=0.2;

	double width = 5;

	//the file for learning curve data
	File curve;

	RBF(){
		curve = new File("learning.curve");
		patterns = new ArrayList<>();
		teacher_p = new ArrayList<>();
	}

	/* initializing centers, 
	 * widths, weights and input vectors
	 *
	 */ 
	public void init(){
		
		weights = new double[K][M];
		Random rand = new Random();
		rand.setSeed(243132550);	
		for(int h=0;h<weights.length;h++)
			for(int m=0;m<weights[h].length;m++)
				weights[h][m]=getRandomInRange(startRange,endRange,rand);
		
	}
	
	public static double getRandomInRange(double start,double end,Random rand){
		if(start>end)
			throw new IllegalArgumentException("Start cannot exceed End.");

		double range = end - start;	
		double randNum = start+(range * rand.nextDouble());

		return randNum;
	
	}	
	


	/* Feed forward the input
	 * @input rbflayer, input: the net input vector
	 * @output the vector of output y_m
	 */ 
	double [] feedForward(RBF_Layer rbfLayer, double [] input){
		double [] r_k = rbfLayer.evaluate(input);	
		double [] y_m = new double[M];
		for(int m=0;m<M;m++)
			for(int k=0;k<K;k++){
				y_m[m] = r_k[k]*weights[k][m];
		
			}
		return y_m;
	}

	/* Learning weights through gradient descent method
	 * @input eta:learning rate
	 * 	  y_m: the computed output
	 * 	  t_m: teacher value for output
	 * @
	 */ 
	void learnWeights(double eta,double [] y_m, double [] t_m, double [] out_k){
		double delta_km = 0;	
		for(int m=0;m<M;m++){
			delta_km = 0;
			for(int k=0;k<K;k++){
				delta_km = eta*(t_m[m]-y_m[m])*out_k[k];	
				weights[k][m]+=delta_km;
			}
		}
	
	}

	/* Computing the Mean squared error 
	 *  for computed output and teacher value
	 *
	 */ 	
	double computeError(double [] y_m, double [] t_m){
		double error=0;
		for(int m=0;m<y_m.length;m++){
			error += Math.pow(y_m[m]-t_m[m],2);
		}
		return error/y_m.length;
	}

	public void readPatterns(){
		double [] intput = new double[N];
		double [] t_m = new double[M];

		for(int i=0;i<P;i++){
			for(int j=0;j<N;j++){
				intput[j] = in.nextDouble();
			}	
			patterns.add(i,intput);

			for(int m=0;m<M;m++){
				t_m[m] = in.nextDouble();
			}
			teacher_p.add(t_m);

		}
	}


	void readTestData(int numData){
		double [] input = new double[N];
		double [] t_m = new double[M];
		
		patterns = new ArrayList<>();
		teacher_p = new ArrayList<>();

		for(int i=0;i<numData;i++){
			for(int j=0;j<N;j++){
				input[j] = in.nextDouble();
			}	
			patterns.add(i,input);

			for(int m=0;m<M;m++){
				t_m[m] = in.nextDouble();
			}
			teacher_p.add(t_m);

		}
	}

	public RBF_Layer run(InputStream inStream){
		in = new InputReader(inStream,"#");
		P = in.nextInt();	
	 	N = in.nextInt();
		M = in.nextInt();
		//M = 2;
		
		K = 4;
		
		readPatterns();		
		init();
		RBF_Layer rbfLayer = new RBF_Layer(K, N);
		rbfLayer.setCenters(patterns);
		rbfLayer.setWidths(width);
		
		double [] y_m;	
		double [] t_m;	

		//number of iteration
		int numIter=1;

		String output="";
		for(int iter=0;iter<numIter;iter++){
			try( FileWriter fileWriter = new FileWriter(curve,false)){
				for(int p=0;p<P;p++){
					y_m = feedForward(rbfLayer,patterns.get(p));
					t_m = teacher_p.get(p);
					learnWeights(eta,y_m,t_m,rbfLayer.r_k);
					output=(p+1)+" "+computeError(y_m,t_m)+"\n";
					fileWriter.write(output);
				}
			}
			catch(IOException e){
				System.out.println("Error in writing to file"+e);
			}
		}
		
		System.out.println("The data was written to the 'learning.curve' file ");
		System.out.println("Computing the test data with this network ... ");

		return rbfLayer;
	}


	//computing the test data with learned net
	void Compute(InputStream inStream, RBF_Layer rbfLayer){
		in = new InputReader(inStream,"#");

		//number of input 
		int numData = in.nextInt();
		
		//reading the test data
		readTestData(numData);		
		
		double [] y_m;	
		double [] t_m;	

		double error=0;

		for(int i=0;i<numData;i++){
			y_m = feedForward(rbfLayer,patterns.get(i));
			t_m = teacher_p.get(i);
			error += computeError(y_m,t_m);
		}
					
		System.out.println("The average error with test data "+(error/numData));
	
	}


	public static void main(String [] args)throws FileNotFoundException{
		
		if(args.length<2 || args[0].isEmpty())
			throw new FileNotFoundException("File not found!\nPlease check the file name and the path to input file\nOr the number of files, 2 files is needd");
	
		InputStream inputStream;
		InputStream testInStream;
		try {
			inputStream = new FileInputStream(args[0]);
			testInStream = new FileInputStream(args[1]);
		}
		catch (IOException e) {
		 throw new RuntimeException(e);
		}


		RBF rbf = new RBF();
		RBF_Layer layer = rbf.run(inputStream);
		rbf.Compute(testInStream,layer);
	}
}





class InputReader{
	StringTokenizer tokenizer;
	BufferedReader reader;
	String skipLineChar;
	
	public InputReader(InputStream stream,String skipChar){
		reader = new BufferedReader(new InputStreamReader(stream));
		tokenizer = null;
		skipLineChar = skipChar;
	}
	
	public InputReader(InputStream stream){
		reader = new BufferedReader(new InputStreamReader(stream));
		tokenizer = null;
	}
	
	public InputReader(){
	
		reader = new BufferedReader(new InputStreamReader(System.in));
	}
	
	public String readLine(){
			try{
				return reader.readLine();		
			}
			catch (IOException error){
				throw new RuntimeException(error);
			}
		}
	
	
	public Iterator<String> FileIterator(){
			try{
				return reader.lines().iterator();		
			}
			catch (Exception error){
				throw new RuntimeException(error);
			}
		}
	String next(){
		String line;
		while(tokenizer == null || !tokenizer.hasMoreTokens()) {
			try{
				if(skipLineChar==null)
					tokenizer = new StringTokenizer(reader.readLine());
				else{
					line= reader.readLine();
					while(line.startsWith(skipLineChar))
						line=reader.readLine();
					tokenizer = new StringTokenizer(line);
				}
			}
			catch (IOException error){
				throw new RuntimeException(error);
			}
		
		}
		return tokenizer.nextToken();
	}
	
	
	public int nextInt(){
		return Integer.parseInt(next());
	}
	public long nextLong() {
	            return Long.parseLong(next());
	}
	public double nextDouble(){
		return Double.parseDouble(next());
	}
}
