create table if not exists "transaction_table"(
    id_transfer varchar references "transfer_table"(id) not null ,
    label varchar
)