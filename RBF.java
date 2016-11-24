import java.util.*;
import java.io.*;

class RBF{
	PrintWriter out;
	InputReader in;
	double [] intput;
	RBF(){
		out = new PrintWriter(System.out);
	}

	public void run(InputStream inStream){
		in = new InputReader(inStream);
		int N = in.nextInt();

		intput=new double[N];			
	
	}






	public static void main(String [] args)throws FileNotFoundException{
		
		if(args.length==0 || args[0].isEmpty())
			throw new FileNotFoundException("File not found! Please check the file name and the path to input file");
	
		InputStream inputStream;
		try {
			inputStream = new FileInputStream(args[0]);
	
		}
		catch (IOException e) {
		 throw new RuntimeException(e);
		}


		RBF rbf = new RBF();
		rbf.run(inputStream);
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
				line= reader.readLine();
				while(line.startsWith(skipLineChar))
					line=reader.readLine();
				tokenizer = new StringTokenizer(line);
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
