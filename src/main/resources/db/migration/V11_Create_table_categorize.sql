create table if not exists "categorize"(
    id varchar primary key default uuid_generate_v4(),
    name varchar not null unique,
    type transaction_type not null
);