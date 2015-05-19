package userDefinedFunction;

import java.util.HashSet;

import localsearch.constraints.basic.IsEqual;
import localsearch.functions.basic.FuncMinus;
import localsearch.functions.basic.FuncVarConst;
import localsearch.model.AbstractInvariant;
import localsearch.model.ConstraintSystem;
import localsearch.model.IFunction;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;

public class Abs extends AbstractInvariant implements IFunction{
	private IFunction _f1;
	private IFunction _f2;
	private int _value;
	private int _maxValue;
	private int _minValue;
	private VarIntLS[] _x;
	private LocalSearchManager _ls;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LocalSearchManager ls = new LocalSearchManager();
		ConstraintSystem S;
		
		VarIntLS[] x = new VarIntLS[2];
		x[0] = new VarIntLS(ls, 0, 10);
		x[1] = new VarIntLS(ls, 0, 10);
		
		S = new ConstraintSystem(ls);		
		
		S.post(new IsEqual(new Abs(x[0],x[1]), 2));
		ls.close();
		localsearch.search.TabuSearch ts = new localsearch.search.TabuSearch();
		ts.search(S, 5, 30, 100, 50);
		System.out.println(x[0].getValue());
		System.out.println(x[1].getValue());
		
	}
	
	public Abs(VarIntLS x, VarIntLS y) {
		// TODO Auto-generated constructor stub
		_ls = x.getLocalSearchManager();
		_f1 = new FuncVarConst(x);
		_f2 = new FuncVarConst(y);
		post();
	}
	
	public Abs(IFunction f1, IFunction f2) {
		// TODO Auto-generated constructor stub
		_ls = f1.getLocalSearchManager();
		_f1 = f1;
		_f2 = f2;
		post();
	}

	public Abs(IFunction f, VarIntLS x){
		_ls = f.getLocalSearchManager();
		_f1 = f;
		_f2 = new FuncVarConst(x);
		post();
	}
	
	public Abs(IFunction f, int val){
		_ls = f.getLocalSearchManager();
		_f1 = f;
		_f2 = new FuncVarConst(_ls, val);
		post();
	}
	
	public Abs(VarIntLS x, int val){
		_ls = x.getLocalSearchManager();
		_f1 = new FuncVarConst(x);
		_f2 = new FuncVarConst(_ls, val);
		post();
	}
	
	private void post() {
		HashSet<VarIntLS> S = new HashSet<VarIntLS>();
		VarIntLS[] x1 = _f1.getVariables();
		VarIntLS[] x2 = _f2.getVariables();

		if (x1 != null) {
			for (int i = 0; i < x1.length; i++) {
				S.add(x1[i]);
			}
		}
		if (x2 != null) {
			for (int i = 0; i < x2.length; i++) {
				S.add(x2[i]);
			}
		}

		_x = new VarIntLS[S.size()];
		int i = 0;
		for (VarIntLS e : S) {
			_x[i] = e;
			i++;
		}

		_value = Math.abs(_f1.getValue() - _f2.getValue());

		// need to be verified
		_maxValue = Math.abs(_f1.getMaxValue() + _f2.getMaxValue());
		_minValue = Math.abs(_f1.getMinValue() - _f2.getMinValue());
		_ls.post(this);
	}
	
	@Override
	public int getAssignDelta(VarIntLS x, int val) {
		// TODO Auto-generated method stub
		if (!(x.IsElement(_x)))
			return 0;

		// need to be verified
		return _f1.getAssignDelta(x, val) + _f2.getAssignDelta(x, val);

	}

	@Override
	public int getSwapDelta(VarIntLS x, VarIntLS y) {
		// TODO Auto-generated method stub
		if ((!(x.IsElement(_x))) && (!(y.IsElement(_x))))
			return 0;
		// need to be verified
		return _f1.getSwapDelta(x, y) + _f2.getSwapDelta(x, y);
	}


	@Override
	public int getMaxValue() {
		// TODO Auto-generated method stub
		return this._maxValue;
	}

	@Override
	public int getMinValue() {
		// TODO Auto-generated method stub
		return this._minValue;
	}

	@Override
	public int getValue() {
		// TODO Auto-generated method stub
		return this._value;
	}
	
	public String name() {
		return "Abs";
	}
	
	public LocalSearchManager getLocalSearchManager() {
		return this._ls;
	}

	public VarIntLS[] getVariables() {
		return this._x;
	}
	
	@Override
	public void propagateInt(VarIntLS x, int val) {
		_value = Math.abs(_f1.getValue() - _f2.getValue());
	}

	@Override
	public void initPropagate() {
		_value = Math.abs(_f1.getValue() - _f2.getValue());
	}
}
