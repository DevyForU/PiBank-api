create table if not exists "account"(
    id varchar primary key  default uuid_generate_v4(),
    id_user varchar references "user"(id) not null ,
    main_balance double precision not null ,
    loans double precision not null,
    loans_interest double precision not null,
    credit_allow double precision not null,
    over_draft_limit boolean default false
);