CREATE TYPE transactionType AS ENUM ('CREDIT', 'DEBIT');

create table if not exists "transaction"(
    id varchar primary key default uuid_generate_v4(),
    idTransfer varchar references "transfer"(id) not null ,
    idAccount varchar references "account"(id) not null ,
    date timestamp without time zone default now(),
    type transactionType NOT NULL
);
