CREATE TABLE co_045_features (
  FEATURE_NAME    VARCHAR(100) PRIMARY KEY,
  FEATURE_ENABLED INTEGER,
  STRATEGY_ID     VARCHAR(200),
  STRATEGY_PARAMS VARCHAR(2000)
);

INSERT INTO co_045_features(
  feature_name, feature_enabled, strategy_id, strategy_params)
VALUES ('MANUALLY_ROLL_OVER_REMINDER_EMAIL', 0, NULL, NULL);
