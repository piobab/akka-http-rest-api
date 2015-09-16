CREATE TABLE "user_auth" (
  id SERIAL PRIMARY KEY,
  created_at BIGINT NOT NULL,
  email VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL
);

CREATE TABLE "user" (
  id SERIAL PRIMARY KEY,
  first_name VARCHAR(100),
  last_name VARCHAR(100),
  gender INT,
  street_address VARCHAR(100),
  postal_code VARCHAR(100),
  postal_city VARCHAR(100),
  user_auth_id BIGINT NOT NULL
);

ALTER TABLE "user" ADD CONSTRAINT "user_auth_id_fk" FOREIGN KEY ("user_auth_id") REFERENCES "user_auth" ("id");