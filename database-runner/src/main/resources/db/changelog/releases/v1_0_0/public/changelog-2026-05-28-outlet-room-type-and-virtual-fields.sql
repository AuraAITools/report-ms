-- liquibase formatted sql

-- Adds OutletRoomModel.type / url / passcode / description.
--   outlet_rooms.type        -> PHYSICAL | VIRTUAL  (@Enumerated STRING, @Column nullable = false; no SQL default — callers must set it)
--   outlet_rooms.url         -> VARCHAR(2048)        (nullable; populated when type = VIRTUAL)
--   outlet_rooms.passcode    -> VARCHAR(255)         (nullable)
--   outlet_rooms.description -> TEXT                 (nullable; long-form room description)
-- OutletRoomModel is @Audited, so the same columns land on audit.outlet_rooms_aud
-- (nullable, no backfill — past revisions had no value for these fields).

-- changeset kevin:2026-05-28-outlet-room-type-1
ALTER TABLE public."outlet_rooms"
    ADD COLUMN "type" VARCHAR(255) NOT NULL;

-- changeset kevin:2026-05-28-outlet-room-url-1
ALTER TABLE public."outlet_rooms"
    ADD COLUMN "url" VARCHAR(2048);

-- changeset kevin:2026-05-28-outlet-room-passcode-1
ALTER TABLE public."outlet_rooms"
    ADD COLUMN "passcode" VARCHAR(255);

-- changeset kevin:2026-05-28-outlet-room-description-1
ALTER TABLE public."outlet_rooms"
    ADD COLUMN "description" TEXT;

-- changeset kevin:2026-05-28-outlet-room-type-2
ALTER TABLE audit."outlet_rooms_aud"
    ADD COLUMN "type" VARCHAR(255);

-- changeset kevin:2026-05-28-outlet-room-url-2
ALTER TABLE audit."outlet_rooms_aud"
    ADD COLUMN "url" VARCHAR(2048);

-- changeset kevin:2026-05-28-outlet-room-passcode-2
ALTER TABLE audit."outlet_rooms_aud"
    ADD COLUMN "passcode" VARCHAR(255);

-- changeset kevin:2026-05-28-outlet-room-description-2
ALTER TABLE audit."outlet_rooms_aud"
    ADD COLUMN "description" TEXT;
