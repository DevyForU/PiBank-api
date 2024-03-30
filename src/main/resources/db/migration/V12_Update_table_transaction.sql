alter table "transaction"
add column id_categorize varchar references "categorize"(id) ;