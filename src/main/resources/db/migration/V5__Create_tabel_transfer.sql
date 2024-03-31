create table if not exists "transfer"(
    id varchar primary key default uuid_generate_v4(),
    id_sender varchar references "account"(id) not null ,
    amount double precision not null,
    registration_date timestamp without time zone default now(),
    effective_date timestamp without time zone not null,
    reason varchar not null ,
    label varchar,
    is_canceled bool default false
);
