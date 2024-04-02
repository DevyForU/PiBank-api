CREATE TYPE category_type AS ENUM ('INCOME', 'OUTCOME');
create table if not exists "category"(
    id varchar primary key default uuid_generate_v4(),
    name varchar not null unique,
    type category_type not null
);