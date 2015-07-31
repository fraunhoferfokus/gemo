-- Table: users

-- DROP TABLE users;

CREATE TABLE users
(
  _id serial NOT NULL,
  firstname text,
  lastname text,
  username text,
  password text,
  email text,
  birthdate text,
  street text,
  housenr text,
  postalcode text,
  location text,
  phonenumber text,
  bankaccountowner text,
  bankaccountnumber text,
  bankcode text,
  preferences text,
  vehicletype text,
  publictransportaffinity text,
  drivinglicenseclass text,
  drivinglicensedate text,
  drivinglicenselocation text,
  drivinglicenseid text,
  lang text,
  creditcardnumber text,
  termsandconditionsdate text,
  CONSTRAINT users_pkey PRIMARY KEY (_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE users
  OWNER TO gemo;
