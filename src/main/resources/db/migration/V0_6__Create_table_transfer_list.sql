create table if not exists "transferList"(
    idTransfer varchar references "transfer"(id) not null ,
    idReceiver varchar references "account"(id)
);
