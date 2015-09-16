package flextrade.flexvision.fx.base.feature.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import flextrade.flexvision.fx.base.feature.FeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.togglz.core.Feature;
import org.togglz.core.manager.FeatureManager;

import javax.annotation.PostConstruct;
import java.util.Set;

public class CachedFeatureServiceImpl implements FeatureService{

	@Autowired
	private FeatureManager featureManager;

	private Cache<Feature, Boolean> featuresCache;

	@PostConstruct
	public void init() {
		featuresCache = CacheBuilder.<Feature, Boolean>newBuilder().build();
		Set<Feature> features = featureManager.getFeatures();
		features.stream().forEach(feature -> {
			if (featureManager.isActive(feature)) {
				featuresCache.put(feature, Boolean.TRUE);
			} else {
				featuresCache.put(feature, Boolean.FALSE);
			}
		});
	}

	@Override
	public Set<Feature> getFeatures() {
		return featuresCache.asMap().keySet();
	}

	@Override
	public boolean isActive(Feature feature) {
		Boolean ifPresent = featuresCache.getIfPresent(feature);
		return ifPresent == null ? false : ifPresent;
	}

	@Override
	public boolean isNotActive(Feature feature) {
		return !isActive(feature);
	}
}
