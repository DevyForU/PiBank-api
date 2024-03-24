create table if not exists "transferList"(
    idTransfer varchar references "transer"(id) not null ,
    idReceiver varchar references "account"(id)
)