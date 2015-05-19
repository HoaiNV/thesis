package algorithms;

import java.io.PrintWriter;
import java.util.Random;

import localsearch.constraints.alldifferent.AllDifferent;
import localsearch.constraints.basic.IsEqual;
import localsearch.functions.basic.FuncMinus;
import localsearch.functions.basic.FuncPlus;
import localsearch.functions.basic.FuncVarConst;
import localsearch.functions.sum.Sum;
import localsearch.model.ConstraintSystem;
import localsearch.model.IConstraint;
import localsearch.model.IFunction;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;
import localsearch.selectors.MinMaxSelector;

public class MyQueen {

	int n;
	LocalSearchManager ls;
	ConstraintSystem S;
	VarIntLS[][] x;
	Random R = new Random();

	void stateModel(int nbQueen) {
		this.n = nbQueen;
		ls = new LocalSearchManager();

		// khoi tao x[i][j] in range [0,1]
		x = new VarIntLS[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				x[i][j] = new VarIntLS(ls, 0, 1);
			}
		}

		// khoi tao constraint system
		S = new ConstraintSystem(ls);

		// moi hang chi co toi da 1 con 1

		// for (int i = 0; i < n; i++) {
		// IFunction[] f = new IFunction[n];
		// // IFunction sum = new IFunction;
		// int temp = 0;
		// for (int j = 0; j < n; j++) {
		// f[j] = new FuncPlus(x[i][j], 0);
		// temp += x[i][j].getValue();
		// }
		// S.post(new AtMost(f, 1, 1));
		// // S.post(new IsEqual(, 1));
		// }

		// for (int i = 0; i < n; i++) {
		// IFunction[] f = new IFunction[n];
		// for (int j = 0; j < n; j++) {
		// f[j] = new FuncPlus(x[j][i], 0);
		// }
		// S.post(new AtMost(f, 1, 1));
		// }

		IFunction[] sumOfaRow = new IFunction[n];
		IConstraint[] c1 = new IConstraint[n];

		// khoi tao tong 1 hang bang 0
		for (int i = 0; i < n; i++) {
			sumOfaRow[i] = new FuncVarConst(ls, 0);
		}

		// constraint tong 1 hang phai bang 1
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x.length; j++) {
				sumOfaRow[i] = new FuncPlus(sumOfaRow[i], x[i][j]);
			}
			c1[i] = new IsEqual(sumOfaRow[i], 1);
			S.post(c1[i]);

		}

		// constraint tong 1 cot phai bang 1
		IFunction[] sumOfaCol = new IFunction[n];
		IConstraint[] c2 = new IConstraint[n];

		// khoi tao tong = 0
		for (int i = 0; i < n; i++) {
			sumOfaCol[i] = new FuncVarConst(ls, 0);			
		}
		
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x.length; j++) {
				sumOfaCol[i] = new FuncPlus(sumOfaCol[i], x[j][i]);
			}			
			c2[i] = new IsEqual(sumOfaCol[i], 1);
			S.post(c2[i]);
		}

		//duong cheo
		IFunction[] f1 = new IFunction[n];
		IFunction[] f2 = new IFunction[n];
		
		
		for (int i = 0; i < n; i++) {
			f1[i] = new FuncVarConst(ls, 0);
			f2[i] = new FuncVarConst(ls, 0);
		}

		for (int i = 0; i < n; i++) {
//			int temp = 0;
//			for (int j = 0; j < n; j++) {
//				temp += x[j][i].getValue()*j;
//			}
//			int temp1 = temp-i;
//			int temp2 = temp+i;
//			f1[i] = new FuncPlus(f1[i], temp1);
//			f2[i] = new FuncPlus(f1[i], temp2);
			f1[i] = new Sum(x[i]);
		}
		
		IConstraint c3 = new AllDifferent(f1);
		S.post(c3);
		
		
		for (int i = 0; i < n; i++) {
			int temp = 0;
			for (int j = 0; j < n; j++) {
				temp += x[j][i].getValue()*j;
			}
			int temp2 = temp+i;
			f2[i] = new FuncPlus(f1[i], temp2);
		}
		
		IConstraint c4 = new AllDifferent(f2);
		S.post(c4);
		
		
		
		// // constraint tong 1 duong cheo phu phai bang 1
		// IFunction[] sumOfaCD = new IFunction[2 * n];
		// IConstraint[] c3 = new IConstraint[2 * n];
		// // khoi tao tong 1 duong cheo bang 0
		// VarIntLS t3 = new VarIntLS(ls, 0, 0);
		// for (int i = 0; i < 2 * n; i++) {
		// sumOfaCD[i] = new FuncPlus(t3, 0);
		// }
		//
		// // tong duong cheo phu bang 1
		// for (int i = 0; i < n; i++) {
		// for (int j = 0; j <= i; j++) {
		// sumOfaCD[i] = new FuncPlus(sumOfaCD[i], x[i - j][j]);
		// if (i != n - 1)
		// sumOfaCD[2 * n - 1 - i] = new FuncPlus(sumOfaCD[2 * n - 1
		// - i], x[j][i - j]);
		// }
		// c3[i] = new LessOrEqual(sumOfaCD[i], 1);
		// S.post(c3[i]);
		// if (i != n - 1) {
		// c3[2 * n - 1 - i] = new LessOrEqual(sumOfaCD[2 * n - 1 - i], 1);
		// S.post(c3[2 * n - 1 - i]);
		// }
		// }

		// constraint tong 1 duong cheo chinh phai bang 1
		// IFunction[] sumOfaD = new IFunction[2 * n];
		// IConstraint[] c4 = new IConstraint[2 * n];
		// // khoi tao tong 1 duong cheo bang 0
		// VarIntLS t4 = new VarIntLS(ls, 0, 0);
		// for (int i = 0; i < 2 * n; i++) {
		// sumOfaD[i] = new FuncPlus(t4, 0);
		// }
		//
		// // tong duong cheo phu bang 1
		// for (int i = 0; i < n; i++) {
		// for (int j = 0; j < n - i; j++) {
		// sumOfaD[i] = new FuncPlus(sumOfaD[i], x[i + j][j]);
		// if (i != 0)
		// sumOfaD[2 * n - 1 - i] = new FuncPlus(
		// sumOfaD[2 * n - 1 - i], x[j][i + j]);
		// }
		// c4[i] = new LessOrEqual(sumOfaD[i], 1);
		// S.post(c4[i]);
		// if (i != 0) {
		// c4[2 * n - 1 - i] = new LessOrEqual(sumOfaD[2 * n - 1 - i], 1);
		// S.post(c4[2 * n - 1 - i]);
		// }
		//
		// }

		ls.close();
	}// end of statemodel

	public void tabuSearch() {
		localsearch.search.TabuSearch ts = new localsearch.search.TabuSearch();
		ts.search(S, 20, 30, 20000, 50);

	}

	public void printHTML(String fn) {
		try {
			PrintWriter out = new PrintWriter(fn);
			out.println("<table border = 1>");
			for (int i = 0; i < x.length; i++) {
				out.println("<tr>");
				for (int j = 0; j < x.length; j++) {
					if (x[i][j].getValue() == 1) {
						out.println("<td width = 10 height = 10 bgcolor='red'>");
					} else {
						out.println("<td width = 10 height = 10 bgcolor='green'>");
					}
					//
					out.println("</td>");
				}
				out.println("</tr>");
			}
			out.println("</table>");
			out.println("number of violation: " + S.violations());

			// out.println();
			// for (int i = 0; i < n; i++) {
			// for (int j = 0; j < n; j++) {
			// out.print(x[i][j].getValue() + " ");
			// }
			// out.println("");
			// //
			// }
			out.close();
		} catch (Exception ex) {

		}
	}

	public void search(int maxIter) {

		// generate initial solution randomly
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				int v = R.nextInt(1);
				x[i][j].setValuePropagate(v);
				printHTML("MyQueen0.html");
				// System.out.println(x[i].getValue());
			}

		}

		int k = 0;
		MinMaxSelector mms = new MinMaxSelector(S);
		while (k < maxIter && S.violations() > 0) {
			VarIntLS sel_x = mms.selectMostViolatedVariable();
			int sel_v = mms.selectMostPromissingValue(sel_x);
			sel_x.setValuePropagate(sel_v);// local move, update value,
											// functions, constraints

			printHTML("MyQueen" + (k + 1) + ".html");

			k++;
			System.out.println("Step " + k + ", S.violations = "
					+ S.violations());
		}

		// print solution
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < x.length; j++) {
				// System.out.print("x[" + i + "]"+"[" + j + "] = " +
				// x[i][j].getValue());
				System.out.print(x[i][j].getValue() + " ");
			}
			System.out.println();
		}

	}// end of search

	// //////////////////////////////////////////
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MyQueen Q = new MyQueen();
		// Q.test();

		Q.stateModel(4);
		// Q.search(8);
		Q.tabuSearch();
		Q.printHTML("MyQueen.html");
	}

}
