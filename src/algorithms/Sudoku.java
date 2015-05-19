package algorithms;

import localsearch.model.*;
import localsearch.selectors.MinMaxSelector;
import localsearch.constraints.alldifferent.*;

import java.io.PrintWriter;
import java.util.*;

class Move {
	int i, j;
	int val;

	public Move(int i, int j, int val) {
		this.i = i;
		this.j = j;
		this.val = val;
	}
}

class SwapMove {
	int i1, j1, i2, j2;

	public SwapMove(int i1, int j1, int i2, int j2) {
		this.i1 = i1;
		this.j1 = j1;
		this.i2 = i2;
		this.j2 = j2;
	}
}

public class Sudoku {

	/**
	 * @param args
	 */
	int N3 = 3;
	int N9 = 9;

	LocalSearchManager ls;
	VarIntLS[][] x;
	ConstraintSystem S;

	Random R = new Random();

	public Sudoku(int sz) {
		N3 = sz;
		N9 = N3 * N3;
	}

	public void stateModel() {
		
		// khoi tao
		ls = new LocalSearchManager();
		x = new VarIntLS[N9][N9];
		for (int i = 0; i < N9; i++)
			for (int j = 0; j < N9; j++)
				x[i][j] = new VarIntLS(ls, 1, N9); //range [1..9]

		
		S = new ConstraintSystem(ls); //khoi tao constraint system
		
		VarIntLS[] y;
		for (int i = 0; i < N9; i++) {
			y = new VarIntLS[N9];
			for (int j = 0; j < N9; j++)
				y[j] = x[i][j];
//			S.post(new AllDifferent(y)); //trong 1 hang phai khac nhau [1..9]
			S.post(new AllDifferent(x[i]));
		}

		for (int j = 0; j < N9; j++) {
			y = new VarIntLS[N9];
			for (int i = 0; i < N9; i++)
				y[i] = x[i][j];
			S.post(new AllDifferent(y)); //trong 1 cot phai khac nhau [1..9]
//			S.post(new AllDifferent(x[j]));
		}

//		for (int i = 0; i < N3; i++) {
//			for (int j = 0; j < N3; j++) {
//				y = new VarIntLS[N9];
//				int idx = -1;
//				for (int i1 = 0; i1 < N3; i1++) {
//					for (int j1 = 0; j1 < N3; j1++) {
//						idx++;
//						y[idx] = x[N3 * i + i1][N3 * j + j1];
//					}
//				}
//				S.post(new AllDifferent(y)); ////trong 1 nhom N3 phai khac nhau [1..9]
//			}
//		}
		ls.close();
	}

	public void init() {
		for (int i = 0; i < N9; i++) {
			for (int j = 0; j < N9; j++)
				x[i][j].setValuePropagate(j + 1);
		}

	}

	public void _search() {
		localsearch.search.TabuSearch ts = new localsearch.search.TabuSearch();
		ts.search(S, 50, 30, 10000, 50);
	}

	public void search() {
		System.out.println("Init Search, S.violations = " + S.violations());

		int it = 0;

		ArrayList<SwapMove> SL = new ArrayList<SwapMove>();
		init();
		while (it < 1000000 && S.violations() > 0) {
			int minDelta = 1000000;
			SL.clear();
			for (int i = 0; i < N9; i++) {
				for (int j1 = 0; j1 < N9 - 1; j1++) {
					for (int j2 = j1 + 1; j2 < N9; j2++) {
						int d = S.getSwapDelta(x[i][j1], x[i][j2]);
						if (d < minDelta) {
							SL.clear();
							SL.add(new SwapMove(i, j1, i, j2));
							minDelta = d;
						} else if (d == minDelta) {
							SL.add(new SwapMove(i, j1, i, j2));
						}
					}
				}
			}

			SwapMove m = SL.get(R.nextInt(SL.size()));
			// x[m.i1][m.j1].swapValuePropagate(x[m.i2][m.j2]);
			// x[m.i1][m.j1].swapValuePropagate(x[m.i2][m.j2]);

			System.out.println("Step " + it + ", " + "swap x[" + m.i1 + ","
					+ m.j1 + "] = " + m.i2 + "," + m.j2 + "], S.violations = "
					+ S.violations());

			it++;
		}

		for (int i = 0; i < N9; i++) {
			for (int j = 0; j < N9; j++) {
				System.out.print(x[i][j].getValue() + " ");
			}
			System.out.println();
		}
	}

	public void printSolutionHTML(String fn) {
		try {
			PrintWriter out = new PrintWriter(fn);
			out.println("<table border = 1>");
			for (int i = 0; i < N9; i++) {
				out.println("<tr>");
				for (int j = 0; j < N9; j++) {
					out.print("<td width=30 heigh=30 align='�enter'>"
							+ x[i][j].getValue() + "</td>");
				}
				out.println("</tr>");
			}
			out.println("</table>");
			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Sudoku S = new Sudoku(3);
		S.stateModel();
		S._search();
		S.printSolutionHTML("sudoku.html");
	}

}
