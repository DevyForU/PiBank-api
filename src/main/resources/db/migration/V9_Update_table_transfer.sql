alter table "transfer"
add column ref varchar not null unique default uuid_generate_v4(),
add column id_receiver varchar references "account"(id) not null;

