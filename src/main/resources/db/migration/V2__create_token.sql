CREATE TABLE "token" (
  "value" VARCHAR(255) PRIMARY KEY,
  "created_at" BIGINT NOT NULL,
  "valid" BOOLEAN NOT NULL,
  "user_id" BIGINT NOT NULL
);