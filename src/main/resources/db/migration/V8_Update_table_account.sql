alter table "account"
add column interest_rate_before_7_days decimal(5,2) not null default 0.00,
add coulumn interest_rate_after_7_days decimal(5,2) not null default 0.00;