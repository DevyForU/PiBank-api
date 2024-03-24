create table if not exists "labeling"(
    idTransaction varchar references "transaction"(id) not null ,
    idLabel varchar references "label"(id) not null
) ;