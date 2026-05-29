-- liquibase formatted sql

-- Extracts the relationship field from accounts into a new parents table.
-- ParentModel has a @OneToOne FK (account_id) pointing back to AccountModel.
-- Existing accounts with a non-null relationship get a corresponding parent row.

-- changeset kevin:2026-05-13-parent-model-1
CREATE TABLE public."parents"
(
    "created_at"   TIMESTAMP WITH TIME ZONE,
    "updated_at"   TIMESTAMP WITH TIME ZONE,
    "version"      BIGINT,
    "id"           UUID         NOT NULL,
    "account_id"   UUID,
    "relationship" VARCHAR(255),
    "tenant_id"    VARCHAR(255) NOT NULL,
    "created_by"   VARCHAR(255),
    "updated_by"   VARCHAR(255),
    CONSTRAINT "parents_pkey" PRIMARY KEY ("id"),
    CONSTRAINT "fk_parents_account" FOREIGN KEY ("account_id") REFERENCES public."accounts" ("id")
);

-- changeset kevin:2026-05-13-parent-model-2 splitStatements:false
DO
$$
DECLARE
rec RECORD;
    new_parent_id
UUID;
BEGIN
FOR rec IN
SELECT id,
       relationship,
       tenant_id,
       version,
       created_at,
       created_by,
       updated_at,
       updated_by
FROM public.accounts
WHERE relationship IS NOT NULL LOOP
        new_parent_id := gen_random_uuid();
INSERT INTO public.parents (id, account_id, relationship, tenant_id, version, created_at, created_by, updated_at,
                            updated_by)
VALUES (new_parent_id, rec.id, rec.relationship, rec.tenant_id, rec.version, rec.created_at, rec.created_by,
        rec.updated_at, rec.updated_by);
END LOOP;
END $$;

-- changeset kevin:2026-05-13-parent-model-3
ALTER TABLE public."accounts" DROP COLUMN "relationship";

-- changeset kevin:2026-05-13-parent-model-4
CREATE TABLE audit."parents_aud"
(
    "rev"          INTEGER NOT NULL,
    "revtype"      SMALLINT,
    "id"           UUID    NOT NULL,
    "account_id"   UUID,
    "relationship" VARCHAR(255),
    CONSTRAINT "parents_aud_pkey" PRIMARY KEY ("rev", "id")
);
