package algorithms;

import java.util.ArrayList;

import localsearch.model.*;
import localsearch.constraints.basic.*;
import localsearch.functions.basic.*;

import java.io.*;

import userDefinedFunction.Abs;
public class Model {

	/**
	 * @param args
	 */
	public Data D;
	public VarIntLS[] x_day;// x_day[c] is the day of fCourse c
	public VarIntLS[] x_slot;// x_slot[c] is the slot of fCourse c
	public VarIntLS[] x_room;// x_room[c] is the room of fCourse c;
	public LocalSearchManager ls;
	public ConstraintSystem S;
	public LocalSearchManager _ls;
	public ConstraintSystem _S;
	
	public Model(Data D){
		this.D = D;
	}
	

	
	public void stateModel(){ 
		//chi moi khoi tao
		ls = new LocalSearchManager();
		x_day = new VarIntLS[D.nbFCourses];
		x_slot = new VarIntLS[D.nbFCourses];
		
		for(int i = 0; i < D.nbFCourses; i++){
			x_day[i] = new VarIntLS(ls,0,D.nbDays-1);
			x_slot[i] = new VarIntLS(ls,0,2*D.nbSlotOfHalfDay-1);
		}
		S = new ConstraintSystem(ls);
//		for(int c = 0; c < D.nbCourses; c++){
//			ArrayList<Integer> L = D.fCourse[c]; //fCourse is an arraylist of arraylists
//			for(int j1 = 0; j1 < L.size()-1; j1++){
////				for (int i = j1+1; i < L.size(); i++) {
////					int fc1 = L.get(j1);
////					int fc2 = L.get(i);
////					IConstraint c1 = new IsEqual(x_day[fc1],x_day[fc2]);
////					IConstraint c2 = new IsEqual(new FuncPlus(x_slot[fc1],1),x_slot[fc2]);
////					S.post(new Implicate(c1,c2));
////					
////					IConstraint c3 = new NotEqual(x_day[fc1],x_day[fc2]);
////					IConstraint c4 = new IsEqual(new FuncPlus(x_day[fc1], 2), x_day[fc2]);
//////					IConstraint c4 = new IsEqual(new Abs(x_day[fc1],x_day[fc2]),2);
////					S.post(new Implicate(c3,c4));
////				}
//				
//				int fc1 = L.get(j1);
//				int fc2 = L.get(j1+1);
//				IConstraint c1 = new IsEqual(x_day[fc1],x_day[fc2]);
//				IConstraint c2 = new IsEqual(new FuncPlus(x_slot[fc1],1),x_slot[fc2]);
//				S.post(new Implicate(c1,c2));
//				
//				IConstraint c3 = new NotEqual(x_day[fc1],x_day[fc2]);
////				IConstraint c4 = new IsEqual(new FuncPlus(x_day[fc1], 2), x_day[fc2]);
//				IConstraint c4 = new IsEqual(new Abs(x_day[fc1],x_day[fc2]),2);
//				S.post(new Implicate(c3,c4));
//				
//			}
//		}
		
		IConstraint[][] cd = new IConstraint[D.nbFCourses][D.nbFCourses];
		IConstraint[][] cs = new IConstraint[D.nbFCourses][D.nbFCourses];
		for(int i = 0; i < D.nbFCourses-1; i++){
			for(int j = i + 1; j < D.nbFCourses; j++){
				cd[i][j] = new IsEqual(x_day[i],x_day[j]);
				cs[i][j] = new NotEqual(x_slot[i],x_slot[j]);
			}
		}
		for(int i = 0; i < D.nbFCourses-1; i++){
			for(int j = i + 1; j < D.nbFCourses; j++){
				if(D.classOfFCourse[i] == D.classOfFCourse[j] || D.teacherOfFCourse[i] == D.teacherOfFCourse[j]){
					S.post(new Implicate(cd[i][j],cs[i][j]));
				}
				
			}
		}

		
		for(int i = 0; i < D.nbFCourses-1; i++){
			for(int j = i + 1; j < D.nbFCourses; j++){
				if(D.classOfFCourse[i] == D.classOfFCourse[j]){
					IConstraint ci = new LessOrEqual(x_slot[i], D.nbSlotOfHalfDay-1);
					IConstraint cj = new LessOrEqual(x_slot[j], D.nbSlotOfHalfDay-1);
					S.post(new Implicate(ci,cj));
					
					ci = new LessOrEqual(new FuncVarConst(ls,D.nbSlotOfHalfDay),x_slot[i]);
					cj = new LessOrEqual(new FuncVarConst(ls,D.nbSlotOfHalfDay),x_slot[j]);
					S.post(new Implicate(ci,cj));
				}
				
			}
		}

		for(int i = 0; i < D.nbFCourses; i++){
			int t = D.teacherOfFCourse[i];
			for(int j = 0; j < D.busyOfTeacher[t].size(); j++){
				DaySlot ds = D.busyOfTeacher[t].get(j);
				IConstraint c1 = new IsEqual(x_day[i],ds.d);
				IConstraint c2 = new NotEqual(x_slot[i],ds.s);
				S.post(new Implicate(c1,c2));
			}
		}
		ls.close();
	}
	
	public void _stateModel2(){
		//new local search manager _ls
		_ls = new LocalSearchManager();
		
		//new constraint system _S
		_S = new ConstraintSystem(_ls);
		
		//keep the old variables
		
		
		//include basic constraints
		IConstraint[][] cd = new IConstraint[D.nbFCourses][D.nbFCourses];
		IConstraint[][] cs = new IConstraint[D.nbFCourses][D.nbFCourses];
		
		for(int i = 0; i < D.nbFCourses-1; i++){
			for(int j = i + 1; j < D.nbFCourses; j++){
				cd[i][j] = new IsEqual(x_day[i],x_day[j]);
				cs[i][j] = new NotEqual(x_slot[i],x_slot[j]);
			}
		}
		for(int i = 0; i < D.nbFCourses-1; i++){
			for(int j = i + 1; j < D.nbFCourses; j++){
				if(D.classOfFCourse[i] == D.classOfFCourse[j] || D.teacherOfFCourse[i] == D.teacherOfFCourse[j]){
					//1 lop ko the hok 2 slot cung 1 luc
					//1 giao vien ko the day 2 lop cung 1 luc
					S.post(new Implicate(cd[i][j],cs[i][j])); 
				}
				
			}
		}
		
		//1 lop chi nen hok trong 1 buoi
		for(int i = 0; i < D.nbFCourses-1; i++){
			for(int j = i + 1; j < D.nbFCourses; j++){
				if(D.classOfFCourse[i] == D.classOfFCourse[j]){
					IConstraint ci = new LessOrEqual(x_slot[i], D.nbSlotOfHalfDay-1);
					IConstraint cj = new LessOrEqual(x_slot[j], D.nbSlotOfHalfDay-1);
					
					S.post(new Implicate(ci,cj));
					
					ci = new LessOrEqual(new FuncVarConst(ls,D.nbSlotOfHalfDay),x_slot[i]);
					cj = new LessOrEqual(new FuncVarConst(ls,D.nbSlotOfHalfDay),x_slot[j]);
					S.post(new Implicate(ci,cj));
				}
				
			}
		}

		//giao vien ban thi khong the day
		for(int i = 0; i < D.nbFCourses; i++){
			int t = D.teacherOfFCourse[i];
			for(int j = 0; j < D.busyOfTeacher[t].size(); j++){
				DaySlot ds = D.busyOfTeacher[t].get(j);
				IConstraint c1 = new IsEqual(x_day[i],ds.d);
				IConstraint c2 = new NotEqual(x_slot[i],ds.s);
				S.post(new Implicate(c1,c2));
			}
		}
		
		
		//add some new constraints
		
		
		
		_ls.close();
	}
	
	public void search(){
		localsearch.search.TabuSearch ts = new localsearch.search.TabuSearch();
		ts.search(S, 50, 30, 10000, 50);
	}
	
	public void _search2(){
		//this choose day vs slot for each fragment
		this.search();
		
		//after choose day for each fragment
		//make swapping between fragments
		
	}
	public int findFCourse(int d, int sl, int Class){
		for(int i = 0; i < D.nbFCourses; i++){
			if(x_day[i].getValue() == d && x_slot[i].getValue() == sl && D.classOfFCourse[i] == Class) return i;
		}
		return -1;
	}
	public void printResultHTML(String fn){
		try{
			PrintWriter out = new PrintWriter(fn);
			out.println("<table border = 1>");
			for(int i_cl = 0; i_cl < D.nbClasses; i_cl++){
				out.println("<tr>");
				out.println("<td colspan=5>" + "Class " + i_cl + "</td>");
				for(int i_sl = 0; i_sl < D.nbSlotOfHalfDay*2; i_sl++){
					out.println("<tr>");
					for(int i_d = 0; i_d < D.nbDays; i_d++){
						out.println("<td height = 20 width = 20>");
						int fc = findFCourse(i_d,i_sl,i_cl);
						if(fc >= 0) out.println("C" + D.courseOfFCourse[fc]);
						out.println("</td>");
					}
					out.println("</tr>");
				}
				out.println("</tr>");
			}
			out.println("</table>");
			out.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void printSol(String fn){
		try {			 
			File file = new File(fn);
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			for (int i_cl = 0; i_cl < D.nbClasses; i_cl++) { //for each class i
				ArrayList<Integer> c_List = D.coursesOfClass[i_cl];
				//for each course c of class i
				for (Integer c : c_List) {		
					ArrayList<Integer> fc_List = D.fCourse[c];
					//for each fragment fc of course c
					for (Integer fc : fc_List) {
						String s = i_cl+", "+c+", "+D.teacherOfCourse[c]+", "+
								fc+", "+x_day[fc].getValue()+", "+ x_slot[fc].getValue()+"\n";
						bw.write(s);
					}
				}
			}
			
			bw.close();
 
			System.out.println("Done");
 
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Data D = new Data();
		D.loadData("data.txt");
		
		Model M = new Model(D);
//		M._model();
		M.stateModel();
		M.search();
		
		M.printResultHTML("timetable.html");
		M.printSol("data_temp.txt");
	}

}
