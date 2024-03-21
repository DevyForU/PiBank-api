create table if not exists "account"(
    id varchar primary key  default uuid_generate_v4(),
    id_user varchar references "user"(id) not null ,
    credit_allow double precision not null,
    over_draft_limit boolean default false
);