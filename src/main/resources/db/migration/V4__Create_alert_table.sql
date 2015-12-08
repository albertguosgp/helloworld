CREATE TABLE alert (
  id SERIAL UNIQUE,
  maxx_user VARCHAR(32),
  status VARCHAR(32),
  symbol VARCHAR(16),
  side VARCHAR(8),
  price VARCHAR(128),
  firm VARCHAR(128),
  delivery VARCHAR(16),
  message TEXT,
  create_timestamp TIMESTAMP WITH TIME ZONE,
  trigger_timestamp TIMESTAMP WITH TIME ZONE,
  delete_timestamp TIMESTAMP WITH TIME ZONE,
  expire_timestamp TIMESTAMP WITH TIME ZONE,
  CONSTRAINT alertPk PRIMARY KEY (id)
);
