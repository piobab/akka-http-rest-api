CREATE TABLE "user_auth" (
  "id" SERIAL PRIMARY KEY,
  "email" VARCHAR(50) NOT NULL UNIQUE,
  "password" VARCHAR(255) NOT NULL,
  "user_id" BIGINT NOT NULL
);

CREATE TABLE "user" (
  "id" SERIAL PRIMARY KEY,
  "created_at" BIGINT NOT NULL,
  "first_name" VARCHAR(100) NOT NULL,
  "last_name" VARCHAR(100) NOT NULL,
  "gender" INT NOT NULL,
  "street_address" VARCHAR(100),
  "postal_code" VARCHAR(100),
  "postal_city" VARCHAR(100),
  "role" INT NOT NULL DEFAULT 0
);

ALTER TABLE "user_auth" ADD CONSTRAINT "user_id_fk" FOREIGN KEY("user_id") REFERENCES "user"("id");