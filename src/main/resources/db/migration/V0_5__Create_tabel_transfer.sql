create table if not exists "transfer"(
    id varchar primary key default uuid_generate_v4(),
    idSender varchar references "account"(id) not null ,
    amount double precision not null,
    registrationDate timestamp without time zone default now(),
    effectiveDate timestamp without time zone not null,
    reason varchar not null ,
    label varchar,
    isCanceled bool default false
);
