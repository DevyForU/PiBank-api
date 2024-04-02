create table if not exists "balance_history"(
    id varchar primary key default uuid_generate_v4(),
    id_account varchar references "account"(id) not null ,
    main_balance double precision not null default 0.0,
    loans double precision not null default 0.0,
    loans_interest double precision not null default 0.0,
    date timestamp without time zone default now()

);