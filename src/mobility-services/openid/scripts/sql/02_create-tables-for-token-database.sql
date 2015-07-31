CREATE TABLE accesstoken
(
  access_token text NOT NULL,
  valid_until text,
  scope text,
  username text,
  CONSTRAINT accesstoken_pkey PRIMARY KEY (access_token)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE accesstoken
  OWNER TO gemo;

-- Table: authcode

-- DROP TABLE authcode;

CREATE TABLE authcode
(
  _id serial NOT NULL,
  client_id text,
  auth_code text,
  valid_until text,
  scope text,
  redirect_uri text,
  username text,
  CONSTRAINT authcode_pkey PRIMARY KEY (_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE authcode
  OWNER TO gemo;

-- Table: clients

-- DROP TABLE clients;

CREATE TABLE clients
(
  client_id text NOT NULL,
  hashed_client_secret text NOT NULL,
  client_name text,
  client_url text,
  client_description text,
  redirect_uri text NOT NULL,
  CONSTRAINT client_id_pkey PRIMARY KEY (client_name)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE clients
  OWNER TO gemo;

-- Table: refreshtoken

-- DROP TABLE refreshtoken;

CREATE TABLE refreshtoken
(
  _id serial NOT NULL,
  client_id text,
  refresh_token text,
  valid_until text,
  scope text,
  username text,
  CONSTRAINT refreshtoken_pkey PRIMARY KEY (_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE refreshtoken
  OWNER TO gemo;
  
-- Table: scopes

-- DROP TABLE scopes;

CREATE TABLE scopes
(
  name text NOT NULL,
  description text,
  CONSTRAINT scopes_pkey PRIMARY KEY (name)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE scopes
  OWNER TO gemo;

