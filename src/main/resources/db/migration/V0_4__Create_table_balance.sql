create table if not exists "balance"(
    id varchar primary key default uuid_generate_v4(),
    idAccount varchar references "account"(id) not null ,
    mainBalance double precision not null ,
    loans double precision not null default 0.0,
    loansInterest double precision not null,
    date timestamp without time zone default now()

);