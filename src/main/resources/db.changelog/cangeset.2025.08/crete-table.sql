CREATE TABLE IF NOT EXISTS notification_qutbox (
    id UUID         PRIMARY KEY,
    created_at      TIMESTAMPE NOT NULL DEFAULT now(),
    topic           VARCHAR NOT NULL,
    key             VARCHAR NOT NULL,
    value           TEXT NOT NULL,
    sent            BOOLEAN NOT NULL,
    attempt         INTEGER NOT NULL
)