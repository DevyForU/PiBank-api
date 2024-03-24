create table if not exists "label"(
    id varchar primary key default uuid_generate_v4(),
    name varchar not null unique ,
    labelType varchar not null check ( labelType = 'DEBIT' or labelType = 'CREDIT')
);