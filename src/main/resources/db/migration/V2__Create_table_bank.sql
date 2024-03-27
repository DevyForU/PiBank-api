create table if not exists "bank"(
    id varchar primary key default uuid_generate_v4(),
    ref varchar not null unique,
    name varchar not null unique
);
