create table if not exists "transfer"(
    id varchar primary key default uuid_generate_v4(),
    transfer_reason varchar not null ,
    amount double precision not null ,
    effective_date date not null ,
    registration_date date not null ,
    is_canceled boolean default false
);