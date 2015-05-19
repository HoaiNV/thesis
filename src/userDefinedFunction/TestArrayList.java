package userDefinedFunction;

import java.util.ArrayList;

public class TestArrayList {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<Integer> l = new ArrayList<>();
		l.add(1);
		l.add(2);
		l.add(3);
		ArrayList<Integer> l1 = new ArrayList<>();
		l1.add(1);
		l1.add(2);
//		l.removeAll(l1);
		
		ArrayList<Integer> l2 = new ArrayList<>();
		l2 = (ArrayList<Integer>) l.clone();
		l2.removeAll(l1);
		
		for (Integer e : l2) {
			System.out.println(e);
		}
		
	}

}
