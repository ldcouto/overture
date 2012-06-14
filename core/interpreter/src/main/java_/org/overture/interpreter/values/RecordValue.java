/*******************************************************************************
 *
 *	Copyright (C) 2008 Fujitsu Services Ltd.
 *
 *	Author: Nick Battle
 *
 *	This file is part of VDMJ.
 *
 *	VDMJ is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *
 *	VDMJ is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU General Public License
 *	along with VDMJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 ******************************************************************************/

package org.overture.interpreter.values;

import java.util.Iterator;

import org.overture.ast.types.AFieldField;
import org.overture.ast.types.ARecordInvariantType;
import org.overture.ast.types.PType;
import org.overture.config.Settings;
import org.overture.interpreter.assistant.type.PTypeAssistantInterpreter;
import org.overture.interpreter.assistant.type.SInvariantTypeAssistantInterpreter;
import org.overture.interpreter.runtime.Context;
import org.overture.interpreter.runtime.ValueException;



public class RecordValue extends Value
{
	private static final long serialVersionUID = 1L;
	public final ARecordInvariantType type;
	public final FieldMap fieldmap;
	public final FunctionValue invariant;

	// mk_ expressions
	public RecordValue(ARecordInvariantType type,	ValueList values, Context ctxt)
		throws ValueException
	{
		this.type = type;
		this.fieldmap = new FieldMap();
		this.invariant = SInvariantTypeAssistantInterpreter.getInvariant(type,ctxt);

		if (values.size() != type.getFields().size())
		{
			abort(4078, "Wrong number of fields for " + type.getName(), ctxt);
		}

		Iterator<AFieldField> fi = type.getFields().iterator();

		for (Value v: values)
		{
			AFieldField f = fi.next();
			fieldmap.add(f.getTag(), v.convertTo(f.getType(), ctxt), !f.getEqualityAbstraction());
		}

		if (invariant != null && Settings.invchecks)
		{
			// In VDM++ and VDM-RT, we do not want to do thread swaps half way
			// through an invariant check, so we set the atomic flag around the
			// conversion. This also stops VDM-RT from performing "time step"
			// calculations.

			ctxt.threadState.setAtomic(true);
			boolean inv = invariant.eval(invariant.location, this, ctxt).boolValue(ctxt);
			ctxt.threadState.setAtomic(false);

			if (!inv)
			{
				abort(4079, "Type invariant violated by mk_ arguments", ctxt);
			}
		}
	}

	// mu_ expressions
	public RecordValue(ARecordInvariantType type,	FieldMap mapvalues, Context ctxt)
		throws ValueException
	{
		this.type = type;
		this.fieldmap = new FieldMap();
		this.invariant = SInvariantTypeAssistantInterpreter.getInvariant(type,ctxt);

		if (mapvalues.size() != type.getFields().size())
		{
			abort(4080, "Wrong number of fields for " + type.getName(), ctxt);
		}

		Iterator<AFieldField> fi = type.getFields().iterator();

		while (fi.hasNext())
		{
			AFieldField f = fi.next();
			Value v = mapvalues.get(f.getTag());

			if (v == null)
			{
				abort(4081, "Field not defined: " + f.getTag(), ctxt);
			}

			fieldmap.add(f.getTag(), v.convertTo(f.getType(), ctxt), !f.getEqualityAbstraction());
		}

		if (invariant != null &&
			!invariant.eval(invariant.location, this, ctxt).boolValue(ctxt))
		{
			abort(4082, "Type invariant violated by mk_ arguments", ctxt);
		}
		if (invariant != null && Settings.invchecks)
		{
			// In VDM++ and VDM-RT, we do not want to do thread swaps half way
			// through an invariant check, so we set the atomic flag around the
			// conversion. This also stops VDM-RT from performing "time step"
			// calculations.

			ctxt.threadState.setAtomic(true);
			boolean inv = invariant.eval(invariant.location, this, ctxt).boolValue(ctxt);
			ctxt.threadState.setAtomic(false);

			if (!inv)
			{
				abort(4079, "Type invariant violated by mk_ arguments", ctxt);
			}
		}
	}

	// Only called by clone()
	private RecordValue(ARecordInvariantType type, FieldMap mapvalues, FunctionValue invariant)
	{
		this.type = type;
		this.invariant = invariant;
		this.fieldmap = mapvalues;
	}

	// State records - invariant handled separately
	public RecordValue(ARecordInvariantType type, NameValuePairList mapvalues)
	{
		this.type = type;
		this.invariant = null;
		this.fieldmap = new FieldMap();

		for (NameValuePair nvp: mapvalues)
		{
			AFieldField f = PTypeAssistantInterpreter.findField(type,nvp.name.name);
			this.fieldmap.add(nvp.name.name, nvp.value, !f.getEqualityAbstraction());
		}
	}

	@Override
	public RecordValue recordValue(Context ctxt)
	{
		return this;
	}

	@Override
	public Value getUpdatable(ValueListenerList listeners)
	{
		FieldMap nm = new FieldMap();

		for (FieldValue fv: fieldmap)
		{
			Value uv = fv.value.getUpdatable(listeners);
			nm.add(fv.name, uv, fv.comparable);
		}

		return UpdatableValue.factory(new RecordValue(type, nm, invariant), listeners);
	}

	@Override
	public Value getConstant()
	{
		FieldMap nm = new FieldMap();

		for (FieldValue fv: fieldmap)
		{
			Value uv = fv.value.getConstant();
			nm.add(fv.name, uv, fv.comparable);
		}

		return new RecordValue(type, nm, invariant);
	}

	@Override
	public boolean equals(Object other)
	{
		if (other instanceof Value)
		{
			return compareTo((Value)other) == 0;
		}
		else
		{
			return false;
		}
	}

	@Override
	public int compareTo(Value other)
	{
		Value val = other.deref();

		if (val instanceof RecordValue)
		{
			RecordValue ot = (RecordValue)val;

			if (ot.type.equals(type))
			{
				for (AFieldField f: type.getFields())
				{
					if (!f.getEqualityAbstraction())
					{
						Value fv = fieldmap.get(f.getTag());
						Value ofv = ot.fieldmap.get(f.getTag());

						if (fv == null || ofv == null)
						{
							return -1;
						}

						int comp = fv.compareTo(ofv);

						if (comp != 0)
						{
							return comp;
						}
					}
				}

				return 0;
			}
		}

		return -1;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("mk_" + type.getName() + "(");

		Iterator<AFieldField> fi = type.getFields().iterator();

		if (fi.hasNext())
		{
    		String ftag = fi.next().getTag();
    		sb.append(fieldmap.get(ftag));

    		while (fi.hasNext())
    		{
    			ftag = fi.next().getTag();
    			sb.append(", " + fieldmap.get(ftag));
    		}
		}

		sb.append(")");
		return sb.toString();
	}

	@Override
	public int hashCode()
	{
		return type.getName().hashCode() + fieldmap.hashCode();
	}

	@Override
	public String kind()
	{
		return type.toString();
	}

	@Override
	public Value convertValueTo(PType to, Context ctxt) throws ValueException
	{
		if (to.equals(type))
		{
			return this;
		}
		else
		{
			return super.convertValueTo(to, ctxt);
		}
	}

	@Override
	public Object clone()
	{
		return new RecordValue(type, (FieldMap)fieldmap.clone(), invariant);
	}
}