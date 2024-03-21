create table if not exists "transaction"(
    id_transfer varchar references "transfer"(id) not null ,
    label varchar
);