create table if not exists "transfer_list"(
    id_transfer varchar references "transfer"(id) not null ,
    id_receiver varchar references "account"(id)
);
