/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */

package org.apache.poi.hssf.record.formula.functions;

import org.apache.poi.hssf.record.formula.eval.ErrorEval;
import org.apache.poi.hssf.record.formula.eval.Eval;
import org.apache.poi.hssf.record.formula.eval.EvaluationException;
import org.apache.poi.hssf.record.formula.eval.NumberEval;
import org.apache.poi.hssf.record.formula.eval.OperandResolver;
import org.apache.poi.hssf.record.formula.eval.ValueEval;

/**
 * @author Amol S. Deshmukh &lt; amolweb at ya hoo dot com &gt;
 */
public abstract class NumericFunction implements Function {

	static final double ZERO = 0.0;
	static final double TEN = 10.0;
	static final double LOG_10_TO_BASE_e = Math.log(TEN);

	protected static final double singleOperandEvaluate(Eval arg, int srcCellRow, short srcCellCol) throws EvaluationException {
		ValueEval ve = OperandResolver.getSingleValue(arg, srcCellRow, srcCellCol);
		double result = OperandResolver.coerceValueToDouble(ve);
		checkValue(result);
		return result;
	}

	private static final void checkValue(double result) throws EvaluationException {
		if (Double.isNaN(result) || Double.isInfinite(result)) {
			throw new EvaluationException(ErrorEval.NUM_ERROR);
		}
	}

	public final Eval evaluate(Eval[] args, int srcCellRow, short srcCellCol) {
		double result;
		try {
			result = eval(args, srcCellRow, srcCellCol);
			checkValue(result);
		} catch (EvaluationException e) {
			return e.getErrorEval();
		}
		return new NumberEval(result);
	}

	protected abstract double eval(Eval[] args, int srcCellRow, short srcCellCol) throws EvaluationException;

	/* -------------------------------------------------------------------------- */
	// intermediate sub-classes (one-arg, two-arg and multi-arg


	public static abstract class OneArg extends NumericFunction {
		protected OneArg() {
			// no fields to initialise
		}
		protected final double eval(Eval[] args, int srcCellRow, short srcCellCol) throws EvaluationException {
			if (args.length != 1) {
				throw new EvaluationException(ErrorEval.VALUE_INVALID);
			}
			double d = singleOperandEvaluate(args[0], srcCellRow, srcCellCol);
			return evaluate(d);
		}
		protected abstract double evaluate(double d) throws EvaluationException;
	}

	public static abstract class TwoArg extends NumericFunction {
		protected TwoArg() {
			// no fields to initialise
		}
		protected final double eval(Eval[] args, int srcCellRow, short srcCellCol) throws EvaluationException {
			if (args.length != 2) {
				throw new EvaluationException(ErrorEval.VALUE_INVALID);
			}
			double d0 = singleOperandEvaluate(args[0], srcCellRow, srcCellCol);
			double d1 = singleOperandEvaluate(args[1], srcCellRow, srcCellCol);
			return evaluate(d0, d1);
		}
		protected abstract double evaluate(double d0, double d1) throws EvaluationException;
	}

	public static abstract class MultiArg extends NumericFunction {
		private final int _minArgs;
		private final int _maxArgs;
		protected MultiArg(int minArgs, int maxArgs) {
			_minArgs = minArgs;
			_maxArgs = maxArgs;
		}
		protected final double eval(Eval[] args, int srcCellRow, short srcCellCol) throws EvaluationException {
			int nArgs = args.length;
			if (nArgs < _minArgs || nArgs > _maxArgs) {
				throw new EvaluationException(ErrorEval.VALUE_INVALID);
			}
			double[] ds = new double[nArgs];
			for(int i=0; i<nArgs; i++) {
				ds[i] = singleOperandEvaluate(args[i], srcCellRow, srcCellCol);
			}
			return evaluate(ds);
		}
		protected abstract double evaluate(double[] ds) throws EvaluationException;
	}

	/* -------------------------------------------------------------------------- */


	public static final Function ABS = new OneArg() {
		protected double evaluate(double d) {
			return Math.abs(d);
		}
	};
	public static final Function ACOS = new OneArg() {
		protected double evaluate(double d) {
			return Math.acos(d);
		}
	};
	public static final Function ACOSH = new OneArg() {
		protected double evaluate(double d) {
			return MathX.acosh(d);
		}
	};
	public static final Function ASIN = new OneArg() {
		protected double evaluate(double d) {
			return Math.asin(d);
		}
	};
	public static final Function ASINH = new OneArg() {
		protected double evaluate(double d) {
			return MathX.asinh(d);
		}
	};
	public static final Function ATAN = new OneArg() {
		protected double evaluate(double d) {
			return Math.atan(d);
		}
	};
	public static final Function ATANH = new OneArg() {
		protected double evaluate(double d) {
			return MathX.atanh(d);
		}
	};
	public static final Function COS = new OneArg() {
		protected double evaluate(double d) {
			return Math.cos(d);
		}
	};
	public static final Function COSH = new OneArg() {
		protected double evaluate(double d) {
			return MathX.cosh(d);
		}
	};
	public static final Function DEGREES = new OneArg() {
		protected double evaluate(double d) {
			return Math.toDegrees(d);
		}
	};
	public static final Function DOLLAR = new OneArg() {
		protected double evaluate(double d) {
			return d;
		}
	};
	public static final Function EXP = new OneArg() {
		protected double evaluate(double d) {
			return Math.pow(Math.E, d);
		}
	};
	public static final Function FACT = new OneArg() {
		protected double evaluate(double d) {
			return MathX.factorial((int)d);
		}
	};
	public static final Function INT = new OneArg() {
		protected double evaluate(double d) {
			return Math.round(d-0.5);
		}
	};
	public static final Function LN = new OneArg() {
		protected double evaluate(double d) {
			return Math.log(d);
		}
	};
	public static final Function LOG10 = new OneArg() {
		protected double evaluate(double d) {
			return Math.log(d) / LOG_10_TO_BASE_e;
		}
	};
	public static final Function RADIANS = new OneArg() {
		protected double evaluate(double d) {
			return Math.toRadians(d);
		}
	};
	public static final Function SIGN = new OneArg() {
		protected double evaluate(double d) {
			return MathX.sign(d);
		}
	};
	public static final Function SIN = new OneArg() {
		protected double evaluate(double d) {
			return Math.sin(d);
		}
	};
	public static final Function SINH = new OneArg() {
		protected double evaluate(double d) {
			return MathX.sinh(d);
		}
	};
	public static final Function SQRT = new OneArg() {
		protected double evaluate(double d) {
			return Math.sqrt(d);
		}
	};

	public static final Function TAN = new OneArg() {
		protected double evaluate(double d) {
			return Math.tan(d);
		}
	};
	public static final Function TANH = new OneArg() {
		protected double evaluate(double d) {
			return MathX.tanh(d);
		}
	};


	/* -------------------------------------------------------------------------- */

	public static final Function ATAN2 = new TwoArg() {
		protected double evaluate(double d0, double d1) throws EvaluationException {
			if (d0 == ZERO && d1 == ZERO) {
				throw new EvaluationException(ErrorEval.DIV_ZERO);
			}
			return Math.atan2(d1, d0);
		}
	};
	public static final Function CEILING = new TwoArg() {
		protected double evaluate(double d0, double d1) {
			return MathX.ceiling(d0, d1);
		}
	};
	public static final Function COMBIN = new TwoArg() {
		protected double evaluate(double d0, double d1) throws EvaluationException {
			if (d0 > Integer.MAX_VALUE || d1 > Integer.MAX_VALUE) {
				throw new EvaluationException(ErrorEval.NUM_ERROR);
			}
			return  MathX.nChooseK((int) d0, (int) d1);
		}
	};
	public static final Function FLOOR = new TwoArg() {
		protected double evaluate(double d0, double d1) throws EvaluationException {
			if (d1 == ZERO) {
				if (d0 == ZERO) {
					return ZERO;
				}
				throw new EvaluationException(ErrorEval.DIV_ZERO);
			}
			return MathX.floor(d0, d1);
		}
	};
	public static final Function MOD = new TwoArg() {
		protected double evaluate(double d0, double d1) throws EvaluationException {
			if (d1 == ZERO) {
				throw new EvaluationException(ErrorEval.DIV_ZERO);
			}
			return MathX.mod(d0, d1);
		}
	};
	public static final Function POWER = new TwoArg() {
		protected double evaluate(double d0, double d1) {
			return Math.pow(d0, d1);
		}
	};
	public static final Function ROUND = new TwoArg() {
		protected double evaluate(double d0, double d1) {
			return MathX.round(d0, (int)d1);
		}
	};
	public static final Function ROUNDDOWN = new TwoArg() {
		protected double evaluate(double d0, double d1) {
			return MathX.roundDown(d0, (int)d1);
		}
	};
	public static final Function ROUNDUP = new TwoArg() {
		protected double evaluate(double d0, double d1) {
			return MathX.roundUp(d0, (int)d1);
		}
	};

	/* -------------------------------------------------------------------------- */

	public static final Function LOG = new MultiArg(1,2) {
		protected double evaluate(double[] ds) {

			double logE = Math.log(ds[0]);
			if (ds.length == 1) {
				return logE / LOG_10_TO_BASE_e;
			}
			double base = ds[1];
			if (base == Math.E) {
				return logE;
			}
			return logE / Math.log(base);
		}
	};
}