INSERT INTO clients (client_id,hashed_client_secret,client_name,client_url,client_description,redirect_uri)
VALUES  ('8dh9cpkijndku0i1hbogeckdio','$2a$10$4AGAHaW52BZWvkxfz8Iu4.JB6CwpJasGU0MS2K7FjPwsCWu/H6O/C','asdf','google.de','asdf2','http://localhost:8080/openid/redirect');

INSERT INTO accesstoken (access_token,valid_until,scope,username)
VALUES ('f222a9a7d569d4e01a42a1fd77d70dd4','1890237619967','read','asdfasdf2');


INSERT into authcode (client_id,auth_code,valid_until,scope,redirect_uri,username)
VALUES ('8dh9cpkijndku0i1hbogeckdio','80c4f9ade44a408d76dec4cdb738959b','1794629826047','read write','http://localhost:8080/openid/redirect','asdfasdf2');

INSERT into refreshtoken (client_id,refresh_token,valid_until,scope,username)
VALUES ('8dh9cpkijndku0i1hbogeckdio','581b9ec5a4c44846601561f7db36834d','1411660128763','read','asdfasdf2');

INSERT into scopes (name,description)
VALUES ('read', 'read erlaubt dem Client lesend auf die Services zuzugreifen.');

INSERT into scopes (name,description)
VALUES ('write', 'write erlaubt dem Client schreibend auf die Services zuzugreifen.');