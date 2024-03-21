create table if not exists "account_table"(
    id varchar primary key  default uuid_generate_v4(),
    id_user varchar references "user_table"(id) not null ,
    credit_allow double precision not null,
    over_draft_limit boolean default false
);