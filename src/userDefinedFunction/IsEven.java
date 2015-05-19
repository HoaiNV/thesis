package userDefinedFunction;

import localsearch.constraints.basic.IsEqual;
import localsearch.functions.basic.FuncVarConst;
import localsearch.model.AbstractInvariant;
import localsearch.model.ConstraintSystem;
import localsearch.model.IConstraint;
import localsearch.model.IFunction;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;

public class IsEven extends AbstractInvariant implements IConstraint{

	private IConstraint	_c;
	private LocalSearchManager _ls;

	public IsEven(VarIntLS x){
		_ls = x.getLocalSearchManager();
		IFunction[] f = new IFunction[1];		
		f[0] = new FuncVarConst(_ls,x.getValue()%2); //problem
		_c = new IsEqual(f[0], 0);
		post();
	}
	
	public IsEven(IFunction f){
		_ls = f.getLocalSearchManager();
		IFunction func = new FuncVarConst(_ls,f.getValue()%2); //problem
		_c = new IsEqual(func, 0);
		post();
	}
	
	private void post(){
		_ls.post(this);
	}
	
	@Override
	public int getAssignDelta(VarIntLS x, int val) {
		// TODO Auto-generated method stub
		return _c.getAssignDelta(x, val);
	}

	@Override
	public int getSwapDelta(VarIntLS x, VarIntLS y) {
		// TODO Auto-generated method stub
		return _c.getSwapDelta(x, y);
	}

	@Override
	public int violations() {
		// TODO Auto-generated method stub
		return _c.violations();
	}

	@Override
	public int violations(VarIntLS x) {
		// TODO Auto-generated method stub
		return _c.violations(x);
	}
	
	@Override
	public VarIntLS[] getVariables() {
		//return _x;
		return _c.getVariables();
	}
	
	@Override
	public void propagateInt(VarIntLS x, int val) {
		// DO NOTHING
	}

	@Override
	public void initPropagate() {
		// DO NOHTING
	}
	
	public void print(){
		// DO NOHTING
	}

	@Override
	public LocalSearchManager getLocalSearchManager() {
		// TODO Auto-generated method stub
		return _ls;
	}

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LocalSearchManager ls = new LocalSearchManager();
		VarIntLS x = new VarIntLS(ls,1,10);
		ConstraintSystem S = new ConstraintSystem(ls);
		
		IFunction[] f = new IFunction[1];
		x.setValue(4);
		
		
		S.post(new IsEven(x));
		ls.close();
		
		localsearch.search.TabuSearch ts = new localsearch.search.TabuSearch();
		ts.search(S, 5, 30, 100, 5);
		System.out.println(x.getValue());
		


		
//		f[0] = new FuncVarConst(ls,x.getValue()%2);

//		System.out.println(f[0].getValue());
	}

}
