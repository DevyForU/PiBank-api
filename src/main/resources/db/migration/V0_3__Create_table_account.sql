create table if not exists "account"(
    id varchar primary key uuid_generate_v4(),
    accountNumber varchar not null unique ,
    idUser varchar references "user"(id) not null ,
    idBank varchar references "bank"(id) not null ,
    creditAllow double precision not null ,
    overDraftLimit bool default false
);