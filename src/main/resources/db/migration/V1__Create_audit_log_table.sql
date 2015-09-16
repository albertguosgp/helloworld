CREATE TABLE audit_log (
  id        SERIAL UNIQUE,
  maxx_user  VARCHAR(32),
  operation VARCHAR(128),
  audit_timestamp      TIMESTAMP WITH TIME ZONE,
  remarks    VARCHAR(128),
  CONSTRAINT firmAuditPK PRIMARY KEY (id)
);
