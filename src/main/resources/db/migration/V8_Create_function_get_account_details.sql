CREATE OR REPLACE FUNCTION get_details_by_account_number(account_number varchar) 
RETURNS TABLE (
    account_number varchar,
    main_balance double precision,
    loans double precision,
    loans_interest double precision,
    date TIMESTAMP WITHOUT TIME ZONE
) AS $$
BEGIN
    RETURN QUERY 
    SELECT
        "account".account_number,
        "balance_history".main_balance,
        "account".loans,
        "account".loans_interest,
        "balance_history".date
    FROM
        "balance_history"
    JOIN
        "account" ON balance_history.id_account = "account".id
    JOIN
        "bank" ON account.id_bank = "bank".id
    JOIN
        "user" ON "account".id_user = "user".id
    WHERE
        "account".account_number = account_number;
END;
$$ LANGUAGE plpgsql;
