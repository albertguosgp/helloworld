CREATE TABLE default_features (
  FEATURE_NAME    VARCHAR(100) PRIMARY KEY,
  FEATURE_ENABLED INTEGER,
  STRATEGY_ID     VARCHAR(200),
  STRATEGY_PARAMS VARCHAR(2000)
);
