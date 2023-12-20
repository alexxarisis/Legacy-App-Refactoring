package org.alexxarisis.model.strategies;

import java.util.HashMap;

public class VersionsStrategyFactory {

	private final HashMap<String, VersionsStrategy> strategies;
	
	public VersionsStrategyFactory() {
		strategies = new HashMap<>();
		strategies.put("volatileStrategy", new VolatileVersionsStrategy());
		strategies.put("stableStrategy", new StableVersionsStrategy());
	}

	public VersionsStrategy createStrategy(String type) {
		return strategies.get(type);
	}
}