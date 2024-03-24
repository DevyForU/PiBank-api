create extension if not exists "uuid-ossp";
create table if not exists "user"(
    id varchar primary key default uuid_generate_v4(),
    firstname varchar,
    lastname varchar,
    birthday date ,
    netMonthSalary double precision not null check ( netMonthSalary > 0 ),
    check ( age(birthday) >= '21 years')
                      );
