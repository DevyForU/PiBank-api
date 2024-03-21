create extension if not exists "uuid-ossp";
create table if not exists "user_table"(
    id varchar primary key default uuid_generate_v4(),
    firstname varchar not null,
    lastname varchar ,
    birthday date not null ,
    net_month_salary double precision not null
)