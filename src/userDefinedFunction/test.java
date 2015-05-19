package userDefinedFunction;

import localsearch.constraints.alldifferent.AllDifferent;
import localsearch.constraints.atmost.AtMost;
import localsearch.model.ConstraintSystem;
import localsearch.model.IConstraint;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;

public class test {
	int n;
	LocalSearchManager ls;
	ConstraintSystem S;
	VarIntLS[] x;
	VarIntLS[] y;
	
	public static void main(String[] arges){
		
		test t = new test();		
		t.stateModel(5);
		t.tabuSearch(t.S);		
		for (int i = 0; i < t.x.length; i++) {
			System.out.print(t.x[i].getValue() + " ");
		}
		System.out.println();
	
		
		LocalSearchManager _ls = new LocalSearchManager();
		ConstraintSystem _S;		
		t.y = new VarIntLS[5];
		for (int i = 3; i < 5; i++) {
			t.y[i] = new VarIntLS(_ls, 0, 10);
		}
		_S = new ConstraintSystem(_ls);		
		_S.post(new AllDifferent(t.y));
		_ls.close();
		t.tabuSearch(_S);
		for (int i = 0; i < t.x.length; i++) {
			System.out.print(t.y[i].getValue() + " ");
		}
		System.out.println();
		
	}
	
	public void tabuSearch(ConstraintSystem CS) {
		localsearch.search.TabuSearch ts = new localsearch.search.TabuSearch();
		ts.search(CS, 20, 30, 20000, 50);

	}
	public void stateModel(int n){
		this.n = n;
		ls = new LocalSearchManager();
		x = new VarIntLS[n];
		for (int i = 3; i < n; i++) {
			x[i] = new VarIntLS(ls, 0, 10);
		}
		x[0] = new VarIntLS(ls, 5, 8);
		x[1] = new VarIntLS(ls, 6, 10);
		x[2] = new VarIntLS(ls, 0, 9);
		
		S = new ConstraintSystem(ls);
		
		IConstraint c = new AllEqual(x);
		S.post(c);
		
		ls.close();
		
		
	}
}
