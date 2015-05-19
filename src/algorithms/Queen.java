package algorithms;

import java.io.PrintWriter;
import java.util.Random;

import userDefinedFunction.Minus1;
import localsearch.constraints.alldifferent.AllDifferent;
import localsearch.functions.basic.FuncMinus;
import localsearch.functions.basic.FuncPlus;
import localsearch.model.ConstraintSystem;
import localsearch.model.IFunction;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;
import localsearch.selectors.MinMaxSelector;

public class Queen {

	int n;
	LocalSearchManager ls;
	ConstraintSystem S;
	VarIntLS[] x;
	
	Random R = new Random();
	
	public void stateModel(int nbQueens){
		this.n = nbQueens;
		ls = new LocalSearchManager();
		
		//khoi tao bien
		x = new VarIntLS[n];
		for(int i = 0; i < n; i++){
			x[i] = new VarIntLS(ls,0,n-1); //(local search manager, min value, max value)
		}
		
		
		//khoi tao constraint system
		S = new ConstraintSystem(ls);	
		
		//ko cung 1 hang
		S.post(new AllDifferent(x));
		

		IFunction[] f = new IFunction[n];		
		//duong cheo phu
		for(int i = 0; i < n; i++){
			f[i] = new FuncPlus(x[i],i);
		}					
		S.post(new AllDifferent(f));
		
		//duong cheo chinh
		f = new IFunction[n];		
		for(int i = 0; i < n; i++){
			f[i] = new Minus1(x[i],i);
		}					
		S.post(new AllDifferent(f));
		
		ls.close();// mandatory
	}
	
	public void tabuSearch(){
		localsearch.search.TabuSearch ts = new localsearch.search.TabuSearch();
		ts.search(S, 20, 30, 10000, 100);
		
	}
	
	public void search(int maxIter){
		
		// generate initial solution randomly
		for(int i = 0; i < n; i++){
			int v = R.nextInt(n);
			x[i].setValuePropagate(v);
//			printHTML("queen0.html");
		}
		
		int k = 0;
		MinMaxSelector mms = new MinMaxSelector(S);
		while(k < maxIter && S.violations() > 0){
			VarIntLS sel_x = mms.selectMostViolatedVariable(); 
			int sel_v = mms.selectMostPromissingValue(sel_x);
			sel_x.setValuePropagate(sel_v);// local move, update value, functions, constraints
			
			
			
//			printHTML("queen"+(k+1)+".html");
			
			k++;
			System.out.println("Step " + k + 
					", S.violations = " + S.violations());
		}
		
		// print solution
//		for(int i = 0; i < n; i++)
//			System.out.println("x[" + i + "] = " + x[i].getValue());
	}
	public void printHTML(String fn){
		try{
			PrintWriter out = new PrintWriter(fn);
			out.println("<table border = 1>");
			for(int i = 0; i < x.length; i++){
				out.println("<tr>");
				for(int j = 0; j < x.length; j++){
					if(x[j].getValue() == i){
						out.println("<td width = 10 height = 10 bgcolor='red'>");
					}else{
						out.println("<td width = 10 height = 10 bgcolor='green'>");
					}
					out.println("</td>");
				}
				out.println("</tr>");
			}
			out.println("</table>");
			out.println("number of violation: "+S.violations());
			out.close();
		}catch(Exception ex){
			
		}
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Queen Q = new Queen();
//		Q.test();
		
		Q.stateModel(5);
//		Q.search(8);
		Q.tabuSearch();
		Q.printHTML("queen.html");
	}

}
