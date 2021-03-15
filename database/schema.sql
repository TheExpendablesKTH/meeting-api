-- -------------------------------------------------------------
-- TablePlus 3.7.1(332)
--
-- https://tableplus.com/
--
-- Database: meetings
-- Generation Time: 2021-03-15 21:21:03.2830
-- -------------------------------------------------------------


DROP TABLE IF EXISTS "public"."residents";
-- This script only contains the table creation statements and does not fully represent the table in the database. It's still missing: indices, triggers. Do not use it as a backup.

-- Sequence and defined type
CREATE SEQUENCE IF NOT EXISTS residents_id_seq;

-- Table Definition
CREATE TABLE "public"."residents" (
    "id" int4 NOT NULL DEFAULT nextval('residents_id_seq'::regclass),
    "name" varchar NOT NULL,
    PRIMARY KEY ("id")
);

