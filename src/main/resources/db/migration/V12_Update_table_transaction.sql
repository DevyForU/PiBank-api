alter table "transaction"
add column id_category varchar references "category"(id) ;
