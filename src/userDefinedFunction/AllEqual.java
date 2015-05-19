package userDefinedFunction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import localsearch.constraints.basic.IsEqual;
import localsearch.model.AbstractInvariant;
import localsearch.model.IConstraint;
import localsearch.model.IFunction;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;

public class AllEqual extends AbstractInvariant implements IConstraint {
	private IConstraint[] _c;
	private LocalSearchManager _ls;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		

	}


	public AllEqual(VarIntLS[] x) {
		if (x != null) {
			_ls = x[0].getLocalSearchManager();
			
			int len = x.length-1;
			_c = new IConstraint[len];
			for (int i = 0; i < len; i++) {
				_c[i] = new IsEqual(x[i], x[i+1]);
			}
			
			post();
		} 
		System.out.println("reached");
	}

	public AllEqual(IFunction[] f){
		if (f != null) {
			_ls = f[0].getLocalSearchManager();
			int len = f.length-1;
			IConstraint[] _c = new IConstraint[len];
			for (int i = 0; i < len; i++) {
				_c[i] = new IsEqual(f[i], f[i+1]);
			}
			post();
		} 
	}
	
	private void post(){
		_ls.post(this);
	}
	
	@Override
	public VarIntLS[] getVariables() {
		HashSet<VarIntLS> S = new HashSet<VarIntLS>();
		if (_c == null) {
			System.out.println("null");
		}
		for (int i = 0; i < _c.length; i++) {
			
			VarIntLS[] temp = new VarIntLS[_c[i].getVariables().length]; 
			temp = _c[i].getVariables();
			int len = temp.length;
			for (int j = 0; j < len; j++) {
				S.add(temp[j]);
			}
		}
		
		VarIntLS[] temp = new VarIntLS[S.size()];
		int i = 0;
		for (VarIntLS e : S) {
			temp[i] = e;
			i++;
		}
		return temp;
	}

	
	@Override
	public int getAssignDelta(VarIntLS x, int val) {
		int sum = 0;
		for (int i = 0; i < _c.length; i++) {
			sum += _c[i].getAssignDelta(x, val);
		}
		return sum;
	}

	@Override
	public int getSwapDelta(VarIntLS x, VarIntLS y) {
		int sum = 0;
		for (int i = 0; i < _c.length; i++) {
			sum += _c[i].getSwapDelta(x, y);
		}
		return sum;
	}

	@Override
	public int violations() {
		int sum = 0;
		for (int i = 0; i < _c.length; i++) {
			sum += _c[i].violations();
		}
		return sum;
	}

	@Override
	public int violations(VarIntLS x) {
		int sum = 0;
		for (int i = 0; i < _c.length; i++) {
			sum += _c[i].violations(x);
		}
		return sum;
	}
	
	public LocalSearchManager getLocalSearchManager(){
		return this._ls;
	}
	
	public IConstraint[] getIConstraint(){
		return this._c;
	}

	@Override
	public void propagateInt(VarIntLS x, int val) {
		// DO NOTHING
	}
	
	@Override
	public void initPropagate() {
		// DO NOHTING
	}

}
