package userDefinedFunction;

import com.sun.org.apache.xpath.internal.operations.Equals;

import localsearch.constraints.alldifferent.AllDifferent;
import localsearch.constraints.atmost.AtMost;
import localsearch.constraints.basic.IsEqual;
import localsearch.constraints.basic.NotEqual;
import localsearch.functions.basic.FuncPlus;
import localsearch.model.ConstraintSystem;
import localsearch.model.IConstraint;
import localsearch.model.IFunction;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;

public class TestUDF {

	int n;
	LocalSearchManager ls;
	ConstraintSystem S;
	VarIntLS[] x;

	void stateModel(int nbVariable) {
		this.n = nbVariable;
		ls = new LocalSearchManager();
		
		x = new VarIntLS[n];
		IFunction[] f = new IFunction[n];
		IConstraint[] c = new IConstraint[n];
		
		
		for (int i = 0; i < n; i++) {
			x[i] = new VarIntLS(ls, 1, 2);
			f[i] = new FuncPlus(x[i],0);
		}

		S = new ConstraintSystem(ls);
//		S.post(new AllDifferent(x));
		
		c[0] = new NotEqual(f[1],f[2]);
		c[1] = new AtMost(f, 1, 1);
		S.post(c[1]);
		S.post(c[0]);

		ls.close();
	}// end of stating model

	public void tabuSearch() {
		localsearch.search.TabuSearch ts = new localsearch.search.TabuSearch();
		ts.search(S, 20, 30, 20000, 50);
	}
	
	public void printSol(){
		for (int i = 0; i < x.length; i++) {
			System.out.println("x["+i+"] = " + x[i].getValue());
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestUDF t = new TestUDF();
		t.stateModel(3);
		t.tabuSearch();
		t.printSol();		
	}

}
