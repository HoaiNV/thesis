package algorithms;

import java.util.*;
import java.io.*;
//////////////////////////////////////////////////////////
class DaySlot{
	public int d;
	public int s;
	public DaySlot(int d, int s){
		this.d = d; this.s = s;
	}
}
///////////////////////////////////////////////////////////
public class Data {

	/**
	 * @param nbFCourses:
	 * fragment course (fCourse), e.g., a course C consists of 3 credits, then it has 3 fragments, 
	 * that can be attributed to 3 different slots
	 */	
	public int nbFCourses;  
	
	public int nbTeachers;
	public int nbClasses;
	public int[] teacherOfFCourse;// teacherOfCourse[c] is the teacher who teaches the fCourse c
	public int[] classOfFCourse;// classOfFCourse[c] is the class that is learning the fCourse c
	public int nbDays = 5;// Mon,..., Fri
	public int nbSlotOfHalfDay = 3;// 3 slots in the morning and 3 slots in the afternoon
	
	public int nbCourses;
	public ArrayList<Integer>[] fCourse;// fCourse[i] is the list of fCourse of the course i
	public ArrayList<Integer>[] coursesOfClass;// courseOfClass[i] is the list of course of class i
	public int[] courseOfFCourse;// courseOfFCourse[i] is the course of FCourse i
	public int[] teacherOfCourse;
	public int[] classOfCourse;
	public ArrayList<DaySlot>[] busyOfTeacher;
	
	public void geneData(String fn){
		try{
			int nbFCoursePerCourse = 3;
			int nbCoursePerClass = 5;
			nbClasses = 10;
			nbCourses = nbClasses * nbCoursePerClass;
			nbFCourses = nbCourses * nbFCoursePerCourse;
			nbTeachers = nbCourses;
			
			PrintWriter out = new PrintWriter(fn);
			
			out.println("#nbDays #nbSlotsOfHalfDay");
			out.println(nbDays + "  " + nbSlotOfHalfDay);
			
			out.println("#nbClasses");
			out.println(nbClasses);
			
			out.println("#nbTeacher");
			out.println(nbTeachers);
			
			out.println("teacher busy_day busy_slot");
			out.println(-1);
			
			out.println("nbCourses");
			out.println(nbCourses);
			
			out.println("#Course Teacher");
			int idxCourse = -1;
			for(int i_cl = 0; i_cl < nbClasses; i_cl++){				
				for(int i = 0; i < nbCoursePerClass; i++){
					idxCourse++;
					int idxTeacher = idxCourse;
					out.println(idxCourse + " " + idxTeacher + " " + i_cl);
				}
				
				
			}
			out.println(-1);
			
			out.println("#nbFCourses");
			out.println(nbFCourses);
			
			out.println("#FCourse Course");
			int idxFCourse = -1;
			for(int i = 0; i < nbCourses; i++){
				for(int j = 0; j < nbFCoursePerCourse; j++){
					idxFCourse++;
					out.println(idxFCourse + " " + i);
				}
			}
			out.println(-1);
			out.close();
			System.out.println("Data::genData finished");
		}catch(Exception ex){
			
		}
	}
	
	
	public void loadData(String fn){
		try{
			Scanner in = new Scanner(new File(fn));
			String line = in.nextLine();
			nbDays = in.nextInt();
			nbSlotOfHalfDay = in.nextInt();
			line = in.nextLine();
			line = in.nextLine();
			
			nbClasses = in.nextInt();
			line = in.nextLine();
			line = in.nextLine();
			
			
			nbTeachers = in.nextInt();
			
			line = in.nextLine();
			line = in.nextLine();
			busyOfTeacher = new ArrayList[nbTeachers];
			for(int i_t = 0;i_t < nbTeachers; i_t++)
				busyOfTeacher[i_t] = new ArrayList<DaySlot>();
			
			while(true){
				int t = in.nextInt();
				if(t == -1) break;
				int b_d = in.nextInt();
				int b_s = in.nextInt();
				busyOfTeacher[t].add(new DaySlot(b_d,b_s));
			}
			
			
			line = in.nextLine();
			line = in.nextLine();
			nbCourses = in.nextInt();
			
			line = in.nextLine();
			line = in.nextLine();
			
			teacherOfCourse = new int[nbCourses];
			classOfCourse = new int[nbCourses];
			coursesOfClass = new ArrayList[nbClasses];
			for (int i = 0; i < nbClasses; i++) {
				coursesOfClass[i] = new ArrayList<Integer>();
			}
			
			while(true){
				int c = in.nextInt();
				if(c == -1) break;
				int t = in.nextInt();
				int cl = in.nextInt();
				
				//System.out.println("c = " + c + ", t = " + t + ", nbTeachers = " + nbTeachers + ", nbCourses = " + nbCourses);
				teacherOfCourse[c] = t;
				classOfCourse[c] = cl;
				coursesOfClass[cl].add(c);
			}
			
			line = in.nextLine();
			line = in.nextLine();
			nbFCourses = in.nextInt();
			
			courseOfFCourse = new int[nbFCourses];
			fCourse = new ArrayList[nbCourses];
			
			line = in.nextLine();
			line = in.nextLine();
			while(true){
				int fc = in.nextInt();
				if(fc == -1) break;
				int c = in.nextInt();
				courseOfFCourse[fc] = c;
				
				if(fCourse[c] == null) fCourse[c] = new ArrayList<Integer>();
				
				fCourse[c].add(fc);
			}
			in.close();
			
			classOfFCourse = new int[nbFCourses];
			teacherOfFCourse = new int[nbFCourses];
			for(int i_fc = 0; i_fc < nbFCourses; i_fc++){
				int c = courseOfFCourse[i_fc];
				classOfFCourse[i_fc] = classOfCourse[c];
				teacherOfFCourse[i_fc] = teacherOfCourse[c];
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Data D = new Data();
		D.geneData("data.txt");
	}

}
