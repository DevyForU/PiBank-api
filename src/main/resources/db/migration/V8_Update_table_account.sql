ALTER TABLE "account"
    ADD COLUMN interest_rate_before_7_days double precision NOT NULL DEFAULT 0.00,
ADD COLUMN interest_rate_after_7_days double precision NOT NULL DEFAULT 0.00;
