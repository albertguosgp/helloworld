package flextrade.flexvision.fx.base.feature;

import org.togglz.core.Feature;

import java.util.Set;

/**
 * Feature service to check all features status currently configured.
 * */
public interface FeatureService {
	Set<Feature> getFeatures();
	boolean isActive(Feature feature);
}
