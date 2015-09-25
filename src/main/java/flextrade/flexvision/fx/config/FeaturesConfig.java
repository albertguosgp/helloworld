package flextrade.flexvision.fx.config;

import flextrade.flexvision.fx.base.feature.SupportedFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.togglz.core.Feature;
import org.togglz.core.manager.TogglzConfig;
import org.togglz.core.repository.StateRepository;
import org.togglz.core.repository.jdbc.JDBCStateRepository;
import org.togglz.core.repository.util.DefaultMapSerializer;
import org.togglz.core.user.NoOpUserProvider;
import org.togglz.core.user.UserProvider;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Slf4j
@Configuration
public class FeaturesConfig implements TogglzConfig{
	@Value("${product.id:default}")
	private String productId;

	@Autowired
	private DataSource dataSource;

	@PostConstruct
	public void init() {
		log.info("Active product id is {} ", productId);
	}

	@Override
	public Class<? extends Feature> getFeatureClass() {
		return SupportedFeature.class;
	}

	@Override
	public StateRepository getStateRepository() {
		JDBCStateRepository.Builder builder = JDBCStateRepository.newBuilder(dataSource);
		builder.createTable(false).tableName(productId + "features").noCommit(true).serializer(DefaultMapSerializer.singleline());
		return builder.build();
	}

	@Override
	public UserProvider getUserProvider() {
		return new NoOpUserProvider();
	}
}
