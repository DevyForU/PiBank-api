CREATE TYPE transaction_type AS ENUM ('DEBIT', 'CREDIT');
create table if not exists "transaction"(
    id varchar primary key default uuid_generate_v4(),
    id_transfer varchar references "transfer"(id) not null ,
    id_account varchar references "account"(id) not null ,
    date timestamp without time zone default now(),
    type transaction_type not null,
    id_categorize varchar references "category"(id)
);
