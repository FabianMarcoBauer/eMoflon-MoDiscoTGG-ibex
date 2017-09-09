package org.emoflon.ibex.tgg.operational.csp.constraints.factories;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Supplier;
import org.emoflon.ibex.tgg.operational.csp.RuntimeTGGAttributeConstraint;

import org.emoflon.ibex.tgg.operational.csp.constraints.custom.UserDefined_isInParameter;
import org.emoflon.ibex.tgg.operational.csp.constraints.custom.UserDefined_isReturn;
import org.emoflon.ibex.tgg.operational.csp.constraints.custom.UserDefined_jVisibility2umlVisibility;
import org.emoflon.ibex.tgg.operational.csp.constraints.custom.UserDefined_nonParameterizedName;
import org.emoflon.ibex.tgg.operational.csp.constraints.custom.UserDefined_parameterizedNameFitsNonParameterizedName;

public class UserDefinedRuntimeTGGAttrConstraintFactory extends RuntimeTGGAttrConstraintFactory {

	private Collection<String> constraints; 
	private Map<String, Supplier<RuntimeTGGAttributeConstraint>> creators;
	
	public UserDefinedRuntimeTGGAttrConstraintFactory() {
		initialize();
	}
	
	private void initialize() {
		creators = new HashMap<>();
		creators.put("isInParameter", () -> new UserDefined_isInParameter());
		creators.put("isReturn", () -> new UserDefined_isReturn());
		creators.put("jVisibility2umlVisibility", () -> new UserDefined_jVisibility2umlVisibility());
		creators.put("nonParameterizedName", () -> new UserDefined_nonParameterizedName());
		creators.put("parameterizedNameFitsNonParameterizedName", () -> new UserDefined_parameterizedNameFitsNonParameterizedName());
		
		constraints = new HashSet<String>();
		constraints.addAll(creators.keySet());
	}
	
	@Override
	public RuntimeTGGAttributeConstraint createRuntimeTGGAttributeConstraint(String name) {
		Supplier<RuntimeTGGAttributeConstraint> creator = creators.get(name);
		if(creator == null)
			throw new RuntimeException("CSP not implemented");
		return creator.get();
	}
	
	@Override
	public boolean containsRuntimeTGGAttributeConstraint(String name) {
		return constraints.contains(name);
	}
}
