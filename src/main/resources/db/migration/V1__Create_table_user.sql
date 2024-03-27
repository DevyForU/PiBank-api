create extension if not exists "uuid-ossp";
create table if not exists "user"(
    id varchar primary key default uuid_generate_v4(),
    first_name varchar,
    last_name varchar,
    birthday date ,
    net_month_salary double precision not null check ( net_month_salary > 0 ),
    check ( age(birthday) >= '21 years')
                      );
