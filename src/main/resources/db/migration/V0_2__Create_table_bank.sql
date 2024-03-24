create table if not exists "bank"(
    id varchar primary key default uuid_generate_v4(),
    name varchar not null unique ,
    reference varchar not null unique
)