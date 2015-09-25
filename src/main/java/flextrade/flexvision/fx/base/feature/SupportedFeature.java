package flextrade.flexvision.fx.base.feature;

import org.togglz.core.Feature;
import org.togglz.core.context.FeatureContext;

public enum SupportedFeature implements Feature{

	MANUALLY_ROLL_OVER_REMINDER_EMAIL;

	public boolean isActive() {
		return FeatureContext.getFeatureManager().isActive(this);
	}
}
