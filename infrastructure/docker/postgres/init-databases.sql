-- Create User (if not already created by environment variables)
DO
$do$
BEGIN
   IF NOT EXISTS (
      SELECT FROM pg_catalog.pg_roles
      WHERE  rolname = 'aurax') THEN
      CREATE ROLE aurax LOGIN PASSWORD 'aurax_local' SUPERUSER;
   END IF;
END
$do$;

-- Create Databases
CREATE DATABASE aurax_identity OWNER aurax;
CREATE DATABASE aurax_tenant OWNER aurax;
CREATE DATABASE aurax_asset OWNER aurax;
CREATE DATABASE aurax_incident OWNER aurax;
CREATE DATABASE aurax_workflow OWNER aurax;
CREATE DATABASE aurax_audit OWNER aurax;
CREATE DATABASE aurax_analytics OWNER aurax;
CREATE DATABASE aurax_knowledge OWNER aurax;

-- Grant Privileges
GRANT ALL PRIVILEGES ON DATABASE aurax_identity TO aurax;
GRANT ALL PRIVILEGES ON DATABASE aurax_tenant TO aurax;
GRANT ALL PRIVILEGES ON DATABASE aurax_asset TO aurax;
GRANT ALL PRIVILEGES ON DATABASE aurax_incident TO aurax;
GRANT ALL PRIVILEGES ON DATABASE aurax_workflow TO aurax;
GRANT ALL PRIVILEGES ON DATABASE aurax_audit TO aurax;
GRANT ALL PRIVILEGES ON DATABASE aurax_analytics TO aurax;
GRANT ALL PRIVILEGES ON DATABASE aurax_knowledge TO aurax;

-- Connect to knowledge DB and enable pgvector
\c aurax_knowledge;
CREATE EXTENSION IF NOT EXISTS vector;
