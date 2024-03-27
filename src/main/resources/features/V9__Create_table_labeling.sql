create table if not exists "labeling"(
    id_transaction varchar references "transaction"(id) not null ,
    id_label varchar references "label"(id) not null
) ;