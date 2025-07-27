#!/bin/bash

set -Eeo pipefail

source "$(which docker-entrypoint.sh)"

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
	------------ create roles and grant db-level permissions
	CREATE ROLE et_dbo WITH LOGIN PASSWORD '$(less $PG_DBO_PWD_FILE)';
	CREATE ROLE et_api WITH LOGIN PASSWORD '$(less $PG_API_PWD_FILE)';

	-- only explicitly allowed roles can connect
	REVOKE ALL ON DATABASE $POSTGRES_DB FROM public;

	GRANT connect ON DATABASE $POSTGRES_DB TO et_dbo, et_api;
	GRANT create ON DATABASE $POSTGRES_DB TO et_dbo;


	------------ create schema
	CREATE SCHEMA IF NOT EXISTS et AUTHORIZATION et_dbo;

	------------ schema permissions for et_api
	GRANT USAGE ON SCHEMA et TO et_api;

	-- allow DML on future tables created by postgres and et_dbo users
	ALTER DEFAULT privileges for role postgres, et_dbo IN SCHEMA et
	GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO et_api;

	-- allow DML on all existing tables
	GRANT SELECT, INSERT, UPDATE, DELETE on all tables in schema et TO et_api;
	-----------------------------


	------------ schema permissions for et_dbo
	-- includes usage and create (allows to create objects within schema)
	grant ALL PRIVILEGES ON SCHEMA et, public TO et_dbo;

	-- for future objects created by postgres user
	ALTER DEFAULT privileges for role postgres IN SCHEMA et, public
	GRANT ALL PRIVILEGES ON TABLES TO et_dbo;

	ALTER DEFAULT privileges for role postgres IN SCHEMA et, public
	GRANT ALL PRIVILEGES ON SEQUENCES TO et_dbo;

	-- for existing objects
	GRANT ALL PRIVILEGES on all tables in schema et, public TO et_dbo;
	GRANT ALL privileges on all sequences IN SCHEMA et, public TO et_dbo;
	-----------------------------
	CREATE EXTENSION pg_stat_statements;
EOSQL
