create table if not exists "account"(
    id varchar primary key default uuid_generate_v4(),
    account_number varchar not null unique ,
    id_user varchar references "user"(id) not null ,
    id_bank varchar references "bank"(id) not null ,
    credit_allow double precision not null default 0.0,
    over_draft_limit bool default false,
    main_balance double precision not null default 0.0,
    loans double precision not null default 0,
    loans_interest double precision not null default 0
);