package algorithms;

import java.util.ArrayList;

import localsearch.model.*;
import localsearch.constraints.atmost.AtMost;
import localsearch.constraints.basic.*;
import localsearch.functions.basic.*;

import java.io.*;
public class Model1 {

	/**
	 * @param args
	 */
	public Data D;
	public VarIntLS[] x_day;// x_day[c] is the day of fCourse c
	public VarIntLS[] x_slot;// x_slot[c] is the slot of fCourse c
	// public VarIntLS[] x_room;// x_room[c] is the room of fCourse c;
	public LocalSearchManager ls;
	public ConstraintSystem S;

	public Model1(Data D) {
		this.D = D;
	}

	public void stateModel() {
		// khoi tao
		ls = new LocalSearchManager();
		x_day = new VarIntLS[D.nbFCourses];
		x_slot = new VarIntLS[D.nbFCourses];

		for (int i = 0; i < D.nbFCourses; i++) {
			x_day[i] = new VarIntLS(ls, 0, D.nbDays - 1);
			x_slot[i] = new VarIntLS(ls, 0, 2 * D.nbSlotOfHalfDay - 1);
		}
		S = new ConstraintSystem(ls);

		// constraints

		for (int c = 0; c < D.nbCourses; c++) {
			ArrayList<Integer> L = D.fCourse[c]; // fCourse: an arraylist of
													// arraylists

			for (int j1 = 0; j1 < L.size() - 1; j1++) {
				// for (int i = j1+1; i < L.size(); i++) {
				// //nhu the nay ms du 3 cap: 0-1, 0-2, 1-2
				// int fc1 = L.get(j1);
				// int fc2 = L.get(i);
				// }
				int fc1 = L.get(j1);
				int fc2 = L.get(j1 + 1);

				// neu ko cung ngay thi cach nhau 2 ngay
				IConstraint c3 = new NotEqual(x_day[fc1], x_day[fc2]);
				IConstraint c4 = new LessOrEqual(new FuncPlus(x_day[fc1], 2),x_day[fc2]);
				S.post(new Implicate(c3, c4));

				// thu 2,4,6 thi hok 1 slot/môn thôi.
				if (x_day[fc1].getValue() % 2 == 0
						|| x_day[fc2].getValue() % 2 == 0) {

					IConstraint c1 = new NotEqual(x_day[fc1], x_day[fc2]);
					IConstraint c2 = new IsEqual(x_slot[fc1], x_slot[fc2]);
					S.post(c1);
					S.post(c2);
				}
				// else {
				// thứ 3 vs thứ 5 chưa express đc

				// IConstraint c1 = new IsEqual(x_day[fc1],x_day[fc2]);
				// IConstraint c2 = new IsEqual(new
				// FuncPlus(x_slot[fc1],1),x_slot[fc2]);
				// S.post(new Implicate(c1,c2));

				// ArrayList<Integer> list = (ArrayList<Integer>) L.clone();
				// list.remove(fc1);
				// list.remove(fc2);
				// int fc3 = list.get(0);
				//
				// IFunction[] f = new IFunction[3];
				// f[0] = new FuncVarConst(x_slot[fc1]);
				// f[1] = new FuncVarConst(x_slot[fc2]);
				// f[2] = new FuncVarConst(x_slot[fc3]);
				// IConstraint c00 = new AtMost(f,1,1);
				// S.post(c00);
				// }
			}
		}

		// -----------------------------------------------------------------------
		// mot lop ko the hok 2 slot cung 1 luc
		// 1 giao vien ko day 2 lop trong cung 1 slot cua 1 ngay
		IConstraint[][] cd = new IConstraint[D.nbFCourses][D.nbFCourses];
		IConstraint[][] cs = new IConstraint[D.nbFCourses][D.nbFCourses];
		for (int i = 0; i < D.nbFCourses - 1; i++) {
			for (int j = i + 1; j < D.nbFCourses; j++) {
				cd[i][j] = new IsEqual(x_day[i], x_day[j]);
				cs[i][j] = new NotEqual(x_slot[i], x_slot[j]);
			}
		}
		for (int i = 0; i < D.nbFCourses - 1; i++) {
			for (int j = i + 1; j < D.nbFCourses; j++) {
				if (D.classOfFCourse[i] == D.classOfFCourse[j] // mot lop ko the học 2 slot cùng 1 lúc
						|| D.teacherOfFCourse[i] == D.teacherOfFCourse[j]) { // 1 giáo viên ko dạy 2 lớp cùng 1 slot của 1 ngày
					S.post(new Implicate(cd[i][j], cs[i][j]));
				}

			}
		}

		// -----------------------------------------------------------------------
		// hok ca trong buoi sang hoac buoi chieu
		for (int i = 0; i < D.nbFCourses - 1; i++) {
			for (int j = i + 1; j < D.nbFCourses; j++) {
				if (D.classOfFCourse[i] == D.classOfFCourse[j]) {
					IConstraint ci = new LessOrEqual(x_slot[i],
							D.nbSlotOfHalfDay - 1);
					IConstraint cj = new LessOrEqual(x_slot[j],
							D.nbSlotOfHalfDay - 1);
					S.post(new Implicate(ci, cj));

					ci = new LessOrEqual(
							new FuncVarConst(ls, D.nbSlotOfHalfDay), x_slot[i]);
					cj = new LessOrEqual(
							new FuncVarConst(ls, D.nbSlotOfHalfDay), x_slot[j]);
					S.post(new Implicate(ci, cj));
				}

			}
		}

		// -----------------------------------------------------------------------
		// ngày d, slot s giáo viên x bận thì slot đó ko giáo viên x ko dạy
		// for(int i = 0; i < D.nbFCourses; i++){
		// int t = D.teacherOfFCourse[i];
		// for(int j = 0; j < D.busyOfTeacher[t].size(); j++){
		// DaySlot ds = D.busyOfTeacher[t].get(j);
		// IConstraint c1 = new IsEqual(x_day[i],ds.d);
		// IConstraint c2 = new NotEqual(x_slot[i],ds.s);
		// S.post(new Implicate(c1,c2));
		// }
		// }

		// -----------------------------------------------------------------------
		ls.close();
	}

	public void search() {
		localsearch.search.TabuSearch ts = new localsearch.search.TabuSearch();
		ts.search(S, 20, 30, 6000, 50);
	}

	public int findFCourse(int d, int sl, int Class) {
		// matrix 1(size: nbFcousre x 4): fcourse course x_day x_slot
		// matrix 2(size: nbFcousre x 3): course teacher class
		for (int i = 0; i < D.nbFCourses; i++) {
			if (x_day[i].getValue() == d && x_slot[i].getValue() == sl
					&& D.classOfFCourse[i] == Class)
				return i;
		}
		return -1;
	}

	public void printResultHTML(String fn) {
		try {
			PrintWriter out = new PrintWriter(fn);
			out.println("<table border = 1>");

			for (int i_cl = 0; i_cl < D.nbClasses; i_cl++) { // for each class
				out.println("<tr>");
				out.println("<td colspan=5>" + "Class " + i_cl + "</td>");
				for (int i_sl = 0; i_sl < D.nbSlotOfHalfDay * 2; i_sl++) { // for each slot sl
					out.println("<tr>");
					for (int i_d = 0; i_d < D.nbDays; i_d++) { // for each day at slot sl
						out.println("<td height = 20 width = 20>");
						int fc = findFCourse(i_d, i_sl, i_cl);
						if (fc >= 0)
							out.println("C" + D.courseOfFCourse[fc]);
						out.println("</td>");
					}
					out.println("</tr>");
				}
				out.println("</tr>");
			}

			out.println("</table>");
			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void printSol() {
		// Fcourse[i], c = courseOfFCourse[i], teacherOfFCourse[c],
		// x_day[i].value, x_slot[i].value, classOfFCourse[i]
		System.out.println("i, class, course, teacher, day, slot:");
		for (int i = 0; i < D.nbFCourses; i++) {
			int cl = D.classOfFCourse[i];
			int course = D.courseOfFCourse[i];
			int tc = D.teacherOfFCourse[i];
			int d = x_day[i].getValue();
			int sl = x_slot[i].getValue();
			System.out.println(i+", "+cl + ", " + course + ", " + tc + ", " + d + ", "+ sl);
		}

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Data D = new Data();
		D.loadData("data.txt");

		Model1 M = new Model1(D);
		M.stateModel();
		M.search();

		M.printResultHTML("timetable.html");
		M.printSol();

	}

}
