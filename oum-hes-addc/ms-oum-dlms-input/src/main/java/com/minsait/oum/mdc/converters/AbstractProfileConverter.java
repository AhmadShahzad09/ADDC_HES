package com.minsait.oum.mdc.converters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractProfileConverter<S, D> {

	public D toList(Iterable<S> source) {

		List<D> elements = new ArrayList<>();

		for (Iterator<S> it = source.iterator(); it.hasNext();) {
			S s = it.next();
			D d = convert(s);
			elements.addAll((Collection<? extends D>) d);
		}

		return (D) elements;
	}

	public abstract D convert(S source);
}
