package userDefinedFunction;

import java.util.HashSet;

import localsearch.functions.basic.FuncVarConst;
import localsearch.model.AbstractInvariant;
import localsearch.model.IFunction;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;

public class Minus1 extends AbstractInvariant implements IFunction {

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
		
//		VarIntLS[] x = new VarIntLS[10];
//		for (int i = 0; i < x.length; i++) {
//			x[i] = new VarIntLS(_ls, 0, 100);
//		}
//		x[3].setValue(10);
//		
//		IFunction[] f = new IFunction[x.length];
//		for (int i = 0; i < f.length; i++) {
//			f[i] = new Minus1(x[i], 10);
//		}
//		_ls.close();
//		System.out.println(f[3].getValue());
		
		VarIntLS x = new VarIntLS(ls, 0, 100);
		VarIntLS y = new VarIntLS(ls, 0, 100);
		x.setValue(10);
		y.setValue(4);
		System.out.println((new Minus1(x,y))._value);
		
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

		_value = _f1.getValue() - _f2.getValue();

		// need to be verified
		_maxValue = _f1.getMaxValue() - _f2.getMinValue();
		_minValue = _f1.getMinValue() - _f2.getMaxValue();
		_ls.post(this);
	}

	public Minus1(IFunction f1, IFunction f2) {
		_ls = f1.getLocalSearchManager();
		_f1 = f1;
		_f2 = f2;
		post();
	}

	public Minus1(IFunction f, VarIntLS x) {
		_ls = f.getLocalSearchManager();
		_f1 = f;
		_f2 = new FuncVarConst(x);
		post();
	}

	public Minus1(IFunction f, int val) {
		_ls = f.getLocalSearchManager();
		_f1 = f;
		_f2 = new FuncVarConst(_ls, val);
		post();
	}

	public Minus1(VarIntLS x, VarIntLS y) {
		_ls = x.getLocalSearchManager();
		_f1 = new FuncVarConst(x);
		_f2 = new FuncVarConst(y);
		post();
	}

	public Minus1(VarIntLS x, int val) {
		_ls = x.getLocalSearchManager();
		_f1 = new FuncVarConst(x);
		_f2 = new FuncVarConst(_ls, val);
		post();
	}
	
	@Override
	public void propagateInt(VarIntLS x, int val) {
		_value = _f1.getValue() - _f2.getValue();
	}

	@Override
	public void initPropagate() {
		_value = _f1.getValue() - _f2.getValue();
	}


	@Override
	public int getAssignDelta(VarIntLS x, int val) {
		// TODO Auto-generated method stub
		// return (!(x.IsElement(_x))) ? 0 : _f1.getAssignDelta(x, val) +
		// _f2.getAssignDelta(x, val);
		if (!(x.IsElement(_x)))
			return 0;

		// need to be verified
		return _f1.getAssignDelta(x, val) + _f2.getAssignDelta(x, val);

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
	public int getSwapDelta(VarIntLS x, VarIntLS y) {
		// TODO Auto-generated method stub
		if ((!(x.IsElement(_x))) && (!(y.IsElement(_x))))
			return 0;
		// need to be verified
		return _f1.getSwapDelta(x, y) + _f2.getSwapDelta(x, y);

	}

	@Override
	public int getValue() {
		// TODO Auto-generated method stub
		return this._value;
	}

	public LocalSearchManager getLocalSearchManager() {
		return this._ls;
	}

	public VarIntLS[] getVariables() {
		return this._x;
	}

	public String name() {
		return "Minus1";
	}

}
