create table if not exists "transaction"(
    id varchar primary key default uuid_generate_v4(),
    id_transfer varchar references "transfer"(id) not null ,
    label varchar,
    type varchar
);